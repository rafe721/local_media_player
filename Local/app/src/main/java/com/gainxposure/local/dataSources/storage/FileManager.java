package com.gainxposure.local.dataSources.storage;

import android.content.Context;
import android.util.Log;

import java.io.File;

/**
 * Created by Rahul on 27/02/2017.
 */

public class FileManager {

    private Context context;

    /* Constructor */
    public FileManager(Context context) {
        this.context = context;
    }

    public boolean clearFile (String file_name) {
        boolean isDeleted = true;

        String absoluteFilename = new StorageHandler(this.context).getMediaDirectory() + "/" + file_name;
        Log.i("HandySan", "Absolute File Path: " + absoluteFilename);

        File file = new File(absoluteFilename);
        if (file.exists()) {
            if (file.delete()) {
                Log.i("HandySan", "clearFile: "+ file.getName() + " deleted");
            } else {
                Log.i("HandySan", "clearFile: "+ file.getName() + " not deleted.");
            }
        } else {
            Log.i("HandySan", "clearFile: "+ file.getName() + " does not exist.");
        }

        return isDeleted;
    }

}
