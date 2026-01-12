package fr.projet.factures.dao;

import fr.projet.factures.model.Client;

import jakarta.persistence.EntityManager;
import fr.projet.factures.util.JPAUtil;

public class ClientDAOImpl extends GenericDAOImpl<Client, Long> implements ClientDAO {
    public ClientDAOImpl() {
        super(Client.class);
    }

    @Override
    public long count() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("SELECT COUNT(c) FROM Client c", Long.class).getSingleResult();
        } finally {
            em.close();
        }
    }
}
