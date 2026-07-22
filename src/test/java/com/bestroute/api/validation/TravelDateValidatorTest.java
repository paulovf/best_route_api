package com.bestroute.api.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TravelDateValidatorTest {

	private final TravelDateValidator validator = new TravelDateValidator();

	@Test
	@DisplayName("Should return true when travel date is null (handled by @NotNull)")
	void shouldReturnTrueWhenTravelDateIsNull() {
		boolean isValid = validator.isValid(null, null);
		assertTrue(isValid);
	}

	@Test
	@DisplayName("Should return true when travel date is exactly today")
	void shouldReturnTrueWhenTravelDateIsToday() {
		OffsetDateTime today = OffsetDateTime.now();
		boolean isValid = validator.isValid(today, null);
		assertTrue(isValid);
	}

	@Test
	@DisplayName("Should return true when travel date is within 1 year from now")
	void shouldReturnTrueWhenTravelDateIsWithinOneYear() {
		OffsetDateTime sixMonthsFromNow = OffsetDateTime.now().plusMonths(6);
		boolean isValid = validator.isValid(sixMonthsFromNow, null);
		assertTrue(isValid);
	}

	@Test
	@DisplayName("Should return false when travel date is in the past")
	void shouldReturnFalseWhenTravelDateIsInThePast() {
		OffsetDateTime yesterday = OffsetDateTime.now().minusDays(1);
		boolean isValid = validator.isValid(yesterday, null);
		assertFalse(isValid);
	}

	@Test
	@DisplayName("Should return false when travel date is more than 1 year in the future")
	void shouldReturnFalseWhenTravelDateIsMoreThanOneYearInFuture() {
		OffsetDateTime oneYearAndOneDayFromNow = OffsetDateTime.now().plusYears(1).plusDays(1);
		boolean isValid = validator.isValid(oneYearAndOneDayFromNow, null);
		assertFalse(isValid);
	}

}
