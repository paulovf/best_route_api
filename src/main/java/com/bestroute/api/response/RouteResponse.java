package com.bestroute.api.response;

import com.bestroute.domain.model.route.Option;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record RouteResponse(UUID id, String originCity, String originState, String destinationCity,
		String destinationState, OffsetDateTime travelDate, List<Option> options) {
}
