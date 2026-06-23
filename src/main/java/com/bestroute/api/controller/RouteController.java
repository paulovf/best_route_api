package com.bestroute.api.controller;

import com.bestroute.application.dto.RouteRequest;
import com.bestroute.application.dto.RouteResponse;
import com.bestroute.application.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {
  private final RouteService routeService;

  // Injeção via construtor
  public RouteController(RouteService routeService) {
    this.routeService = routeService;
  }

  @PostMapping("/search")
  public ResponseEntity<RouteResponse> searchRoute(@Valid @RequestBody RouteRequest request) {
    RouteResponse response = routeService.getOrCreateRoute(request);

    return ResponseEntity.ok(response);
  }
}
