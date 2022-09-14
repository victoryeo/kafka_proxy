## kafka proxy
written in spring boot, translate Rest API to Kafka stream

## curl command
```
curl -X GET http://localhost:8081/api/appname
curl -X POST -H "Content-Type: application/json" -X POST -d '{"orderType":0,"price":10, "quantity":60}' http://localhost:8081/api/order
curl -X POST -H "Content-Type: application/json" -X POST -d '{"orderType":1,"price":10, "quantity":60}' http://localhost:8081/api/order
```
## maven command
#### to run
./mvnw spring-boot:run
#### to install
./mvnw clean install