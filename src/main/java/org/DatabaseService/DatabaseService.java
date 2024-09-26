package org.DatabaseService;

import org.ConfirmationService.ConfirmationObject;
import org.Kafka.ReservationObject;
import org.apache.kafka.common.config.ConfigResource;

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

    public List<ReservationObject> getAllReservations() {
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

    public List<ConfirmationObject> getlConfirmationsById(String id) {
        List<ConfirmationObject> confirmations = new ArrayList<>();
        String query = "SELECT c.id AS c_id, c.confirmationDate AS c_confirmationDate, c.confirmationNumber AS c_confirmationNumber, " +
                "r.id AS r_id, r.firstname AS r_firstname, r.lastname AS r_lastname, r.date AS r_date, r.peopleCount AS r_peopleCount, " +
                "r.email AS r_email, r.phoneNumber AS r_phoneNumber, r.specialRequests AS r_specialRequests, r.highChair AS r_highChair, " +
                "r.tableID AS r_tableID, r.numberChairs AS r_numberChairs " +
                "FROM confirmations c JOIN reservations r ON c.reservationId = r.id WHERE r.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationObject reservation = new ReservationObject(
                            rs.getString("r_id"),
                            rs.getString("r_firstname"),
                            rs.getString("r_lastname"),
                            rs.getTimestamp("r_date"),
                            rs.getInt("r_peopleCount"),
                            rs.getString("r_email"),
                            rs.getString("r_phoneNumber"),
                            rs.getString("r_specialRequests"),
                            rs.getBoolean("r_highChair"),
                            rs.getString("r_tableID"),
                            rs.getInt("r_numberChairs")
                    );

                    ConfirmationObject confirmation = new ConfirmationObject(
                            rs.getString("c_id"),
                            rs.getTimestamp("c_confirmationDate"),
                            rs.getString("c_confirmationNumber"),
                            reservation
                    );
                    confirmations.add(confirmation);
                }
            }
            return confirmations;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public List<ConfirmationObject> getlConfirmationsByName(String firstname, String lastname) {
        List<ConfirmationObject> confirmations = new ArrayList<>();
        String query = "SELECT c.id AS c_id, c.confirmationDate AS c_confirmationDate, c.confirmationNumber AS c_confirmationNumber, " +
                "r.id AS r_id, r.firstname AS r_firstname, r.lastname AS r_lastname, r.date AS r_date, r.peopleCount AS r_peopleCount, " +
                "r.email AS r_email, r.phoneNumber AS r_phoneNumber, r.specialRequests AS r_specialRequests, r.highChair AS r_highChair, " +
                "r.tableID AS r_tableID, r.numberChairs AS r_numberChairs " +
                "FROM confirmations c JOIN reservations r ON c.reservationId = r.id WHERE r.firstname = ? AND r.lastname = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ReservationObject reservation = new ReservationObject(
                            rs.getString("r_id"),
                            rs.getString("r_firstname"),
                            rs.getString("r_lastname"),
                            rs.getTimestamp("r_date"),
                            rs.getInt("r_peopleCount"),
                            rs.getString("r_email"),
                            rs.getString("r_phoneNumber"),
                            rs.getString("r_specialRequests"),
                            rs.getBoolean("r_highChair"),
                            rs.getString("r_tableID"),
                            rs.getInt("r_numberChairs")
                    );

                    ConfirmationObject confirmation = new ConfirmationObject(
                            rs.getString("c_id"),
                            rs.getTimestamp("c_confirmationDate"),
                            rs.getString("c_confirmationNumber"),
                            reservation
                    );
                    confirmations.add(confirmation);
                }
            }
            return confirmations;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveConfirmation(ConfirmationObject confirmation) {
        String sql = "INSERT INTO confirmations (id, confirmationdate, confirmationnumber, reservationId) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, confirmation.getId());
            statement.setTimestamp(2, confirmation.getConfirmationDate());
            statement.setString(3, confirmation.getConfirmationNumber());
            statement.setString(4, confirmation.getReservation().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error saving confirmation");
            e.printStackTrace();
        }
    }
}