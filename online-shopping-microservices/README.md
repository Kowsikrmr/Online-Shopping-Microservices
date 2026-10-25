# Online Shopping Order Management — Microservices Assignment

This starter implementation contains two independent Spring Boot services:
- `product-service` on port 8081
- `order-service` on port 8082

PostgreSQL uses one server with two separate databases (`productdb` and `orderdb`).
The Order Service calls Product Service over HTTP and stores an order snapshot.

## Prerequisites
- Java 17
- Docker Desktop / Docker Engine
- Minikube and kubectl for Kubernetes deployment
- Postman (optional, for manual API testing)

## A. Run the integrated application with Docker Compose

From the project root:

```bash
docker compose -f deployment/docker-compose.yml up --build -d
docker compose -f deployment/docker-compose.yml ps
docker compose -f deployment/docker-compose.yml logs -f
```

Test the APIs at `http://localhost:8081` and `http://localhost:8082`.
Stop the services with:

```bash
docker compose -f deployment/docker-compose.yml down
```

To delete the local database volume and all saved demo data too:

```bash
docker compose -f deployment/docker-compose.yml down -v
```

## B. API examples

Create a product:

```bash
curl -i -X POST http://localhost:8081/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Wireless Mouse","description":"USB wireless mouse","price":799.00,"stockQuantity":20}'
```

List products:

```bash
curl -i http://localhost:8081/products
```

Create an order (replace productId with the ID returned by product creation):

```bash
curl -i -X POST http://localhost:8082/orders \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"quantity":2}'
```

List orders:

```bash
curl -i http://localhost:8082/orders
```

## C. Deploy to Minikube

Build the images locally first:

```bash
docker build -t product-service:1.0 ./product-service
docker build -t order-service:1.0 ./order-service
minikube start
minikube image load product-service:1.0
minikube image load order-service:1.0
kubectl apply -f deployment/kubernetes/00-postgres-config.yaml
kubectl apply -f deployment/kubernetes/01-postgres.yaml
kubectl rollout status deployment/postgres --timeout=180s
kubectl apply -f deployment/kubernetes/02-product-service.yaml
kubectl apply -f deployment/kubernetes/03-order-service.yaml
kubectl get pods,services,deployments
```

Access the services from two separate terminals:

```bash
kubectl port-forward service/product-service 8081:8081
```

```bash
kubectl port-forward service/order-service 8082:8082
```

Then use the same API examples above. View the Dashboard with:

```bash
minikube dashboard
```

If you previously ran the database and its initialization script, the script only runs
on an empty PostgreSQL data directory. For a clean local Kubernetes demo, delete and
recreate the Postgres pod/data as appropriate before changing the init SQL.

## D. Separate GitHub repositories

Create two GitHub repositories, `product-service` and `order-service`.
From each service directory, initialize and push that service independently:

```bash
git init
git add .
git commit -m "Initial implementation"
git branch -M main
git remote add origin YOUR_REPOSITORY_URL
git push -u origin main
```

Replace `YOUR_REPOSITORY_URL` with the actual URL of the repository you create.

Keep credentials out of public repositories. The demo credentials in these manifests are
only for local coursework; do not reuse them in a real deployment.

## Notes
- `ddl-auto=update` is acceptable for this learning prototype; production systems should
  use controlled schema migrations.
- The Kubernetes database manifest is a local demonstration setup without persistent
  storage. Data may be lost when the database pod is recreated.
- Product stock is checked but not reserved/decremented, so this is not a complete
  inventory or payment system.
- Run the APIs locally first, then Docker Compose, then Minikube. Troubleshoot each stage
  before proceeding to the next.
