package com.lifedrained.injector.injector;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
     Scene scene;
    public static HostServices hostServices;



    @Override
    public void start(Stage stage) throws IOException {
        hostServices = getHostServices();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view.fxml"));
         scene = new Scene(fxmlLoader.load(), 600, 420);
        stage.setTitle("Injector by Drained");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(HelloApplication.class.getResourceAsStream("assets/syringe_icon.svg.png"))));
        stage.setResizable(false);
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }


}