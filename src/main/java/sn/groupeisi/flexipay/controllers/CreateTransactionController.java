package sn.groupeisi.flexipay.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sn.groupeisi.flexipay.enums.TypeCarte;
import sn.groupeisi.flexipay.enums.TypeTransaction;
import sn.groupeisi.flexipay.services.TransactionService;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

public class CreateTransactionController {

    @FXML
    private PasswordField codePinField;

    @FXML
    private TextField commercantField;

    @FXML
    private TextField numeroCarteField;

    @FXML
    private Button effectuerTransactionBtn;

    @FXML
    private TextField montantField;

    @FXML
    private ComboBox<TypeTransaction> typeTransactionCombo;

    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        // Ensure the ComboBox is populated with the enum values
        typeTransactionCombo.setItems(FXCollections.observableArrayList(TypeTransaction.values()));
        typeTransactionCombo.setPromptText("Sélectionner un type de transaction");
    }

    @FXML
    void effectuerTransaction(ActionEvent event) {
        try {
            // Récupérer les valeurs des champs
            String numeroCarte = numeroCarteField.getText().trim();
            String codePin = codePinField.getText().trim();
            String commercant = commercantField.getText().trim();
            String montantString = montantField.getText().trim();
            TypeTransaction typeTransaction = typeTransactionCombo.getValue();

            // Validation des champs obligatoires
            if (numeroCarte.isEmpty() || codePin.isEmpty() || commercant.isEmpty() || montantString.isEmpty() || typeTransaction == null) {
                Utils.showNotification("Erreur", "Tous les champs sont obligatoires.", NotificationType.WARNING);
                return;
            }

            // Vérification du numéro de carte
            if (numeroCarte.length() != 16 || !numeroCarte.matches("\\d+")) {
                Utils.showNotification("Erreur", "Le numéro de carte doit contenir exactement 16 chiffres.", NotificationType.WARNING);
                return;
            }

            // Conversion du montant en double
            double montant;
            try {
                montant = Double.parseDouble(montantString);
                if (montant <= 0) {
                    throw new IllegalArgumentException("Le montant doit être supérieur à zéro.");
                }
            } catch (NumberFormatException e) {
                Utils.showNotification("Erreur", "Montant invalide ! Veuillez entrer un nombre valide.", NotificationType.WARNING);
                return;
            }

            // Appel au service pour effectuer la transaction sans OTP
            transactionService.effectuerTransactionSansOtp(numeroCarte, montant, commercant, typeTransaction, codePin);

            resetForm();

            Utils.showNotification("Succès", "Transaction initiée avec succès !", NotificationType.SUCCESS);

        } catch (IllegalArgumentException e) {
            // Notification en cas de données invalides
            Utils.showNotification("Erreur", e.getMessage(), NotificationType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Une erreur inattendue s'est produite.", NotificationType.ERROR);
        }
    }

    // Méthode pour réinitialiser le formulaire
    private void resetForm() {
        numeroCarteField.clear();
        codePinField.clear();
        commercantField.clear();
        montantField.clear();
        typeTransactionCombo.getSelectionModel().clearSelection();
    }

}
