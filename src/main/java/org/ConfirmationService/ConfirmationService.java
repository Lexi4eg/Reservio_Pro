package org.ConfirmationService;

import org.Kafka.KafkaService;
import org.Logging.LoggingService;

public class ConfirmationService {
    LoggingService loggingService = new LoggingService();

    public void sendConfirmation( ConfirmationObject confirmation) {
        KafkaService kafkaService = new KafkaService();
        kafkaService.sendConfirmation(confirmation);
        loggingService.log("1", "Confirmation sent: " + confirmation);
    }
}
