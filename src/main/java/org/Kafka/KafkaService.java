package org.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Properties;

public class KafkaService {

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper;

    public KafkaService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void sendMessage(String topic, MessageObject message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            producer.send(new ProducerRecord<>(topic, jsonMessage));
            producer.flush();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
}