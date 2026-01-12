package fr.projet.factures.dao;

import fr.projet.factures.model.Client;

public interface ClientDAO extends GenericDAO<Client, Long> {
    long count();
}
