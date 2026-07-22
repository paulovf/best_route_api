package com.bestroute.api.request;

import com.bestroute.api.validation.ValidTravelDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record RouteRequest(@NotBlank(message = "The origin city is required") String originCity,

		@NotBlank(message = "The origin state is required") @Size(min = 2, max = 2,
				message = "The origin state must have 2 letters") String originState,

		@NotBlank(message = "The destination city is required") String destinationCity,

		@NotBlank(message = "The destination state is required") @Size(min = 2, max = 2,
				message = "The destination state must have 2 letters") String destinationState,

		@NotNull(message = "The travel date is required") @ValidTravelDate(
				message = "The travel date must be greater than or equal to the current day and less than 1 year from now") OffsetDateTime travelDate) {
}
