package com.bestroute.api.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;

public record RouteRequest(@NotBlank(message = "The origin city is required") String originCity,

		@NotBlank(message = "The origin state is required") @Size(min = 2, max = 2,
				message = "The origin state must have 2 letters") String originState,

		@NotBlank(message = "The destination city is required") String destinationCity,

		@NotBlank(message = "The destination state is required") @Size(min = 2, max = 2,
				message = "The origin state must have 2 letters") String destinationState,

		@NotNull(message = "The travel date is required") OffsetDateTime travelDate) {
}
