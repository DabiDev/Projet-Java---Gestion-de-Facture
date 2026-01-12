package fr.projet.factures.dao;

import fr.projet.factures.model.Facture;
import fr.projet.factures.model.StatutFacture;
import java.math.BigDecimal;

public interface FactureDAO extends GenericDAO<Facture, Long> {
    BigDecimal sumTotalByStatut(StatutFacture statut);
    long countByStatut(StatutFacture statut);
    java.util.List<Facture> search(String query, StatutFacture statut);
}
