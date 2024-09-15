package org.DatabaseService;

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
}