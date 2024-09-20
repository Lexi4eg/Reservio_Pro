package org.HttpClientEndpoint;

import static spark.Spark.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;
import org.Kafka.ReservationObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws SQLException {
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