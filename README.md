# Quantity Measurement App (Backend Only)

Spring Boot backend for quantity conversion, comparison, arithmetic operations, JWT authentication, optional Google OAuth2 login, and operation history.

## Run

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

## API Docs

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/api-docs`

## Main Endpoints

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`
- `GET /api/v1/auth/me`
- `GET /api/v1/metadata/measurements`
- `POST /api/v1/quantities/{add|subtract|multiply|divide|compare|convert}`
- `GET /api/v1/quantities/history/operation/{operation}`
- `GET /api/v1/quantities/history/type/{measurementType}`
- `GET /api/v1/quantities/history/errored`
- `GET /api/v1/quantities/count/{operation}`

## Default Local User

- Username: `admin`
- Password: `Admin@12345`
