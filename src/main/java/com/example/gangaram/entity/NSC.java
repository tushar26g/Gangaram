package com.example.gangaram.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NSC")
public class NSC {
    @Id
    private String id;
    private String name;
    private String instrument_key;

    // Getters and setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstrument_Key() {
        return instrument_key;
    }

    public void setInstrumentKey(String instrument_key) {
        this.instrument_key = instrument_key;
    }
}
