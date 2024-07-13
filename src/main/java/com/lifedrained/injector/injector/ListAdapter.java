package com.lifedrained.injector.injector;

import javafx.event.EventHandler;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ListAdapter extends ListCell<ProcessData> {
    private HBox hbox;
    private VBox vbox;
    private Text pid,name;
    private ImageView view;

    public ListAdapter() {
        super();
        vbox = new VBox();
        hbox = new HBox();
        name = new Text();
        pid = new Text();
        view = new ImageView();

        view.setFitWidth(16);
        view.setFitHeight(16);
        view.setPreserveRatio(true);
        hbox.getChildren().addAll(view,name);
        vbox.getChildren().addAll(hbox,pid);

    }
    @Override
    protected void updateItem(ProcessData data, boolean empty) {
        super.updateItem(data, empty);

            name.setText(data.getName());
            pid.setText(String.valueOf(data.getPid()));
            if(data.getIcon()!=null) {
                view.setImage(data.getIcon());
                setGraphic(view);
            }


    }


}
