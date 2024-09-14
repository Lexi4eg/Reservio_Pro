package org.DatabaseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Kafka.ReservationObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
    }

    public void saveReservation(String reservation) throws SQLException {
        String query = "INSERT INTO reservations (id, vorname, nachname, datum, personen, email, telefonnummer, sonderWuensche, kinderstuh, tableidl) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ReservationObject reservationObject = parseMessage(reservation);
            stmt.setString(1, reservationObject.getId());
            stmt.setString(2, reservationObject.getFirstname());
            stmt.setString(3, reservationObject.getLastname());
            stmt.setTimestamp(4, new java.sql.Timestamp(reservationObject.getDate().getTime()));
            stmt.setInt(5, reservationObject.getPeopleCount());
            stmt.setString(6, reservationObject.getEmail());
            stmt.setString(7, reservationObject.getPhoneNumber());
            stmt.setString(8, reservationObject.getSpecialRequests());
            stmt.setBoolean(9, reservationObject.getHighChair());
            stmt.executeUpdate();
        }
    }

    private ReservationObject parseMessage(String reservation) {
        // Implement JSON parsing logic here
        // For example, using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(reservation, ReservationObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse message", e);
        }
    }
}