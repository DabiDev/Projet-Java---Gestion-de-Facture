package fr.projet.factures.dao;

import fr.projet.factures.model.Produit;

public class ProduitDAOImpl extends GenericDAOImpl<Produit, Long> implements ProduitDAO {
    public ProduitDAOImpl() {
        super(Produit.class);
    }
}
