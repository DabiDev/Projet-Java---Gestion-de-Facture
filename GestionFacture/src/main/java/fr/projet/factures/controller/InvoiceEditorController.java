package fr.projet.factures.controller;

import fr.projet.factures.model.Client;
import fr.projet.factures.model.Facture;
import fr.projet.factures.model.LigneFacture;
import fr.projet.factures.model.Produit;
import fr.projet.factures.service.ClientService;
import fr.projet.factures.service.FactureService;
import fr.projet.factures.service.ProduitService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;
import org.kordamp.ikonli.javafx.FontIcon;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class InvoiceEditorController {

    @FXML private ComboBox<Client> clientCombo;
    @FXML private DatePicker datePicker;
    @FXML private TableView<LigneFacture> linesTable;
    @FXML private TableColumn<LigneFacture, String> descCol;
    @FXML private TableColumn<LigneFacture, Integer> qtyCol;
    @FXML private TableColumn<LigneFacture, BigDecimal> priceCol;
    @FXML private TableColumn<LigneFacture, String> totalLineCol;
    @FXML private TableColumn<LigneFacture, Void> actionCol;
    @FXML private Label totalLabel;

    private final ClientService clientService = new ClientService();
    private final FactureService factureService = new FactureService();

    private final ObservableList<LigneFacture> currentLines = FXCollections.observableArrayList();

    @FXML private ComboBox<Produit> productCombo;

    private final ProduitService produitService = new ProduitService();

    @FXML
    public void initialize() {
        // Init Fields
        clientCombo.getItems().setAll(clientService.getAllClients());
        productCombo.getItems().setAll(produitService.getAllProduits());
        datePicker.setValue(LocalDate.now());

        // Init Table
        linesTable.setItems(currentLines);
        linesTable.setEditable(true);

        descCol.setCellFactory(TextFieldTableCell.forTableColumn());
        descCol.setOnEditCommit(e -> e.getRowValue().setDescription(e.getNewValue()));
        descCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        qtyCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        qtyCol.setOnEditCommit(e -> {
            e.getRowValue().setQuantite(e.getNewValue());
            refreshTotal();
            linesTable.refresh();
        });
        qtyCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getQuantite()));

        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new BigDecimalStringConverter()));
        priceCol.setOnEditCommit(e -> {
            e.getRowValue().setPrixUnitaire(e.getNewValue());
            refreshTotal();
            linesTable.refresh();
        });
        priceCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrixUnitaire()));

        totalLineCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("%.2f €", cellData.getValue().getMontantLigne())));

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("");
            {
                btn.setGraphic(new FontIcon("fas-trash"));
                btn.setOnAction(event -> {
                    LigneFacture item = getTableView().getItems().get(getIndex());
                    currentLines.remove(item);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null); else setGraphic(btn);
            }
        });

        currentLines.addListener((ListChangeListener<LigneFacture>) c -> refreshTotal());
    }

    @FXML
    private void handleAjouterLigne() {
        LigneFacture newLine = new LigneFacture("Service", 1, BigDecimal.ZERO);
        currentLines.add(newLine);
    }

    @FXML
    private void handleAjouterProduitSelectionne() {
        Produit selected = productCombo.getValue();
        if (selected != null) {
            LigneFacture newLine = new LigneFacture(selected.getNom(), 1, selected.getPrix());
            currentLines.add(newLine);
            productCombo.getSelectionModel().clearSelection();
        } else {
            showAlert("Veuillez sélectionner un produit.");
        }
    }

    private void refreshTotal() {
        BigDecimal total = currentLines.stream()
                .map(LigneFacture::getMontantLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        totalLabel.setText(String.format("%.2f €", total));
    }

    @FXML
    private void handleEnregistrer() {
        Client client = clientCombo.getValue();
        if (client == null) {
            showAlert("Veuillez sélectionner un client.");
            return;
        }
        if (currentLines.isEmpty()) {
            showAlert("Veuillez ajouter au moins une ligne.");
            return;
        }

        try {
            Facture facture = factureService.createFacture(client);
            facture.setDate(datePicker.getValue());
            for (LigneFacture line : currentLines) {
                 facture.addLigne(line);
            }
            factureService.saveFacture(facture);
            closeWindow();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur lors de l'enregistrement: " + e.getMessage());
        }
    }

    @FXML
    private void handleAnnuler() {
        closeWindow();
    }

    private void closeWindow() {
        ((Stage) totalLabel.getScene().getWindow()).close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText(msg);
        alert.show();
    }
}
