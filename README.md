## kafka proxy
written in spring boot, translate Rest API to Kafka stream

## Setup Kafka 

### Installation (Windows)

1. Go to https://kafka.apache.org/downloads and download latest version
2. Extract the zip file
3. Rename the file to simple name e.g : kafka

### Start zookeeper (Windows)

```
cd kafka
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```

### Start Kafka broker (Windows)
```
.\bin\windows\kafka-server-start.bat .\config\server.properties
```

## curl command
```
curl -X GET http://localhost:8081/api/appname
```
Sell order
```
curl -X POST -H "Content-Type: application/json" -X POST -d '{"orderID":"1", "tokenType":"stock", "tokenName":"AAPL","orderType":0,"price":10, "quantity":60}' http://localhost:8081/api/order
```
Buy order
```
curl -X POST -H "Content-Type: application/json" -X POST -d '{"orderID":"2", "tokenType":"stock", "tokenName":"AAPL","orderType":1,"price":10, "quantity":60}' http://localhost:8081/api/order
```

With access token 
````
curl -X POST -H "Content-Type: application/json" -H "Access-Token: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6ImpvaG4iLCJpYXQiOjE2NjUwMjA3OTYsImV4cCI6MTY2NTAyNDM5Nn0.iM5bea67KAE3WHHN-mNpip4ETKVeEbVYvMiEoZ6vA7c" -X POST -d '{"orderID":"3049", "tokenType":"bond", "tokenName":"AAPLE","orderType":0,"price":10, "quantity":60}' http://localhost:8081/api/order
````

Get price list of all tokens
```
curl -X GET http://localhost:8081/price/list
```
Get price by token name
```
curl -X GET http://localhost:8080/price/token/AAPL
```
get price by id
```
curl -X GET http://localhost:8080/price/1
```
## maven command
#### to run
./mvnw spring-boot:run
#### to install
./mvnw clean install
### to test
./mvnw test
### to run single test
./mvnw test -Dtest="KafkaConsumerTests"
### check dependency version
./mvnw dependency:tree

### build an executable jar package
mvn clean package
### run using java
java -jar my-application.jar
### create .mvn directory with needed jar inside
./mvn wrapper:wrapper