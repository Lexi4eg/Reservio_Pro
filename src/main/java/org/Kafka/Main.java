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
        ReservationObject reservationObject = new ReservationObject(
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

        kafkaService.sendReservation("reservations", reservationObject);
        kafkaService.close();

        System.out.println("Message sent to Kafka topic");
        System.out.println("Key: " + key);
        System.out.println("Reservation: " + reservationObject);
        System.out.println("Topic: reservations" + reservationObject.getEmail());

        try {
            DatabaseService databaseService = new DatabaseService();
            KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(databaseService, objectMapper);
            kafkaConsumerService.consumeReservations("reservations");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}