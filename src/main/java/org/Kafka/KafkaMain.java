package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;
import org.Logging.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class KafkaMain {
    private static final Logger log = LoggerFactory.getLogger(KafkaMain.class);

    public static void main(String[] args){
        ObjectMapper objectMapper = new ObjectMapper();
        LoggingService loggingService = new LoggingService();

        try {
            DatabaseService databaseService = new DatabaseService();
            KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(databaseService, objectMapper);
            kafkaConsumerService.consumeMessages("confirmations", "reservations");
        } catch (SQLException e) {
           loggingService.log("1",e.getMessage());
           e.printStackTrace();
        }
    }
}