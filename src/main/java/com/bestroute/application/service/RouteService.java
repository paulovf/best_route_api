package com.bestroute.application.service;

import com.bestroute.api.request.RouteRequest;
import com.bestroute.api.response.RouteResponse;
import com.bestroute.application.service.prompt.RoutePromptProvider;
import com.bestroute.domain.repository.RouteRepository;
import com.bestroute.domain.model.Route;
import com.bestroute.infraestructure.exception.RouteGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RouteService {

	private final RouteRepository routeRepository;

	private final ChatClient chatClient;

	private final RoutePromptProvider promptProvider;

	public RouteService(RouteRepository routeRepository, ChatClient.Builder chatClientBuilder,
	                    RoutePromptProvider promptProvider) {
		this.routeRepository = routeRepository;
		this.chatClient = chatClientBuilder.build();
		this.promptProvider = promptProvider;
	}

	@Transactional
	public RouteResponse getOrCreateRoute(RouteRequest request) {
		Optional<Route> existingRoute = routeRepository
				.findByOriginCityAndOriginStateAndDestinationCityAndDestinationStateAndTravelDate(
						request.originCity().trim(), request.originState().toUpperCase().trim(),
						request.destinationCity().trim(), request.destinationState().toUpperCase().trim(),
						request.travelDate());

		return existingRoute.map(this::mapToResponse).orElseGet(() -> searchRote(request));
	}

	private RouteResponse mapToResponse(Route route) {
		return new RouteResponse(route.getId(), route.getOriginCity(), route.getOriginState(),
				route.getDestinationCity(), route.getDestinationState(), route.getTravelDate(), route.getApiResponse());
	}

	private RouteResponse searchRote(RouteRequest request) {
		String prompt = this.promptProvider.buildPrompt(request.originCity(), request.originState(),
				request.destinationCity(), request.destinationState(), request.travelDate());

		String jsonResponse = chatClient.prompt()
				.messages(new UserMessage(prompt))
				.call()
				.content();

		// 1. Declaramos as variáveis no escopo principal do método
		List<Map<String, Object>> aiSuggestions = null;
		String cleanJson = null;

		if (jsonResponse != null && !jsonResponse.isBlank()) {
			try {
				// 2. Remove as marcações de markdown do JSON
				cleanJson = jsonResponse.replaceAll("```json|```", "").trim();

				ObjectMapper objectMapper = new ObjectMapper();
				aiSuggestions = objectMapper.readValue(cleanJson, new TypeReference<List<Map<String, Object>>>() {});
			} catch (Exception e) {
				throw new RouteGenerationException("Failed to parse AI response into JSON structural map", e);
			}
		}

		// Validação de segurança
		if (aiSuggestions == null || aiSuggestions.isEmpty() || cleanJson == null) {
			throw new RouteGenerationException(
					"Unable to generate a valid itinerary between %s (%s) and %s (%s).".formatted(request.originCity(),
							request.originState(), request.destinationCity(), request.destinationState()));
		}

		// 3. Criamos a entidade e injetamos o JSON limpo que agora está acessível
		Route routeToSave = createRote(request);
		routeToSave.setApiResponse(cleanJson); // 👈 Escopo corrigido com sucesso!

		Route savedRoute = routeRepository.save(routeToSave);

		return mapToResponse(savedRoute);
	}

	private Route createRote(RouteRequest request) {
		Route newRoute = new Route();
		newRoute.setOriginCity(request.originCity().trim());
		newRoute.setOriginState(request.originState().toUpperCase().trim());
		newRoute.setDestinationCity(request.destinationCity().trim());
		newRoute.setDestinationState(request.destinationState().toUpperCase().trim());
		newRoute.setTravelDate(request.travelDate());

		return newRoute;
	}

}