package fr.projet.factures.service;

import fr.projet.factures.dao.UserDAO;
import fr.projet.factures.dao.UserDAOImpl;
import fr.projet.factures.model.User;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAOImpl();
    }

    public User authenticate(String username, String password) {
        User user = userDAO.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            // In real app: verify hash
            return user;
        }
        return null;
    }

    public void register(String username, String password) {
        // In real app: hash password
        User user = new User(username, password);
        userDAO.save(user);
    }
    
    public boolean hasUsers() {
        return !userDAO.findAll().isEmpty();
    }
}
