package fr.projet.factures.controller;

import fr.projet.factures.MainApp;
import fr.projet.factures.model.User;
import fr.projet.factures.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        // Seed default user if none exist
        if (!userService.hasUsers()) {
            userService.register("admin", "admin");
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userService.authenticate(username, password);

        if (user != null) {
            loadMainView();
        } else {
            errorLabel.setText("Identifiants incorrects.");
            errorLabel.setVisible(true);
        }
    }

    private void loadMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/projet/factures/view/MainView.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setTitle("Gestion de Factures - Dashboard");
            // Determine size based on screen or simply Maximize?
            // stage.setMaximized(true); 
            stage.setWidth(1200);
            stage.setHeight(800);
            
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            
        } catch (IOException e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors du chargement de l'application.");
            errorLabel.setVisible(true);
        }
    }
}
