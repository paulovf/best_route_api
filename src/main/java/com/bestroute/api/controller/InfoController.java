package com.bestroute.api.controller;

import com.bestroute.api.response.ApiInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@RestController
@RequestMapping("/api/v1/info")
public class InfoController {

	@Value("${app.version}")
	private String appVersion;

	@GetMapping
	public ResponseEntity<ApiInfoResponse> getApiInfo() {
		ApiInfoResponse info = new ApiInfoResponse("BestRoute API", this.appVersion, "UP",
				LocalDateTime.now(ZoneOffset.UTC));

		return ResponseEntity.ok(info);
	}

}