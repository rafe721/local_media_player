package com.gainxposure.local.ui.carousel;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gainxposure.local.PlayCompleteListener;
import com.gainxposure.local.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarouselImage.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarouselImage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarouselImage extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILE_PATH = "file_path";
    private static final String ARG_POSITION = "position";

    // TODO: Rename and change types of parameters
    private String m_file_path;
    private int m_position;

    private OnFragmentInteractionListener mListener;

    private View view;

    private Handler handler;

    private PlayCompleteListener playCompletionListener;

    public CarouselImage() {
        // Required empty public constructor
        handler = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarouselImage.
     */
    // TODO: Rename and change types and number of parameters
    public static CarouselImage newInstance(String param1, int param2) {
        CarouselImage fragment = new CarouselImage();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, param1);
        args.putInt(ARG_POSITION, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            m_file_path = getArguments().getString(ARG_FILE_PATH);
            m_position = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        this.view = inflater.inflate(R.layout.fragment_carousel_image, container, false);
        ImageView image = (ImageView) view.findViewById(R.id.image_view);
        Bitmap bMap = BitmapFactory.decodeFile(this.m_file_path);
        image.setImageBitmap(bMap);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is Visible: do Stuff");
            Long delay = 0L;
            if (null != this.m_file_path || !"".equalsIgnoreCase(this.m_file_path)){
                delay = 10000L;
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    Log.i("LOCAL", "onCreateView: completing Media: " + m_file_path);
                    if (null != playCompletionListener) {
                        Log.i("LOCAL", "setUserVisibleHint: Calling PlayCompletionListener");
                        playCompletionListener.onPlayCompleted(m_position);
                    } else {
                        Log.i("LOCAL", "setUserVisibleHint: No PlayCompletionListener");
                    }
                }
            }, delay);
        } else {
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is not Visible Anymore: stop stuff from happening");
            handler.removeCallbacksAndMessages(null);
        }
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

    public void setPlayCompletionListener(PlayCompleteListener aListener)
    {
        try
        {
            this.playCompletionListener = aListener;
        }
        catch (Exception e)
        {
            Log.i("LOCAL", "Exception when assigning PlayCompletionListener: " + e.getMessage());
        }
    }
}
