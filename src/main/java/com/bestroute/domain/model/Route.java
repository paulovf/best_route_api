package com.bestroute.domain.model;

import com.bestroute.domain.model.route.Option;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@SuppressWarnings("unused")
@Table(name = "routes", uniqueConstraints = { @UniqueConstraint(name = "uk_route_search_cache_insensitive",
		columnNames = { "origin_city", "origin_state", "destination_city", "destination_state", "travel_date" }) })
public class Route {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@NotBlank
	@Column(name = "origin_city", nullable = false)
	private String originCity;

	@NotBlank
	@Size(min = 2, max = 2)
	@Column(name = "origin_state", nullable = false, length = 2)
	private String originState;

	@NotBlank
	@Column(name = "destination_city", nullable = false)
	private String destinationCity;

	@NotBlank
	@Size(min = 2, max = 2)
	@Column(name = "destination_state", nullable = false, length = 2)
	private String destinationState;

	@NotNull
	@Column(name = "travel_date", nullable = false)
	private OffsetDateTime travelDate;

	@NotNull
	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "api_response", nullable = false, columnDefinition = "jsonb")
	private List<Option> apiResponse;

	@CreationTimestamp
	@Column(name = "inserted_at", nullable = false, updatable = false)
	private OffsetDateTime insertedAt;

	@UpdateTimestamp
	@Column(name = "updated_at", nullable = false)
	private OffsetDateTime updatedAt;

	public Route() {
		// Default constructor for Jackson use
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getOriginCity() {
		return originCity;
	}

	public void setOriginCity(String originCity) {
		this.originCity = originCity;
	}

	public String getOriginState() {
		return originState;
	}

	public void setOriginState(String originState) {
		this.originState = originState;
	}

	public String getDestinationCity() {
		return destinationCity;
	}

	public void setDestinationCity(String destinationCity) {
		this.destinationCity = destinationCity;
	}

	public String getDestinationState() {
		return destinationState;
	}

	public void setDestinationState(String destinationState) {
		this.destinationState = destinationState;
	}

	public OffsetDateTime getTravelDate() {
		return travelDate;
	}

	public void setTravelDate(OffsetDateTime travelDate) {
		this.travelDate = travelDate;
	}

	public List<Option> getApiResponse() {
		return apiResponse;
	}

	public void setApiResponse(List<Option> apiResponse) {
		this.apiResponse = apiResponse;
	}

	public OffsetDateTime getInsertedAt() {
		return insertedAt;
	}

	public void setInsertedAt(OffsetDateTime insertedAt) {
		this.insertedAt = insertedAt;
	}

	public OffsetDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(OffsetDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
