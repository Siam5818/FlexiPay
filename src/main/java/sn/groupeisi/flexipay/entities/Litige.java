package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import sn.groupeisi.flexipay.enums.StatutLitige;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "litiges")
public class Litige {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String motif;

    @Column(name = "date_signalement", nullable = false)
    private LocalDate dateSignalement;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutLitige statutLitige;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @OneToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    public Litige(String motif, Admin admin, Transaction transaction) {
        this.motif = motif;
        this.dateSignalement = LocalDate.now();
        this.statutLitige = StatutLitige.EN_COURS;
        this.admin = admin;
        this.transaction = transaction;
    }
}
