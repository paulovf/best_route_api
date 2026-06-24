package com.bestroute.application.service;

import com.bestroute.application.dto.RouteRequest;
import com.bestroute.application.dto.RouteResponse;
import com.bestroute.domain.repository.RouteRepository;
import com.bestroute.domain.model.Route;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class RouteService {

	private final RouteRepository routeRepository;

	public RouteService(RouteRepository routeRepository) {
		this.routeRepository = routeRepository;
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
		Route newRoute = createRote(request);

		// Colocamos uma lista vazia temporária por enquanto, já que não chamamos a IA
		// ainda
		newRoute.setApiResponse(new ArrayList<>());

		Route savedRoute = routeRepository.save(newRoute);

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
