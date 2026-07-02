package com.bestroute.domain.repository;

import com.bestroute.domain.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RouteRepository extends JpaRepository<Route, UUID> {

	@Query(value = """
			SELECT * FROM routes r
			WHERE lower(public.immutable_unaccent(r.origin_city)) = lower(public.immutable_unaccent(:origin_city))
			  AND lower(r.origin_state) = lower(:origin_state)
			  AND lower(public.immutable_unaccent(r.destination_city)) = lower(public.immutable_unaccent(:destination_city))
			  AND lower(r.destination_state) = lower(:destination_state)
			  AND r.travel_date = :travel_date
			""",
			nativeQuery = true)
	Optional<Route> find(@Param("origin_city") String originCity, @Param("origin_state") String originState,
			@Param("destination_city") String destinationCity, @Param("destination_state") String destinationState,
			@Param("travel_date") OffsetDateTime travelDate);

}
