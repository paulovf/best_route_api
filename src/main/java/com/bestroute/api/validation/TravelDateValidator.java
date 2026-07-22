package com.bestroute.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;

public class TravelDateValidator implements ConstraintValidator<ValidTravelDate, OffsetDateTime> {

	@Override
	public boolean isValid(OffsetDateTime value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		}
		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		LocalDate travelDate = value.toLocalDate();
		LocalDate maxDate = today.plusYears(1);

		return !travelDate.isBefore(today) && travelDate.isBefore(maxDate);
	}

}
