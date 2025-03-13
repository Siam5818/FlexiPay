package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.flexipay.enums.StatutCarte;

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

    @Column(nullable = false)
    private String typeCarte;

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

    private double cashback;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<Transaction> transactions;

    @OneToMany(mappedBy = "carte", cascade = CascadeType.ALL)
    private List<Abonnement> abonnements;

    // Constructeur modifié pour générer automatiquement numéroCarte et cvv
    public CarteBancaire(Client client, double solde) {
        this.numeroCarte = generateNumeroCarte();
        this.cvv = generateCVV();
        this.dateExpiration = LocalDate.now().plusYears(3); // Expiration dans 3 ans
        this.statutCarte = StatutCarte.ACTIVE;
        this.client = client;
        this.solde = solde;
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
}
