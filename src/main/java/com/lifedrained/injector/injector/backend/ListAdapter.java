package com.lifedrained.injector.injector.backend;

import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.io.FilenameUtils;

public class ListAdapter extends ListCell<ProcessData> {
    public HBox hbox;
    public VBox vbox;
    public Text pid,name;
    public ImageView view;

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
        if(!empty || data != null) {
            name.setText(data.getName());
            pid.setText(String.valueOf(data.getPid()));

                view.setImage(data.getIcon());
                setText(FilenameUtils.getName( data.getName())+"\n"+data.getPid());

        }
    }


}
