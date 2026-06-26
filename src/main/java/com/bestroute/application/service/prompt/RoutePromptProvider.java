package com.bestroute.application.service.prompt;

import java.time.OffsetDateTime;

public interface RoutePromptProvider {

	String buildPrompt(String originCity, String originState, String destinationCity, String destinationState,
			OffsetDateTime travelDate);

}
