# Product Service

Spring Boot REST API for creating and retrieving products.

## Endpoints
- `POST /products`
- `GET /products`
- `GET /products/{id}`

## Run locally
Requires Java 17, Maven, and PostgreSQL. Set `SPRING_DATASOURCE_URL`,
`SPRING_DATASOURCE_USERNAME`, and `SPRING_DATASOURCE_PASSWORD` if your
local database settings differ, then run `mvn spring-boot:run`.
