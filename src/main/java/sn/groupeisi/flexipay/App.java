package sn.groupeisi.flexipay;

import jakarta.persistence.EntityManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sn.groupeisi.flexipay.utils.JpaUtil;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/login.fxml"));
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(parent);
        stage.setTitle("Connexion-FlexyPay");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        EntityManager em = JpaUtil.getEntityManager();
        launch();
    }
}