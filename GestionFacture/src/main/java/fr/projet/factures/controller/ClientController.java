package fr.projet.factures.controller;

import fr.projet.factures.model.Client;
import fr.projet.factures.service.ClientService;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.util.Optional;

public class ClientController {

    @FXML private TableView<Client> clientTable;
    @FXML private TableColumn<Client, String> nomCol;
    @FXML private TableColumn<Client, String> emailCol;
    @FXML private TableColumn<Client, String> telCol;
    @FXML private TableColumn<Client, String> adresseCol;

    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        nomCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        telCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelephone()));
        adresseCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAdresse()));

        refreshTable();
    }

    private void refreshTable() {
        clientTable.getItems().setAll(clientService.getAllClients());
    }

    @FXML
    private void handleNouveauClient() {
        Dialog<Client> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Client");
        dialog.setHeaderText("Saisissez les informations du client");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nomField = new TextField();
        TextField emailField = new TextField();
        TextField telField = new TextField();
        TextField adresseField = new TextField();

        grid.add(new Label("Nom:"), 0, 0);
        grid.add(nomField, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new Label("Téléphone:"), 0, 2);
        grid.add(telField, 1, 2);
        grid.add(new Label("Adresse:"), 0, 3);
        grid.add(adresseField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return new Client(nomField.getText(), emailField.getText(), telField.getText(), adresseField.getText());
            }
            return null;
        });

        Optional<Client> result = dialog.showAndWait();
        result.ifPresent(client -> {
            try {
                clientService.saveClient(client);
                refreshTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        });
    }
}
