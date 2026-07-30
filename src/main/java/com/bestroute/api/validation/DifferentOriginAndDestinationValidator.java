package com.bestroute.api.validation;

import com.bestroute.api.request.RouteRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentOriginAndDestinationValidator
		implements ConstraintValidator<DifferentOriginAndDestination, RouteRequest> {

	@Override
	public boolean isValid(RouteRequest request, ConstraintValidatorContext context) {
		if (request == null) {
			return true;
		}

		if (request.originCity() == null || request.originState() == null || request.destinationCity() == null
				|| request.destinationState() == null) {
			return true;
		}

		boolean isSameCity = request.originCity().trim().equalsIgnoreCase(request.destinationCity().trim());
		boolean isSameState = request.originState().trim().equalsIgnoreCase(request.destinationState().trim());

		return !(isSameCity && isSameState);
	}

}
