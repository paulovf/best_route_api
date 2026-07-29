package com.bestroute.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = DifferentOriginAndDestinationValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentOriginAndDestination {

	String message() default "The origin and destination cannot be exactly the same";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
