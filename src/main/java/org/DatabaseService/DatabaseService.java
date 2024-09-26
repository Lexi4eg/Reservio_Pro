package org.DatabaseService;

import org.Kafka.ReservationObject;

import java.sql.*;
import java.util.ArrayList;
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
            statement.setInt(11, reservation.getNumberChairs() != null ? reservation.getNumberChairs() : 0);
            statement.executeUpdate();
        }
    }

    public void deleteReservation(String id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
    }

    public void updateResrvation(ReservationObject reservation) throws SQLException {
        String sql = "UPDATE reservations SET firstname = ?, lastname = ?, date = ?, peopleCount = ?, email = ?, phoneNumber = ?, specialRequests = ?, highChair = ?, tableID = ?, numberChairs = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, reservation.getFirstname());
            statement.setString(2, reservation.getLastname());
            statement.setTimestamp(3, reservation.getDate());
            statement.setInt(4, reservation.getPeopleCount());
            statement.setString(5, reservation.getEmail());
            statement.setString(6, reservation.getPhoneNumber());
            statement.setString(7, reservation.getSpecialRequests());
            statement.setBoolean(8, reservation.getHighChair());
            statement.setString(9, reservation.getTableID());
            statement.setInt(10, reservation.getNumberChairs());
            statement.setString(11, reservation.getId());
            statement.executeUpdate();
        }
    }

 public List<ReservationObject> getAllReservations(){
        List<ReservationObject> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationObject reservation = new ReservationObject(
                            rs.getString("id"),
                            rs.getString("firstname"),
                            rs.getString("lastname"),
                            rs.getTimestamp("date"),
                            rs.getInt("peopleCount"),
                            rs.getString("email"),
                            rs.getString("phoneNumber"),
                            rs.getString("specialRequests"),
                            rs.getBoolean("highChair"),
                            rs.getString("tableID"),
                            rs.getInt("numberChairs")
                    );
                    reservations.add(reservation);
                }
            }
            return reservations;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
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