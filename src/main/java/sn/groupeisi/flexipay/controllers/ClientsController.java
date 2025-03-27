package sn.groupeisi.flexipay.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sn.groupeisi.flexipay.entities.Client;
import sn.groupeisi.flexipay.services.ClientService;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

public class ClientsController {

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnRechercher;

    @FXML
    private Button btnSupprimer;

    @FXML
    private AnchorPane client_espace;

    @FXML
    private TableColumn<Client, Long> colId;

    @FXML
    private TableColumn<Client, String> colNom;

    @FXML
    private TableColumn<Client, String> colPrenom;

    @FXML
    private TableColumn<Client, String> colAdresse;

    @FXML
    private TableColumn<Client, String> colTelephone;

    @FXML
    private TableColumn<Client, String> colEmail;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Client> tableClients;

    private final ClientService clientService = new ClientService();

    @FXML
    public void initialize() {
        // Configuration des colonnes du tableau
        colId.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        colNom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getPrenom()));
        colAdresse.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getAdresse()));
        colTelephone.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getTelephone()));
        colEmail.setCellValueFactory(data -> new ReadOnlyStringWrapper(data.getValue().getEmail()));

        // Charger les clients dans le tableau
        loadClients();
    }

    // Méthode pour charger tous les clients
    private void loadClients() {
        ObservableList<Client> clientList = FXCollections.observableArrayList(clientService.getAllClients());
        tableClients.setItems(clientList);
    }

    // Ajouter un nouveau client
    @FXML
    void ajouterClient(ActionEvent event) {
        try {
            // Charger le fichier FXML pour ajouter un client
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/create_client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Créer un client");
            stage.setScene(new Scene(root));
            stage.show();

            // Rafraîchir la liste après la fermeture de la fenêtre
            stage.setOnHidden(windowEvent -> loadClients());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder au formulaire de création de client.", NotificationType.ERROR);
        }
    }

    // Modifier un client sélectionné
    @FXML
    void modifierClient(ActionEvent event) {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            Utils.showNotification("Erreur", "Veuillez sélectionner un client à modifier.", NotificationType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/edit_client.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Modifier Client");
            stage.setScene(new Scene(root));

            // Passer les données du client sélectionné au formulaire de modification
            EditClientController editController = loader.getController();
            editController.setClientData(selectedClient);

            stage.show();

            // Rafraîchir la liste après la fermeture de la fenêtre
            stage.setOnHidden(windowEvent -> loadClients());
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Impossible d'accéder au formulaire de modification de client.", NotificationType.ERROR);
        }
    }

    // Rechercher un client par nom
    @FXML
    void rechercherClient(ActionEvent event) {
        String searchQuery = searchField.getText().trim();
        if (searchQuery.isEmpty()) {
            loadClients(); // Afficher tous les clients si la recherche est vide
        } else {
            ObservableList<Client> filteredClients = FXCollections.observableArrayList(
                    clientService.searchClientsByName(searchQuery)
            );
            tableClients.setItems(filteredClients);
        }
    }

    // Supprimer un client sélectionné
    @FXML
    void supprimerClient(ActionEvent event) {
        Client selectedClient = tableClients.getSelectionModel().getSelectedItem();
        if (selectedClient == null) {
            Utils.showNotification("Erreur", "Veuillez sélectionner un client à supprimer.", NotificationType.WARNING);
            return;
        }

        try {
            clientService.deleteClient(selectedClient.getId());
            Utils.showNotification("Succès", "Le client a été supprimé avec succès.", NotificationType.SUCCESS);
            loadClients(); // Rafraîchir la liste après suppression
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showNotification("Erreur", "Une erreur est survenue lors de la suppression du client.", NotificationType.ERROR);
        }
    }
}
