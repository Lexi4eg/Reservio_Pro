package org.HttpClientEndpoint;

import static spark.Spark.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.Kafka.ReservationObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        port(4567);

        post("/sendReservation", (request, response) -> {
            String body = request.body();
            ObjectMapper objectMapper = new ObjectMapper();
            ReservationObject messageObject = objectMapper.readValue(body, ReservationObject.class);

            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

            KafkaProducer<String, String> producer = new KafkaProducer<>(props);
            ProducerRecord<String, String> record = new ProducerRecord<>("reservations", messageObject.getId(), objectMapper.writeValueAsString(messageObject));
            producer.send(record);
            producer.close();

            response.status(200);
            return "Message sent to Kafka";
        });

        System.out.println("HTTP server started on port 4567");
    }
}