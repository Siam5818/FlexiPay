package sn.groupeisi.flexipay.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.IOException;

public class Utils {
    // 🔹 Affichage des notifications
    public static void showNotification(String title, String message, NotificationType type) {
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotificationType(type);
        tray.showAndDismiss(javafx.util.Duration.seconds(3)); // Affiche pendant 3 secondes
    }

    // 🔹 Chargement d'une nouvelle page
    public static void loadPage(Stage currentStage, String fxmlPath, String title) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Utils.class.getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            currentStage.setScene(scene);
            currentStage.setTitle(title);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showNotification("Erreur", "Impossible de charger la page " + fxmlPath, NotificationType.ERROR);
        }
    }
}
