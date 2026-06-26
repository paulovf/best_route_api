package com.bestroute.infraestructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class RouteGenerationException extends RuntimeException {

	public RouteGenerationException(String message) {
		super(message);
	}

}
