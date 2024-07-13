package com.lifedrained.injector.injector;

import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class Singleton {
   static Singleton INSTANCE;
   private Label debugLbl;
   private Button copyBtn;
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
}
