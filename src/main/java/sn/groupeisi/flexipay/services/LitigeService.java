package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import sn.groupeisi.flexipay.entities.Admin;
import sn.groupeisi.flexipay.entities.Litige;
import sn.groupeisi.flexipay.entities.Transaction;
import sn.groupeisi.flexipay.enums.StatutLitige;
import sn.groupeisi.flexipay.interfaces.ILitige;
import sn.groupeisi.flexipay.utils.JpaUtil;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

public class LitigeService implements ILitige {
    public void signalerLitige(Long transactionId, String motif, Long adminId) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            // Récupération de la transaction à partir de son ID
            Transaction trans = em.find(Transaction.class, transactionId);
            if (trans == null) {
                throw new IllegalArgumentException("Transaction introuvable.");
            }

            // Vérification s'il y a déjà un litige pour cette transaction
            if (isLitigeEnCours(transactionId, em)) {
                throw new IllegalStateException("Un litige est déjà en cours pour cette transaction.");
            }

            // Récupération de l'administrateur (si fourni)
            Admin admin = null;
            if (adminId != null) {
                admin = em.find(Admin.class, adminId);
                if (admin == null) {
                    throw new IllegalArgumentException("Administrateur introuvable.");
                }
            }

            // Création du litige
            Litige litige = new Litige(motif, admin, trans);
            em.persist(litige);
            transaction.commit();

            System.out.println("Litige signalé avec succès pour la transaction.");
            Utils.showNotification(
                    "Litige Signalé",
                    "Litige pour la transaction ID : " + transactionId + " soumis avec succès.",
                    NotificationType.SUCCESS
            );

        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            Utils.showNotification(
                    "Erreur Signalement Litige",
                    "Une erreur est survenue lors du signalement du litige.",
                    NotificationType.ERROR
            );
            System.err.println("Erreur lors de la soumission du litige : " + e.getMessage());
        } finally {
            em.close();
        }
    }

    private boolean isLitigeEnCours(Long transactionId, EntityManager em) {
        TypedQuery<Litige> query = em.createQuery(
                "SELECT l FROM Litige l WHERE l.transaction.id = :transactionId AND l.statutLitige = :statutEnCours",
                Litige.class
        );
        query.setParameter("transactionId", transactionId);
        query.setParameter("statutEnCours", StatutLitige.EN_COURS);

        return !query.getResultList().isEmpty();
    }
}
