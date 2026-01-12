package fr.projet.factures.controller;

import fr.projet.factures.dao.ClientDAO;
import fr.projet.factures.dao.ClientDAOImpl;
import fr.projet.factures.dao.FactureDAO;
import fr.projet.factures.dao.FactureDAOImpl;
import fr.projet.factures.model.StatutFacture;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.math.BigDecimal;

public class DashboardController {

    @FXML private Label totalClientsLabel;
    @FXML private Label chiffreAffairesLabel;
    @FXML private Label enAttenteLabel;
    @FXML private Label nbFacturesAttenteLabel;

    private final ClientDAO clientDAO;
    private final FactureDAO factureDAO;

    public DashboardController() {
        this.clientDAO = new ClientDAOImpl();
        this.factureDAO = new FactureDAOImpl();
    }

    @FXML
    public void initialize() {
        loadStatistics();
    }

    private void loadStatistics() {
        // Total Clients
        long totalClients = clientDAO.count();
        totalClientsLabel.setText(String.valueOf(totalClients));

        // Chiffre d'Affaires (Total Payée)
        BigDecimal ca = factureDAO.sumTotalByStatut(StatutFacture.PAYEE);
        chiffreAffairesLabel.setText(String.format("%.2f €", ca));

        // Montant en Attente
        BigDecimal pending = factureDAO.sumTotalByStatut(StatutFacture.EN_ATTENTE);
        enAttenteLabel.setText(String.format("%.2f €", pending));

        // Nombre de factures en attente
        long nbPending = factureDAO.countByStatut(StatutFacture.EN_ATTENTE);
        nbFacturesAttenteLabel.setText(String.valueOf(nbPending));
    }
}
