package com.bestroute.domain.repository;

import com.bestroute.domain.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {

	Optional<Route> findByOriginCityAndOriginStateAndDestinationCityAndDestinationStateAndTravelDate(String originCity,
			String originState, String destinationCity, String destinationState, OffsetDateTime travelDate);

}
