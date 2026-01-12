package fr.projet.factures.controller;

import fr.projet.factures.model.Facture;
import fr.projet.factures.service.FactureService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import fr.projet.factures.service.PdfService;
import javafx.stage.FileChooser;
import java.io.File;

public class FactureController {

    @FXML private javafx.scene.control.TextField searchField;
    @FXML private javafx.scene.control.ComboBox<fr.projet.factures.model.StatutFacture> statutFilter;

    @FXML private TableView<Facture> factureTable;
    @FXML private TableColumn<Facture, String> numeroCol;
    @FXML private TableColumn<Facture, String> dateCol;
    @FXML private TableColumn<Facture, String> clientCol;
    @FXML private TableColumn<Facture, String> totalCol;
    @FXML private TableColumn<Facture, String> statutCol;

    private final FactureService factureService = new FactureService();

    @FXML
    public void initialize() {
        numeroCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate().toString()));
        clientCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getClient().getNom()));
        totalCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getTotal())));
        statutCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatut().getLabel()));

        // Setup filters
        statutFilter.getItems().add(null); // Option for "All"
        statutFilter.getItems().addAll(fr.projet.factures.model.StatutFacture.values());
        
        // Listeners for auto-search
        searchField.textProperty().addListener((obs, oldVal, newVal) -> refreshTable());
        statutFilter.valueProperty().addListener((obs, oldVal, newVal) -> refreshTable());

        refreshTable();
    }

    private void refreshTable() {
        String query = searchField.getText();
        fr.projet.factures.model.StatutFacture statut = statutFilter.getValue();
        
        if ((query == null || query.isBlank()) && statut == null) {
            factureTable.getItems().setAll(factureService.getAllFactures());
        } else {
            factureTable.getItems().setAll(factureService.searchFactures(query, statut));
        }
    }



    @FXML
    private void handleNouvelleFacture() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fr/projet/factures/view/InvoiceEditorView.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Nouvelle Facture");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh after edit
            refreshTable();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Impossible d'ouvrir l'éditeur de facture: " + e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void handleExportPdf() {
        Facture selectedFacture = factureTable.getSelectionModel().getSelectedItem();
        if (selectedFacture == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner une facture à exporter.");
            alert.showAndWait();
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer la facture en PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier PDF", "*.pdf"));
        fileChooser.setInitialFileName("Facture_" + selectedFacture.getNumero() + ".pdf");
        
        File file = fileChooser.showSaveDialog(factureTable.getScene().getWindow());
        if (file != null) {
            try {
                PdfService pdfService = new PdfService();
                pdfService.generateInvoicePdf(selectedFacture, file);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Succès");
                alert.setHeaderText(null);
                alert.setContentText("La facture a été exportée avec succès !");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText("Une erreur est survenue lors de la génération du PDF: " + e.getMessage());
                alert.showAndWait();
            }
        }
    }
}
