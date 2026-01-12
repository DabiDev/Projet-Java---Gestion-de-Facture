package fr.projet.factures.service;

import fr.projet.factures.dao.FactureDAO;
import fr.projet.factures.dao.FactureDAOImpl;
import fr.projet.factures.model.Client;
import fr.projet.factures.model.Facture;
import fr.projet.factures.model.LigneFacture;
import fr.projet.factures.model.StatutFacture;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class FactureService {

    private final FactureDAO factureDAO;

    public FactureService() {
        this.factureDAO = new FactureDAOImpl();
    }

    public Facture createFacture(Client client) {
        if (client == null) throw new IllegalArgumentException("Le client est obligatoire.");
        Facture facture = new Facture();
        facture.setClient(client);
        facture.setDate(LocalDate.now());
        facture.setStatut(StatutFacture.EN_ATTENTE);
        // Generate a simple unique number for now (YYYYMM-UUID)
        facture.setNumero(LocalDate.now().toString().replace("-", "") + "-" + UUID.randomUUID().toString().substring(0, 8));
        return facture;
    }

    public void addLigne(Facture facture, String description, int quantite, double prixUnit) {
        LigneFacture ligne = new LigneFacture();
        ligne.setDescription(description);
        ligne.setQuantite(quantite);
        ligne.setPrixUnitaire(java.math.BigDecimal.valueOf(prixUnit));
        facture.addLigne(ligne);
    }

    public void createFacture(Facture facture) {
        factureDAO.save(facture);
    }
    
    public java.util.List<Facture> searchFactures(String query, fr.projet.factures.model.StatutFacture statut) {
        return factureDAO.search(query, statut);
    }

    public void saveFacture(Facture facture) {
        if (facture.getLignes().isEmpty()) {
            throw new IllegalArgumentException("La facture doit contenir au moins une ligne.");
        }
        if (facture.getId() == null) {
            factureDAO.save(facture);
        } else {
            factureDAO.update(facture);
        }
    }

    public List<Facture> getAllFactures() {
        return factureDAO.findAll();
    }
}
