package org.Logging;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Map;

public class LoggingService {
    private KafkaProducer<String, String> producer;

    public LoggingService() {
        producer = new KafkaProducer<>(Map.of(
                "bootstrap.servers", "localhost:9092",
                "key.serializer", "org.apache.kafka.common.serialization.StringSerializer",
                "value.serializer", "org.apache.kafka.common.serialization.StringSerializer"
        ));
    }

    public void log(String message) {
        producer.send(new ProducerRecord<>("log", message));
    }

    public void close() {
        producer.close();
    }
}