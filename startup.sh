#!/bin/bash

# Start the first service
java -cp /app/spring-boot-application.jar org.Logging.LoggingOutputService &
echo "Started LoggingOutputService"

# Start the second service
java -cp /app/spring-boot-application.jar org.HttpClientEndpoint.HttpClientService &
echo "Started HttpClientEndpoint"

# Start the third service
java -cp /app/spring-boot-application.jar org.Kafka.KafkaMain &
echo "Started Kafka"

# Wait for all background processes to finish
wait