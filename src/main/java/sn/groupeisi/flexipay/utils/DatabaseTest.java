package sn.groupeisi.flexipay.utils;

import jakarta.persistence.EntityManager;

public class DatabaseTest {
    public static void main(String[] args) {
        EntityManager em = null;

        try {
            // Obtention de l'EntityManager via JpaUtil
            em = JpaUtil.getEntityManager();
            System.out.println("Connexion à la base de données réussie !");
        } catch (Exception e) {
            System.err.println("Erreur de connexion à la base de données : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Fermer l'EntityManager et l'EntityManagerFactory proprement
            if (em != null && em.isOpen()) em.close();
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
