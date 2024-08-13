module com.lifedrained.injector.injector {

    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.kordamp.ikonli.javafx;
    requires javafx.base;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.sun.jna.platform;
    requires com.sun.jna;
    requires org.apache.logging.log4j.core;
    requires java.desktop;
    requires javafx.swing;
    requires org.apache.commons.io;
    requires io.reactivex.rxjava3;
    requires javafx.media;

    opens com.lifedrained.injector.injector to javafx.fxml;
    exports com.lifedrained.injector.injector;
    exports com.lifedrained.injector.injector.jnainterfaces;
    opens com.lifedrained.injector.injector.jnainterfaces to javafx.fxml;
    exports com.lifedrained.injector.injector.backend;
    opens com.lifedrained.injector.injector.backend to javafx.fxml;
    exports com.lifedrained.injector.injector.controllers;
    opens com.lifedrained.injector.injector.controllers to javafx.fxml;
}