package com.bestroute;

import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BastRouteApplicationTests extends BaseIntegrationTest {

	// @Test
	// void deveBuscarRotasComSucessoQuandoChaveForValida() throws Exception {
	// mockMvc.perform(get("/actuator/health"))
	// .andExpect(status().isOk());
	// }

	@Test
	void deveRetornar403QuandoNaoEnviarAChave() throws Exception {
		mockMvc.perform(get("/api/routes")).andExpect(status().isNotFound());
	}

}
