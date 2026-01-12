package fr.projet.factures.service;

import fr.projet.factures.dao.ClientDAO;
import fr.projet.factures.dao.ClientDAOImpl;
import fr.projet.factures.model.Client;

import java.util.List;

public class ClientService {

    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAOImpl();
    }

    public void saveClient(Client client) {
        validateClient(client);
        if (client.getId() == null) {
            clientDAO.save(client);
        } else {
            clientDAO.update(client);
        }
    }

    public List<Client> getAllClients() {
        return clientDAO.findAll();
    }

    public void deleteClient(Client client) {
        clientDAO.delete(client);
    }

    private void validateClient(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("Le client ne peut pas être null.");
        }
        if (client.getNom() == null || client.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom du client est obligatoire.");
        }
        // Add more validation as needed (email format, etc.)
    }
}
