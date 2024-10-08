package org.ConfirmationService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Kafka.ReservationObject;

import java.sql.Timestamp;

public class ConfirmationObject {
    private String id;
    private Timestamp confirmationDate;
    private String confirmationNumber;
    private ReservationObject reservation;

    // Default constructor
    public ConfirmationObject() {}

    // Constructor with parameters
    @JsonCreator
    public ConfirmationObject(
            @JsonProperty("id") String id,
            @JsonProperty("confirmationDate") Timestamp confirmationDate,
            @JsonProperty("confirmationNumber") String confirmationNumber,
            @JsonProperty("reservation") ReservationObject reservation
    ) {
        this.id = id;
        this.confirmationDate = confirmationDate;
        this.confirmationNumber = confirmationNumber;
        this.reservation = reservation;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getConfirmationDate() {
        return confirmationDate;
    }

    public void setConfirmationDate(Timestamp confirmationDate) {
        this.confirmationDate = confirmationDate;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    @Override
    public String toString() {
        return "ConfirmationObject{" +
                "id='" + id + '\'' +
                ", confirmationDate=" + confirmationDate +
                ", confirmationNumber='" + confirmationNumber + '\'' +
                ", reservation=" + reservation +
                '}';
    }

    public ReservationObject getReservation() {
        return reservation;
    }

    public void setReservation(ReservationObject reservation) {
        this.reservation = reservation;
    }
}