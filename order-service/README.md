# Order-service

After service is started you can find API description here:
http://localhost:8080/swagger-ui/index.html

To create an order you can send POST request:

```bash
curl -X 'POST' \
  'http://localhost:8080/api/order' \
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
  'http://localhost:8080/api/order' \
  -H 'accept: */*'
```
