package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "clients")
public class Client {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Column(nullable = false, unique = true)
    private String email;

    private String telephone;

    private String adresse;

    @Column(name = "date_inscription", nullable = false)
    private LocalDate dateInscription;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    private List<CarteBancaire> cartes;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    private Fidelite fidelite;
}
