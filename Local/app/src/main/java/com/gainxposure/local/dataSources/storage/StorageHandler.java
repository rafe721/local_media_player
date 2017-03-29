package com.gainxposure.local.dataSources.storage;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by Rahul on 26/02/2017.
 */

public class StorageHandler {

    Context context;

    public StorageHandler(Context aContext) {
        this.context = aContext;
    }

    public String getMediaDirectory() {

        String media_directory = this.context.getFilesDir ()+ "/media";
        if (this.createDirIfNotExists(media_directory)) {
            return media_directory;
        }
        return "";
    }

    public boolean storeFile() {

        boolean isStored = false;

        File file = new File(getMediaDirectory()+"/Download.php");
        if (file.exists()) {
            Log.i("HandySan", "createDirIfNotExists: "+ file.getName() + " does Exists");
        } else {
            Log.i("HandySan", "createDirIfNotExists: "+ file.getName() + " does not Exist");
        }

        file = new File(getMediaDirectory()+"/Ouvra.php");
        if (file.exists()) {
            Log.i("HandySan", "createDirIfNotExists: "+ file.getName() + " does Exists");
        } else {
            Log.i("HandySan", "createDirIfNotExists: "+ file.getName() + " does not Exist");
        }

        file = new File(getMediaDirectory()+"/Gestapo.php");
        if (file.exists()) {
            Log.i("HandySan", "createDirIfNotExists: "+ file.getName() + " does Exists");
        } else {
            Log.i("HandySan", "createDirIfNotExists: "+ file.getName() + " does not Exist");
        }
        return isStored;
    }

    private boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(path);
        Log.i("HandySan", "createDirIfNotExists: received path: " + path);
        if (!file.exists()) {
            Log.i("HandySan", "createDirIfNotExists: Folder does not exist");
            if (!file.mkdirs()) {
                Log.i("HandySan", "createDirIfNotExists: Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }
}
