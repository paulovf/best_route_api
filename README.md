# BestRoute API 🚀

BestRoute API is an intelligent, high-performance solution built for route calculation, optimization, and cache management in logistics workflows. The application efficiently centralizes itinerary lookups, mitigating redundant third-party API calls through a robust persistence and validation layer.

## 🛠️ Tech Stack & Tools

The project leverages modern, production-grade tools from the Java ecosystem:

- **Language:** Java 21 (LTS)
- **Framework:** Spring Boot 3.3.5
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
| `PORT` | The port on which the embedded Tomcat server will run *(Injected automatically by Render)* | `8080` |
| `SPRING_DATASOURCE_URL` | JDBC connection URL for the PostgreSQL database | `jdbc:postgresql://host:6543/postgres` |
| `SPRING_DATASOURCE_USERNAME` | Database username | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Database password | `your_secure_password` |
| `API_KEY_SECRET` | Secret master key token expected in the request header | `master-key-token-xyz` |

---

## 🚀 How to Run Locally

### Prerequisites
- JDK 21 installed.
- Maven 3.9+ or the included project wrapper.
- A running PostgreSQL database instance (local or remote via Supabase).

### Step-by-Step Guide

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/your-username/bestroute-api.git](https://github.com/your-username/bestroute-api.git)
   cd bestroute-api