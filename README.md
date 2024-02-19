# Saga pattenr in microservice
you can read descriptions in [baeldung](https://www.baeldung.com/cs/saga-pattern-microservices)

The project contains the following subprojects: 

1. Gateway-service
2. Discovery-service (combined with Config-service)
3. Auth-service
4. Order-service
4. Payment-service
4. Delivery-service

All services worked as demonstration

## Environment

To run PostgreSQL with Kafka you have to execute the command in project root Report-service:
```
$sudo docker-compose up -d
```

Also, you have convenient [UI for Apache Kafka](https://github.com/provectus/kafka-ui) at URL

http://localhost:9999/

Now you can run application services from your IDE in this order 
- Discovery
- Auth-service
- Order-service
- Delivery-service
- Inventory-service
- Payment-service
- Gateway-service

After that you can find Swagger UI of every service at URL.

http://localhost:8080/some-prefix/swagger-ui/index.html

Don't forget to put actual port number and prefix of service

At Gateway, you can find joined Swagger UI
http://localhost:9090/swagger-ui.html

## Basic interactions

You can use those curl commands, or you can do all that with Swagger UI

### Authentication

To create user use this request to auth service 
```bash
curl -X 'POST' \
  'http://localhost:8083/auth/user/signup' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "some_user",
  "password": "some_pass"
}'
```

After that you can get a token
```bash
curl -X 'POST' \
  'http://localhost:8083/auth/auth/token/generate' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "name": "some_user",
  "password": "some_pass"
}'
```

Now you can use this token to authenticate requests to other service

### Order service

To create an order you can send POST request:

```bash
curl -X 'POST' \
  'http://localhost:9090/api/order' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
  "description": "string",
  "departureAddress": "string",
  "destinationAddress": "string",
  "cost": 0
}'
```

You can list orders with GET request:

```bash
curl -X 'GET' \
  'http://localhost:9090/api/order' \
  -H 'accept: */*' \
  -H 'Bearer <put token here>'
```

You can change status of order with PATCH request:

```bash
curl -X 'PATCH' \
  'http://localhost:8080/api/order/1' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -H 'Bearer <put token here>'
  -d '{
  "status": "REGISTERED",
  "serviceName": "ORDER_SERVICE",
  "comment": "Some comment to status"
}'
```

## Running all services with docker-compose

To run all services with docker-compose use this command
```bash
 docker-compose -f docker-compose.yml -f docker-compose.services.yml up -d
```
