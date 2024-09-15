package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;

import java.sql.SQLException;

public class KafkaClient {
    public static void main(String[] args) {
        try {
            DatabaseService databaseService = new DatabaseService();
            ObjectMapper objectMapper = new ObjectMapper();
            KafkaConsumerService consumerService = new KafkaConsumerService(databaseService, objectMapper);
            consumerService.consumeReservations("reservations");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}