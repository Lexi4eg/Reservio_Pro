package org.DatabaseService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.Kafka.MessageObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
    }

    public void saveMessage(String message) throws SQLException {
        String query = "INSERT INTO messages (key, vorname, nachname, datum, personen, email, telefonnummer, sonderWuensche, kinderstuhl) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Assuming you have a method to parse the JSON message into a MessageObject
            MessageObject messageObject = parseMessage(message);
            stmt.setString(1, messageObject.getId());
            stmt.setString(2, messageObject.getFirstname());
            stmt.setString(3, messageObject.getLastname());
            stmt.setTimestamp(4, new java.sql.Timestamp(messageObject.getDate().getTime()));
            stmt.setInt(5, messageObject.getPeopleCount());
            stmt.setString(6, messageObject.getEmail());
            stmt.setString(7, messageObject.getPhoneNumber());
            stmt.setString(8, messageObject.getSpecialRequests());
            stmt.setBoolean(9, messageObject.getHighChair());
            stmt.executeUpdate();
        }
    }

    private MessageObject parseMessage(String message) {
        // Implement JSON parsing logic here
        // For example, using Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message, MessageObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse message", e);
        }
    }
}