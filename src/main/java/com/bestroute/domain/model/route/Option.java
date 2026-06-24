package com.bestroute.domain.model.route;

import com.bestroute.domain.model.route.option.Step;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.math.BigDecimal;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Option {

	private Integer totalKilometers;

	private BigDecimal totalAmount;

	private Integer order;

	private String description;

	private List<Step> steps;

	public Option() {
	}

	public Integer getTotalKilometers() {
		return totalKilometers;
	}

	public void setTotalKilometers(Integer totalKilometers) {
		this.totalKilometers = totalKilometers;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Step> getSteps() {
		return steps;
	}

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

}
