package sn.groupeisi.flexipay.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.enums.TypeCarte;
import sn.groupeisi.flexipay.services.ClientService;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

public class CreateClientController {

    @FXML
    private Button btn_annuler;

    @FXML
    private Button btn_creer_client;

    @FXML
    private TextField txt_adresse;

    @FXML
    private TextField txt_nom;

    @FXML
    private TextField txt_prenom;

    @FXML
    private TextField txt_telephone;

    @FXML
    private TextField txt_email;

    @FXML
    private ComboBox<TypeCarte> combo_typeCarte;

    @FXML
    private ComboBox<StatutCarte> combo_statutCarte;

    // Service pour gérer les interactions avec la base de données
    private final ClientService clientService = new ClientService();

    // Initialisation des ComboBox
    @FXML
    public void initialize() {
        // Charger les types de cartes dans la ComboBox
        combo_typeCarte.getItems().addAll(TypeCarte.values());

        // Charger les statuts de cartes dans la ComboBox
        combo_statutCarte.getItems().addAll(StatutCarte.values());
    }

    @FXML
    void annuler(ActionEvent event) {

    }

    @FXML
    void creer_client(ActionEvent event) {
        try {
            // Récupérer les valeurs du formulaire
            String nom = txt_nom.getText().trim();
            String prenom = txt_prenom.getText().trim();
            String adresse = txt_adresse.getText().trim();
            String telephone = txt_telephone.getText().trim();
            String email = txt_email.getText().trim();
            TypeCarte typeCarte = combo_typeCarte.getValue();
            StatutCarte statutCarte = combo_statutCarte.getValue();

            // Vérification des champs obligatoires
            if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || typeCarte == null || statutCarte == null || email.isEmpty()) {
                Utils.showNotification("Erreur", "Tous les champs sont obligatoires !", NotificationType.WARNING);
                return;
            }

            // Appel au service pour créer le client et ses entités associées
            clientService.createClient(nom, prenom, adresse, email, telephone, typeCarte, statutCarte);

            // Notification de succès
            Utils.showNotification("Succès", "Le client a été créé avec succès !", NotificationType.SUCCESS);

            // Réinitialiser le formulaire après succès
            resetForm();

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Une erreur est survenue lors de la création du client.", NotificationType.ERROR);
        }
    }
    // Réinitialisation des champs du formulaire
    private void resetForm() {
        txt_nom.clear();
        txt_prenom.clear();
        txt_adresse.clear();
        txt_telephone.clear();
        combo_typeCarte.getSelectionModel().clearSelection();
        combo_statutCarte.getSelectionModel().clearSelection();
    }

}
