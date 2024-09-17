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
        String query = "INSERT INTO reservations (id, firstname, lastname, date, peoplecount, email, phonenumber, specialrequests, highchair, tableid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, reservation.getId());
            stmt.setString(2, reservation.getFirstname());
            stmt.setString(3, reservation.getLastname());
            stmt.setTimestamp(4, new java.sql.Timestamp(reservation.getDate().getTime()));
            stmt.setInt(5, reservation.getPeopleCount());
            stmt.setString(6, reservation.getEmail());
            stmt.setString(7, reservation.getPhoneNumber());
            stmt.setString(8, reservation.getSpecialRequests());
            stmt.setBoolean(9, reservation.getHighChair());
            stmt.setString(10, reservation.getTableID());
            stmt.executeUpdate();
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
        }
        System.out.println("Table IDs: " + tableIds);
        return tableIds;
    }
}