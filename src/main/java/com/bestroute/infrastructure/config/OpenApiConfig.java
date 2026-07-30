package com.bestroute.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

	@Value("${app.version}")
	private String appVersion;

	@Bean
	public OpenAPI customOpenAPI() {
		final String securitySchemeName = "ApiKeyAuth";

		return new OpenAPI()
			.info(new Info().title("BestRoute API")
				.version(appVersion)
				.description("API for search and calculate route between origin and destination cities."))
			.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
			.components(new Components().addSecuritySchemes(securitySchemeName,
					new SecurityScheme().name("X-API-KEY")
						.type(SecurityScheme.Type.APIKEY)
						.in(SecurityScheme.In.HEADER)
						.description("Enter a valid API key in the field below to unlock and test the endpoints.")));
	}

}
