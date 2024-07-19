package com.lifedrained.injector.injector;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
     Scene scene;
    public static HostServices hostServices;



    @Override
    public void start(Stage stage) throws IOException {
        hostServices = getHostServices();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("view.fxml"));
         scene = new Scene(fxmlLoader.load(), 600, 420);
        stage.setTitle("Injector by Drained");
        stage.setScene(scene);
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("assets/syringe_icon.svg.png"))));
        stage.setResizable(false);
        stage.show();

    }


    public static void main(String[] args) {
        launch();
    }


}