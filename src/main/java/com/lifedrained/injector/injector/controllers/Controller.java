package com.lifedrained.injector.injector.controllers;



import com.lifedrained.injector.injector.Main;
import com.lifedrained.injector.injector.backend.*;
import com.lifedrained.injector.injector.backend.ProcessData;
import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import static com.lifedrained.injector.injector.backend.Singleton.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Controller implements EventHandler<MouseEvent> {
    private Dll dll;
    private  ArrayList<Dll> dlls;
    private static final Logger log = LogManager.getLogger(Controller.class);
    private ProcessData selectedProcess;
    private ProcessGainer processGainer;
    public static Controller INSTANCE;
    @FXML
    private Menu lastDll,edit,guide;
    @FXML
    private MenuItem launchDllList;
    @FXML
    private MenuBar menu_bar;

    @FXML
    private Button choose_dll_btn, injectbtn,findPGbtn;
    @FXML
    private Label path_lbl,   processNameLbl;
    @FXML
    private TextField link;
    @FXML
    private ListView<ProcessData> processList;
    @FXML
    private Button updateList;
    public Controller(){

    }
    @FXML
    private void initialize() {
        prepareMenus();

        processGainer = new ProcessGainer();
        processList.setItems(FXCollections.observableArrayList(processGainer.listProcesses()));
        processList.setCellFactory(new Callback<ListView<ProcessData>, ListCell<ProcessData>>() {
            @Override
            public ListCell<ProcessData> call(ListView<ProcessData> processDataListView) {
                return new ListAdapter() {};
            }
        });
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
                processNameLbl.setText("Selected process : "+ FilenameUtils.getName(selectedProcess.getName()));
            }
        });

        findPGbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                  ArrayList<ProcessData> list  = processGainer.listProcesses();
                  list.removeIf(new Predicate<ProcessData>() {
                      @Override
                      public boolean test(ProcessData processData) {
                          return !FilenameUtils.getName(processData.getName()).equals("Pixel" +
                                  " Gun 3D.exe");
                      }
                  });
                  if(!list.isEmpty()) {
                      selectedProcess = list.get(0);
                      Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"",ButtonType.OK);
                      alert.setTitle("Success");
                      alert.setHeaderText("Successfully found!");
                      alert.show();
                      log.debug("{}:{}", selectedProcess.getName(), selectedProcess.getPid());
                      processNameLbl.setText("Selected process : "+ FilenameUtils.getName(selectedProcess.getName()));
                  }else {
                      Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.CLOSE);
                      alert.setTitle("Error");
                      alert.setHeaderText("Can't find PG3D process.\nRun Pixel Gun, then try again!");
                      alert.show();
                  }
                }
            }
        });


        choose_dll_btn.setOnMouseClicked(this);
        injectbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
              performInjection();
            }
        });
        link.setStyle("-fx-text-fill: blue; -fx-underline: true");
        link.setEditable(false);
        link.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    HostServices hostServices = Main.hostServices;
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
              dll = new Dll( fileChooser.showOpenDialog(injectbtn.getScene().getWindow()));
            updatePathLbl();
        }
    }
    private void performInjection(){
        if (selectedProcess != null) {
            if (dll != null){
                if (!FilenameUtils.getName(selectedProcess.getName()).equals("Pixel Gun 3D.exe")) {


                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Are you sure to inject dll into non PG3D process?"
                            , ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
                        @Override
                        public void accept(ButtonType buttonType) {
                            if (buttonType == ButtonType.YES) {
                                Injector injector = new Injector();

                                if (injector.inject(selectedProcess.getPid(), dll.getDll().getAbsolutePath())) {
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.CLOSE);
                                    alert.setHeaderText("Successfully injected!");
                                    alert.show();
                                    getInstance();
                                    try {
                                        FileUtils.copyFileToDirectory(dll.getDll().getAbsoluteFile(),lastDllFolder.toFile());

                                    } catch (IOException e) {
                                        Alert alert1 =  new Alert(Alert.AlertType.ERROR, "", ButtonType.CLOSE);
                                        alert1.setTitle("Error");
                                        alert1.setHeaderText("Can't save dll to recent dlls");
                                        log.error(e);
                                    }catch (IllegalArgumentException ignore){

                                    }
                                    prepareMenus();
                                } else {
                                    Alert alert1 = new Alert(Alert.AlertType.ERROR, "An error was occurred during injection process, " +
                                            "see error in right part of injector", ButtonType.CLOSE);
                                    alert1.show();
                                }
                            } else {
                                alert.close();
                            }
                        }
                    });

                } else {
                    Injector injector = new Injector();
                    if (injector.inject(selectedProcess.getPid(), dll.getDll().getAbsolutePath())) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.CLOSE);
                        alert.setHeaderText("Successfully injected!");
                        alert.show();
                        try {
                            FileUtils.copyFileToDirectory(dll.getDll().getAbsoluteFile(),lastDllFolder.toFile());

                        } catch (IOException e) {
                            Alert alert1 =  new Alert(Alert.AlertType.ERROR, "", ButtonType.CLOSE);
                            alert1.setTitle("Error");
                            alert1.setHeaderText("Can't save dll to recent dlls");
                            log.error(e);
                        }catch (IllegalArgumentException ignore){

                        }
                        prepareMenus();
                    } else {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR, "An error was occurred during injection process, " +
                                "see error in right part of injector", ButtonType.CLOSE);
                        alert1.show();
                    }
                }
        }else {
                Alert alert=  new Alert(Alert.AlertType.ERROR,"", ButtonType.CLOSE);
                alert.setTitle("Error during dll injection");
                alert.setHeaderText("You didn't selected dll to inject!");
                alert.show();
            }
        }else{
            Alert alert=  new Alert(Alert.AlertType.ERROR,"", ButtonType.CLOSE);
            alert.setTitle("Error during dll injection");
            alert.setHeaderText("You didn't selected process for injection!");
            alert.show();
        }
}


