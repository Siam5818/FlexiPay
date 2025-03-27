package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.TypeCarte;
import sn.groupeisi.flexipay.interfaces.ICarteBancaire;
import sn.groupeisi.flexipay.utils.JpaUtil;

import java.util.List;

public class CarteBancaireService implements ICarteBancaire {

    // Méthode pour créer une nouvelle carte bancaire
    @Override
    public void createCarteBancaire(TypeCarte typeCarte, StatutCarte statutCarte) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            // Création d'une nouvelle carte bancaire
            CarteBancaire carteBancaire = new CarteBancaire();
            carteBancaire.setTypeCarte(typeCarte);
            carteBancaire.setStatutCarte(statutCarte);
            carteBancaire.setSolde(0.0); // Par défaut à 0
            carteBancaire.setLimiteJournaliere(5000.0); // Exemple de limite journalière

            em.persist(carteBancaire); // Persister dans la base
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Méthode pour récupérer toutes les cartes bancaires disponibles
    @Override
    public ObservableList<CarteBancaire> getAllCartesBancaires() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<CarteBancaire> query = em.createQuery("SELECT c FROM CarteBancaire c", CarteBancaire.class);
            List<CarteBancaire> result = query.getResultList();
            return FXCollections.observableArrayList(result);
        } finally {
            em.close();
        }

    }

    // Méthode pour récupérer une carte bancaire par son ID
    @Override
    public CarteBancaire getCarteBancaireById(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(CarteBancaire.class, id);
        } finally {
            em.close();
        }
    }

    // Méthode pour mettre à jour une carte bancaire existante
    @Override
    public void updateCarteBancaire(CarteBancaire carteBancaire) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(carteBancaire); // Mettre à jour la carte bancaire
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    // Méthode pour récupérer tous les statuts possibles des cartes
    @Override
    public List<StatutCarte> getAllStatutsCarte() {
        // Utilisation des enums pour charger les statuts
        return List.of(StatutCarte.values());
    }

    // Méthode pour récupérer tous les types possibles des cartes
    @Override
    public List<TypeCarte> getAllTypesCarte() {
        // Utilisation des enums pour charger les types
        return List.of(TypeCarte.values());
    }
}
