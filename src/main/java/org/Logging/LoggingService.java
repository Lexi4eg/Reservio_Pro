package org.Logging;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;

public class LoggingService {
    private KafkaProducer<String, String> producer;

    public LoggingService() {
        Map<String, Object> config = new HashMap<>();
        config.put("bootstrap.servers", "localhost:9092");
        config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        config.put("acks", "all");
        config.put("retries", "0");
        config.put("linger.ms", "1");

        producer = new KafkaProducer<>(config);
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