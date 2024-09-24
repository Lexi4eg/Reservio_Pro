package org.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.DatabaseService.DatabaseService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;

class KafkaConsumerServiceTest {

    @Mock
    private DatabaseService databaseService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private KafkaConsumer<String, String> consumer;

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testConsumeReservations() throws Exception {
        String topic = "reservations";
        String jsonReservation = "{\"id\":\"1\",\"firstname\":\"John\",\"lastname\":\"Doe\",\"date\":\"2024-09-24T19:29:47.579+0000\",\"peopleCount\":4,\"email\":\"john.doe@example.com\",\"phoneNumber\":\"123-456-7890\",\"specialRequests\":\"None\",\"highChair\":false,\"tableID\":\"A1\",\"numberChairs\":4}";
        ReservationObject reservationObject = new ReservationObject();
        reservationObject.setId("1");
        reservationObject.setFirstname("John");
        reservationObject.setLastname("Doe");
        reservationObject.setDate(Timestamp.valueOf("2024-09-24 19:29:47.579"));
        reservationObject.setPeopleCount(4);
        reservationObject.setEmail("john.doe@example.com");
        reservationObject.setPhoneNumber("123-456-7890");
        reservationObject.setSpecialRequests("None");
        reservationObject.setHighChair(false);
        reservationObject.setTableID("A1");
        reservationObject.setNumberChairs(4);

        ConsumerRecord<String, String> record = new ConsumerRecord<>(topic, 0, 0, null, jsonReservation);
        ConsumerRecords<String, String> records = new ConsumerRecords<>(Collections.singletonMap(new org.apache.kafka.common.TopicPartition(topic, 0), Collections.singletonList(record)));

        when(consumer.poll(any(Duration.class))).thenReturn(records);
        when(objectMapper.readValue(jsonReservation, ReservationObject.class)).thenReturn(reservationObject);

        doNothing().when(databaseService).saveReservation(any(ReservationObject.class));

        kafkaConsumerService.consumeReservations(topic);

        verify(databaseService, times(1)).saveReservation(any(ReservationObject.class));
    }
}