package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "fidelites")
public class Fidelite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int pointsAcquis;

    @Column(name = "date_dernier_credit", nullable = false)
    private LocalDate dateDernierCredit;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    public Fidelite(Client client) {
        this.client = client;
        this.pointsAcquis = 0;
        this.dateDernierCredit = LocalDate.now();
    }
}
