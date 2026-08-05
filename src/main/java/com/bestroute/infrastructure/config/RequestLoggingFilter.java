package com.bestroute.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		long startTime = System.currentTimeMillis();

		try {
			filterChain.doFilter(request, response);
		}
		finally {
			long duration = System.currentTimeMillis() - startTime;

			String uri = request.getRequestURI();
			if (!uri.contains("/actuator") && !uri.contains("/swagger-ui")) {
				log.info("HTTP {} {} | Status: {} | Time: {}ms", request.getMethod(), uri, response.getStatus(),
						duration);
			}
		}
	}

}
