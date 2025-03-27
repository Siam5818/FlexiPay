package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import sn.groupeisi.flexipay.entities.Admin;
import sn.groupeisi.flexipay.interfaces.IAdmin;
import sn.groupeisi.flexipay.utils.JpaUtil;

public class AdminImp implements IAdmin {
    @Override
    public Admin seConnecter(String username, String password) {
        try (EntityManager em = JpaUtil.getEntityManager()) {
            TypedQuery<Admin> query = em.createQuery(
                    "SELECT a FROM Admin a WHERE a.username = :username AND a.password = :password", Admin.class);
            query.setParameter("username", username);
            query.setParameter("password", password);
            return query.getSingleResult();
        } catch (Exception e) {
            System.out.println("Erreur lors de la connexion : " + e.getMessage());
            return null;
        }
    }
}
