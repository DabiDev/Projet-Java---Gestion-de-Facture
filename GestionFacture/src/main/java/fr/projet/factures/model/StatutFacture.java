package fr.projet.factures.model;

public enum StatutFacture {
    EN_ATTENTE("En attente"),
    PAYEE("Payée"),
    ANNULEE("Annulée");

    private final String label;

    StatutFacture(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
