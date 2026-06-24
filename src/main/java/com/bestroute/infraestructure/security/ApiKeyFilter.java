package com.bestroute.infraestructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Collections;

public class ApiKeyFilter extends OncePerRequestFilter {

	private final String expectedApiKey;

	public ApiKeyFilter(String expectedApiKey) {
		this.expectedApiKey = expectedApiKey;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain) throws ServletException, IOException {

		String requestKey = request.getHeader("X-API-KEY");

		if (expectedApiKey.equals(requestKey)) {
			var authentication = new UsernamePasswordAuthenticationToken("M2M_APP", null, Collections.emptyList());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		else if (requestKey != null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid api key.");
			return;
		}

		filterChain.doFilter(request, response);
	}

}
