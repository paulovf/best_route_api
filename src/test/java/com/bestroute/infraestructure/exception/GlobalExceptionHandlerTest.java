package com.bestroute.infraestructure.exception;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerTest {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new TestController())
			.setControllerAdvice(new GlobalExceptionHandler())
			.build();
	}

	@Test
	@DisplayName("Should return 400 Bad Request and format validation errors to snake_case")
	void shouldHandleValidationExceptionAndFormatFieldsToSnakeCase() throws Exception {
		String invalidJsonPayload = """
				{
				    "originCity": "",
				    "destinationState": ""
				}
				""";

		mockMvc.perform(post("/test-validation").contentType(MediaType.APPLICATION_JSON).content(invalidJsonPayload))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.timestamp", is(notNullValue())))
			.andExpect(jsonPath("$.status", is(400)))
			.andExpect(jsonPath("$.message", is("Request fields invalids")))
			.andExpect(jsonPath("$.errors.origin_city", is("City cannot be blank")))
			.andExpect(jsonPath("$.errors.destination_state", is("State cannot be blank")));
	}

	private record DummyRequest(@NotBlank(message = "City cannot be blank") String originCity,
			@NotBlank(message = "State cannot be blank") String destinationState) {
	}

	@RestController
	private static class TestController {

		@PostMapping("/test-validation")
		public void handleTest(@Valid @RequestBody DummyRequest request) {
		}

	}

}
