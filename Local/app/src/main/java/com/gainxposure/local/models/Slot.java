package com.gainxposure.local.models;

import java.io.File;

/**
 * Created by Rahul on 26/02/2017.
 */

public class Slot {

    private int media_id = 0;

    private String file_name = "";

    private String source = "";

    private String path = "";

    private int slot_no = 0;

    private boolean selected = false;

    /* CONSTRUCTORS */

    public Slot() {
    }

    public Slot(int media_id, String file_name, String source, String path, int slot_no) {
        this.media_id = media_id;
        this.file_name = file_name;
        this.source = source;
        this.path = path;
        this.slot_no = slot_no;
        this.selected = false;
    }

    public Slot(int media_id, String file_name, String source, String path, int slot_no, boolean selected) {
        this.media_id = media_id;
        this.file_name = file_name;
        this.source = source;
        this.path = path;
        this.slot_no = slot_no;
        this.selected = selected;
    }
    /* GETTERS and SETTERS */

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public int getSlot_no() {
        return slot_no;
    }

    public void setSlot_no(int slot_no) {
        this.slot_no = slot_no;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
