package sn.groupeisi.flexipay.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "otp_authentifications")
public class OtpAuthentification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codeOtp;

    @Column(nullable = false)
    private String canal;

    @Column(name = "date_generation", nullable = false)
    private LocalDateTime dateGeneration;

    @Column(name = "date_expiration", nullable = false)
    private LocalDateTime dateExpiration;

    @Column(nullable = false)
    private boolean valide; // Si l'OTP est encore valide

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction; // Transaction associée

    private static final int DUREE_VALIDITE_MINUTES = 2;
    public OtpAuthentification(String codeOtp, String canal, boolean valide, Transaction transaction) {
        this.codeOtp = codeOtp;
        this.canal = canal;
        this.dateGeneration = LocalDateTime.now();
        this.dateExpiration = LocalDateTime.now().plusMinutes(DUREE_VALIDITE_MINUTES);
        this.valide = valide;
        this.transaction = transaction;
    }

    // Vérifie si l'OTP est encore valide
    public boolean isOtpValide() {
        return this.valide && LocalDateTime.now().isBefore(this.dateExpiration);
    }

    // Invalide un OTP après usage
    public void invaliderOtp() {
        this.valide = false;
    }
}
