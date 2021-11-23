package ar.com.aws.api.util.logging.filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.ThreadContext;

public class Log4JContextFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		try {
			
			ThreadContext.put("IP", request.getRemoteAddr());
			ThreadContext.put("ID", UUID.randomUUID().toString());
			
			chain.doFilter(request, response);
			
		} finally {
			ThreadContext.clearMap();
		}
	}

	@Override
	public void destroy() {}

}
