module attendify {
    requires com.google.gson;
    requires com.zaxxer.hikari;
    requires jakarta.mail;
    requires java.datatransfer;
    requires java.desktop;
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires jbcrypt;
    requires opencv;
    requires software.amazon.awssdk.core;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.rekognition;
    requires software.amazon.awssdk.auth;
    requires org.slf4j;

    opens com.project.gui.dashboard to javafx.fxml;

    exports com.project.gui.dashboard;
    exports com.project.gui.user;
}