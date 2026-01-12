package fr.projet.factures;

import atlantafx.base.theme.PrimerLight;
import fr.projet.factures.util.JPAUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class    MainApp extends Application {
    @Override
    public void start(Stage stage) {
        // Apply AtlantaFX Theme
        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());

        // Initialize Database Connection (Test)
        try {
            JPAUtil.getEntityManagerFactory();
            System.out.println("Database connection established successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to connect to database.");
        }

        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/fr/projet/factures/view/LoginView.fxml"));
            Parent root = loader.load();
            
            // Login view is smaller
            Scene scene = new Scene(root, 400, 350);
            scene.getStylesheets().add(MainApp.class.getResource("/fr/projet/factures/styles.css").toExternalForm());
            
            stage.setTitle("Connexion");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        JPAUtil.shutdown();
    }

    public static void main(String[] args) {
        launch();
    }
}
