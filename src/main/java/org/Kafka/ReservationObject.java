package org.Kafka;

import java.sql.Timestamp;

public class ReservationObject {
    private String id;
    private String firstname;
    private String lastname;
    private Timestamp date;
    private int peopleCount;
    private String email;
    private String phoneNumber;
    private String specialRequests;
    private Boolean highChair;
    private String tableID;
    private Integer numberChairs;

    // Default constructor
    public ReservationObject() {
    }

    // Parameterized constructor
    public ReservationObject(
            String id,
            String firstname,
            String lastname,
            Timestamp date,
            int peopleCount,
            String email,
            String phoneNumber,
            String specialRequests,
            Boolean highChair,
            String tableID,
            Integer numberChairs
    ) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.peopleCount = peopleCount;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.specialRequests = specialRequests;
        this.highChair = highChair;
        this.tableID = tableID;
        this.numberChairs = numberChairs;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public Boolean getHighChair() {
        return highChair;
    }

    public void setHighChair(Boolean highChair) {
        this.highChair = highChair;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public Integer getNumberChairs() {
        return numberChairs;
    }

    public void setNumberChairs(Integer numberChairs) {
        this.numberChairs = numberChairs;
    }

    @Override
    public String toString() {
        return "ReservationObject{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", date=" + date +
                ", peopleCount=" + peopleCount +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", specialRequests='" + specialRequests + '\'' +
                ", highChair=" + highChair +
                ", tableID='" + tableID + '\'' +
                ", numberChairs=" + numberChairs +
                '}';
    }
}