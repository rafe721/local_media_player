package com.gainxposure.local.ui.fileChooser;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gainxposure.local.R;
import com.gainxposure.local.dataSources.db.SlotDB;
import com.gainxposure.local.models.MediaFile;
import com.gainxposure.local.models.Slot;
import com.gainxposure.local.utils.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FileChooser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FileChooser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FileChooser extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;
    private FileChooser.MyCustomAdapter dataAdapter = null;
    private Stack<String> paths;
    private String home;
    private String adsHome;

    private String curr_path;

    public FileChooser() {
        // Required empty public constructor
        this.paths = new Stack<>();
        this.home = Environment.getExternalStorageDirectory().getAbsolutePath();
        /* this.home = "/sdcard"; //FileUtils.getStorageDirectories()[0];
        for (String path :
                FileUtils.getStorageDirectories()) {
            Log.i("LOCAL", "FileChooser: aPath: " + path);
        }
        this.home = FileUtils.getStorageDirectories()[0]; */
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FileChooser.
     */
    // TODO: Rename and change types and number of parameters
    public static FileChooser newInstance(String param1, String param2) {
        FileChooser fragment = new FileChooser();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_file_chooser, container, false);

        try {
            /* File aFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Slides" );
            String[] fileList = aFile.list(); */
            // this.home = Environment.getExternalStorageDirectory().getAbsolutePath();
            this.adsHome = view.getContext().getFilesDir().getAbsolutePath() + "/ads";
            displayListView(this.home);
            //Generate list View from ArrayList
            checkButtonClick();

        } catch (Exception e){
            Log.i("LOCAL", "FileChooser onCreateView: " + e.getMessage() + " Cause: " + e.getCause());
            e.printStackTrace();
        }

        return this.view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and fileCheckBox
        void onFragmentInteraction(Uri uri);
    }

    private void displayListView(String path) {
        Log.i("LOCAL", "displayListView: Pushing: " + path);
        this.paths.push(path);
        this.curr_path = path;
        /* USB Drive Location */
        String USBDrive_0 = "/mnt/usbhost0";
        String USBDrive_1 = "/mnt/usbhost1";
        String USBDrive_2 = "/mnt/usbhost2";
        String USBDrive_3 = "/mnt/usbhost3";

        Log.i("LOCAL", "displayListView: Depth: " + paths.toString());
        ArrayList<MediaFile> fileList = new ArrayList<>();
        /*
        * If the path is Home:
        * 1.
        * */
        if (path.equalsIgnoreCase(this.home)) {
            MediaFile usbDrive = new MediaFile(USBDrive_0);
            if (usbDrive.exists() && usbDrive.getFileList().size() > 0) {
                fileList.add(new MediaFile(usbDrive.getAbsolutePath()));
            }

            usbDrive = new MediaFile(USBDrive_1);
            if (usbDrive.exists() && usbDrive.getFileList().size() > 0) {
                fileList.add(new MediaFile(usbDrive.getAbsolutePath()));
            }

            usbDrive = new MediaFile(USBDrive_2);
            if (usbDrive.exists() && usbDrive.getFileList().size() > 0) {
                fileList.add(new MediaFile(usbDrive.getAbsolutePath()));
            }

            usbDrive = new MediaFile(USBDrive_3);
            if (usbDrive.exists() && usbDrive.getFileList().size() > 0) {
                fileList.add(new MediaFile(usbDrive.getAbsolutePath()));
            }
        }
        fileList.addAll(new MediaFile(path).getFileList());
        Log.i("LOCAL", "displayListView: fileList Length: " + fileList.size());

        //create an ArrayAdaptar from the String Array
        dataAdapter = new FileChooser.MyCustomAdapter(this.getContext(),
                R.layout.country_info, fileList);
        ListView listView = (ListView) this.view.findViewById(R.id.File_list);

        listView.setAdapter(dataAdapter);

        listView.setDividerHeight(0);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                // listView.getItemAtPosition(position);
                File country = (MediaFile) parent.getItemAtPosition(position);
                if (country.isDirectory()) {
                    displayListView(country.getAbsolutePath());
                } else {
                    Toast.makeText(getContext(),
                            country.getName(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void checkButtonClick() {

        ImageButton myButton = (ImageButton) this.view.findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                responseText.append("The following files were added...\n");

                ArrayList<MediaFile> countryList = dataAdapter.countryList;
                for(int i=0;i<countryList.size();i++){
                    MediaFile country = countryList.get(i);
                    if(country.isSelected() && (FileUtils.isImageFile(country.getAbsolutePath()) || FileUtils.isVideoFile(country.getAbsolutePath()))){
                        // delete the file
                        responseText.append("\n" + country.getName());
                        try {
                            Slot aSlot = new Slot (0, country.getName(), "UPLOAD", adsHome + "/" + country.getName(), 0);
                            new SlotDB(getContext()).addSlot(aSlot);
                            copyFile(country.getAbsolutePath(), adsHome + "/" + country.getName());
                        } catch (IOException ioe) {
                            Log.i("LOCAL", "onClick: " + ioe.getMessage());
                            ioe.printStackTrace();
                        }
                    }
                }


                Toast.makeText(getContext(),
                        responseText, Toast.LENGTH_LONG).show();

            }
        });

        ImageButton backButton = (ImageButton) this.view.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String previous_path;
                if (!curr_path.equalsIgnoreCase(home)) {
                    do {
                        previous_path = paths.pop();
                    } while (previous_path.equalsIgnoreCase(curr_path));
                    displayListView(previous_path); // load home directory
                    Log.i("LOCAL", "onClick: " + previous_path);
                } else {
                    Toast.makeText(getContext(),
                            "Home - SDCard", Toast.LENGTH_LONG).show();
                }

            }
        });

        ImageButton refreshButton = (ImageButton) this.view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String previous_path = paths.pop();
                displayListView(previous_path); // load home directory

                Toast.makeText(getContext(), "Refreshed Directory", Toast.LENGTH_LONG).show();


            }
        });


        ImageButton homeButton = (ImageButton) this.view.findViewById(R.id.home_button);
        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                paths.empty(); // empty stack
                displayListView(home); // load home directory

                Toast.makeText(getContext(),
                        "Home - SDCard", Toast.LENGTH_LONG).show();

            }
        });

    }

    public static void copyFile(String sourcePath, String destPath) throws IOException {
        Log.i("LOCAL", "CopyFile: Source: " + sourcePath + " destination: " + destPath);
        File sourceFile = new File(sourcePath);
        File destFile = new File(destPath);

        // check if destination parent directory exists
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();
        Log.i("LOCAL", "Destination Dir exists/created");

        // create destination file is not exists
        if (!destFile.exists()) {
            Log.i("LOCAL", "File Doesnot Exist");
            destFile.createNewFile();
            Log.i("LOCAL", "CopyFile: File Created");
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
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
    }

    private class MyCustomAdapter extends ArrayAdapter<MediaFile> {

        final String usb_directory_prefix = "usbhost";

        final String usb_name_prefix = "USB Drive";

        private ArrayList<MediaFile> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<MediaFile> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            ImageView icon;
            TextView fileName;
            CheckBox fileCheckBox;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FileChooser.MyCustomAdapter.ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.file_item, null);

                holder = new FileChooser.MyCustomAdapter.ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.file_icon);
                holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
                holder.fileCheckBox = (CheckBox) convertView.findViewById(R.id.file_checkbox);
                convertView.setTag(holder);

                holder.fileCheckBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        MediaFile country = (MediaFile) cb.getTag();
                        /* Toast.makeText(getContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show(); */
                        if (FileUtils.isVideoFile(country.getAbsolutePath()) || FileUtils.isImageFile(country.getAbsolutePath())) {
                            country.setSelected(cb.isChecked());
                        } else {
                            Toast.makeText(getContext(),
                                    "Not a media File.",
                                    Toast.LENGTH_LONG).show();
                            cb.setChecked(false);
                        }
                    }
                });
            }
            else {
                holder = (FileChooser.MyCustomAdapter.ViewHolder) convertView.getTag();
            }

            MediaFile aFile = countryList.get(position);
            if (aFile.getName().equalsIgnoreCase(usb_directory_prefix + "0")) {
                holder.fileName.setText(usb_name_prefix + " 0");
            } else if (aFile.getName().equalsIgnoreCase(usb_directory_prefix + "1")) {
                holder.fileName.setText(usb_name_prefix + " 1");
            } else if (aFile.getName().equalsIgnoreCase(usb_directory_prefix + "2")) {
                holder.fileName.setText(usb_name_prefix + " 2");
            } else if (aFile.getName().equalsIgnoreCase(usb_directory_prefix + "3")) {
                holder.fileName.setText(usb_name_prefix + " 3");
            } else {
                holder.fileName.setText(aFile.getName());
            }
            // check boxtext not set
            holder.fileCheckBox.setChecked(aFile.isSelected());
            holder.fileCheckBox.setTag(aFile);
            if (aFile.isDirectory()) {
                if (aFile.getName().equalsIgnoreCase(usb_directory_prefix)){
                    holder.icon.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.ic_usb_connected));
                } else {
                    // holder.icon.setImageResource(R.drawable.ic_folder_48);
                    holder.icon.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.ic_folder_48));
                }
                holder.fileCheckBox.setVisibility(View.GONE);
            } else if (aFile.isFile()) {
                if (FileUtils.isImageFile(aFile.getAbsolutePath())){
                    Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(aFile.getPath()), 120, 120);
                    holder.icon.setImageBitmap(resized);
                } else if (FileUtils.isVideoFile(aFile.getAbsolutePath())) {
                    Bitmap b = ThumbnailUtils.createVideoThumbnail(aFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                    holder.icon.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false));
                } else {
                    holder.icon.setImageBitmap(BitmapFactory.decodeResource(getContext().getResources(),
                            R.drawable.ic_file_48));
                }
            }

            return convertView;

        }

    }

}
