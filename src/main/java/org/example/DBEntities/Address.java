package org.example.DBEntities;

import java.util.UUID;

public class Address {
    private UUID id;
    private final String text;

    public Address(String text) {
        this(null, text);
    }

    public Address(UUID id, String text) {
        this.id = id;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}
