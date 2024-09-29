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

    public void log(String level, String message) {
        if (!"DEBUG".equalsIgnoreCase(level)) {
            producer.send(new ProducerRecord<>("log", level + ": " + message));
        }
    }

    public void close() {
        producer.close();
    }
}