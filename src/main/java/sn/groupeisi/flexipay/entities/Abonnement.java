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

    public Abonnement(String service, double montant, StatutAbonnement statutAbonnement, CarteBancaire carte) {
        this.service = service;
        this.montant = montant;
        this.datePrelevement = LocalDate.now();
        this.statutAbonnement = statutAbonnement;
        this.carte = carte;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public double getMontant() {
        return montant;
    }

    public void setMontant(double montant) {
        this.montant = montant;
    }

    public LocalDate getDatePrelevement() {
        return datePrelevement;
    }

    public void setDatePrelevement(LocalDate datePrelevement) {
        this.datePrelevement = datePrelevement;
    }

    public StatutAbonnement getStatutAbonnement() {
        return statutAbonnement;
    }

    public void setStatutAbonnement(StatutAbonnement statutAbonnement) {
        this.statutAbonnement = statutAbonnement;
    }

    public CarteBancaire getCarte() {
        return carte;
    }

    public void setCarte(CarteBancaire carte) {
        this.carte = carte;
    }
}
