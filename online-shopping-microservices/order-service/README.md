# Order Service

Spring Boot REST API for creating and retrieving orders. It calls Product Service
over HTTP to verify a product and capture the product name and price at order time.

## Endpoints
- `POST /orders`
- `GET /orders`
- `GET /orders/{id}`

## Run locally
Requires Java 17, Maven, and PostgreSQL. Configure the database and
`PRODUCT_SERVICE_URL`, then run `mvn spring-boot:run`.
