package com.lifedrained.injector.injector;


import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.lifedrained.injector.injector.Singleton.*;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;


public class Controller implements EventHandler<MouseEvent> {
    private File dll;
    private static final Logger log = LogManager.getLogger(Controller.class);
    private ArrayList<ProcessData> listProcess;
    private ProcessData selectedProcess;
    private ProcessGainer processGainer;
    @FXML
    private Button choose_dll_btn, injectbtn,copy_btn;
    @FXML
    private Label path_lbl, exception_lbl,  processNameLbl;
    @FXML
    private TextField link;
    @FXML
    private ListView<ProcessData> processList;
    @FXML
    private Button updateList;
    @FXML
    private void initialize() {

        processGainer = new ProcessGainer();
        processList.setItems(FXCollections.observableArrayList(processGainer.listProcesses()));
        updateList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ProcessGainer gainer = new ProcessGainer();
                processList.setItems(FXCollections.observableArrayList(gainer.listProcesses()));
            }
        });
        processList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ProcessData data = processList.getSelectionModel().getSelectedItem();
                selectedProcess = data;
                log.debug("{}:{}", selectedProcess.getName(), selectedProcess.getPid());
                processNameLbl.setText("Process name: "+ FilenameUtils.getName(selectedProcess.getName()));
            }
        });
        copy_btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Clipboard clipboard = Clipboard.getSystemClipboard();
                ClipboardContent content = new ClipboardContent();
                content.putString(exception_lbl.getText());
                clipboard.setContent(content);
            }
        });
        getInstance().setCopyBtn(copy_btn);
        getInstance().setDebugLbl(exception_lbl);
        choose_dll_btn.setOnMouseClicked(this);
        injectbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (selectedProcess != null) {
                    if(!FilenameUtils.getName(selectedProcess.getName()).equals("Pixel Gun 3D.exe")) {


                        Alert alert = new Alert(Alert.AlertType.INFORMATION,"Are you sure to inject dll into non PG3D process?"
                        ,ButtonType.YES, ButtonType.NO);
                        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
                            @Override
                            public void accept(ButtonType buttonType) {
                                if (buttonType == ButtonType.YES) {
                                    Injector injector = new Injector();
                                    if(injector.inject(selectedProcess.getPid(), dll.getAbsolutePath())) {
                                        Alert alert =  new Alert(Alert.AlertType.CONFIRMATION,"", ButtonType.CLOSE);
                                        alert.setHeaderText("Successfully injected!");
                                        alert.show();
                                    }else {
                                        Alert alert1 = new Alert(Alert.AlertType.ERROR, "An error was occurred during injection process, " +
                                                "see error in right part of injector", ButtonType.CLOSE);
                                        alert1.show();
                                    }
                                }else{
                                    alert.close();
                                }
                            }
                        });

                    }else{
                        Injector injector = new Injector();
                        if(injector.inject(selectedProcess.getPid(), dll.getAbsolutePath())) {
                            Alert alert =  new Alert(Alert.AlertType.CONFIRMATION,"", ButtonType.CLOSE);
                            alert.setHeaderText("Successfully injected!");
                            alert.show();
                        }else {
                            Alert alert1 = new Alert(Alert.AlertType.ERROR, "An error was occurred during injection process, " +
                                    "see error in right part of injector", ButtonType.CLOSE);
                            alert1.show();
                        }
                    }
                }
            }
        });
        link.setStyle("-fx-text-fill: blue; -fx-underline: true");
        link.setEditable(false);
        link.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    HostServices hostServices = HelloApplication.hostServices;
                    hostServices.showDocument("https://www.youtube.com/@TechnoKVofficial/videos");
                }
            }
        });
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Pick dll that you want to inject");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("DLL files (*.dll)", "*.dll");
            fileChooser.getExtensionFilters().add(filter);
             dll = fileChooser.showOpenDialog(copy_btn.getScene().getWindow());
            if (dll != null) {
                path_lbl.setText("Picked dll: "+dll.getAbsolutePath());
            }
        }
    }

}
