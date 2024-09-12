package org.Kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class MessageObject {
    private String key;
    private String vorname;
    private String nachname;
    private Date datum;
    private int personen;
    private String email;
    private String telefonnummer;
    private String sonderWuensche;
    private int kinderstuhl;

    // Default constructor
    public MessageObject() {}

    // Parameterized constructor
    @JsonCreator
    public MessageObject(
            @JsonProperty("key") String key,
            @JsonProperty("vorname") String vorname,
            @JsonProperty("nachname") String nachname,
            @JsonProperty("datum") Date datum,
            @JsonProperty("personen") int personen,
            @JsonProperty("email") String email,
            @JsonProperty("telefonnummer") String telefonnummer,
            @JsonProperty("sonderWuensche") String sonderWuensche,
            @JsonProperty("kinderstuhl") int kinderstuhl) {
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

    // Getters and setters
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getVorname() { return vorname; }
    public void setVorname(String vorname) { this.vorname = vorname; }

    public String getNachname() { return nachname; }
    public void setNachname(String nachname) { this.nachname = nachname; }

    public Date getDatum() { return datum; }
    public void setDatum(Date datum) { this.datum = datum; }

    public int getPersonen() { return personen; }
    public void setPersonen(int personen) { this.personen = personen; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefonnummer() { return telefonnummer; }
    public void setTelefonnummer(String telefonnummer) { this.telefonnummer = telefonnummer; }

    public String getSonderWuensche() { return sonderWuensche; }
    public void setSonderWuensche(String sonderWuensche) { this.sonderWuensche = sonderWuensche; }

    public int getKinderstuhl() { return kinderstuhl; }
    public void setKinderstuhl(int kinderstuhl) { this.kinderstuhl = kinderstuhl; }


}