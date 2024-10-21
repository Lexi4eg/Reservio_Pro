package org.DatabaseService;

import org.ConfirmationService.ConfirmationObject;
import org.Kafka.ReservationObject;
import org.Logging.LoggingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseService {
    private static final Logger log = LoggerFactory.getLogger(DatabaseService.class);
    private final Connection connection;
    private final LoggingService loggingService;

    public DatabaseService() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "password");
        this.loggingService = new LoggingService();
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
        loggingService.log("1", "Reservation saved with ID: " + reservation.getId());
    }

    public void deleteReservation(String id) throws SQLException {
        String sql = "DELETE FROM reservations WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
        loggingService.log("1", "Reservation deleted with ID: " + id);
    }



    public void deleteConfirmation(String id) throws SQLException {
        String sql = "DELETE FROM confirmations WHERE reservationId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            statement.executeUpdate();
        }
        loggingService.log("1", "Confirmation deleted with reservation ID: " + id);
    }

    public void updateReservation(ReservationObject reservation) throws SQLException {
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
        } catch (SQLException e) {
            loggingService.log("1", "Error updating reservation with ID: " + reservation.getId());
            throw e;
        }
        loggingService.log("1", "Reservation updated with ID: " + reservation.getId());
    }

    public List<ReservationObject> getAllReservations() {
        List<ReservationObject> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations";
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
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
        } catch (SQLException e) {
            loggingService.log("1", "Error getting all reservations");
        }
        loggingService.log("1", "All reservations retrieved with count: " + reservations.size());
        return reservations;
    }

    public List<String> getTableIdsByTime(String date) throws SQLException {
        List<String> tableIds = new ArrayList<>();
        String query = "SELECT tableid FROM reservations WHERE date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            Timestamp timestamp = Timestamp.valueOf(date.replace("T", " ").replace("Z", ""));
            stmt.setTimestamp(1, timestamp);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tableIds.add(rs.getString("tableid"));
                }
            }
        }
        loggingService.log("1", "Table IDs retrieved by time with count: " + tableIds.size());
        loggingService.log("1", "Table IDs: " + tableIds);
        return tableIds;
    }

    public List<ConfirmationObject> getConfirmationsById(String id) {
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
        } catch (SQLException e) {
            loggingService.log("1", "Error getting confirmations by ID");
        }
        loggingService.log("1", "Confirmations retrieved by ID with count: " + confirmations.size());
        return confirmations;
    }

    public List<ConfirmationObject> getConfirmationsByName(String firstname, String lastname) {
        List<ConfirmationObject> confirmations = new ArrayList<>();
        String query = "SELECT c.id AS c_id, c.confirmationDate AS c_confirmationDate, c.confirmationNumber AS c_confirmationNumber, " +
                "r.id AS r_id, r.firstname AS r_firstname, r.lastname AS r_lastname, r.date AS r_date, r.peopleCount AS r_peopleCount, " +
                "r.email AS r_email, r.phoneNumber AS r_phoneNumber, r.specialRequests AS r_specialRequests, r.highChair AS r_highChair, " +
                "r.tableID AS r_tableID, r.numberChairs AS r_numberChairs " +
                "FROM confirmations c JOIN reservations r ON c.reservationId = r.id WHERE r.firstname ILIKE ? AND r.lastname ILIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstname);
            stmt.setString(2, lastname);
            loggingService.log("1", "Executing query with firstname: " + firstname + ", lastname: " + lastname);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    loggingService.log("1", "ResultSet row: " +
                            "c_id=" + rs.getString("c_id") + ", " +
                            "c_confirmationDate=" + rs.getTimestamp("c_confirmationDate") + ", " +
                            "c_confirmationNumber=" + rs.getString("c_confirmationNumber") + ", " +
                            "r_id=" + rs.getString("r_id") + ", " +
                            "r_firstname=" + rs.getString("r_firstname") + ", " +
                            "r_lastname=" + rs.getString("r_lastname") + ", " +
                            "r_date=" + rs.getTimestamp("r_date") + ", " +
                            "r_peopleCount=" + rs.getInt("r_peopleCount") + ", " +
                            "r_email=" + rs.getString("r_email") + ", " +
                            "r_phoneNumber=" + rs.getString("r_phoneNumber") + ", " +
                            "r_specialRequests=" + rs.getString("r_specialRequests") + ", " +
                            "r_highChair=" + rs.getBoolean("r_highChair") + ", " +
                            "r_tableID=" + rs.getString("r_tableID") + ", " +
                            "r_numberChairs=" + rs.getInt("r_numberChairs"));

                    ReservationObject reservation = new ReservationObject();
                    reservation.setId(rs.getString("r_id"));
                    reservation.setFirstname(rs.getString("r_firstname"));
                    reservation.setLastname(rs.getString("r_lastname"));
                    reservation.setDate(rs.getTimestamp("r_date"));
                    reservation.setPeopleCount(rs.getInt("r_peopleCount"));
                    reservation.setEmail(rs.getString("r_email"));
                    reservation.setPhoneNumber(rs.getString("r_phoneNumber"));
                    reservation.setSpecialRequests(rs.getString("r_specialRequests"));
                    reservation.setHighChair(rs.getBoolean("r_highChair"));
                    reservation.setTableID(rs.getString("r_tableID"));
                    reservation.setNumberChairs(rs.getInt("r_numberChairs"));

                    loggingService.log("1", "Reservation: " + reservation.toString());

                    ConfirmationObject confirmation = new ConfirmationObject(
                            rs.getString("c_id"),
                            rs.getTimestamp("c_confirmationDate"),
                            rs.getString("c_confirmationNumber"),
                            reservation
                    );

                    confirmations.add(confirmation);
                }
            }
        } catch (SQLException e) {
            loggingService.log("1", "Error getting confirmations by name: " + e.getMessage());
        }
        loggingService.log("1", "Confirmations retrieved by name with count: " + confirmations.size());
        loggingService.log("1", "Confirmations: " + confirmations.toString());
        return confirmations;
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
            loggingService.log("1", "Error saving confirmation");
        }
    }
}