package org.Kafka;

import org.DatabaseService.DatabaseService;

import java.sql.SQLException;

public class KafkaClient {
    public static void main(String[] args) {
        try {
            DatabaseService databaseService = new DatabaseService();
            KafkaConsumerService consumerService = new KafkaConsumerService(databaseService);
            consumerService.consumeMessages("message");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}