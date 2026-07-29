package com.bestroute.api.validation;

import com.bestroute.api.request.RouteRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DifferentOriginAndDestinationValidatorTest {

	private DifferentOriginAndDestinationValidator validator;

	private ConstraintValidatorContext context;

	@BeforeEach
	void setUp() {
		validator = new DifferentOriginAndDestinationValidator();
		context = null;
	}

	@Test
	@DisplayName("Should return valid (true) when the request object is null")
	void shouldReturnTrueWhenRequestIsNull() {
		assertTrue(validator.isValid(null, context));
	}

	@Test
	@DisplayName("Should return valid (true) when there are null fields (leaves rejection to @NotBlank)")
	void shouldReturnTrueWhenFieldsAreNull() {
		RouteRequest request = new RouteRequest(null, "SP", "Rio de Janeiro", "RJ", OffsetDateTime.now());

		assertTrue(validator.isValid(request, context));
	}

	@Test
	@DisplayName("Should return valid (true) when origin and destination are completely different")
	void shouldReturnTrueWhenOriginAndDestinationAreDifferent() {
		RouteRequest request = new RouteRequest("São Paulo", "SP", "Rio de Janeiro", "RJ", OffsetDateTime.now());

		assertTrue(validator.isValid(request, context));
	}

	@Test
	@DisplayName("Should return valid (true) when it is the same city but different states")
	void shouldReturnTrueWhenSameCityDifferentState() {
		RouteRequest request = new RouteRequest("Campinas", "SP", "Campinas", "GO", OffsetDateTime.now());

		assertTrue(validator.isValid(request, context));
	}

	@Test
	@DisplayName("Should return valid (true) when it is the same state but different cities")
	void shouldReturnTrueWhenSameStateDifferentCity() {
		RouteRequest request = new RouteRequest("São Paulo", "SP", "Campinas", "SP", OffsetDateTime.now());

		assertTrue(validator.isValid(request, context));
	}

	@Test
	@DisplayName("Should return invalid (false) when origin and destination city and state are exactly the same")
	void shouldReturnFalseWhenOriginAndDestinationAreExactlyTheSame() {
		RouteRequest request = new RouteRequest("São Paulo", "SP", "São Paulo", "SP", OffsetDateTime.now());

		assertFalse(validator.isValid(request, context));
	}

	@Test
	@DisplayName("Should return invalid (false) when they are the same even with extra whitespaces and case differences")
	void shouldReturnFalseWhenOriginAndDestinationAreTheSameIgnoringCaseAndSpaces() {
		RouteRequest request = new RouteRequest(" são paulo ", "sp", "SÃO PAULO", " SP", OffsetDateTime.now());

		assertFalse(validator.isValid(request, context));
	}

}
