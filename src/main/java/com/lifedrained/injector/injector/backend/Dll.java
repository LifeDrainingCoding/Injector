package com.lifedrained.injector.injector.backend;

import java.io.File;

public class Dll {
    private File dll;
    private boolean isChecked =false;
    private int pos;

    public Dll(File dll){
        this.dll =dll;
    }
    public Dll(File dll, int pos){
        this.dll =dll;
        this.pos = pos;
    }

    public File getDll() {
        return dll;
    }

    public void setDll(File dll) {
        this.dll = dll;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
