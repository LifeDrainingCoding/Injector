package com.lifedrained.injector.injector;

import javafx.scene.image.Image;
import org.apache.commons.io.FilenameUtils;

public class ProcessData {
    public String name, path;
    public int pid;
    public Image icon;
    public ProcessData(String name, int pid, Image icon, String path) {
        this.name = name;
        this.pid = pid;
        this.icon = icon;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }
    @Override
    public String toString() {
        if (getName().equals("N/A")){
            return getName() + "\n"+ getPid();
        }else {

            return FilenameUtils.getName( getName())+"\n"+getPid();
        }
    }
}
