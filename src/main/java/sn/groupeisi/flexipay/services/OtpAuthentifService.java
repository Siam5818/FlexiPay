package sn.groupeisi.flexipay.services;

import sn.groupeisi.flexipay.utils.JpaUtil;
import sn.groupeisi.flexipay.utils.Utils;
import sn.groupeisi.flexipay.entities.Transaction;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.entities.OtpAuthentification;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import tray.notification.NotificationType;

public class OtpAuthentifService {

    // Génération et envoi de l'OTP
    public void genererEtEnvoyerOtp(Long transactionId, String canal) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Récupérer la transaction
            Transaction trans = em.find(Transaction.class, transactionId);
            if (trans == null) {
                throw new IllegalArgumentException("Transaction introuvable.");
            }

            // Récupérer la carte bancaire associée à la transaction
            CarteBancaire carte = trans.getCarte();
            if (carte == null) {
                throw new IllegalArgumentException("Carte bancaire introuvable pour la transaction.");
            }

            // Générer un OTP Code
            String otp = generateOtp();

            // Créer un nouvel OTP Authentification
            OtpAuthentification otpAuth = new OtpAuthentification(otp, canal, true, trans);
            em.persist(otpAuth);

            // Envoyer l'OTP via le canal approprié
            if (canal.equalsIgnoreCase("EMAIL")) {
                String email = getClientEmail(carte); // Récupérer l'email du client
                if (email != null && !email.isEmpty()) {
                    String subject = "Votre code OTP pour la transaction";
                    String body = "Votre code OTP est : " + otp + ". Veuillez la fournir a un agent pour valide votre transaction.";
                    // Appel de la méthode d'envoi d'email avec l'OTP
                    EmailService emailService = new EmailService();
                    emailService.sendEmail(email, subject, body);
                } else {
                    throw new IllegalArgumentException("L'email du client est introuvable.");
                }
            }

            transaction.commit();
            Utils.showNotification("Generation d'OTP", "Un nouvel OTP a été généré pour valider la transaction.", NotificationType.SUCCESS);
            System.out.println("Generation et envoie d'un nouvel OTP: " +otp+" voici l'otp genere.");
        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
            Utils.showNotification("Erreur Transaction", e.getMessage(), NotificationType.ERROR);
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erreur : " + e.getMessage());
            Utils.showNotification("Erreur Technique", "Une erreur technique est survenue lors de la génération de l'OTP.", NotificationType.ERROR);
        } finally {
            em.close();
        }
    }

    // Générateur de code OTP (6 chiffres aléatoires)
    public String generateOtp() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }

    // Récupérer l'email du client via la carte bancaire
    public String getClientEmail(CarteBancaire carte) {
        if (carte != null && carte.getClient() != null) {
            String email = carte.getClient().getEmail();
            if (email != null && !email.trim().isEmpty()) {
                System.out.println("Email du client trouve: "+email);
                return email.trim();
            }
        }
        throw new IllegalArgumentException("Email introuvable pour le client.");
    }
}