public void prepareMenus() {
        guide.getItems().clear();
    menu_bar.getMenus().clear();
    getInstance();
    File folder = lastDllFolder.toFile();
    File[] files = folder.listFiles();
    if (files != null){
        if (files.length > 0) {

            dlls = new ArrayList<Dll>();
            for (int i = 0; i < files.length; i++) {
                Dll dll = new Dll(files[i]);
                dlls.add(dll);
            }
            dlls.removeIf(new Predicate<Dll>() {
                @Override
                public boolean test(Dll file) {

                    return dlls.indexOf(file) > 4;
                }
            });
            lastDll.getItems().clear();
            if (folder.isDirectory()) {


                for (int i = 0; i < dlls.size(); i++) {
                    Dll currentDll = dlls.get(i);
                    MenuItem item = new MenuItem();
                    item.setText(FilenameUtils.getName(currentDll.getDll().getName()));
                    item.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            dll = currentDll;
                            updatePathLbl();
                        }
                    });
                    item.setVisible(true);

                    lastDll.getItems().add(item);


                }

                menu_bar.getMenus().add(lastDll);

            }
        } else {
            log.info("No Recent dlls");
        }
}
    MenuItem item = new MenuItem();
    item.setText("Show video guide");
    item.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            HostServices hostServices = Main.hostServices;
            hostServices.showDocument("https://youtu.be/V-G1UHb04ac");
        }
    });
    guide.getItems().add(item);
    edit.getItems().clear();
    launchDllList.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            File folder = lastDllFolder.toFile();
            File[] files = folder.listFiles();
            if (files != null){
                for (int i = 0; i < files.length; i++) {
                    try {
                       FileUtils.delete(files[i]);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                do {
                    if(new ArrayList<File>(Arrays.asList(folder.listFiles())).isEmpty()){
                        prepareMenus();
                        break;
                    }

                }while (!new ArrayList<File>(Arrays.asList(folder.listFiles())).isEmpty());
            }

        }
    });

    edit.getItems().add(launchDllList);
    menu_bar.getMenus().add(edit);
    menu_bar.getMenus().add(guide);
}
private void updatePathLbl(){
        try {


            if (dll != null) {
                path_lbl.setText("Picked dll: " + dll.getDll().getAbsolutePath());
            }
        }catch (NullPointerException e){
            System.out.println("no dll was picked");
        }
}


}
