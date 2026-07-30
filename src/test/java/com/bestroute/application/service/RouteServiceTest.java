package com.bestroute.application.service;

import com.bestroute.api.request.RouteRequest;
import com.bestroute.api.response.RouteResponse;
import com.bestroute.application.service.prompt.RoutePromptProvider;
import com.bestroute.domain.model.Route;
import com.bestroute.domain.repository.RouteRepository;
import com.bestroute.application.exception.RouteGenerationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.retry.TransientAiException;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

	@Mock
	private RouteRepository routeRepository;

	@Mock
	private RoutePromptProvider promptProvider;

	@Mock
	private ChatClient.Builder chatClientBuilder;

	@Mock
	private ChatClient chatClient;

	@Mock
	private ChatClient.ChatClientRequestSpec requestSpec;

	@Mock
	private ChatClient.CallResponseSpec responseSpec;

	private RouteService routeService;

	private final OffsetDateTime now = OffsetDateTime.now();

	@BeforeEach
	void setUp() {
		when(chatClientBuilder.build()).thenReturn(chatClient);

		routeService = new RouteService(routeRepository, chatClientBuilder, promptProvider);
	}

	@Test
	@DisplayName("When route exists, return a route from database (Cache Hit)")
	void shouldReturnExistingRouteWhenFoundInDatabase() {
		RouteRequest request = createRouteRequest("São Paulo", "SP", "Rio de Janeiro", "RJ", now);
		Route existingRoute = createRoute("São Paulo", "SP");

		when(routeRepository.find("São Paulo", "SP", "Rio de Janeiro", "RJ", now))
			.thenReturn(Optional.of(existingRoute));

		RouteResponse response = routeService.getOrCreateRoute(request);

		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(existingRoute.getId());

		verify(chatClient, never()).prompt();
		verify(routeRepository, never()).save(any());
	}

	@Test
	@DisplayName("When route does not exist, call AI, parse cassette JSON, and save to database (Cache Miss)")
	void shouldCreateAndSaveNewRouteWhenNotFoundInDatabase() {
		RouteRequest request = createRouteRequest("Curitiba", "PR", "Londrina", "PR", now);
		Route savedRoute = createRoute("Curitiba", "PR");

		when(routeRepository.find("Curitiba", "PR", "Londrina", "PR", now)).thenReturn(Optional.empty());

		when(promptProvider.buildPrompt(any(), any(), any(), any(), any())).thenReturn("Mocked Prompt String");

		when(chatClient.prompt()).thenReturn(requestSpec);
		when(requestSpec.messages(any(UserMessage.class))).thenReturn(requestSpec);
		when(requestSpec.call()).thenReturn(responseSpec);
		String mockAiJsonCassette = """
						```json
				[
				  {
				    "description": "Best highway route avoiding traffic",
				    "total_kilometers": 430.5,
				    "total_amount": 0.0,
				    "total_duration_hours": 5.0,
				    "order": 1,
				    "steps": []
				  }
				]
				```
				""";
		when(responseSpec.content()).thenReturn(mockAiJsonCassette);

		when(routeRepository.save(any(Route.class))).thenReturn(savedRoute);

		RouteResponse response = routeService.getOrCreateRoute(request);

		assertThat(response).isNotNull();
		assertThat(response.id()).isEqualTo(savedRoute.getId());

		verify(promptProvider, times(1)).buildPrompt(any(), any(), any(), any(), any());
		verify(chatClient, times(1)).prompt();
		verify(routeRepository, times(1)).save(any(Route.class));
	}

	@Test
	@DisplayName("When AI returns 503 overloaded error, throw customized RouteGenerationException")
	void shouldThrowRouteGenerationExceptionWhenAiIsOverloaded() {
		RouteRequest request = createRouteRequest("Curitiba", "PR", "Londrina", "PR", now);

		when(routeRepository.find("Curitiba", "PR", "Londrina", "PR", now)).thenReturn(Optional.empty());

		when(promptProvider.buildPrompt(any(), any(), any(), any(), any())).thenReturn("Mocked Prompt String");

		when(chatClient.prompt()).thenReturn(requestSpec);
		when(requestSpec.messages(any(UserMessage.class))).thenReturn(requestSpec);
		when(requestSpec.call()).thenReturn(responseSpec);
		when(responseSpec.content()).thenThrow(new TransientAiException("AI Server Overloaded"));

		assertThatThrownBy(() -> routeService.getOrCreateRoute(request)).isInstanceOf(RouteGenerationException.class)
			.hasMessage("AI servers are currently experiencing high demand. Please try again in a few moments.");

		verify(routeRepository, never()).save(any());
	}

	private RouteRequest createRouteRequest(String oCity, String oState, String dCity, String dState,
			OffsetDateTime travelDate) {
		return new RouteRequest(oCity, oState, dCity, dState, travelDate);
	}

	private Route createRoute(String oCity, String oState) {
		Route route = new Route();
		route.setId(UUID.randomUUID());
		route.setOriginCity(oCity.trim());
		route.setOriginState(oState.toUpperCase().trim());
		route.setDestinationCity("Rio de Janeiro");
		route.setDestinationState("RJ");
		route.setTravelDate(now);
		route.setApiResponse(new ArrayList<>());
		return route;
	}

}
