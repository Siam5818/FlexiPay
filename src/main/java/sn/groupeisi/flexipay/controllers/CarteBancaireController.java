package sn.groupeisi.flexipay.controllers;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import sn.groupeisi.flexipay.entities.CarteBancaire;
import sn.groupeisi.flexipay.enums.StatutCarte;
import sn.groupeisi.flexipay.services.CarteBancaireService;
import sn.groupeisi.flexipay.services.TransactionService;

import java.time.LocalDate;
import java.util.Objects;

public class CarteBancaireController {

    @FXML
    private Button btnModifier;

    @FXML
    private ComboBox<StatutCarte> StatutsCarteAModifier;

    @FXML
    private TableColumn<CarteBancaire, LocalDate> dateExpirationColumn;

    @FXML
    private TableColumn<CarteBancaire, Long> idColumn;

    @FXML
    private TableColumn<CarteBancaire, String> numeroColumn;

    @FXML
    private TableColumn<CarteBancaire, String> soldeColumn;

    @FXML
    private TableColumn<CarteBancaire, String> statutColumn;

    @FXML
    private TableView<CarteBancaire> tableCartes;

    private final CarteBancaireService carteBancaireService = new CarteBancaireService();

    @FXML
    public void initialize() {
        // Configuration des colonnes du tableau
        idColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        numeroColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(cellData.getValue().getNumeroCarte()));
        dateExpirationColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(cellData.getValue().getDateExpiration()));
        soldeColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(String.format("%.2f XOF", cellData.getValue().getSolde())));
        statutColumn.setCellValueFactory(cellData-> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatutCarte().toString()));

        // Charger les données dans le tableau
        loadCartes();

        // Ajouter les statuts possibles dans la ComboBox
        StatutsCarteAModifier.getItems().setAll(StatutCarte.values());
    }

    private void loadCartes() {
        try {
            // Charger les cartes depuis le service
            tableCartes.setItems(carteBancaireService.getAllCartesBancaires());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors du chargement des cartes bancaires.");
        }
    }

    @FXML
    public void miseajourStatut(ActionEvent actionEvent) {
        // Récupérer la carte sélectionnée
        CarteBancaire selectedCarte = tableCartes.getSelectionModel().getSelectedItem();
        if (selectedCarte == null) {
            System.out.println("Aucune carte sélectionnée pour mise à jour.");
            return;
        }

        // Récupérer le statut sélectionné dans la ComboBox
        StatutCarte nouveauStatut = StatutsCarteAModifier.getSelectionModel().getSelectedItem();
        if (Objects.isNull(nouveauStatut)) {
            System.out.println("Veuillez sélectionner un nouveau statut.");
            return;
        }

        // Vérifier si le statut est déjà appliqué
        if (selectedCarte.getStatutCarte() == nouveauStatut) {
            System.out.println("La carte est déjà dans ce statut.");
            return;
        }

        // Mettre à jour le statut de la carte
        try {
            selectedCarte.setStatutCarte(nouveauStatut); // Modifier localement l'objet
            carteBancaireService.updateCarteBancaire(selectedCarte); // Sauvegarder la modification dans la base
            System.out.println("Statut de la carte mis à jour avec succès.");

            loadCartes(); // Rafraîchir la liste des cartes après modification
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour du statut de la carte.");
        }
    }
}
