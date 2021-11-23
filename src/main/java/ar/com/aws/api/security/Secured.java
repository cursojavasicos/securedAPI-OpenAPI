package ar.com.aws.api.security;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ar.com.aws.api.util.Constants;

@Target({METHOD, ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Secured {

	String[] validateMethod() default {Constants.TOKEN};
	
	String[] roles() default {};
	
	/**
	 * Si este valor es TRUE y estamos utilizando Token JWT como validateMethod el token será utilizado una unica vez
	 * @return
	 */
	boolean unique() default false;
}
