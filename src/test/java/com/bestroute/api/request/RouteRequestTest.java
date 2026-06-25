package com.bestroute.api.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class RouteRequestTest {

	private Validator validator;

	@BeforeEach
	void setUp() {
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			validator = factory.getValidator();
		}
	}

	@Test
	@DisplayName("Should have no violations when RouteRequest is perfectly valid")
	void shouldPassValidationWhenRequestIsValid() {
		RouteRequest request = new RouteRequest("São Paulo", "SP", "Rio de Janeiro", "RJ", OffsetDateTime.now());

		Set<ConstraintViolation<RouteRequest>> violations = validator.validate(request);
		assertThat(violations).isEmpty();
	}

	@Test
	@DisplayName("Should capture violations when mandatory fields are blank or null")
	void shouldFailValidationWhenFieldsAreMissing() {
		RouteRequest request = new RouteRequest("", "SP", "Rio de Janeiro", null, null);

		Set<ConstraintViolation<RouteRequest>> violations = validator.validate(request);

		assertThat(violations).hasSize(3);
		assertThat(violations).extracting(ConstraintViolation::getMessage)
			.containsExactlyInAnyOrder("The origin city is required", "The destination state is required",
					"The travel date is required");
	}

	@Test
	@DisplayName("Should fail validation when states do not have exactly 2 characters")
	void shouldFailValidationWhenStateSizeIsInvalid() {
		RouteRequest request = new RouteRequest("São Paulo", "S", "Rio de Janeiro", "RJX", OffsetDateTime.now());

		Set<ConstraintViolation<RouteRequest>> violations = validator.validate(request);

		assertThat(violations).hasSize(2);
		assertThat(violations).extracting(ConstraintViolation::getMessage)
			.containsExactlyInAnyOrder("The origin state must have 2 letters",
					"The destination state must have 2 letters");
	}

}
