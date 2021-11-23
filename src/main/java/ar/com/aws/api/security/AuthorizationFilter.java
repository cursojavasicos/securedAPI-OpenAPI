package ar.com.aws.api.security;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import ar.com.aws.api.beans.RolInfo;
import ar.com.aws.api.beans.UserInfo;
import ar.com.aws.api.bo.RolBO;
import ar.com.aws.api.bo.UserBO;
import ar.com.aws.api.exceptions.BOException;
import ar.com.aws.api.util.Constants;
import ar.com.aws.api.util.JWTRepository;

@Aspect
@Component
public class AuthorizationFilter {
	
	private final Logger log = LogManager.getLogger(AuthorizationFilter.class);
	
	@Autowired
	private UserBO userBO;

	@Autowired
	private RolBO rolBO;

	@Before(value = "secureInterceptor() && @annotation(secured)", argNames = "secured" )
	public void adviceSecureInterceptor(JoinPoint jp, Secured secured) throws Throwable {
		
		if (log.isInfoEnabled()) {
			log.info("SECURED METHOD [" + jp.getSignature().getName() + "] - " + arrayToStr(secured.validateMethod()) + " - " + arrayToStr(secured.roles()));
		}
		
		Map<String, Object> parameters = this.getParameterValue(MethodSignature.class.cast(jp.getSignature()).getMethod(), jp.getArgs());
		
		List<String> methods = Arrays.asList(secured.validateMethod());
		
		boolean validated = false;
		
		if (methods.contains(Constants.TOKEN) && !validated) {
			if (parameters.get(Constants.TOKEN) != null && !parameters.get(Constants.TOKEN).toString().isEmpty()) {
				validateToken(parameters.get(Constants.TOKEN).toString(), secured.roles(), secured.unique());
				validated = true;
			}
		}
		
		if (methods.contains(Constants.APIKEY) && !validated) {
			if (parameters.get(Constants.APIKEY) != null && !parameters.get(Constants.APIKEY).toString().isEmpty()) {
				validateKey(parameters.get(Constants.APIKEY).toString());
				validated = true;
			}
		}

		if (methods.contains(Constants.BASIC) && !validated) {
			if (parameters.get(Constants.BASIC) != null && !parameters.get(Constants.BASIC).toString().isEmpty()) {
				validateBasic(parameters.get(Constants.BASIC).toString());
				validated = true;
			}
		}

		if (!validated) {
			if (log.isErrorEnabled()) {
				log.error("[VALIDATE] - Method validation not especific!");
			}
			
			throw new ClientErrorException("UNAUTHORIZED - Method validation not especific!", Response.Status.UNAUTHORIZED);
		}

		if (log.isInfoEnabled()) {
			log.info("SECURED METHOD [" + jp.getSignature().getName() + "] - " + arrayToStr(secured.validateMethod()) + " - " + arrayToStr(secured.roles()) + " --> AUTHORIZED!!!");
		}
	
	}

	private void validateKey(String apiKey) {
		if (apiKey == null || apiKey.trim().isEmpty()) {
			if (log.isErrorEnabled()) {
				log.error("[VALIDATE-API-KEY] - API-KEY NOT FOUND");
			}
			
			throw new ClientErrorException("UNAUTHORIZED - Security API-KEY not found", Response.Status.UNAUTHORIZED);
		}
		
		if (!Constants.VALID_API_KEY.equals(apiKey)) {
			if (log.isErrorEnabled()) {
				log.error("[VALIDATE-API-KEY] - ApiKey [" + apiKey + "] - UNAUTHORIZED");
			}
			
			throw new ClientErrorException("UNAUTHORIZED - Authorization has been refused for the provided credentials with the request", Response.Status.UNAUTHORIZED);
		}
	}

	private void validateBasic(String token) {
		if (token == null || token.trim().isEmpty()) {
			if (log.isErrorEnabled()) {
				log.error("[VALIDATE-TOKEN] - Token NOT FOUND");
			}
			
			throw new ClientErrorException(createBasicFaultResponse());
		} else {
			String subToken = token;
			
			if ((token.startsWith("Basic ")) || (token.startsWith("basic "))) {
				subToken = token.substring(6);
			}
			
			try {
				String decodeValue = new String(Base64.getDecoder().decode(subToken.getBytes()));
				
				String[] userKeyPair = decodeValue.split(":");
				
				if (userKeyPair.length == 2) {
					try {
						UserInfo uInfo = userBO.getUserByName(userKeyPair[0]);
						
						if (!uInfo.getPassword().equals(userKeyPair[1])) {
							throw new ClientErrorException(createBasicFaultResponse());
						}
					} catch (BOException e) {
						throw new ClientErrorException(createBasicFaultResponse());
					}
				} else {
					throw new ClientErrorException(createBasicFaultResponse());
				}
			} catch (IllegalArgumentException e) {
				throw new ClientErrorException(createBasicFaultResponse());
			}
		}
		
	}

	private Response createBasicFaultResponse() {
        return Response.status(Response.Status.UNAUTHORIZED).header("WWW-Authenticate", "Basic realm=\"API\"").build();
    }
	
