package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.flexipay.enums.StatutTransaction;
import sn.groupeisi.flexipay.enums.TypeTransaction;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeTransaction typeTransaction;

    @Column(nullable = false)
    private double montant;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutTransaction statutTransaction;

    private String commercant;

    @ManyToOne
    @JoinColumn(name = "carte_id", nullable = false)
    private CarteBancaire carte;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private AuthentificationTransaction authentificationTransaction;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Litige litige;
}
