package fr.projet.factures.service;

import fr.projet.factures.dao.ProduitDAO;
import fr.projet.factures.dao.ProduitDAOImpl;
import fr.projet.factures.model.Produit;

import java.util.List;

public class ProduitService {

    private final ProduitDAO produitDAO;

    public ProduitService() {
        this.produitDAO = new ProduitDAOImpl();
    }

    public void saveProduit(Produit produit) {
        if (produit == null) throw new IllegalArgumentException("Le produit ne peut pas être null.");
        if (produit.getReference() == null || produit.getReference().trim().isEmpty()) {
            throw new IllegalArgumentException("La référence est obligatoire.");
        }
        if (produit.getPrix() == null || produit.getPrix().signum() < 0) {
            throw new IllegalArgumentException("Le prix doit être positif.");
        }

        if (produit.getId() == null) {
            produitDAO.save(produit);
        } else {
            produitDAO.update(produit);
        }
    }

    public List<Produit> getAllProduits() {
        return produitDAO.findAll();
    }

    public void deleteProduit(Produit produit) {
        produitDAO.delete(produit);
    }
}
