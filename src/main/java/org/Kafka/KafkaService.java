package org.Kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.ConfirmationService.ConfirmationObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Properties;

public class KafkaService {

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper;

    public KafkaService() {
        this.objectMapper = new ObjectMapper();
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        producer = new KafkaProducer<>(props);
    }

    public void sendReservation(String topic, ReservationObject reservation) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(reservation);
            producer.send(new ProducerRecord<>(topic, jsonMessage));
            producer.flush();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void sendConfirmation(ConfirmationObject confirmation) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(confirmation);
            producer.send(new ProducerRecord<>("confirmations", jsonMessage));
            producer.flush();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    public void close() {
        producer.close();
    }
}