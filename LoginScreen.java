package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    private UserManager userManager = new UserManager();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("🔐 Library System Login");

        Label title = new Label("Welcome to Library");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #006064;");

        // Đăng nhập
        TextField loginUsernameField = new TextField();
        loginUsernameField.setPromptText("Username");
        loginUsernameField.setStyle("-fx-background-radius: 5;");

        PasswordField loginPasswordField = new PasswordField();
        loginPasswordField.setPromptText("Password");
        loginPasswordField.setStyle("-fx-background-radius: 5;");

        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #00acc1; -fx-text-fill: white; -fx-background-radius: 5;");

        Button registerButton = new Button("Register");
        registerButton.setStyle("-fx-background-color: #26a69a; -fx-text-fill: white; -fx-background-radius: 5;");

        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");

        loginButton.setOnAction(e -> {
            String username = loginUsernameField.getText();
            String password = loginPasswordField.getText();
            User user = userManager.login(username, password);
            if (user != null) {
                messageLabel.setText("Login successful!");
                messageLabel.setStyle("-fx-text-fill: green;");
                launchLibraryApp(primaryStage, user);
            } else {
                messageLabel.setText("Invalid username or password.");
            }
        });

        registerButton.setOnAction(e -> {
            RegisterScreen registerScreen = new RegisterScreen();
            registerScreen.start(primaryStage);
        });

        VBox box = new VBox(15,
                title,
                new Label("Login"),
                loginUsernameField,
                loginPasswordField,
                loginButton,
                registerButton,
                messageLabel
        );
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: #e0f7fa;");

        Scene scene = new Scene(box, 400, 400);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void launchLibraryApp(Stage stage, User loggedInUser) {
        LibraryAppGUI libraryApp = new LibraryAppGUI();
        libraryApp.init(loggedInUser);
        libraryApp.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}