package com.bestroute.application.service.prompt;

import com.bestroute.application.service.ia.Gamini;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GaminiTest {

	private Gamini gamini;

	@BeforeEach
	void setUp() {
		gamini = new Gamini();
	}

	@Test
	@DisplayName("Should successfully replace all placeholders in the template text")
	void shouldBuildPromptWithReplacedPlaceholders() {
		String templateMock = "Create a route from {originCity} ({originState}) to {destinationCity} ({destinationState}) on {travelDate}.";

		Resource resourceMock = new ByteArrayResource(templateMock.getBytes(StandardCharsets.UTF_8));

		ReflectionTestUtils.setField(gamini, "promptResource", resourceMock);

		OffsetDateTime travelDate = OffsetDateTime.of(2026, 7, 12, 10, 0, 0, 0, ZoneOffset.UTC);

		String finalPrompt = gamini.buildPrompt("Rio de Janeiro", "RJ", "São Paulo", "SP", travelDate);

		String expectedPrompt = "Create a route from Rio de Janeiro (RJ) to São Paulo (SP) on 2026-07-12T10:00Z.";
		assertThat(finalPrompt).isEqualTo(expectedPrompt);
	}

	@Test
	@DisplayName("Should throw RuntimeException when prompt resource fails to read")
	void shouldThrowRuntimeExceptionWhenIOExceptionOccurs() throws IOException {
		Resource flawedResourceMock = Mockito.mock(Resource.class);
		Mockito.when(flawedResourceMock.getContentAsString(StandardCharsets.UTF_8))
			.thenThrow(new IOException("Simulated disk error or missing file"));

		ReflectionTestUtils.setField(gamini, "promptResource", flawedResourceMock);

		OffsetDateTime now = OffsetDateTime.now();

		assertThatThrownBy(() -> gamini.buildPrompt("Rio", "RJ", "Santos", "SP", now))
			.isInstanceOf(RuntimeException.class)
			.hasMessageStartingWith("Failed to read prompt template file")
			.hasCauseInstanceOf(IOException.class);
	}

}
