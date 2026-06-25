package com.bestroute.application.service.ia;

import com.bestroute.application.service.prompt.RoutePromptProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;

@Component
public class Gamini implements RoutePromptProvider {

	@Value("classpath:prompts/gemini-route.st")
	private Resource promptResource;

	@Override
	public String buildPrompt(String originCity, String originState, String destinationCity, String destinationState,
			OffsetDateTime travelDate) {
		try {
			String template = promptResource.getContentAsString(StandardCharsets.UTF_8);

			return template.replace("{originCity}", originCity)
				.replace("{originState}", originState)
				.replace("{destinationCity}", destinationCity)
				.replace("{destinationState}", destinationState)
				.replace("{travelDate}", travelDate.toString());

		}
		catch (IOException e) {
			throw new RuntimeException("Failed to read prompt template file", e);
		}
	}

}
