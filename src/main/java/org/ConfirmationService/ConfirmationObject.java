package org.ConfirmationService;

import org.Kafka.ReservationObject;

import java.sql.Timestamp;

public class ConfirmationObject {
    public String id;
    public Timestamp ConfirmationDate;
    public String confirmationNumber;
    public ReservationObject reservation;


    public ConfirmationObject(String id, Timestamp confirmationDate, String confirmationNumber, ReservationObject reservation) {
        this.id = id;
        ConfirmationDate = confirmationDate;
        this.confirmationNumber = confirmationNumber;
        this.reservation = reservation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getConfirmationDate() {
        return ConfirmationDate;
    }

    public void setConfirmationDate(Timestamp confirmationDate) {
        ConfirmationDate = confirmationDate;
    }

    public String getConfirmationNumber() {
        return confirmationNumber;
    }

    public void setConfirmationNumber(String confirmationNumber) {
        this.confirmationNumber = confirmationNumber;
    }

    public ReservationObject getReservation() {
        return reservation;
    }

    public void setReservation(ReservationObject reservation) {
        this.reservation = reservation;
    }
}

