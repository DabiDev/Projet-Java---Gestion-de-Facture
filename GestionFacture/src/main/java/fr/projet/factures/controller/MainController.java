package fr.projet.factures.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class MainController {

    @FXML private StackPane contentArea;

    @FXML
    public void initialize() {
        showDashboard();
    }

    @FXML
    private void showDashboard() {
        loadView("DashboardView.fxml");
    }

    @FXML
    private void showClients() {
        loadView("ClientView.fxml");
    }

    @FXML
    private void showProduits() {
        loadView("ProduitView.fxml");
    }



    @FXML
    private void showFactures() {
        loadView("FactureView.fxml"); // TODO: Create FactureView
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/projet/factures/view/" + fxml));
            Parent view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Could not load FXML: " + fxml);
        }
    }
}
