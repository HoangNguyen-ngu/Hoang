package com.example.demo;

import java.io.*;
import java.util.*;

public class UserManager {
    private Map<String, User> accounts = new HashMap<>();
    private static final String ACCOUNT_FILE = "accounts.csv";

    public UserManager() {
        loadAccountsFromFile();
    }

    public boolean register(String username, String password) {
        if (accounts.containsKey(username)) return false;
        User newUser = new User(username, password);
        accounts.put(username, newUser);
        saveAccountsToFile();
        return true;
    }

    public User login(String username, String password) {
        User user = accounts.get(username);
        if (user != null && user.getPassword().equals(password)) return user;
        return null;
    }

    public void saveAccountsToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ACCOUNT_FILE))) {
            for (User user : accounts.values()) {
                pw.println(user.getUsername() + "," + user.getPassword());
            }
        } catch (IOException e) {
            System.err.println("Error saving accounts: " + e.getMessage());
        }
    }

    private void loadAccountsFromFile() {
        File file = new File(ACCOUNT_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length >= 2) {
                    User user = new User(p[0], p[1]);
                    accounts.put(p[0], user);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading accounts: " + e.getMessage());
        }
    }

    public User getUser(String username) {
        return accounts.get(username);
    }

    public Collection<User> getAllUsers() {
        return accounts.values();
    }
}