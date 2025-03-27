package sn.groupeisi.flexipay.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sn.groupeisi.flexipay.entities.Admin;
import sn.groupeisi.flexipay.interfaces.IAdmin;
import sn.groupeisi.flexipay.services.AdminImp;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

public class LoginController {

    @FXML
    private Button btn_connecte;

    @FXML
    private PasswordField txtfld_passeword;

    @FXML
    private TextField txtfld_username;

    @FXML
    void login(ActionEvent event) {
        String username = txtfld_username.getText();
        String password = txtfld_passeword.getText();

        if(username.isEmpty() || password.isEmpty()){
            // Afficher un message d'erreur "Veuillez remplir tous les champs"
            Utils.showNotification("Erreur", "Tous les champs sont obligatoires !", NotificationType.ERROR);
            return;
        }

        IAdmin adminService = new AdminImp();
        Admin admin = adminService.seConnecter(username, password);
        if (admin!= null) {
            Utils.showNotification("Success","Connexion réussie !", NotificationType.SUCCESS);
            Utils.loadPage((Stage) txtfld_username.getScene().getWindow(), "/fxml/dashboard.fxml", "Dahboard");
        } else {
            // Afficher un message d'erreur
            Utils.showNotification("Connexion échouée", "Nom d'utilisateur ou mot de passe incorrect !", NotificationType.ERROR);
            // Vider les champs
            txtfld_username.clear();
            txtfld_passeword.clear();
            txtfld_username.requestFocus();
        }
    }

}
