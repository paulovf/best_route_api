package com.bestroute.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.Connection;
import java.sql.Statement;

@Configuration
@Profile("test")
class FlywayTestConfiguration {

	@Bean
	public FlywayMigrationStrategy cleanMigrateStrategy() {
		return flyway -> {
			flyway.clean();
			try (Connection conn = flyway.getConfiguration().getDataSource().getConnection();
					Statement stmt = conn.createStatement()) {

				conn.setAutoCommit(true);
				stmt.execute("DROP EXTENSION IF EXISTS unaccent CASCADE;");
				stmt.execute("CREATE EXTENSION unaccent WITH SCHEMA public;");

			}
			catch (Exception e) {
				throw new RuntimeException("Failed to reset the unaccent extension for the tests.", e);
			}
			flyway.migrate();
		};
	}

}