	private void validateToken(String token, String[] roles, boolean accesoUnico) {
		if (token == null || token.trim().isEmpty()) {
			if (log.isErrorEnabled()) {
				log.error("[VALIDATE-TOKEN] - Token NOT FOUND");
			}
			
			throw new ClientErrorException("UNAUTHORIZED - Security Authorization token not found", Response.Status.UNAUTHORIZED);
		
		} else if (roles == null || roles.length == 0) {
		
			if (log.isErrorEnabled()) {
				log.error("[VALIDATE-TOKEN] - Roles Array Empty");
			}
			
			throw new ClientErrorException("UNAUTHORIZED - Security Authorization token not indicate active rol", Response.Status.UNAUTHORIZED);
		
		} else {
		
			String subToken = token;
			
			if ((token.startsWith("Bearer ")) || (token.startsWith("bearer "))) {
				subToken = token.substring(7);
			}

			if (accesoUnico) {
				if (JWTRepository.getInstance().getTokens().contains(subToken)) {
					throw new ClientErrorException("UNAUTHORIZED - Token is used", Response.Status.UNAUTHORIZED);
				}
			}
			
			try {
				
				Algorithm algorithm = Algorithm.HMAC512(Constants.SECRET);

				JWTVerifier verifier = JWT.require(algorithm)
									      .withIssuer("AWS")
									      .withAudience("API")
									      .withSubject("API-Authentication")
									      .build();

				DecodedJWT jwt = verifier.verify(subToken);

				List<String> jwtRoles = (List<String>)jwt.getClaim(Constants.PRIVATE_CLAIM_PREFIX + "USER_ROLES").asList(String.class);
				
				boolean valid = false;
				
				for (int i = 0; i < roles.length && !valid; i++) {
					if (jwtRoles.contains(roles[i])) {
						
						try {
							RolInfo rInfo = rolBO.getRolByName(roles[i]);
							
							valid = rInfo.isEnabled();
							
							if (log.isInfoEnabled()) {
								log.info("[VALIDATE TOKEN] - Rol " + roles[i] + " is Present in Token. [Status Rol = " + (rInfo.isEnabled() ? "ENABLED" : "DISABLED") + "]");
							}
							
						} catch (BOException e) {}
						
					}
					
				}

				if (!valid) {
					if (log.isInfoEnabled()) {
						log.error("[VALIDATE TOKEN] - Roles not present.");
					}
					
					throw new ClientErrorException("UNAUTHORIZED - Authorization has been refused for the provided credentials with the request", Response.Status.UNAUTHORIZED);
					
				}
				
				JWTRepository.getInstance().getTokens().add(subToken);
				
			} catch (AlgorithmMismatchException e) {
				if (log.isErrorEnabled()) {
					log.error("[VALIDATE] - " + "Bad Request - JWT - Algorithm Mismatch Exception - " + e.getMessage());
				}
				
				throw new ClientErrorException("Bad Request - JWT - Algorithm Mismatch Exception - " + e.getMessage(), Response.Status.BAD_REQUEST);
			} catch (SignatureVerificationException e) {
				if (log.isErrorEnabled()) {
					log.error("[VALIDATE] - " + "Bad Request - JWT - Signature Verification Exception - " + e.getMessage());
				}
				
				throw new ClientErrorException("Bad Request - JWT - Signature Verification Exception - " + e.getMessage(), Response.Status.BAD_REQUEST);
			} catch (TokenExpiredException e) {
				if (log.isErrorEnabled()) {
					log.error("[VALIDATE] - " + "Bad Request - JWT - Token Expired Exception - " + e.getMessage());
				}
				
				throw new ClientErrorException("Bad Request - JWT - Token Expired Exception - " + e.getMessage(), Response.Status.BAD_REQUEST);
			} catch (InvalidClaimException e) {
				if (log.isErrorEnabled()) {
					log.error("[VALIDATE] - " + "Bad Request - JWT - Invalid Claim Exception - " + e.getMessage());
				}
				
				throw new ClientErrorException("Bad Request - JWT - Invalid Claim Exception - " + e.getMessage(), Response.Status.BAD_REQUEST);
			} catch (JWTVerificationException e) {
				if (log.isErrorEnabled()) {
					log.error("[VALIDATE] - " + "Bad Request - JWT - Unexpected JWTVerification Exception - " + e.getMessage());
				}
				
				throw new ClientErrorException("Bad Request - JWT - Unexpected JWTVerification Exception - " + e.getMessage(), Response.Status.BAD_REQUEST);
			} catch (IllegalArgumentException e) {
				if (log.isErrorEnabled()) {
					log.error("[VALIDATE] - " + "Bad Request - JWT - Illegal Argument Exception - " + e.getMessage());
				}
				
				throw new ClientErrorException("Bad Request - JWT - Illegal Argument Exception - " + e.getMessage(), Response.Status.BAD_REQUEST);
			}
				
		}
	}
	
	private Map<String, Object> getParameterValue(Method method, Object[] args) {
        Map<String, Object> parametersValue = new HashMap<String, Object>();
        Parameter[] parameters = method.getParameters();
        
        for (int i = 0; i < parameters.length; i++) {
        	Parameter p = parameters[i];
        	
        	if (p.isAnnotationPresent(TokenParameter.class)) {
            	parametersValue.put(Constants.TOKEN, args[i]);
        	} else if (p.isAnnotationPresent(ApiKeyParameter.class)) {
            	parametersValue.put(Constants.APIKEY, args[i]);
        	} else if (p.isAnnotationPresent(BasicParameter.class)) {
            	parametersValue.put(Constants.BASIC, args[i]);
        	} 
        }
        
        return parametersValue;
    }
	
	private String arrayToStr(String[] array) {
		StringBuffer str = new StringBuffer();
		 
		str.append("[");
	 
		boolean first = true;
		 
		for (String s : array) {
			if (first) {
				first = false;
			} else {
				str.append(", ");
			}
			
			str.append(s);
		}
		 
		str.append("]");
		 
		return str.toString();
	}
	 
	 @Pointcut("within(ar.com..api.services..*)")
	 public void secureInterceptor() {}	

	 
}
