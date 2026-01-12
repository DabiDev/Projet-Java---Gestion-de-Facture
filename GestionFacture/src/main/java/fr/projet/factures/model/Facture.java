package fr.projet.factures.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String numero;

    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private StatutFacture statut;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "facture", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<LigneFacture> lignes = new ArrayList<>();

    public Facture() {}

    public Facture(String numero, LocalDate date, Client client) {
        this.numero = numero;
        this.date = date;
        this.client = client;
        this.statut = StatutFacture.EN_ATTENTE;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public StatutFacture getStatut() { return statut; }
    public void setStatut(StatutFacture statut) { this.statut = statut; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }

    public List<LigneFacture> getLignes() { return lignes; }
    public void setLignes(List<LigneFacture> lignes) { this.lignes = lignes; }

    public void addLigne(LigneFacture ligne) {
        lignes.add(ligne);
        ligne.setFacture(this);
    }

    public void removeLigne(LigneFacture ligne) {
        lignes.remove(ligne);
        ligne.setFacture(null);
    }

    public BigDecimal getTotal() {
        return lignes.stream()
                .map(LigneFacture::getMontantLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
