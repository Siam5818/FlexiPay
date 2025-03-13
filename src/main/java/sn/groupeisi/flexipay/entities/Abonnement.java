package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.flexipay.enums.StatutAbonnement;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "abonnements")
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String service;

    @Column(nullable = false)
    private double montant;

    @Column(name = "date_prelevement", nullable = false)
    private LocalDate datePrelevement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutAbonnement statutAbonnement;

    @ManyToOne
    @JoinColumn(name = "carte_id", nullable = false)
    private CarteBancaire carte;
}
