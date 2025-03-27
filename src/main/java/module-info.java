module sn.groupeisi.flexipay {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires static lombok;
    requires TrayNotification;
    requires twilio;
    requires jakarta.mail;

    // Ouverture des packages pour Hibernate et JavaFX
    opens sn.groupeisi.flexipay.entities to org.hibernate.orm.core;
    opens sn.groupeisi.flexipay.controllers to javafx.fxml;
    opens sn.groupeisi.flexipay to javafx.fxml;

    exports sn.groupeisi.flexipay;

}