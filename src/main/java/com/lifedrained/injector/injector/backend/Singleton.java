package com.lifedrained.injector.injector.backend;

import com.lifedrained.injector.injector.controllers.Controller;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Singleton {
   static Singleton INSTANCE;
   private Controller mainCrontroller;
   private Label debugLbl;
   private Button copyBtn;
   private Stage guideStage;
   public static final Path guidePath = Paths.get("src", "main", "resources", "com", "lifedrained",
           "injector", "injector", "guide","guide.mp4");
   public static final Path lastDllFolder = Paths.get("src", "main", "resources", "com", "lifedrained",
           "injector", "injector","lastdll");
    public static Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }
    private Singleton() {}

    public Label getDebugLbl() {
        return debugLbl;
    }

    public void setDebugLbl(Label debugLbl) {
        this.debugLbl = debugLbl;
    }

    public Button getCopyBtn() {
        return copyBtn;
    }

    public void setCopyBtn(Button copyBtn) {
        this.copyBtn = copyBtn;
    }

    public Stage getGuideStage() {
        return guideStage;
    }

    public void setGuideStage(Stage guideStage) {
        this.guideStage = guideStage;
    }

    public Controller getMainCrontroller() {
        return mainCrontroller;
    }

    public void setMainCrontroller(Controller mainCrontroller) {
        this.mainCrontroller = mainCrontroller;
    }
}
