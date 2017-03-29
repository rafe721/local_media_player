package com.gainxposure.local.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Created by Rahul on 10/03/2017.
 */

public class ResourceUtil {

    private Context _context;

    public final String DIR_SEPERATOR = "/";

    public String DIR_FALLBACK;

    public String DIR_ADS;

    public ResourceUtil(Context context) {
        this._context = context;
        this.DIR_FALLBACK = this._context.getFilesDir() + this.DIR_SEPERATOR + "fallback";
        this.DIR_ADS = this._context.getFilesDir() + this.DIR_SEPERATOR + "ads";
        // this.DIR_ADS = Environment.getExternalStorageState() + this.DIR_SEPERATOR + "Slides";
    }

    public String getAdsDir() {
        return this.DIR_ADS;
    }

    public String getFallbackDir() {
        return this.DIR_FALLBACK;
    }

    public void ensureFallbackAvailability() {
        File aFile = new File(this.DIR_FALLBACK);
        // if the fallback directory does not exist, create it.
        if (!aFile.exists()) {
            aFile.mkdir();
        }
        String assetName = "NoMedia.jpg";
        aFile = new File(this.DIR_FALLBACK + this.DIR_SEPERATOR + assetName);
        if (!aFile.exists()) {
            this.moveAsset(assetName);
        }
    }

    private boolean moveAsset(String assertName) {
        boolean isSuccessful = false;
        AssetManager assetManager = this._context.getAssets();
        FileInputStream in = null;
        FileOutputStream out = null;
        AssetFileDescriptor fileDescriptor = null;
        File file = new File(this.DIR_FALLBACK + this.DIR_SEPERATOR + assertName);
        Log.i("LOCAL", "moveAsset: " + file.getAbsoluteFile());
        try {
            fileDescriptor = assetManager.openFd("NoMedia.jpg");
            in = fileDescriptor.createInputStream();
            out = new FileOutputStream(file.getAbsolutePath());
            this.copyFile(in, out);
            isSuccessful = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccessful;
    }

    private void copyFile(FileInputStream in, FileOutputStream out) {
        try {
            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = in.getChannel();
                destination = out.getChannel();
                long anum = destination.transferFrom(source, 0, source.size());
                Log.i("LOCAL", "CopyFile: Length: " + anum);
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        } catch (Exception e) {
            Log.i("LOCAL", "CopyFile: Length: " + e.getMessage());
        }
    }
}
