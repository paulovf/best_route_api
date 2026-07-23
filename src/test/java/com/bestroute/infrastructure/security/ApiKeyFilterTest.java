package com.bestroute.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiKeyFilterTest {

	private static final String VALID_KEY = "super-secret-api-key-123";

	private ApiKeyFilter apiKeyFilter;

	private MockHttpServletRequest request;

	private MockHttpServletResponse response;

	@Mock
	private FilterChain filterChain;

	@BeforeEach
	void setUp() {
		apiKeyFilter = new ApiKeyFilter(VALID_KEY);

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();

		SecurityContextHolder.clearContext();
	}

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	@DisplayName("Should authenticate and continue chain when X-API-KEY header is valid")
	void shouldAuthenticateWhenApiKeyIsValid() throws ServletException, IOException {
		request.addHeader("X-API-KEY", VALID_KEY);

		apiKeyFilter.doFilterInternal(request, response, filterChain);

		var authentication = SecurityContextHolder.getContext().getAuthentication();
		assertThat(authentication).isNotNull();
		assertThat(authentication.getPrincipal()).isEqualTo("M2M_APP");
		assertThat(authentication.getAuthorities()).isEmpty();

		verify(filterChain, times(1)).doFilter(request, response);
	}

	@Test
	@DisplayName("Should return 401 Unauthorized and block chain when X-API-KEY header is invalid")
	void shouldReturnUnauthorizedWhenApiKeyIsInvalid() throws ServletException, IOException {
		request.addHeader("X-API-KEY", "wrong-key-code");

		apiKeyFilter.doFilterInternal(request, response, filterChain);

		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_UNAUTHORIZED);
		assertThat(response.getContentAsString()).isEqualTo("Invalid api key.");

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

		verify(filterChain, never()).doFilter(any(), any());
	}

	@Test
	@DisplayName("Should skip authentication but continue chain when X-API-KEY header is completely missing")
	void shouldProceedWithoutAuthenticationWhenApiKeyIsMissing() throws ServletException, IOException {
		apiKeyFilter.doFilterInternal(request, response, filterChain);

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

		verify(filterChain, times(1)).doFilter(request, response);
		assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK);
	}

}
