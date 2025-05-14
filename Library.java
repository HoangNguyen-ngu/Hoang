package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Library {
    private ObservableList<Document> documents = FXCollections.observableArrayList();
    private Map<String, User> users = new HashMap<>();

    private static final String DOCUMENTS_FILE = "documents.csv";
    private static final String BORROWED_FILE = "borrowed.csv";
    private static final String RATINGS_FILE = "ratings.csv";
    private static final String ACCOUNT_FILE = "accounts.csv"; // Định nghĩa hằng số ACCOUNT_FILE tại đây

    public Library() {
        loadDocumentsFromFile();
        loadUsersFromFile();
        loadBorrowedRecordsFromFile();
    }

    public ObservableList<Document> getDocuments() {
        return documents;
    }

    public Map<String, User> getAllUsers() {
        loadUsersFromFile(); // Tải lại dữ liệu người dùng trước khi trả về
        return users;
    }

    public void addDocument(Document doc) {
        loadDocumentsFromFile(); // Tải lại dữ liệu trước khi thêm
        documents.add(doc);
        saveDocumentsToFile();
    }

    public void removeDocumentById(String id) {
        loadDocumentsFromFile(); // Tải lại dữ liệu trước khi xóa
        documents.removeIf(doc -> doc.getId().equals(id));
        saveDocumentsToFile();
    }

    public Document findById(String id) {
        loadDocumentsFromFile(); // Tải lại dữ liệu trước khi tìm
        return documents.stream()
                .filter(doc -> doc.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User findUserById(String userId) {
        loadUsersFromFile(); // Tải lại dữ liệu người dùng trước khi tìm
        return users.get(userId);
    }

    public void addUser(User user) {
        loadUsersFromFile(); // Tải lại dữ liệu trước khi thêm
        users.put(user.getUsername(), user);
        saveUsersToFile();
    }

    public void clearDocuments() {
        loadDocumentsFromFile(); // Tải lại dữ liệu trước khi xóa tất cả
        documents.clear();
        saveDocumentsToFile();
    }

    private void loadDocumentsFromFile() {
        documents.clear(); // Xóa dữ liệu cũ trước khi tải lại
        Map<String, Document> uniqueDocuments = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCUMENTS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    int quantity = Integer.parseInt(parts[3]);
                    String subject = parts[4];
                    if (uniqueDocuments.containsKey(id)) {
                        Document existingDoc = uniqueDocuments.get(id);
                        existingDoc.setQuantity(existingDoc.getQuantity() + quantity);
                    } else {
                        uniqueDocuments.put(id, new Book(id, title, author, quantity, subject));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File chưa tồn tại, sẽ được tạo khi lưu lần đầu
        } catch (IOException | NumberFormatException e) {
            System.err.println("Failed to load documents: " + e.getMessage());
        }
        documents.addAll(uniqueDocuments.values());
    }

    public void saveDocumentsToFile() {
        Map<String, Document> uniqueDocuments = new HashMap<>();
        for (Document doc : documents) {
            if (uniqueDocuments.containsKey(doc.getId())) {
                Document existingDoc = uniqueDocuments.get(doc.getId());
                existingDoc.setQuantity(existingDoc.getQuantity() + doc.getQuantity());
            } else {
                uniqueDocuments.put(doc.getId(), new Book(doc.getId(), doc.getTitle(), doc.getAuthor(), doc.getQuantity(), doc.getSubject()));
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DOCUMENTS_FILE))) {
            for (Document doc : uniqueDocuments.values()) {
                writer.write(doc.toCSV());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save documents: " + e.getMessage());
        }
    }

    private void loadUsersFromFile() {
        users.clear(); // Xóa dữ liệu cũ trước khi tải lại
        try (BufferedReader reader = new BufferedReader(new FileReader(ACCOUNT_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    User user = new User(parts[0], parts[1]);
                    users.put(parts[0], user);
                }
            }
        } catch (FileNotFoundException e) {
            // File chưa tồn tại, sẽ được tạo khi lưu lần đầu
        } catch (IOException e) {
            System.err.println("Failed to load users: " + e.getMessage());
        }
    }

    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ACCOUNT_FILE))) {
            for (User user : users.values()) {
                writer.write(user.getUsername() + "," + user.getPassword());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save users: " + e.getMessage());
        }
    }

    private void loadBorrowedRecordsFromFile() {
        // Xóa danh sách mượn sách của tất cả người dùng trước khi tải lại
        for (User user : users.values()) {
            user.getBorrowedDocuments().clear();
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(BORROWED_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String userId = parts[0];
                    String docId = parts[1];
                    User user = users.get(userId);
                    Document doc = findById(docId);
                    if (user != null && doc != null) {
                        user.borrowDocument(doc);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // File chưa tồn tại, sẽ được tạo khi lưu lần đầu
        } catch (IOException e) {
            System.err.println("Failed to load borrowed records: " + e.getMessage());
        }
    }

    public void saveBorrowedRecordsToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BORROWED_FILE))) {
            for (User user : users.values()) {
                for (Document doc : user.getBorrowedDocuments()) {
                    String record = user.getUsername() + "," + doc.getId();
                    writer.write(record);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to save borrowed records: " + e.getMessage());
        }
    }
}