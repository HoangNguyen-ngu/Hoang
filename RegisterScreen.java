package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegisterScreen extends Application {
    private UserManager userManager = new UserManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("📝 Library System Register");

        Label title = new Label("Register New Account");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #006064;");

        TextField registerUsernameField = new TextField();
        registerUsernameField.setPromptText("Username");
        registerUsernameField.setStyle("-fx-background-radius: 5;");

        PasswordField registerPasswordField = new PasswordField();
        registerPasswordField.setPromptText("Password");
        registerPasswordField.setStyle("-fx-background-radius: 5;");

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setStyle("-fx-background-radius: 5;");

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #26a69a; -fx-text-fill: white; -fx-background-radius: 5;");

        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: #00acc1; -fx-text-fill: white; -fx-background-radius: 5;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        registerButton.setOnAction(e -> {
            String username = registerUsernameField.getText();
            String password = registerPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            if (!password.equals(confirmPassword)) {
                messageLabel.setText("Passwords do not match.");
                return;
            }
            if (userManager.register(username, password)) {
                messageLabel.setText("Register successful. Returning to login...");
                messageLabel.setStyle("-fx-text-fill: green;");
                // Tự động quay lại màn hình đăng nhập sau 2 giây
                new Thread(() -> {
                    try {
                        Thread.sleep(2000); // Chờ 2 giây để người dùng đọc thông báo
                        javafx.application.Platform.runLater(() -> {
                            LoginScreen loginScreen = new LoginScreen();
                            loginScreen.start(primaryStage);
                        });
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            } else {
                messageLabel.setText("Username already exists.");
            }
        });

        backButton.setOnAction(e -> {
            LoginScreen loginScreen = new LoginScreen();
            loginScreen.start(primaryStage);
        });

        VBox box = new VBox(15,
                title,
                new Label("Register"),
                registerUsernameField,
                registerPasswordField,
                confirmPasswordField,
                registerButton,
                backButton,
                messageLabel
        );
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #e0f7fa;");

        Scene scene = new Scene(box, 400, 450);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}