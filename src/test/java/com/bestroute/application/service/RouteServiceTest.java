package com.bestroute.application.service;

import com.bestroute.application.dto.RouteRequest;
import com.bestroute.application.dto.RouteResponse;
import com.bestroute.domain.repository.RouteRepository;
import com.bestroute.domain.model.Route;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceTest {

  @Mock
  private RouteRepository routeRepository;

  @InjectMocks
  private RouteService routeService;

  private final OffsetDateTime now = OffsetDateTime.now();

  @Test
  @DisplayName("When route exists, return a route from database")
  void shouldReturnExistingRouteWhenFoundInDatabase() {
    RouteRequest request = createRouteRequest("São Paulo", "SP", "Rio de Janeiro", "RJ", now);

    Route existingRoute = createRoute("São Paulo", "SP");

    when(routeRepository.findByOriginCityAndOriginStateAndDestinationCityAndDestinationStateAndTravelDate(
        "São Paulo", "SP", "Rio de Janeiro", "RJ", now))
        .thenReturn(Optional.of(existingRoute));

    RouteResponse response = routeService.getOrCreateRoute(request);

    assertNotNull(response);
    assertEquals(existingRoute.getId(), response.id());
    verify(routeRepository, times(1))
        .findByOriginCityAndOriginStateAndDestinationCityAndDestinationStateAndTravelDate(any(), any(), any(),
            any(), any());
    verify(routeRepository, never()).save(any());
  }

  @Test
  @DisplayName("When route not exists, create a route in database")
  void shouldCreateAndSaveNewRouteWhenNotFoundInDatabase() {
    RouteRequest request = createRouteRequest("  Curitiba  ", "pr", "Londrina", "PR", now);

    Route savedRoute = createRoute("Curitiba", "PR");

    when(routeRepository.findByOriginCityAndOriginStateAndDestinationCityAndDestinationStateAndTravelDate(
        "Curitiba", "PR", "Londrina", "PR", now))
        .thenReturn(Optional.empty());
    when(routeRepository.save(any(Route.class))).thenReturn(savedRoute);

    RouteResponse response = routeService.getOrCreateRoute(request);

    assertNotNull(response);
    assertEquals(savedRoute.getId(), response.id());
    verify(routeRepository, times(1)).save(any(Route.class));
  }

  private RouteRequest createRouteRequest(String oCity, String oState, String dCity, String dState,
                                          OffsetDateTime travelDate) {
    return new RouteRequest(oCity, oState, dCity, dState, travelDate);
  }

  private Route createRoute(String oCity, String oState) {
    Route route = new Route();
    route.setId(UUID.randomUUID());
    route.setOriginCity(oCity);
    route.setOriginState(oState);
    route.setDestinationCity("Rio de Janeiro");
    route.setDestinationState("RJ");
    route.setTravelDate(now);
    route.setApiResponse(new ArrayList<>());

    return route;
  }

}
