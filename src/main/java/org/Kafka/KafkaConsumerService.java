package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ConfirmationService.ConfirmationObject;
import org.ConfirmationService.ConfirmationService;
import org.DatabaseService.DatabaseService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class KafkaConsumerService {
    private final Consumer<String, String> consumer;
    private final DatabaseService databaseService;
    private final ObjectMapper objectMapper;

    public KafkaConsumerService(DatabaseService databaseService, ObjectMapper objectMapper) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
        this.databaseService = databaseService;
        this.objectMapper = objectMapper;
    }

    public void consumeConfirmations(String topic) {
        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Consumed confirmation: %s%n", record.value());

                try {
                    ConfirmationObject confirmation = objectMapper.readValue(record.value(), ConfirmationObject.class);
                    sendConfirmationToFrontend(confirmation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void sendConfirmationToFrontend(ConfirmationObject confirmation) {
        try {
            URL url = new URL("http://localhost:4567/sendConfirmation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            String jsonInputString = objectMapper.writeValueAsString(confirmation);
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            System.out.println("Response Code: " + code);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void consumeReservations(String topic) throws SQLException {
        consumer.subscribe(Collections.singletonList(topic));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Consumed reservation: %s%n", record.value());
                try {
                    ReservationObject reservation = objectMapper.readValue(record.value(), ReservationObject.class);
                    ConfirmationService confirmationService = new ConfirmationService();
                    System.out.println("Reservation: " + reservation.getDate());
                    databaseService.saveReservation(reservation);
                    confirmationService.sendConfirmation(new ConfirmationObject(UUID.randomUUID().toString(), new Timestamp(new Date().getTime()), UUID.randomUUID().toString(), reservation));
                    System.out.println("Confirmation sent with ID: " + reservation.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}