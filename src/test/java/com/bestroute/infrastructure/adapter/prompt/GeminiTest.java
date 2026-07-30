package com.bestroute.infrastructure.adapter.prompt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GeminiTest {

	private Gemini gemini;

	@BeforeEach
	void setUp() {
		gemini = new Gemini();
	}

	@Test
	@DisplayName("Should successfully replace all placeholders in the template text")
	void shouldBuildPromptWithReplacedPlaceholders() {
		String templateMock = "Create a route from {originCity} ({originState}) to {destinationCity} ({destinationState}) on {travelDate}.";

		Resource resourceMock = new ByteArrayResource(templateMock.getBytes(StandardCharsets.UTF_8));

		ReflectionTestUtils.setField(gemini, "promptResource", resourceMock);

		OffsetDateTime travelDate = OffsetDateTime.of(2026, 7, 12, 10, 0, 0, 0, ZoneOffset.UTC);

		String finalPrompt = gemini.buildPrompt("Rio de Janeiro", "RJ", "São Paulo", "SP", travelDate);

		String expectedPrompt = "Create a route from Rio de Janeiro (RJ) to São Paulo (SP) on 2026-07-12T10:00Z.";
		assertThat(finalPrompt).isEqualTo(expectedPrompt);
	}

	@Test
	@DisplayName("Should throw RuntimeException when prompt resource fails to read")
	void shouldThrowRuntimeExceptionWhenIOExceptionOccurs() throws IOException {
		Resource flawedResourceMock = mock(Resource.class);
		when(flawedResourceMock.getContentAsString(StandardCharsets.UTF_8))
			.thenThrow(new IOException("Simulated disk error or missing file"));

		ReflectionTestUtils.setField(gemini, "promptResource", flawedResourceMock);

		OffsetDateTime now = OffsetDateTime.now();

		assertThatThrownBy(() -> gemini.buildPrompt("Rio", "RJ", "Santos", "SP", now))
			.isInstanceOf(RuntimeException.class)
			.hasMessageStartingWith("Failed to read prompt template file")
			.hasCauseInstanceOf(IOException.class);
	}

}
