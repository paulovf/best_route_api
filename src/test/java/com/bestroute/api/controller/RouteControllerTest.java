package com.bestroute.api.controller;

import com.bestroute.application.dto.RouteRequest;
import com.bestroute.application.dto.RouteResponse;
import com.bestroute.application.service.RouteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RouteController.class)
class RouteControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private RouteService routeService;

	@Test
	@WithMockUser
	@DisplayName("When passing valid params, return HTTP status 200")
	void shouldReturnOkWhenPayloadIsValid() throws Exception {
		OffsetDateTime now = OffsetDateTime.now();
		RouteResponse mockResponse = new RouteResponse(UUID.randomUUID(), "São Paulo", "SP", "Rio de Janeiro", "RJ",
				now, new ArrayList<>());
		when(routeService.getOrCreateRoute(any(RouteRequest.class))).thenReturn(mockResponse);

		String validJson = """
				{
				    "origin_city": "São Paulo",
				    "origin_state": "SP",
				    "destination_city": "Rio de Janeiro",
				    "destination_state": "RJ",
				    "travel_date": "%s"
				}
				""".formatted(now.toString());

		mockMvc.perform(
				post("/api/v1/routes/search").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(validJson))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.origin_city").value("São Paulo"))
			.andExpect(jsonPath("$.origin_state").value("SP"))
			.andExpect(jsonPath("$.destination_city").value("Rio de Janeiro"))
			.andExpect(jsonPath("$.destination_state").value("RJ"))
			.andExpect(jsonPath("$.travel_date").value(now.toString()));
	}

	@Test
	@WithMockUser
	@DisplayName("When passing invalid params, return HTTP status 400")
	void shouldReturnBadRequestWithSnakeCaseErrorsWhenStateIsInvalid() throws Exception {
		OffsetDateTime now = OffsetDateTime.now();
		String invalidJson = """
				{
				    "origin_city": "São Paulo",
				    "origin_state": "SÃO PAULO",
				    "destination_city": "Rio de Janeiro",
				    "destination_state": "RJ",
				    "travel_date": "%s"
				}
				""".formatted(now.toString());

		mockMvc.perform(
				post("/api/v1/routes/search").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(invalidJson))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.message").value("Request fields invalids"))
			.andExpect(jsonPath("$.errors.origin_state").exists())
			.andExpect(jsonPath("$.errors.origin_state").value("The origin state must have 2 letters"));
	}

}
