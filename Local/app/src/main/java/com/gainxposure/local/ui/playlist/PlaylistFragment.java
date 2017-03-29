package com.gainxposure.local.ui.playlist;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gainxposure.local.R;
import com.gainxposure.local.models.MediaFile;
import com.gainxposure.local.utils.PlaylistChangeListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaylistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaylistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistFragment extends Fragment {
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

    public PlaylistFragment() {
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
    public static PlaylistFragment newInstance(String param1, String param2) {
        PlaylistFragment fragment = new PlaylistFragment();
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
        this.view = inflater.inflate(R.layout.fragment_playlist, container, false);

        try {
            /* File aFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Slides" );
            String[] fileList = aFile.list(); */
            this.adHome = view.getContext().getFilesDir().getAbsolutePath() + "/ads/";
            // this.adHome = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Slides";
            displayListView(this.adHome);
            //Generate list View from ArrayList
            Log.i("LOCAL", "onCreateView: List Can be displayed");
            // temporary Safety
            checkButtonClick();
            // Log.i("LOCAL", "onCreate: " + e.getMessage() + " Cause: " + e.getCause());

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void setPlaylistChangeListener(PlaylistChangeListener aListener) {
        this.playlistChangeListener = aListener;
    }

    private void displayListView(String path) {

        MediaFile adLocation = new MediaFile(path);

        if (!adLocation.exists()) {
            adLocation.mkdir();
        }

        // Array List of Files
        ArrayList<MediaFile> fileList = adLocation.getFileList();
        Log.i("LOCAL", "displayListView: FileArr Size" + fileList.size());

        //create an ArrayAdapter from the String Array
        dataAdapter = new MyCustomAdapter(this.getContext(),
                R.layout.country_info, fileList);
        ListView listView = (ListView) this.view.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                File country = (MediaFile) parent.getItemAtPosition(position);
                if (country.isDirectory()) {
                    displayListView(country.getAbsolutePath());
                } else {
                    Toast.makeText(getContext(),
                            "Clicked on Row: " + country.getName(),
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
                responseText.append("The following files were deleted...\n");

                ArrayList<MediaFile> countryList = dataAdapter.countryList;
                for(int i=0;i<countryList.size();i++){
                    MediaFile country = countryList.get(i);
                    /* if (country.isSelected()) {
                        responseText.append("\n" + country.getName());
                    } */
                    try {
                        if (country.isSelected()) {
                            responseText.append("\n" + country.getName());
                            Log.i("LOCAL", "onClick: " + country.getName() + " will be deleted");
                            File aFile = new File(country.getAbsolutePath());
                            aFile.delete();
                            aFile.getAbsoluteFile().delete();

                            if (aFile.exists()) {
                                Log.i("LOCAL", "onClick: could not delete 1");
                                aFile.getCanonicalFile().delete();
                                if (country.exists()) {
                                    Log.i("LOCAL", "onClick: could not delete 2");
                                    getActivity().getApplicationContext().deleteFile(country.getName());
                                }
                            }
                        }
                    } catch (IOException ioe) {
                        Log.i("LOCAL", "onClick: ioe:" + ioe.getMessage());
                    } catch (Exception e) {
                        Log.i("LOCAL", "onClick: e:" + e.getMessage());
                    }

                }

                Toast.makeText(getContext(),
                        responseText, Toast.LENGTH_LONG).show();


                displayListView(adHome);
                playlistChangeListener.onPlaylistChange();

            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<MediaFile> {

        private ArrayList<MediaFile> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<MediaFile> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.country_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.file_name);
                holder.name = (CheckBox) convertView.findViewById(R.id.file_checkbox);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        MediaFile country = (MediaFile) cb.getTag();
                        Toast.makeText(getContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            File country = countryList.get(position);
            holder.code.setText(" (" +  country.getName() + ")");
            holder.name.setText(country.getName());
            holder.name.setChecked(country.isDirectory());
            holder.name.setTag(country);
            Log.i("LOCAL", "getView: FileName: " + country.getName());

            return convertView;

        }

    }
}
