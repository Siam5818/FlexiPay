module sn.groupeisi.flexipay {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires static lombok;

    opens sn.groupeisi.flexipay to javafx.fxml;
    opens sn.groupeisi.flexipay.entities to org.hibernate.orm.core;

    exports sn.groupeisi.flexipay;
    exports sn.groupeisi.flexipay.controllers;
}