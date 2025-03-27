package sn.groupeisi.flexipay.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sn.groupeisi.flexipay.entities.Transaction;
import sn.groupeisi.flexipay.enums.StatutTransaction;
import sn.groupeisi.flexipay.services.TransactionService;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

import java.util.List;

public class TransactionController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, Long> idColumn;

    @FXML
    private TableColumn<Transaction, String> commercantColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> statutColumn;

    @FXML
    private TableColumn<Transaction, String> montantColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TextField otpField;

    @FXML
    private Button ajouterTransactionBtn;

    @FXML
    private Button annulerTransactionBtn;

    @FXML
    private Button validerTransactionBtn;

    private final TransactionService transactionService = new TransactionService();

    @FXML
    public void initialize() {
        // Configuration des colonnes pour le tableau des transactions
        idColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        commercantColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getCommercant()));
        typeColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getTypeTransaction().name()));
        statutColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getStatutTransaction().name()));
        montantColumn.setCellValueFactory(data ->
                new ReadOnlyObjectWrapper<>(String.format("%,.0f XOF", data.getValue().getMontant())));
        dateColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDate().toString()));

        // Charger les transactions
        loadTransactions();
    }

    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionService.getAllTransactions();
            ObservableList<Transaction> transactionList = FXCollections.observableArrayList(transactions);
            transactionTable.setItems(transactionList);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible de charger les transactions.", NotificationType.ERROR);
        }
    }

    @FXML
    void ajouterTransaction(ActionEvent event) {
        try {
            // Charger le fichier FXML pour ajouter une transaction
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create_transaction.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Créer une transaction");
            stage.setScene(new Scene(root));
            stage.show();

            // Rafraîchir la liste après la fermeture de la fenêtre
            stage.setOnHidden(windowEvent -> loadTransactions());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder au formulaire de création de transaction.", NotificationType.ERROR);
        }
    }

    @FXML
    void modifierStatutTransaction(ActionEvent event) {
        // Récupérer la transaction sélectionnée
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            Utils.showNotification("Erreur", "Veuillez sélectionner une transaction à modifier.", NotificationType.WARNING);
            return;
        }

        try {
            // Vérifier si l'événement a été déclenché par le bouton "Annuler"
            if (event.getSource() == annulerTransactionBtn) {
                // Vérifier le statut actuel de la transaction
                if (selectedTransaction.getStatutTransaction() == StatutTransaction.ANNULEE) {
                    Utils.showNotification("Erreur", "Cette transaction est déjà annulée.", NotificationType.WARNING);
                    return;
                }
                if (selectedTransaction.getStatutTransaction() == StatutTransaction.VALIDE) {
                    Utils.showNotification("Erreur", "Impossible d'annuler une transaction validée.", NotificationType.WARNING);
                    return;
                }

                // Annuler la transaction
                transactionService.annulerTransaction(selectedTransaction.getId());
                Utils.showNotification("Succès", "La transaction a été annulée avec succès.", NotificationType.SUCCESS);

            } else if (event.getSource() == validerTransactionBtn) {
                // Vérifier le statut actuel de la transaction
                if (selectedTransaction.getStatutTransaction() == StatutTransaction.VALIDE) {
                    Utils.showNotification("Erreur", "Cette transaction est déjà validée.", NotificationType.WARNING);
                    return;
                }
                if (selectedTransaction.getStatutTransaction() == StatutTransaction.ANNULEE) {
                    Utils.showNotification("Erreur", "Impossible de valider une transaction annulée.", NotificationType.WARNING);
                    return;
                }

                // Récupérer l'OTP depuis le champ de saisie
                String otpSaisi = otpField.getText().trim();
                if (otpSaisi.isEmpty()) {
                    Utils.showNotification("Erreur", "Veuillez saisir un OTP valide pour valider la transaction.", NotificationType.WARNING);
                    return;
                }

                // Valider la transaction avec l'OTP
                transactionService.validerTransactionAvecOtp(selectedTransaction.getId(), otpSaisi);
                Utils.showNotification("Succès", "La transaction a été validée avec succès !", NotificationType.SUCCESS);
                otpField.clear();
            }

            // Rafraîchir la liste des transactions
            loadTransactions();

        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Une erreur est survenue lors de la modification du statut.", NotificationType.ERROR);
        }
    }


    @FXML
    void annulerTransaction(ActionEvent event) {
        // Récupérer la transaction sélectionnée
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            Utils.showNotification("Erreur", "Veuillez sélectionner une transaction à annuler.", NotificationType.WARNING);
            return;
        }

        try {
            // Annuler la transaction
            transactionService.annulerTransaction(selectedTransaction.getId());
            Utils.showNotification("Succès", "La transaction a été annulée avec succès.", NotificationType.SUCCESS);

            // Rafraîchir la liste des transactions
            loadTransactions();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Une erreur est survenue lors de l'annulation.", NotificationType.ERROR);
        }
    }
}
