package sn.groupeisi.flexipay.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sn.groupeisi.flexipay.entities.Abonnement;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.enums.StatutAbonnement;
import sn.groupeisi.flexipay.services.AbonnementService;
import sn.groupeisi.flexipay.services.CarteBancaireService;
import sn.groupeisi.flexipay.utils.Utils;
import tray.notification.NotificationType;

import java.time.LocalDate;

public class AbonnementController {

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnUpdate;

    @FXML
    private TableColumn<Abonnement, Long> idColumn;

    @FXML
    private TableColumn<Abonnement, String> numCarteColumn;

    @FXML
    private ComboBox<CarteBancaire> carteComboBox;

    @FXML
    private TableColumn<Abonnement, String> serviceColumn;

    @FXML
    private TableColumn<Abonnement, Double> montantColumn;

    @FXML
    private TableColumn<Abonnement, LocalDate> datePrelevementColumn;

    @FXML
    private TableColumn<Abonnement, StatutAbonnement> statutColumn;

    @FXML
    private TableView<Abonnement> tableAbonnements;

    @FXML
    private TextField serviceField;

    @FXML
    private TextField montantField;

    @FXML
    private DatePicker datePrelevementPicker;

    @FXML
    private ComboBox<StatutAbonnement> statutComboBox;

    private final AbonnementService abonnementService = new AbonnementService();
    private final CarteBancaireService carteBancaireService = new CarteBancaireService();
    private ObservableList<Abonnement> abonnements;

    @FXML
    public void initialize() {
        // Configure columns with entity fields
        idColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getId()));
        numCarteColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getCarte().getNumeroCarte()));
        serviceColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getService()));
        montantColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getMontant()));
        datePrelevementColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getDatePrelevement()));
        statutColumn.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getStatutAbonnement()));

        // Load StatutAbonnement enum values into ComboBox
        statutComboBox.setItems(FXCollections.observableArrayList(StatutAbonnement.values()));

        // Charger les cartes bancaires dans la ComboBox
        loadCartesBancaires();

        // Load the list of abonnements into the TableView
        loadAbonnements();
    }

    private void loadAbonnements() {
        abonnements = FXCollections.observableArrayList(abonnementService.getAllAbonnements());
        tableAbonnements.setItems(abonnements);
    }

    private void loadCartesBancaires() {
        ObservableList<CarteBancaire> cartes = FXCollections.observableArrayList(carteBancaireService.getAllCartesBancaires());
        carteComboBox.setItems(cartes);

        // Afficher seulement le numéro de carte dans la ComboBox
        carteComboBox.setConverter(new javafx.util.StringConverter<CarteBancaire>() {
            @Override
            public String toString(CarteBancaire carte) {
                return (carte != null) ? carte.getNumeroCarte() : "";
            }

            @Override
            public CarteBancaire fromString(String string) {
                return null; // Non utilisé
            }
        });
    }

    @FXML
    void enregistrerAbonnement(ActionEvent event) {
        CarteBancaire selectedCarte = carteComboBox.getSelectionModel().getSelectedItem();
        String service = serviceField.getText();
        double montant;

        try {
            montant = Double.parseDouble(montantField.getText());
        } catch (NumberFormatException e) {
            System.out.println("Montant invalide !");
            return;
        }

        StatutAbonnement statut = statutComboBox.getSelectionModel().getSelectedItem();

        // Vérification des champs obligatoires
        if (selectedCarte == null || service.isEmpty() || statut == null) {
            System.out.println("Tous les champs doivent être remplis !");
            return;
        }

        // Création de l'abonnement avec la carte sélectionnée
        Abonnement nouvelAbonnement = abonnementService.createAbonnement(
                selectedCarte.getNumeroCarte(), service, montant, statut
        );

        if (nouvelAbonnement != null) {
            System.out.println("Abonnement créé avec succès !");
            Utils.showNotification("Abonement","Creation d'une nouvelle Abonnement", NotificationType.SUCCESS);

            resetForm();

            loadAbonnements();
        } else {
            System.out.println("Erreur lors de la création de l'abonnement !");
            Utils.showNotification("Abonement","Erreur lors de la creation d'une nouvelle Abonnement", NotificationType.ERROR);
        }
    }

    @FXML
    void deleteAbonnement(ActionEvent event) {
        Abonnement selectedAbonnement = tableAbonnements.getSelectionModel().getSelectedItem();
        if (selectedAbonnement == null) {
            System.out.println("No abonnement selected.");
            return;
        }
        abonnementService.deleteAbonnement(selectedAbonnement.getId());
        loadAbonnements(); // Refresh the table
    }

    @FXML
    void updateAbonnement(ActionEvent event) {
        Abonnement selectedAbonnement = tableAbonnements.getSelectionModel().getSelectedItem();
        if (selectedAbonnement == null) {
            System.out.println("No abonnement selected.");
            return;
        }

        // Update fields
        selectedAbonnement.setService(serviceField.getText());
        try {
            selectedAbonnement.setMontant(Double.parseDouble(montantField.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid montant value.");
            return;
        }
        selectedAbonnement.setDatePrelevement(datePrelevementPicker.getValue());
        selectedAbonnement.setStatutAbonnement(statutComboBox.getSelectionModel().getSelectedItem());

        // Persist updated abonnement
        abonnementService.updateAbonnement(selectedAbonnement);
        loadAbonnements(); // Refresh the table
    }

    private void resetForm() {
        serviceField.clear();
        montantField.clear();
        datePrelevementPicker.setValue(null);
        statutComboBox.getSelectionModel().clearSelection();
        carteComboBox.getSelectionModel().clearSelection();
    }

}