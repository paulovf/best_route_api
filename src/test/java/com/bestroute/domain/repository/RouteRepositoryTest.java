package com.bestroute.domain.repository;

import com.bestroute.domain.model.Route;
import com.bestroute.domain.model.route.Option;
import com.bestroute.domain.model.route.option.Step;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class RouteRepositoryTest {

	@Autowired
	private RouteRepository routeRepository;

	private Route validRoute;

	@BeforeEach
	void setUp() {
		routeRepository.deleteAll();

		validRoute = createBaseValidRoute();
	}

	@Test
	@DisplayName("When passing invalid params, return error")
	void shouldThrowExceptionWhenFieldsAreNull() {
		Route invalidRoute = new Route();

		assertThrows(Exception.class, () -> {
			routeRepository.saveAndFlush(invalidRoute);
		});
	}

	@Test
	@DisplayName("When passing invalid params, return error")
	void shouldThrowExceptionWhenFieldsAreInvalid() {
		validRoute.setOriginState("SP-EXTENSO");

		assertThrows(ConstraintViolationException.class, () -> {
			routeRepository.saveAndFlush(validRoute);
		});
	}

	@Test
	@DisplayName("When option not exist, return empty list")
	void shouldReturnEmptyWhenFindWithInvalidOrNonExistentFields() {
		Optional<Route> result = routeRepository.find("Belo Horizonte", "MG", "Curitiba", "PR", OffsetDateTime.now());

		assertThat(result).isEmpty();
	}

	@Test
	@DisplayName("When passing valid params, save a route")
	void shouldSaveRouteWithValidFieldsSuccess() {
		Route savedRoute = routeRepository.saveAndFlush(validRoute);

		assertThat(savedRoute.getId()).isNotNull();
		assertThat(savedRoute.getOriginCity()).isEqualTo("São Paulo");
	}

	@Test
	@DisplayName("When passing valid params, save a option and list in order by asc")
	void shouldSaveMultipleOptionsAndStepsAndReturnThemSortedByOrderAscending() {
		Step step2 = createBaseStep("bus", 2);
		Step step1 = createBaseStep("plane", 1);

		Option option2 = createBaseOption("Opção mais demorada", 2, List.of(step2));
		Option option1 = createBaseOption("Opção mais rápida", 1, List.of(step1));

		validRoute.setApiResponse(new ArrayList<>(List.of(option2, option1)));

		Route savedRoute = routeRepository.saveAndFlush(validRoute);

		Route retrievedRoute = routeRepository.findById(savedRoute.getId()).orElseThrow();
		List<Option> optionsFromDb = retrievedRoute.getApiResponse();

		optionsFromDb.sort(Comparator.comparing(Option::getOrder));

		assertThat(optionsFromDb.get(0).getOrder()).isEqualTo(1);
		assertThat(optionsFromDb.get(1).getOrder()).isEqualTo(2);

		assertThat(optionsFromDb.getFirst().getDescription()).isEqualTo("Opção mais rápida");
	}

	@Test
	@DisplayName("When passing valid params, return a valid route list")
	void shouldFindRouteUsingCustomRepositoryMethod() {
		routeRepository.saveAndFlush(validRoute);

		Optional<Route> retrievedRouteOpt = routeRepository.find(validRoute.getOriginCity(),
				validRoute.getOriginState(), validRoute.getDestinationCity(), validRoute.getDestinationState(),
				validRoute.getTravelDate());

		assertThat(retrievedRouteOpt).isPresent();
		Route retrievedRoute = retrievedRouteOpt.get();
		assertThat(retrievedRoute.getOriginCity()).isEqualTo("São Paulo");
		assertThat(retrievedRoute.getDestinationCity()).isEqualTo("Rio de Janeiro");
	}

	private Route createBaseValidRoute() {
		Step step = createBaseStep("car", 1);
		Option option = createBaseOption("Rota Padrão", 1, List.of(step));

		Route route = new Route();
		route.setOriginCity("São Paulo");
		route.setOriginState("SP");
		route.setDestinationCity("Rio de Janeiro");
		route.setDestinationState("RJ");
		route.setTravelDate(OffsetDateTime.parse("2026-12-25T12:00:00Z"));
		route.setApiResponse(List.of(option));
		return route;
	}

	private Option createBaseOption(String desc, int order, List<Step> steps) {
		Option option = new Option();
		option.setDescription(desc);
		option.setOrder(order);
		option.setTotalKilometers(100);
		option.setTotalAmount(new BigDecimal("50.00"));
		option.setSteps(steps);
		return option;
	}

	private Step createBaseStep(String transport, int order) {
		Step step = new Step();
		step.setTransportType(transport);
		step.setKilometers(new BigDecimal("100.0"));
		step.setAverageAmount(new BigDecimal("50.00"));
		step.setOriginCity("Cidade A");
		step.setOriginState("AA");
		step.setOriginDeparture("Terminal A");
		step.setDestinationCity("Cidade B");
		step.setDestinationState("BB");
		step.setDestinationArrival("Terminal B");
		step.setOrder(order);
		return step;
	}

}
