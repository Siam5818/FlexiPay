package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.TypeCarte;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "cartes_bancaires")
public class CarteBancaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 16)
    private String numeroCarte;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCarte typeCarte;

    @Column(nullable = false, length = 3)
    private int cvv; // Carte Validation Value

    @Column(name = "date_expiration", nullable = false)
    private LocalDate dateExpiration;

    @Column(nullable = false)
    private double solde;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCarte statutCarte;

    @Column(name = "limite_journaliere")
    private double limiteJournaliere;

    @Column(name = "code_pine", nullable = false)
    private String codePine;

    @Column(name = "cashback")
    private double cashback;


    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<Abonnement> abonnements;

    // Constructeur modifié pour générer automatiquement numéroCarte et cvv
    public CarteBancaire(Client client, TypeCarte typeCarte) {
        this.numeroCarte = generateNumeroCarte();
        this.cvv = generateCVV();
        this.dateExpiration = LocalDate.now().plusYears(3); // Expiration dans 3 ans
        this.statutCarte = StatutCarte.ACTIVE;
        this.client = client;
        this.solde = 0.0;
        this.typeCarte = typeCarte;
        this.codePine = "0000";
        switch (typeCarte) {
            case VISA:
                this.limiteJournaliere = 1313197.64;
                this.cashback = 1.5;
                break;
            case MASTERCARD:
                this.limiteJournaliere = 1500;
                this.cashback = 2.0;
                break;
            case DEBIT:
                this.limiteJournaliere = 3000;
                this.cashback = 1.8;
                break;
            case CREDIT:
                this.limiteJournaliere = 3000;
                this.cashback = 1.7;
                break;
            default:
                throw new IllegalArgumentException("Type de carte inconnu");
        }
    }

    // Génération automatique d'un numéro de carte de 16 chiffres
    private String generateNumeroCarte() {
        Random rand = new Random();
        StringBuilder numero = new StringBuilder("4000"); // Préfixe Visa
        for (int i = 0; i < 12; i++) {
            numero.append(rand.nextInt(10)); // Ajouter 12 chiffres aléatoires
        }
        return numero.toString();
    }

    // Génération d’un CVV (3 chiffres)
    private int generateCVV() {
        return new Random().nextInt(900) + 100; // Génère un nombre entre 100 et 999
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroCarte() {
        return numeroCarte;
    }

    public void setNumeroCarte(String numeroCarte) {
        this.numeroCarte = numeroCarte;
    }

    public TypeCarte getTypeCarte() {
        return typeCarte;
    }

    public void setTypeCarte(TypeCarte typeCarte) {
        this.typeCarte = typeCarte;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }

    public StatutCarte getStatutCarte() {
        return statutCarte;
    }

    public void setStatutCarte(StatutCarte statutCarte) {
        this.statutCarte = statutCarte;
    }

    public double getLimiteJournaliere() {
        return limiteJournaliere;
    }

    public void setLimiteJournaliere(double limiteJournaliere) {
        this.limiteJournaliere = limiteJournaliere;
    }

    public String getCodePine() {
        return codePine;
    }

    public void setCodePine(String codePine) {
        this.codePine = codePine;
    }

    public double getCashback() {
        return cashback;
    }

    public void setCashback(double cashback) {
        this.cashback = cashback;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public List<Abonnement> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<Abonnement> abonnements) {
        this.abonnements = abonnements;
    }
}
