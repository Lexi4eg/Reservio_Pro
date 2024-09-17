package org.DatabaseService;

import org.Kafka.ReservationObject;

import java.io.Console;
import java.sql.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseService {
    private final Connection connection;

    public DatabaseService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
    }

    public void saveReservation(ReservationObject reservation) throws SQLException {
        String sql = "INSERT INTO reservations (id, firstname, lastname, date, peopleCount, email, phoneNumber, specialRequests, highChair, tableID, numberChairs) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reservation.getId());
            statement.setString(2, reservation.getFirstname());
            statement.setString(3, reservation.getLastname());
            statement.setTimestamp(4, reservation.getDate());
            statement.setInt(5, reservation.getPeopleCount());
            statement.setString(6, reservation.getEmail());
            statement.setString(7, reservation.getPhoneNumber());
            statement.setString(8, reservation.getSpecialRequests());
            statement.setBoolean(9, reservation.getHighChair());
            statement.setString(10, reservation.getTableID());
            statement.setInt(11, reservation.getNumberChairs());
            statement.executeUpdate();
        }
    }


    public List<String> getTableIdsByTime(String date) throws SQLException {
        List<String> tableIds = new ArrayList<>();
        System.out.println("Date2: " + date);
        String query = "SELECT tableid FROM reservations WHERE date = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Parse the date string into a Timestamp object
            Timestamp timestamp = Timestamp.valueOf(date.replace("T", " ").replace("Z", ""));
            stmt.setTimestamp(1, timestamp);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tableIds.add(rs.getString("tableid"));
                }
            }

            System.out.println("Table IDs: " + tableIds);
            return tableIds;
        }
    }
}