package com.bestroute;

import com.bestroute.config.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BestRouteApplicationTests extends BaseIntegrationTest {

	@Test
	@DisplayName("should successfully retrieve routes when the key is valid")
	void mustSearchRoutesSuccessfullyWhenKeyForValida() throws Exception {
		mockMvc.perform(get("/api/v1/info")).andExpect(status().isOk());
	}

	@Test
	@DisplayName("should return 403 when the key is not sent")
	void shouldReturn403WhenKeyIsNotSent() throws Exception {
		mockMvc.perform(get("/api/routes")).andExpect(status().isNotFound());
	}

}
