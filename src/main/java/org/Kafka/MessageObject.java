package org.Kafka;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class MessageObject {
    @JsonProperty("key")
    private String key;

    @JsonProperty("vorname")
    private String vorname;

    @JsonProperty("nachname")
    private String nachname;

    @JsonProperty("datum")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private Date datum;

    @JsonProperty("personen")
    private Integer personen;

    @JsonProperty("email")
    private String email;

    @JsonProperty("telefonnummer")
    private String telefonnummer;

    @JsonProperty("sonderWuensche")
    private String sonderWuensche;

    @JsonProperty("kinderstuhl")
    private Integer kinderstuhl;

    public MessageObject(String key, String vorname, String nachname, Date datum, Integer personen, String email, String telefonnummer, String sonderWuensche, Integer kinderstuhl) {
        this.key = key;
        this.vorname = vorname;
        this.nachname = nachname;
        this.datum = datum;
        this.personen = personen;
        this.email = email;
        this.telefonnummer = telefonnummer;
        this.sonderWuensche = sonderWuensche;
        this.kinderstuhl = kinderstuhl;
    }

    // Getters and setters (optional, but recommended for serialization)
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Integer getPersonen() {
        return personen;
    }

    public void setPersonen(Integer personen) {
        this.personen = personen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefonnummer() {
        return telefonnummer;
    }

    public void setTelefonnummer(String telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public String getsonderWuensche() {
        return sonderWuensche;
    }

    public void setsonderWuensche(String sonderWuensche) {
        this.sonderWuensche = sonderWuensche;
    }

    public Integer getKinderstuhl() {
        return kinderstuhl;
    }

    public void setKinderstuhl(Integer kinderstuhl) {
        this.kinderstuhl = kinderstuhl;
    }
}