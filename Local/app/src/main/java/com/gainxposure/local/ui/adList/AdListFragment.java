package com.gainxposure.local.ui.adList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gainxposure.local.R;
import com.gainxposure.local.dataSources.db.SlotDB;
import com.gainxposure.local.models.Slot;
import com.gainxposure.local.utils.FileUtils;
import com.gainxposure.local.utils.PlaylistChangeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AdListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AdListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /* Fragment View */
    private View view;

    /* MyCustomAdaptor */
    private MyCustomAdapter dataAdapter = null;

    /* Ad Home */
    private String adHome;

    private PlaylistChangeListener playlistChangeListener;

    public AdListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdListFragment newInstance(String param1, String param2) {
        AdListFragment fragment = new AdListFragment();
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
        this.view = inflater.inflate(R.layout.fragment_adlist, container, false);

        try {
            this.adHome = view.getContext().getFilesDir().getAbsolutePath() + "/ads/";
            displayListView();
            //Generate list View from ArrayList
            Log.i("LOCAL", "onCreateView: List Can be displayed");
            // temporary Safety
            checkButtonClick();

        } catch (Exception e){
            Log.i("LOCAL", "onCreateView: " + e.getMessage() + " Cause: " + e.getCause());
            e.printStackTrace();
        }

        return view;
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
        // TODO: Update argument type and check
        void onFragmentInteraction(Uri uri);
    }

    public void setPlaylistChangeListener(PlaylistChangeListener aListener) {
        this.playlistChangeListener = aListener;
    }

    private void displayListView() {

        ArrayList<Slot> adList = new SlotDB(this.getContext()).getSlotList();

        // Array List of Files
        Log.i("LOCAL", "displayListView: FileArr Size" + adList.size());

        //create an ArrayAdapter from the String Array
        this.dataAdapter = new MyCustomAdapter(this.getContext(),
                R.layout.country_info, adList);

        ListView listView = (ListView) this.view.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(this.dataAdapter);

        listView.setDividerHeight(0);
        listView.setDivider(null);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Slot aSlot = (Slot) parent.getItemAtPosition(position);
                File aSlotFile = new File(aSlot.getPath());
                if (aSlotFile.isDirectory()) {
                    displayListView();
                } else {
                    Toast.makeText(getContext(),
                            aSlotFile.getName(),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkButtonClick() {

        Button myButton = (Button) this.view.findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                StringBuffer responseText = new StringBuffer();
                // responseText.append("The following files were deleted...\n");

                ArrayList<Slot> countryList = dataAdapter.adList;
                for(int i=0;i<countryList.size();i++){
                    Slot aSlot = countryList.get(i);
                    SlotDB slotDb = new SlotDB(getContext());
                    /* if (aSlot.isSelected()) {
                        responseText.append("\n" + aSlot.getName());
                    } */
                    try {
                        if (aSlot.isSelected()) {
                            responseText.append("\n" + aSlot.getFile_name());
                            Log.i("LOCAL", "onClick: " + aSlot.getFile_name() + " will be deleted");
                            File aFile = new File(aSlot.getPath());
                            aFile.delete();
                            aFile.getAbsoluteFile().delete();

                            if (aFile.exists()) {
                                Log.i("LOCAL", "onClick: could not delete 1");
                                aFile.getCanonicalFile().delete();
                                if (aFile.exists()) {
                                    Log.i("LOCAL", "onClick: could not delete 2");
                                    getActivity().getApplicationContext().deleteFile(aFile.getName());
                                }
                            } else {
                                aFile = new File(adHome + "/" + aSlot.getFile_name());
                                if (aFile.exists()) {
                                    Log.i("LOCAL", "onClick: could not delete 1");
                                    aFile.getCanonicalFile().delete();
                                    if (aFile.exists()) {
                                        Log.i("LOCAL", "onClick: could not delete 2");
                                        getActivity().getApplicationContext().deleteFile(aFile.getName());
                                    }
                                }
                            }
                            // delete Slot Record on the Database.
                            slotDb.deleteSlot(aSlot);
                        }
                    } catch (IOException ioe) {
                        Log.i("LOCAL", "onClick: ioe:" + ioe.getMessage());
                    } catch (Exception e) {
                        Log.i("LOCAL", "onClick: e:" + e.getMessage());
                    }

                }

                if (responseText.length() > 0) {
                    String response = responseText.toString();
                    responseText = new StringBuffer();
                    responseText.append("Deleted: " + response);
                } else {
                    responseText.append("No files were selected/deleted.");
                }

                Toast.makeText(getContext(),
                        responseText, Toast.LENGTH_LONG).show();


                displayListView();
                playlistChangeListener.onPlaylistChange();

            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<Slot> {

        private ArrayList<Slot> adList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<Slot> slotList) {
            super(context, textViewResourceId, slotList);
            this.adList = new ArrayList<>();
            this.adList.addAll(slotList);

        }

        private class ViewHolder {
            ImageView thumbnail;
            TextView title;
            TextView subtitle;
            CheckBox check;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.file_item, null);

                holder = new ViewHolder();
                holder.thumbnail = (ImageView) convertView.findViewById(R.id.file_icon);
                holder.title = (TextView) convertView.findViewById(R.id.file_name);
                holder.check = (CheckBox) convertView.findViewById(R.id.file_checkbox);
                convertView.setTag(holder);

                holder.check.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Slot slot_item = (Slot) cb.getTag();
                        /* Toast.makeText(getContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show(); */
                        slot_item.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }


            Slot ad = adList.get(position);
            holder.title.setText(ad.getFile_name());
            holder.check.setText("");
            holder.check.setChecked(ad.isSelected());
            holder.check.setTag(ad);
            File aFile = new File(ad.getPath());
            if (aFile.isFile()) {
                if (FileUtils.isImageFile(aFile.getAbsolutePath())){
                    /* Bitmap resized = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(aFile.getPath()), 120, 120);
                    holder.thumbnail.setImageBitmap(resized); */
                    holder.thumbnail.setImageResource(R.drawable.ic_picture);
                } else if (FileUtils.isVideoFile(aFile.getAbsolutePath())) {
                    /* Bitmap b = ThumbnailUtils.createVideoThumbnail(aFile.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                    holder.thumbnail.setImageBitmap(Bitmap.createScaledBitmap(b, 120, 120, false)); */
                    holder.thumbnail.setImageResource(R.drawable.ic_video);
                }
            }
            Log.i("LOCAL", "getView: FileName: " + ad.getFile_name());

            return convertView;

        }

    }
}
