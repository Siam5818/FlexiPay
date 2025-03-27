package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import sn.groupeisi.flexipay.entities.Abonnement;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.enums.StatutAbonnement;
import sn.groupeisi.flexipay.interfaces.IAbonnement;
import sn.groupeisi.flexipay.utils.JpaUtil;

import java.time.LocalDate;
import java.util.List;

public class AbonnementService implements IAbonnement {
    @Override
    public List<Abonnement> getAllAbonnements() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Abonnement> query = em.createQuery("SELECT a FROM Abonnement a", Abonnement.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Abonnement getAbonnementById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Abonnement.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Abonnement createAbonnement(String numcarte, String service, double montant, StatutAbonnement statutAbonnement) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        Abonnement abonnement = null;

        try{
            transaction.begin();
            CarteBancaire carte = em.createQuery(
                            "SELECT c FROM CarteBancaire c WHERE c.numeroCarte = :numcarte", CarteBancaire.class)
                    .setParameter("numcarte", numcarte)
                    .getSingleResult();

            if (carte == null) {
                System.out.println("Carte bancaire non trouvée !");
                return null;
            }

            // Création de l'abonnement avec le constructeur
            abonnement = new Abonnement(service, montant, statutAbonnement, carte);
            em.persist(abonnement);
            transaction.commit();
        }catch(Exception e){
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la création de l'abonnement.");
        }
        return abonnement;
    }

    public Abonnement updateAbonnement(Abonnement abonnement) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Abonnement updatedAbonnement = em.merge(abonnement); // Update and return the merged entity
            em.getTransaction().commit();
            return updatedAbonnement;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de l'abonnement.");
        } finally {
            em.close();
        }
    }

    public void deleteAbonnement(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Abonnement abonnement = em.find(Abonnement.class, id);
            if (abonnement != null) {
                em.remove(abonnement);
                em.getTransaction().commit();
            } else {
                System.out.println("Abonnement non trouvé.");
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la suppression de l'abonnement.");
        } finally {
            em.close();
        }
    }
}
