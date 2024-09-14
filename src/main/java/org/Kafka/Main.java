package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;

import java.util.Date;
import java.util.UUID;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        KafkaService kafkaService = new KafkaService(objectMapper);

        String key = UUID.randomUUID().toString();
        ReservationObject messageObject = new ReservationObject(
                key,
                "John",
                "Doe",
                new Date(),
                4,
                "john.doe@example.com",
                "123-456-7890",
                "None",
                true,
                "A1"
        );

        kafkaService.sendReservation("message", messageObject);
        kafkaService.close();

        System.out.println("Message sent to Kafka topic");
        System.out.println("Key: " + key);
        System.out.println("Message: " + messageObject);

        try {
            DatabaseService databaseService = new DatabaseService();
            KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(databaseService);
            kafkaConsumerService.consumeMessages("message");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}