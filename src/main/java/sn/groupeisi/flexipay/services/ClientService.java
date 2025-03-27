package sn.groupeisi.flexipay.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.entities.Client;
import sn.groupeisi.flexipay.entities.Fidelite;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.TypeCarte;
import sn.groupeisi.flexipay.interfaces.IClients;
import sn.groupeisi.flexipay.utils.JpaUtil;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

import java.time.LocalDate;
import java.util.List;

public class ClientService implements IClients {
    public void createClient(String name, String prenom, String adresse, String email, String telephone, TypeCarte typecarte, StatutCarte statutCarte) {
        if (name == null || prenom == null || adresse == null || email == null || telephone == null || typecarte == null || statutCarte == null) {
            throw new IllegalArgumentException("Tous les paramètres sont requis et ne peuvent pas être null.");
        }

        if (telephone.length() > 15) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas dépasser 15 caractères.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("L'adresse email n'est pas valide.");
        }

        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Creation du client
            Client client = new Client(name, prenom, email, telephone, adresse);
            em.persist(client);

            // Creation d'une nouvelle carte pour le client.
            CarteBancaire carte = new CarteBancaire(client, typecarte);
            carte.setStatutCarte(statutCarte);
            em.persist(carte);

            // Initialisation d'une fidelite pour le client.
            if (client.getFidelite() == null) { // Vérification pour éviter les doublons
                Fidelite fidelite = new Fidelite(client);
                em.persist(fidelite);
            }

            // Validation de la transaction
            transaction.commit();

            // Notification
            Utils.showNotification("Create Client", "Client, carte bancaire et fidélité créés avec succès !", NotificationType.SUCCESS);
            System.out.println("Client, carte bancaire et fidélité créés avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            Utils.showNotification("Create Client", "Erreur lors de la création du client.", NotificationType.ERROR);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Client> getAllClients() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c", Client.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Client> searchClientsByName(String name) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            TypedQuery<Client> query = em.createQuery("SELECT c FROM Client c WHERE LOWER(c.nom) LIKE :name", Client.class);
            query.setParameter("name", "%" + name.toLowerCase() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void deleteClient(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Client client = em.find(Client.class, id);
            if (client != null) {
                em.remove(client);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void updateClient(Long id, String name, String prenom, String adresse, String email, String telephone) {
        if (name == null || prenom == null || adresse == null || email == null || telephone == null) {
            throw new IllegalArgumentException("Tous les paramètres sont requis et ne peuvent pas être null.");
        }

        if (telephone.length() > 15) {
            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas dépasser 15 caractères.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("L'adresse email n'est pas valide.");
        }
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            // Trouver le client par son ID
            Client client = em.find(Client.class, id);
            if (client == null) {
                throw new IllegalArgumentException("Client introuvable avec l'ID fourni : " + id);
            }

            // Mise à jour des informations du client
            client.setNom(name);
            client.setPrenom(prenom);
            client.setAdresse(adresse);
            client.setEmail(email);
            client.setTelephone(telephone);

            em.merge(client); // Sauvegarder les modifications dans la base
            transaction.commit();

            // Notification
            Utils.showNotification("Update Client", "Client mis à jour avec succès !", NotificationType.SUCCESS);
            System.out.println("Client mis à jour avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
            Utils.showNotification("Update Client", "Erreur lors de la mise à jour du client.", NotificationType.ERROR);
            throw e;
        } finally {
            em.close();
        }
    }

    /// /////////////////teste/////////////////
    public void addTestClient() {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            // Création d'un client de test
            Client client = Client.builder()
                    .nom("Mohamed")
                    .prenom("Anzize")
                    .email("anzize@test.com")
                    .adresse("Dakar, Sénégal")
                    .telephone("770123456")
                    .dateInscription(LocalDate.now())
                    .build();

            em.persist(client); // Persistance du client
            transaction.commit();
            System.out.println("Client de test ajouté avec succès !");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        } finally {
            em.close();
        }
    }
}
