package fr.projet.factures.dao;

import fr.projet.factures.model.User;

public interface UserDAO extends GenericDAO<User, Long> {
    User findByUsername(String username);
}
