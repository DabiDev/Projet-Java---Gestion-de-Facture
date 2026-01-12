package fr.projet.factures.dao;

import fr.projet.factures.model.Facture;

import fr.projet.factures.model.StatutFacture;
import fr.projet.factures.util.JPAUtil;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;

public class FactureDAOImpl extends GenericDAOImpl<Facture, Long> implements FactureDAO {
    public FactureDAOImpl() {
        super(Facture.class);
    }

    @Override
    public BigDecimal sumTotalByStatut(StatutFacture statut) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Note: We join with lines (lignes) because total is calculated, not stored.
            // But wait, Facture entity doesn't store total. It calculates it.
            // To do this via SQL, we must sum the lines.
            // Or, if we assume we just fetch entities and sum in Java (bad for perf but easier).
            // Better: JPQL Sum.
            String jpql = "SELECT SUM(l.quantite * l.prixUnitaire) FROM Facture f JOIN f.lignes l WHERE f.statut = :statut";
            BigDecimal sum = em.createQuery(jpql, BigDecimal.class)
                    .setParameter("statut", statut)
                    .getSingleResult();
            return sum != null ? sum : BigDecimal.ZERO;
        } finally {
            em.close();
        }
    }

    @Override
    public long countByStatut(StatutFacture statut) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(f) FROM Facture f WHERE f.statut = :statut", Long.class)
                    .setParameter("statut", statut)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    @Override
    public java.util.List<Facture> search(String query, StatutFacture statut) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            StringBuilder jpql = new StringBuilder("SELECT f FROM Facture f WHERE 1=1");
            
            if (query != null && !query.trim().isEmpty()) {
                jpql.append(" AND (LOWER(f.numero) LIKE LOWER(:query) OR LOWER(f.client.nom) LIKE LOWER(:query))");
            }
            
            if (statut != null) {
                jpql.append(" AND f.statut = :statut");
            }
            
            var typedQuery = em.createQuery(jpql.toString(), Facture.class);
            
            if (query != null && !query.trim().isEmpty()) {
                typedQuery.setParameter("query", "%" + query.trim() + "%");
            }
            
            if (statut != null) {
                typedQuery.setParameter("statut", statut);
            }
            
            return typedQuery.getResultList();
        } finally {
            em.close();
        }
    }
}
