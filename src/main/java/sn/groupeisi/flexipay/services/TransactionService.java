package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.entities.OtpAuthentification;
import sn.groupeisi.flexipay.entities.Transaction;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.StatutTransaction;
import sn.groupeisi.flexipay.enums.TypeTransaction;
import sn.groupeisi.flexipay.interfaces.ITransaction;
import sn.groupeisi.flexipay.utils.JpaUtil;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class TransactionService implements ITransaction {
    public void effectuerTransactionSansOtp(String numeroCarte, double montant, String commercant, TypeTransaction typeTransaction, String codePin) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Charger la carte via son numéro unique
            TypedQuery<CarteBancaire> query = em.createQuery(
                    "SELECT c FROM CarteBancaire c WHERE c.numeroCarte = :numeroCarte", CarteBancaire.class
            );
            query.setParameter("numeroCarte", numeroCarte);
            CarteBancaire carte = query.getResultStream().findFirst().orElse(null);

            if (carte == null) {
                throw new IllegalArgumentException("Carte bancaire introuvable.");
            }

            // Vérifier la validité de la carte
            if (carte.getStatutCarte() != StatutCarte.ACTIVE || carte.getDateExpiration().isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("Cette carte est inactive ou expirée.");
            }

            // Vérifier le code PIN
            if (!Objects.equals(carte.getCodePine(), codePin)) {
                throw new IllegalArgumentException("Code PIN incorrect.");
            }

            // Gestion des transactions en fonction du type
            double nouveauSolde = carte.getSolde();

            switch (typeTransaction) {
                case PAIEMENT, RETRAIT -> {
                    if (carte.getSolde() < montant) {
                        throw new IllegalArgumentException("Solde insuffisant pour cette opération.");
                    }
                    if (montant > carte.getLimiteJournaliere()) {
                        throw new IllegalArgumentException("Montant supérieur à la limite journalière.");
                    }
                    nouveauSolde -= montant;
                }
                case DEPOT -> {
                    nouveauSolde += montant;
                }
                case TRANSFERT -> {
                    if (carte.getSolde() < montant) {
                        throw new IllegalArgumentException("Solde insuffisant pour le transfert.");
                    }
                    nouveauSolde -= montant;
                }
                default -> throw new IllegalArgumentException("Type de transaction non géré.");
            }

            // Mise à jour du solde de la carte
            carte.setSolde(nouveauSolde);
            em.merge(carte);

            // Création et enregistrement de la transaction
            Transaction transactionEntity = new Transaction();
            transactionEntity.setCarte(carte);
            transactionEntity.setMontant(montant);
            transactionEntity.setCommercant(commercant);
            transactionEntity.setTypeTransaction(typeTransaction);
            transactionEntity.setStatutTransaction(StatutTransaction.EN_ATTENTE);
            transactionEntity.setDate(LocalDate.now());
            em.persist(transactionEntity);

            transaction.commit();

            // Génération et envoi de l'OTP
            String canal = determineCanal(carte);
            OtpAuthentifService otpService = new OtpAuthentifService();
            otpService.genererEtEnvoyerOtp(transactionEntity.getId(), canal);

            System.out.println("Transaction initiée avec succès. En attente de validation.");
            Utils.showNotification("Succès", "Transaction initiée avec succès, un OTP a été envoyé.", NotificationType.SUCCESS);
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erreur : " + e.getMessage());
            Utils.showNotification("Erreur", "Une erreur inattendue s'est produite.", NotificationType.ERROR);
        } finally {
            em.close();
        }
    }

    private String determineCanal(CarteBancaire carte) {
        if (carte.getClient().getEmail() != null && !carte.getClient().getEmail().isEmpty()) {
            return "EMAIL";
        } else if (carte.getClient().getTelephone() != null && !carte.getClient().getTelephone().isEmpty()) {
            return "SMS";
        } else {
            throw new IllegalArgumentException("Impossible de déterminer le canal pour envoyer l'OTP.");
        }
    }

    public void annulerTransaction(Long transactionId) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Charger la transaction
            Transaction transactionEntity = em.find(Transaction.class, transactionId);
            if (transactionEntity == null) {
                throw new IllegalArgumentException("Transaction introuvable avec l'ID : " + transactionId);
            }

            // Vérifier le statut de la transaction
            if (transactionEntity.getStatutTransaction() == StatutTransaction.ANNULEE) {
                throw new IllegalArgumentException("Cette transaction a déjà été annulée.");
            }
            if (transactionEntity.getStatutTransaction() == StatutTransaction.VALIDE) {
                throw new IllegalArgumentException("Impossible d'annuler une transaction déjà validée.");
            }

            // Récupérer la carte et le montant
            CarteBancaire carte = transactionEntity.getCarte();
            double montant = transactionEntity.getMontant();

            // Vérification du montant
            if (montant <= 0) {
                throw new IllegalArgumentException("Montant de la transaction invalide pour l'annulation.");
            }

            // Remboursement du montant sur la carte
            carte.setSolde(carte.getSolde() + montant);
            em.merge(carte);

            // Mise à jour du statut de la transaction
            transactionEntity.setStatutTransaction(StatutTransaction.ANNULEE);
            em.merge(transactionEntity);

            // Commit de la transaction
            transaction.commit();
            Utils.showNotification("Transaction Annulée", "La transaction a été annulée avec succès.", NotificationType.SUCCESS);
            System.out.println("Transaction annulée avec succès (ID : " + transactionId + ").");

        } catch (Exception e) {
            // Gestion des erreurs
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erreur lors de l'annulation : " + e.getMessage());
            Utils.showNotification("Erreur d'Annulation", "Une erreur est survenue lors de l'annulation de la transaction.", NotificationType.ERROR);
        } finally {
            em.close();
        }
    }

    public void validerTransactionAvecOtp(Long transactionId, String otpSaisi) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            // Charger la transaction
            Transaction transactionEntity = em.find(Transaction.class, transactionId);
            if (transactionEntity == null) {
                throw new IllegalArgumentException("Transaction introuvable avec l'ID : " + transactionId);
            }

            // Vérifier si la transaction est déjà validée ou annulée
            if (transactionEntity.getStatutTransaction() == StatutTransaction.VALIDE) {
                throw new IllegalArgumentException("Cette transaction est déjà validée.");
            } else if (transactionEntity.getStatutTransaction() == StatutTransaction.ANNULEE) {
                throw new IllegalArgumentException("Impossible de valider une transaction annulée.");
            }

            // Récupérer et vérifier l'OTP valide
            TypedQuery<OtpAuthentification> otpQuery = em.createQuery(
                    "SELECT o FROM OtpAuthentification o WHERE o.transaction.id = :transactionId AND o.valide = true",
                    OtpAuthentification.class
            );
            otpQuery.setParameter("transactionId", transactionId);

            OtpAuthentification otp;
            try {
                otp = otpQuery.getSingleResult();
            } catch (NoResultException ex) {
                throw new IllegalArgumentException("Aucun OTP valide trouvé pour cette transaction.");
            }

            // Vérification du code OTP
            if (!otp.getCodeOtp().equals(otpSaisi)) {
                throw new IllegalArgumentException("OTP incorrect.");
            }

            // Vérification de la validité temporelle de l'OTP
            LocalDateTime dateExpiration = otp.getDateExpiration();
            if (dateExpiration == null) {
                throw new IllegalArgumentException("Date d'expiration OTP introuvable.");
            }
            if (dateExpiration.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("OTP expiré.");
            }

            // Marquer l'OTP comme utilisé
            otp.invaliderOtp();
            em.merge(otp);

            // Mettre à jour le statut de la transaction
            transactionEntity.setStatutTransaction(StatutTransaction.VALIDE);
            em.merge(transactionEntity);

            // Valider la transaction
            transaction.commit();
            Utils.showNotification("Succès", "La transaction a été validée avec succès !", NotificationType.SUCCESS);
            System.out.println("Transaction validée avec succès (ID : " + transactionId + ").");

        } catch (IllegalArgumentException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Utils.showNotification("Erreur", e.getMessage(), NotificationType.WARNING);
            System.out.println("Erreur lors de la validation de la transaction : " + e.getMessage());

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("Erreur inattendue : " + e.getMessage());
            Utils.showNotification("Erreur", "Une erreur inattendue s'est produite.", NotificationType.ERROR);

        } finally {
            em.close();
        }
    }


    public List<Transaction> getAllTransactions() {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            TypedQuery<Transaction> query = em.createQuery("SELECT t FROM Transaction t", Transaction.class);
            return query.getResultList();
        }
    }

    public List<Transaction> getRecentTransactions() {
        // Calculer la date d'hier
        LocalDate yesterday = LocalDate.now().minusDays(1);

        try (EntityManager em = JpaUtil.getEntityManager()) {
            // Requête JPQL pour récupérer les transactions des 2 derniers jours
            TypedQuery<Transaction> query = em.createQuery(
                    "SELECT t FROM Transaction t WHERE t.date >= :yesterday", Transaction.class
            );
            query.setParameter("yesterday", yesterday);

            return query.getResultList();
        }
    }
}
