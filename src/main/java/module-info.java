module com.lifedrained.injector.injector {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.sun.jna.platform;
    requires com.sun.jna;
    requires org.apache.logging.log4j.core;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.commons.io;

    opens com.lifedrained.injector.injector to javafx.fxml;
    exports com.lifedrained.injector.injector;
}