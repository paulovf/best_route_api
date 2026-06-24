package com.bestroute.infraestructure.exception;

import com.bestroute.application.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String snakeCaseFieldName = fieldName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
			String errorMessage = error.getDefaultMessage();
			errors.put(snakeCaseFieldName, errorMessage);
		});

		// Monta o DTO de resposta customizado
		ValidationErrorResponse errorResponse = new ValidationErrorResponse(LocalDateTime.now(),
				HttpStatus.BAD_REQUEST.value(), "Request fields invalids", errors);

		return ResponseEntity.badRequest().body(errorResponse);
	}

}
