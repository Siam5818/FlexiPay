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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sn.groupeisi.flexipay.entities.Client;
import sn.groupeisi.flexipay.entities.Transaction;
import sn.groupeisi.flexipay.services.ClientService;
import sn.groupeisi.flexipay.services.DashboardService;
import sn.groupeisi.flexipay.services.TransactionService;
import sn.groupeisi.flexipay.utils.JpaUtil;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

import static java.lang.System.exit;

public class DashboardController {

    @FXML
    private HBox body;

    @FXML
    private Button btn_deconnexion;

    @FXML
    private Button btn_espace_carte;

    @FXML
    private Button btn_espace_client;

    @FXML
    private Button btn_reclamation;

    @FXML
    private Button btn_Abonnement;

    @FXML
    private Button btn_transaction;

    @FXML
    private ImageView cirulaire_image;

    @FXML
    private Label clientactif;

    @FXML
    private AnchorPane main_panel;

    @FXML
    private Label main_title;

    @FXML
    private Label nombrealerte;

    @FXML
    private Label nombrecompte;

    @FXML
    private Pane pane_vu;

    @FXML
    private VBox side_bar;

    @FXML
    private Label side_bar_title;

    @FXML
    private TableView<Transaction> tableRecentTransaction;

    @FXML
    private TableColumn<Transaction, Long> colId;

    @FXML
    private TableColumn<Transaction, String> colType;

    @FXML
    private TableColumn<Transaction, String> colDate;

    @FXML
    private TableColumn<Transaction, String> colMontant;

    @FXML
    private Label transactioneffectuer;

    private final DashboardService dashboardService = new DashboardService();
    private final TransactionService transactionService = new TransactionService();

    // Methode d'initialisation du dashboard
    @FXML
    public void initialize() {
        try {
            // Récupérer les données depuis la base
            long clients = dashboardService.getNombreClients();
            long comptes = dashboardService.getNombreComptes();
            long transactions = dashboardService.getNombreTransactions();
            long alertes = dashboardService.getNombreAlertes();

            // Remplir les labels du dashboard
            clientactif.setText(String.valueOf(clients));
            nombrecompte.setText(String.valueOf(comptes));
            transactioneffectuer.setText(String.valueOf(transactions));
            nombrealerte.setText(String.valueOf(alertes));

            // Configurer les colonnes
            colId.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
            colType.setCellValueFactory(cellData->new ReadOnlyObjectWrapper<>(cellData.getValue().getTypeTransaction().toString()));
            colDate.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(cellData.getValue().getDate().toString()));
            colMontant.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(String.format("%.2f XOF", cellData.getValue().getMontant())));

            // Recuperer les transactions récentes
            loadRecentTransac();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erreur lors du préremplissage du tableau de bord : " + e.getMessage());
        }
    }

    private void loadRecentTransac() {
        ObservableList<Transaction> transacList = FXCollections.observableArrayList(transactionService.getRecentTransactions());
        tableRecentTransaction.setItems(transacList);
    }

    @FXML
    void espace_carte(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/carte_bancaire.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre (Stage) pour l'espace carte
            Stage stage = new Stage();
            stage.setTitle("Espace Carte");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder à l'espace carte", NotificationType.ERROR);
        }
    }

    @FXML
    void espace_client(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/clients.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre (Stage) pour l'espace client
            Stage stage = new Stage();
            stage.setTitle("Espace Client");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder à l'espace client", NotificationType.ERROR);
        }
    }

    @FXML
    void espace_reclamation(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/reclamations.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre (Stage) pour les reclamations
            Stage stage = new Stage();
            stage.setTitle("Reclamations");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder aux reclamations", NotificationType.ERROR);
        }
    }

    @FXML
    void espace_Abonnement(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abonnement.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre (Stage) pour les abonnements
            Stage stage = new Stage();
            stage.setTitle("Abonnements");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder aux Abonnements.", NotificationType.ERROR);
        }
    }

    @FXML
    void espace_transaction(ActionEvent event) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transaction.fxml"));
            Parent root = loader.load();
            // Créer une nouvelle fenêtre (Stage) pour les transactions
            Stage stage = new Stage();
            stage.setTitle("Transactions");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder aux transactions", NotificationType.ERROR);
        }
    }

    @FXML
    void logout(ActionEvent event) {
        Utils.showNotification("Deconnection", "Vous etes deconnecter", NotificationType.SUCCESS);
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        exit(0);
    }
}
