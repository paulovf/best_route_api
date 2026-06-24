package com.bestroute.api.controller;

import com.bestroute.application.dto.ApiInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/info")
public class InfoController {

	@Value("${app.version}")
	private String appVersion;

	@GetMapping
	public ResponseEntity<ApiInfoResponse> getApiInfo() {
		ApiInfoResponse info = new ApiInfoResponse("BestRoute API", this.appVersion, "UP", LocalDateTime.now());

		return ResponseEntity.ok(info);
	}

}
