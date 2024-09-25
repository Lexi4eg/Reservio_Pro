package org.ConfirmationService;

import org.Kafka.KafkaService;

public class ConfirmationService {

    public String topic = "confirmations";
    public void sendConfirmation( ConfirmationObject confirmation) {
        KafkaService kafkaService = new KafkaService();
        kafkaService.sendConfirmation(confirmation);
    }
}
