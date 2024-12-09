package org.ConfirmationService;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.Kafka.ReservationObject;

import java.sql.Timestamp;

public class TableEntry {
    private String id;
    private int numberofseatingposistions;
    private String section;
    private String tableid;

    // Default constructor
    public TableEntry() {}

    // Constructor with parameters
    @JsonCreator
    public TableEntry (
            @JsonProperty("id") String id,
            @JsonProperty("numberofseatingposistions") int numberofseatingposistions,
            @JsonProperty("section") String section,
            @JsonProperty("tableID") String tableID
    ) {
        this.id = id;
        this.numberofseatingposistions = numberofseatingposistions;
        this.section = section;
        this.tableid = tableID;
    }

    // Getters and setters


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumberofseatingposistions() {
        return numberofseatingposistions;
    }

    public void setNumberofseatingposistions(int numberofseatingposistions) {
        this.numberofseatingposistions = numberofseatingposistions;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getTableID() {
        return tableid;
    }

    public void setTableID(String tableID) {
        this.tableid = tableID;
    }
}
