package fr.projet.factures.controller;

import fr.projet.factures.model.Produit;
import fr.projet.factures.service.ProduitService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import java.math.BigDecimal;
import java.util.Optional;

public class ProduitController {

    @FXML private TableView<Produit> produitTable;
    @FXML private TableColumn<Produit, String> referenceCol;
    @FXML private TableColumn<Produit, String> nomCol;
    @FXML private TableColumn<Produit, BigDecimal> prixCol;

    private final ProduitService produitService = new ProduitService();
    private final ObservableList<Produit> produitList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        referenceCol.setCellValueFactory(new PropertyValueFactory<>("reference"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));

        produitTable.setItems(produitList);
        refreshTable();
    }

    private void refreshTable() {
        produitList.setAll(produitService.getAllProduits());
    }

    @FXML
    private void handleAjouterProduit() {
        Dialog<Produit> dialog = new Dialog<>();
        dialog.setTitle("Nouveau Produit");
        dialog.setHeaderText("Saisir les informations du produit");

        ButtonType saveButtonType = new ButtonType("Enregistrer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField refField = new TextField();
        refField.setPromptText("Référence");
        TextField nomField = new TextField();
        nomField.setPromptText("Nom du produit");
        TextField prixField = new TextField();
        prixField.setPromptText("Prix (€)");

        grid.add(new Label("Référence:"), 0, 0);
        grid.add(refField, 1, 0);
        grid.add(new Label("Nom:"), 0, 1);
        grid.add(nomField, 1, 1);
        grid.add(new Label("Prix:"), 0, 2);
        grid.add(prixField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String ref = refField.getText();
                    String nom = nomField.getText();
                    BigDecimal prix = new BigDecimal(prixField.getText());
                    return new Produit(ref, nom, prix);
                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Prix invalide");
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        Optional<Produit> result = dialog.showAndWait();
        result.ifPresent(produit -> {
            try {
                produitService.saveProduit(produit);
                refreshTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Erreur: " + e.getMessage());
                alert.showAndWait();
            }
        });
    }
}
