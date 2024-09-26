package org.HttpClientEndpoint;

import static spark.Spark.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ConfirmationService.ConfirmationObject;
import org.DatabaseService.DatabaseService;
import org.Kafka.KafkaService;
import org.Kafka.ReservationObject;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import java.sql.SQLException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {
        port(4567);
        get("/getConfirmation", (request, response) -> {
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumer-group");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
            consumer.subscribe(Collections.singletonList("confirmations"));

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(5000));
            consumer.close();

            if (records.isEmpty()) {
                response.status(404);
                return "No confirmation messages found";
            }

            ConsumerRecord<String, String> latestRecord = records.iterator().next();
            ObjectMapper objectMapper = new ObjectMapper();
            ConfirmationObject confirmation = objectMapper.readValue(latestRecord.value(), ConfirmationObject.class);

            String jsonResponse = objectMapper.writeValueAsString(confirmation);
            response.type("application/json");
            response.status(200);
            return jsonResponse;
        });

        get("/getConfirmationDB",  (request, response) -> {
            String id = request.queryParams("id");
            DatabaseService dbService = new DatabaseService();
            List<ConfirmationObject> confirmations;

            confirmations = dbService.getAllConfirmations(id);
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(confirmations);
            response.type("application/json");
            response.status(200);
            return jsonResponse;
        });

        post("/sendConfirmation", (request, response) -> {
            String body = request.body();
            ObjectMapper objectMapper = new ObjectMapper();
            ConfirmationObject confirmation = objectMapper.readValue(body, ConfirmationObject.class);

            System.out.println("Received confirmation: " + confirmation.getId());

            response.status(200);
            response.type("application/json");
            return objectMapper.writeValueAsString(confirmation);
        });

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
            System.out.println("Message sent to Kafka");
            System.out.println("Reservation: " + messageObject.getDate());

            response.status(200);
            return "Message sent to Kafka";
        });

        get("/getAllReservations", (request, response) -> {
            DatabaseService dbService = new DatabaseService();
            List<ReservationObject> reservations;

            reservations = dbService.getAllReservations();
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(reservations);
            response.type("application/json");
            response.status(200);
            return jsonResponse;
        });


        get("/getTablesByTime", (request, response) -> {
            String dateString = request.queryParams("date");

            // Print the date
            System.out.println("Date: " + dateString);
            if (dateString == null || dateString.isEmpty()) {
                response.status(400);
                return "Date parameter is missing or empty";
            }

            dateString = dateString.replace(" ", "+");

            ZonedDateTime date;
            try {
                date = ZonedDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXX"));
            } catch (DateTimeParseException e) {
                response.status(400);
                return "Invalid date format";
            }

            String isoDate = date.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

            System.out.println("ISO Date: " + isoDate);

            DatabaseService dbService = new DatabaseService();
            List<String> tableIds;
            try {
                tableIds = dbService.getTableIdsByTime(isoDate);
            } catch (SQLException e) {
                e.printStackTrace();
                response.status(500);
                return "Database error";
            }

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(tableIds);

            response.type("application/json");
            response.status(200);
            return jsonResponse;
        });
    }
}