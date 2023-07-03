module SZU.DID.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base;
    requires javafx.graphics;
    requires weid.java.sdk;
    requires java.desktop;
    requires static lombok;
    requires transitive org.mapstruct.processor;
    requires fisco.bcos.java.sdk;
    requires java.logging;
    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.Time;
    opens org.example.Time to javafx.fxml;
}