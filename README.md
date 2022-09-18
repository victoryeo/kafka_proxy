## kafka proxy
written in spring boot, translate Rest API to Kafka stream

## Setup Kafka 

### Installation (Windows)

1. Go to https://kafka.apache.org/downloads and download latest version
2. Extract the zip file
3. Rename the file to simple name e.g : kafka

### Start zookeeper

```
cd kafka
.\bin\windows\zookeeper-server-start.bat .\config\server.properties
```

### Start Kafka broker
```
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

## curl command
```
curl -X GET http://localhost:8081/api/appname
```
Sell order
```
curl -X POST -H "Content-Type: application/json" -X POST -d '{"type":"stock", "name":"AAPL","orderType":0,"price":10, "quantity":60}' http://localhost:8081/api/order
```
Buy order
```
curl -X POST -H "Content-Type: application/json" -X POST -d '{"type":"stock", "name":"AAPL","orderType":1,"price":10, "quantity":60}' http://localhost:8081/api/order
```
## maven command
#### to run
./mvnw spring-boot:run
#### to install
./mvnw clean install