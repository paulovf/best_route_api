# BestRoute API 🚀

BestRoute API is an intelligent, high-performance solution built for route calculation, optimization, and cache management in logistics workflows. The application efficiently centralizes itinerary lookups, mitigating redundant third-party API calls through a robust persistence and validation layer.

## 🛠️ Tech Stack & Tools

The project leverages modern, production-grade tools from the Java ecosystem:

- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.3.5
- **AI Integration:** Spring AI (Official Spring framework for AI orchestration)
- **LLM / AI Model:** Google Gemini (Powering intelligent route and itinerary generation)
- **Persistence & Migrations:** Spring Data JPA & Flyway (Database schema versioning)
- **Database:** PostgreSQL (Hosted via Supabase with Connection Pooling enabled)
- **Security:** Spring Security (Custom X-API-KEY header authentication filter)
- **Documentation:** Springdoc OpenAPI 3 (Swagger UI)
- **Cloud/Hosting:** Render (Web Services)

---

## 🎨 Core Features

1. **Cost & API Call Optimization:** Intelligent search caching based on origin city, origin state, destination city, destination state, and travel date to prevent duplicate external requests.
2. **Advanced Security:** Malicious and unauthorized traffic blocking via strict validation of the incoming `X-API-KEY` request header.
3. **Fault Resilience:** A global error handling system (`GlobalExceptionHandler`) providing standardized JSON responses for edge cases, such as invalid input data (HTTP 400) or business rule violations like route generation failures (HTTP 422).
4. **Production Ready:** Out-of-the-box automatic database migrations via Flyway and native handling of dynamic environment variables injected by PaaS platforms (like Render).

---

## ⚙️ Required Environment Variables

To run the project locally or in production, ensure the following environment variables are set in your system:

| Variable | Description | Example |
| :--- | :--- | :--- |
| `SPRING_DATASOURCE_URL` | JDBC connection URL for the PostgreSQL database | `jdbc:postgresql://host:6543/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `your_secure_password` |
| `GEMINI_API_KEY` | Google AI Studio API Key for Gemini integration | `AIzaSyYourGeminiKeyXYZ` |
| `API_KEY_SECRET` | Secret master key token expected in the request header | `master-key-token-xyz` |
---

## 🚀 How to Run Locally

### Prerequisites
- **JDK 21** installed (required for CLI and IDE execution).
- **Maven 3.9+** (or use the included `./mvnw` wrapper).
- **Docker Desktop** installed and running (required for containerized execution).
- **IntelliJ IDEA** (Community or Ultimate edition).
- A running PostgreSQL instance (local or remote) and a valid Google Gemini API Key.

### 1. Clone the Repository
```bash
git clone https://github.com/your-username/bestroute-api.git
cd bestroute-api
```

### 2. Choose Your Execution Method

#### 💻 Option A: Command Line (Maven)

1. Set the required environment variables in your terminal:
   ```bash
   export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bestroute
   export SPRING_DATASOURCE_USERNAME=postgres
   export SPRING_DATASOURCE_PASSWORD=root
   export GEMINI_API_KEY=AIzaSyYourGeminiKeyXYZ
   export API_KEY_SECRET=your_local_secret_key
   ```

2. Run the application using the Maven wrapper:
   ```bash
   ./mvnw clean spring-boot:run
   ```

---

#### 🧠 Option B: IntelliJ IDEA

1. Open IntelliJ IDEA, click on **Open**, navigate to the project folder, and select the `pom.xml` file. Open it as a project.
2. Wait for IntelliJ to finish indexing and syncing the Maven dependencies.
3. Locate the main class `BastRouteApplication.java` inside `src/main/java/com/bestroute/`.
4. Click the **Run** menu at the top or the dropdown next to the green play button, and select **Edit Configurations...**
5. In the **Environment variables** field, click the folder/document icon on the right to open the key-value pair builder, and add your variables:
   * `SPRING_DATASOURCE_URL` = `jdbc:postgresql://localhost:5432/bestroute`
   * `SPRING_DATASOURCE_USERNAME` = `postgres`
   * `SPRING_DATASOURCE_PASSWORD` = `root`
   * `GEMINI_API_KEY` = `AIzaSyYourGeminiKeyXYZ`
   * `API_KEY_SECRET` = `your_local_secret_key`
6. Click **Apply** and then **OK**.
7. Click the green **Run** (Play) button or press `Shift + F10` to start the API.

---

#### 🐳 Option C: Docker Container

1. Build the Docker image from the root directory of the project (where the `Dockerfile` is located):
   ```bash
   docker compose build
   ```

2. Run the container:
   ```bash
   docker compose up api
   ```

> 💡 **Networking Note for Docker:** If your PostgreSQL database is running natively on your host machine (and not inside another Docker container), you must change `localhost` to `host.docker.internal` in the `SPRING_DATASOURCE_URL` variable. This allows the isolated container to communicate with your local machine's network.

---

## 🧪 Running Tests

To ensure application stability and verify that the routing logic, caching mechanism, and security filters work as expected, you can run the test suite using either the terminal or your IDE.

### 💻 Option A: Command Line (Maven)
Execute the following command in the project's root directory to run all automated tests (Unit and Integration):
```bash
./mvnw test
```

### 🧠 Option B: IntelliJ IDEA
1. Open the project explorer and navigate to `src/test/java`.
2. Right-click on the `java` folder (to run all tests) or right-click a specific test class.
3. Select **Run 'All Tests'** (or use the shortcut `Ctrl + Shift + F10` on Windows/Linux, `Cmd + Shift + R` on macOS).

### 🐳 Option C: Docker Container (Docker Compose)
If you manage an isolated environment for testing using Docker Compose, you can trigger an interactive shell session inside the test container and target specific scopes:

1. Spin up the container and open its internal terminal session:
   ```bash
   docker compose run --rm tests bash
   ```

2. Once inside the container's environment, execute any of the following standard Maven commands depending on your need:
   * **Run the entire test suite:**
     ```bash
     mvn test
     ```
   * **Run a single specific test class:**
     ```bash
     mvn test -Dtest=RouteRepositoryTest
     ```
   * **Run a single specific test method inside a class:**
     ```bash
     mvn test -Dtest=RouteRepositoryTest#shouldThrowExceptionWhenFieldsAreNull
     ```

---

## 📖 API Documentation (Endpoints)

Once the application is running, you can access the live, interactive Swagger documentation at:
👉 **`http://localhost:8080/swagger-ui/index.html`**

> 💡 **Authentication Note:** To test authenticated endpoints through Swagger, click the green **Authorize** button in the top-right corner and paste the value corresponding to your `API_KEY_SECRET`.

### Key Endpoints

#### 1. Search / Create Optimized Route
- **Endpoint:** `POST /api/v1/routes/search`
- **Headers:** `X-API-KEY: your_local_secret_key`
- **Example Error Response (422 Unprocessable Entity):**
  ```json
  {
      "timestamp": "2026-06-26T14:55:57.962",
      "status": 422,
      "error": "Unprocessable Entity",
      "message": "Unable to generate a valid itinerary between São Paulo (SP) and Alface (SP).",
      "path": "/api/v1/routes/search"
  }
  ```

#### 2. System Information
- **Endpoint:** `GET /api/v1/info`
- **Headers:** `X-API-KEY: your_local_secret_key`
- **Response:** Operational data and status of the BestRoute ecosystem.