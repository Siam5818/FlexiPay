package sn.groupeisi.flexipay.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sn.groupeisi.flexipay.entities.Client;
import sn.groupeisi.flexipay.services.ClientService;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

public class EditClientController {

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_save;

    @FXML
    private TextField txt_adresse;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_nom;

    @FXML
    private TextField txt_prenom;

    @FXML
    private TextField txt_telephone;

    private Client client;

    public void setClientData(Client client) {
        this.client = client;
        txt_nom.setText(client.getNom());
        txt_prenom.setText(client.getPrenom());
        txt_adresse.setText(client.getAdresse());
        txt_telephone.setText(client.getTelephone());
        txt_email.setText(client.getEmail());
    }
    @FXML
    void cancelEdit(ActionEvent event) {
        // Fermer la fenêtre de modification
        Stage stage = (Stage) txt_nom.getScene().getWindow();
        stage.close();
    }

    @FXML
    void saveClient(ActionEvent event) {
        try {
            // Vérifiez les entrées utilisateur
            String nom = txt_nom.getText().trim();
            String prenom = txt_prenom.getText().trim();
            String adresse = txt_adresse.getText().trim();
            String telephone = txt_telephone.getText().trim();
            String email = txt_email.getText().trim();

            // Mettre à jour le client
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setAdresse(adresse);
            client.setTelephone(telephone);
            client.setEmail(email);

            // Sauvegarder les modifications dans la base de données
            ClientService clientService = new ClientService();
            clientService.updateClient(client.getId(), nom, prenom, adresse, email, telephone);

            // Afficher une notification de succès
            Utils.showNotification("Succès", "Les informations du client ont été mises à jour avec succès !", NotificationType.SUCCESS);

            // Fermer la fenêtre
            Stage stage = (Stage) txt_nom.getScene().getWindow();
            stage.close();

        }catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Une erreur est survenue lors de la mise à jour du client.", NotificationType.ERROR);
        }
    }

}