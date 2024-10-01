package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;
import java.sql.SQLException;

public class KafkaMain {
    public static void main(String[] args){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            DatabaseService databaseService = new DatabaseService();
            KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(databaseService, objectMapper);
            kafkaConsumerService.consumeMessages("confirmations", "reservations");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}