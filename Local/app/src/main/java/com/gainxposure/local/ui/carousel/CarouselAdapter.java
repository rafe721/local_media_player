package com.gainxposure.local.ui.carousel;

import android.content.Context;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import com.gainxposure.local.PlayCompleteListener;
import com.gainxposure.local.adaptors.FixedFragmentStatePagerAdapter;
import com.gainxposure.local.dataSources.db.SlotDB;
import com.gainxposure.local.models.Slot;
import com.gainxposure.local.utils.FileUtils;
import com.gainxposure.local.utils.ResourceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Rahul on 9/03/2017.
 */

public class CarouselAdapter extends FixedFragmentStatePagerAdapter {

    private Context _context = null;

    private String path  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Slides";

    private String backupPath  = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Slides";

    private File[] file_list;// = new File(path).listFiles();

    private PlayCompleteListener playCompleteListener;

    private FragmentManager fm;

    public CarouselAdapter(Context context, FragmentManager fm, PlayCompleteListener aListener) {
        super(fm);
        this._context = context;
        ResourceUtil resourceUtil = new ResourceUtil(this._context);
        resourceUtil.ensureFallbackAvailability(); // make sure that fallback resources are available.
        this.backupPath = resourceUtil.DIR_FALLBACK;
        this.file_list = getCirclableFileArray(resourceUtil.getAdsDir());
        this.fm = fm;

        this.playCompleteListener = aListener;
    }

    private File[] getCirclableFileArray(String path) {
        Log.i("LOCAL", "getCirclableFileArray: " + path);
        ArrayList<File> fileList = new ArrayList();
        File[] fileArray = new File(path).listFiles();
        if (null == fileArray || 0 == fileArray.length) {
            Log.i("LOCAL", "getCirclableFileArray: Getting Backup: " + this.backupPath);
            fileArray = new File(this.backupPath).listFiles();
            Log.i("LOCAL", "getCirclableFileArray: Backup Size: " + fileArray.length);
        }
        File[] finalArray = null;

        if (fileArray.length <=1) {
            fileList.addAll(Arrays.asList(fileArray));
        } else {
            fileList.add(fileArray[fileArray.length - 1]);
            fileList.addAll(Arrays.asList(fileArray));
            fileList.add(fileArray[0]);
        }

        finalArray = new File[fileList.size()];
        fileList.toArray(finalArray);

        return finalArray;
    }

    private File[] getCirclableFileArray() {
        Log.i("LOCAL", "getCirclableFileArray: " + path);
        ArrayList<File> fileList = new ArrayList();
        ArrayList<Slot> adList = new SlotDB(this._context).getSlotList();
        for (Slot aSlot :
                adList) {
            fileList.add(new File(aSlot.getPath()));
        }
        File[] fileArray = new File(path).listFiles();
        if (null == fileArray || 0 == fileArray.length) {
            Log.i("LOCAL", "getCirclableFileArray: Getting Backup: " + this.backupPath);
            fileArray = new File(this.backupPath).listFiles();
            Log.i("LOCAL", "getCirclableFileArray: Backup Size: " + fileArray.length);
        }
        File[] finalArray = null;

        if (fileArray.length <=1) {
            fileList.addAll(Arrays.asList(fileArray));
        } else {
            fileList.add(fileArray[fileArray.length - 1]);
            fileList.addAll(Arrays.asList(fileArray));
            fileList.add(fileArray[0]);
        }

        finalArray = new File[fileList.size()];
        fileList.toArray(finalArray);

        return finalArray;
    }

    @Override
    public void notifyDataSetChanged() {
        this.file_list = this.getCirclableFileArray(new ResourceUtil(this._context).getAdsDir());
        super.notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        /* TextFragment Text = TextFragment.newInstance("", position);
        Log.i("LOCAL", "getItem: TextFragment position: " + position);
        Text.setPlayCompletionListener(this.playCompleteListener);
        Text.setText(Integer.toString(position));
        return Text; */

        Log.i("LOCAL", "getItem: returning position: " + position);
        if (null != this.playCompleteListener) {
            Log.i("LOCAL", "getItem: PlayCompletionListener Available");
        } else {
            Log.i("LOCAL", "getItem: PlayCompletionListener not Available");
        }
        if (FileUtils.isImageFile(this.file_list[position].getAbsolutePath())) {
            Log.i("LOCAL", "getItem: Getting ImageFragment with File: " + this.file_list[position].getAbsolutePath());
            CarouselImage anImage = CarouselImage.newInstance(this.file_list[position].getAbsolutePath(), position);
            Log.i("LOCAL", "getItem: Getting ImageFragment PlayCompleteListener: " + this.playCompleteListener);
            anImage.setPlayCompletionListener(this.playCompleteListener);
            return anImage;
        } else if (FileUtils.isVideoFile(this.file_list[position].getAbsolutePath())) {
            Log.i("LOCAL", "getItem: Getting VideoFragment with File: " + this.file_list[position].getAbsolutePath());
            /* CarouselVideo aVideo = CarouselVideo.newInstance(this.file_list[position].getAbsolutePath(), position);
            aVideo.setPlayCompletionListener(this.playCompleteListener); */
            CarouselExo aVideo = CarouselExo.newInstance(this.file_list[position].getAbsolutePath(), position);
            // CarouselVideo aVideo = CarouselVideo.newInstance(this.file_list[position].getAbsolutePath(), position);
            aVideo.setPlayCompletionListener(this.playCompleteListener);
            return aVideo;
        }
        CarouselImage anImage = CarouselImage.newInstance(this.file_list[position].getAbsolutePath(), position);

        anImage.setPlayCompletionListener(this.playCompleteListener);
        return anImage;
    }

    @Override
    public int getCount() {
        return file_list.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.i("LOCAL", "destroyItem: " + position);
        super.destroyItem(container, position, object);
    }

}
