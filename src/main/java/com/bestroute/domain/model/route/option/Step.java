package com.bestroute.domain.model.route.option;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Step {

	private String transportType;

	private BigDecimal kilometers;

	private BigDecimal averageAmount;

	private String originCity;

	private String originState;

	private String originDeparture;

	private String originDepartureType;

	private String destinationCity;

	private String destinationState;

	private String destinationArrival;

	private String destinationArrivalType;

	private Integer order;

	private BigDecimal durationHours;

	public Step() {
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public BigDecimal getKilometers() {
		return kilometers;
	}

	public void setKilometers(BigDecimal kilometers) {
		this.kilometers = kilometers;
	}

	public BigDecimal getAverageAmount() {
		return averageAmount;
	}

	public void setAverageAmount(BigDecimal averageAmount) {
		this.averageAmount = averageAmount;
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

	public String getOriginDeparture() {
		return originDeparture;
	}

	public void setOriginDeparture(String originDeparture) {
		this.originDeparture = originDeparture;
	}

	public String getOriginDepartureType() {
		return originDepartureType;
	}

	public void setOriginDepartureType(String originDepartureType) {
		this.originDepartureType = originDepartureType;
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

	public String getDestinationArrival() {
		return destinationArrival;
	}

	public void setDestinationArrival(String destinationArrival) {
		this.destinationArrival = destinationArrival;
	}

	public String getDestinationArrivalType() {
		return destinationArrivalType;
	}

	public void setDestinationArrivalType(String destinationArrivalType) {
		this.destinationArrivalType = destinationArrivalType;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public BigDecimal getDurationHours() {
		return durationHours;
	}

	public void setDurationHours(BigDecimal durationHours) {
		this.durationHours = durationHours;
	}
}
