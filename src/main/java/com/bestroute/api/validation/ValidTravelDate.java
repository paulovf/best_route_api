package com.bestroute.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TravelDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTravelDate {

	String message() default "The travel date must be today or within 1 year from now";

	Class<?>[] groups() default {};

	@SuppressWarnings("unused")
	Class<? extends Payload>[] payload() default {};

}
