package com.example.demo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;

public class Book extends Document {
    private final StringProperty subject;
    private final IntegerProperty quantityProperty;

    public Book(String id, String title, String author, int quantity, String subject) {
        super(id, title, author, quantity);
        this.subject = new SimpleStringProperty(subject);
        this.quantityProperty = new SimpleIntegerProperty(quantity);
    }

    public String getSubject() {
        return subject.get();
    }

    public StringProperty subjectProperty() {
        return subject;
    }

    public IntegerProperty quantityProperty() {
        return quantityProperty;
    }

    @Override
    public void setQuantity(int quantity) {
        super.setQuantity(quantity);
        this.quantityProperty.set(quantity);
    }

    @Override
    public String getInfo() {
        return String.format("ID: %s, Title: %s, Author: %s, Quantity: %d, Subject: %s",
                id, title, author, quantity, getSubject());
    }

    @Override
    public String toCSV() {
        return String.format("%s,%s,%s,%d,%s", id, title, author, quantity, getSubject());
    }
}