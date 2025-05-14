package com.example.demo;

public abstract class Document {
    protected String id;
    protected String title;
    protected String author;
    protected int quantity;

    public Document(String id, String title, String author, int quantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public abstract String getInfo();
    public abstract String toCSV();
    public abstract String getSubject(); // Thêm phương thức trừu tượng
}