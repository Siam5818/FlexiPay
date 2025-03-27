package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.groupeisi.flexipay.utils.JpaUtil;

public class DashboardService {
    // Méthode pour récupérer le nombre total d'alertes
    public long getNombreAlertes() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(a) FROM Litige a", Long.class); // Remplacez "litige" par l'entité réelle
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
    // Méthode pour récupérer le nombre total de clients
    public long getNombreClients() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM Client c", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Méthode pour récupérer le nombre total de comptes
    public long getNombreComptes() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(c) FROM CarteBancaire c", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    // Méthode pour récupérer le nombre total de transactions effectuées
    public long getNombreTransactions() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery("SELECT COUNT(t) FROM Transaction t", Long.class);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}
