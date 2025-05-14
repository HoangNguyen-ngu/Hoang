package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private List<Document> borrowed = new ArrayList<>();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getName() { return name != null ? name : username; }
    public void setName(String name) { this.name = name; }

    public void borrowDocument(Document doc) {
        if (doc.getQuantity() > 0) {
            doc.setQuantity(doc.getQuantity() - 1);
            borrowed.add(doc);
        }
    }

    public void returnDocument(Document doc) {
        if (borrowed.contains(doc)) {
            doc.setQuantity(doc.getQuantity() + 1);
            borrowed.remove(doc);
        }
    }

    public List<Document> getBorrowedDocuments() {
        return borrowed;
    }
}