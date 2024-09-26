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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Arrays;
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

    public void consumeMessages(String... topics) throws SQLException {
        consumer.subscribe(Arrays.asList(topics));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("Consumed message from topic %s: %s%n", record.topic(), record.value());
                try {
                    if (record.topic().equals("reservations")) {
                        ReservationObject reservation = objectMapper.readValue(record.value(), ReservationObject.class);
                        ConfirmationService confirmationService = new ConfirmationService();
                        System.out.println("Reservation: " + reservation.getDate());
                        databaseService.saveReservation(reservation);
                        confirmationService.sendConfirmation(new ConfirmationObject(UUID.randomUUID().toString(), new Timestamp(new Date().getTime()), UUID.randomUUID().toString(), reservation));
                        System.out.println("Confirmation sent with ID: " + reservation.getId());
                    } else if (record.topic().equals("confirmations")) {
                        ConfirmationObject confirmation = objectMapper.readValue(record.value(), ConfirmationObject.class);
                        databaseService.saveConfirmation(confirmation);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void close() {
        consumer.close();
    }
}