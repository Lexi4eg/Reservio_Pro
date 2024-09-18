package org.Kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.util.Date;

public class ReservationObject {
    @JsonProperty("id")
    private String id;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private  Timestamp date;

    @JsonProperty("peopleCount")
    private int peopleCount;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("specialRequests")
    private String specialRequests;

    @JsonProperty("highChair")
    private Boolean highChair;

    @JsonProperty("tableID")
    private String tableID;

    @JsonProperty("numberChairs")
    private Integer numberChairs;

    // Default constructor
    public ReservationObject() {
    }

    // Parameterized constructor
    @JsonCreator
    public ReservationObject(
            @JsonProperty("id") String id,
            @JsonProperty("firstname") String firstname,
            @JsonProperty("lastname") String lastname,
            @JsonProperty("date")  Timestamp date,
            @JsonProperty("peopleCount") int peopleCount,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("specialRequests") String specialRequests,
            @JsonProperty("highChair") Boolean highChair,
            @JsonProperty("tableID") String tableID,
            @JsonProperty("numberChairs") Integer numberChairs
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

    public ReservationObject(String key, String john, String doe, Date date, int i, String mail, String s, String none, boolean b, String a1) {
    }

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
        return  date;
    }

    public void setDate( Timestamp date) {
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

    public Integer getNumberChairs() {
        return numberChairs;
    }

    public void setNumberChairs(Integer numberChairs) {
        this.numberChairs = numberChairs;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }
}