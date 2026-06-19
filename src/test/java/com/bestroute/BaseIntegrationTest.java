package com.bestroute;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
  protected MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @Value("${app.security.api-key}")
  private String apiKey;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(context)
        .apply(springSecurity())
        .defaultRequest(get("/").header("X-API-KEY", apiKey))
        .build();
  }
}
