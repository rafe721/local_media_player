package com.gainxposure.local.models;

import android.util.Log;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Rahul on 5/03/2017.
 */

public class MediaFile extends File {

    boolean selected = false;

    public MediaFile(String pathname, boolean selected) {
        super(pathname);
        this.selected = selected;
    }

    public MediaFile(String parent, String child, boolean selected) {
        super(parent, child);
        this.selected = selected;
    }

    public MediaFile(File parent, String child, boolean selected) {
        super(parent, child);
        this.selected = selected;
    }

    public MediaFile(URI uri, boolean selected) {
        super(uri);
        this.selected = selected;
    }

    public MediaFile(String pathname) {
        this(pathname, false);
    }

    public MediaFile(String parent, String child) {
        this(parent, child, false);
    }

    public MediaFile(File parent, String child) {
        this(parent, child, false);
    }

    public MediaFile(URI uri) {
        this(uri, false);
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ArrayList<MediaFile> getFileList() {
        Log.i("LOCAL", "getFileList: message: " + this.getAbsolutePath());
        File[] fileArr = this.listFiles();
        ArrayList<MediaFile> fileList = new ArrayList<>();
        for (File aFile :
                fileArr) {
            fileList.add(new MediaFile(aFile.getAbsolutePath()));
        }
        Log.i("LOCAL", "getFileList: message: " + fileList.size());
        return fileList;
    }
}
