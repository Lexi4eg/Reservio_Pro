package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;

import java.util.Date;
import java.util.UUID;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DatabaseService databaseService = new DatabaseService();
            KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(databaseService, objectMapper);
            kafkaConsumerService.consumeReservations("reservations");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}