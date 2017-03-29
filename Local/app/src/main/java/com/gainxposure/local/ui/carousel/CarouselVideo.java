package com.gainxposure.local.ui.carousel;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.gainxposure.local.PlayCompleteListener;
import com.gainxposure.local.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarouselVideo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarouselVideo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarouselVideo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILE_PATH = "param1";
    private static final String ARG_POSITION = "param2";

    // TODO: Rename and change types of parameters
    private String m_file_path;
    private int m_position;

    private OnFragmentInteractionListener mListener;

    private Handler handler;

    private PlayCompleteListener playCompletionListener;

    private View view;

    private VideoView video;

    private MediaController vidControl;

    public CarouselVideo() {
        // Required empty public constructor
        handler = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarouselVideo.
     */
    // TODO: Rename and change types and number of parameters
    public static CarouselVideo newInstance(String param1, int param2) {
        CarouselVideo fragment = new CarouselVideo();
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
        this.view = inflater.inflate(R.layout.fragment_carousel_video, container, false);
        this.video = (VideoView) view.findViewById(R.id.video_view);
        this.vidControl = new MediaController(this.getContext());
        // mMediaController.setAnchorView(findViewById(R.id.container));
        // mMediaController.setMediaPlayer(mMediaPlayerControl);
        // mMediaController.setEnabled(false);
        this.vidControl.setAnchorView(view);
        this.vidControl.setMediaPlayer(this.video);
        this.vidControl.setEnabled(false);

        this.video.setMediaController(vidControl);
        // Bitmap bMap = BitmapFactory.decodeFile(this.m_file_path);
        this.video.setVideoURI(Uri.parse(this.m_file_path));
        // this.video.setVideoPath(this.m_file_path);
        // video.setMediaController(mediaController);
        this.video.requestFocus();
        this.video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                /* if (playCompletionListener != null)
                    playCompletionListener.onPlayCompleted(m_position); */
            }
        });
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


    /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            if (this.m_file_path == null) {
                playCompletionListener.onPlayCompleted(0); // first position
            }
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is Visible: do Stuff");
            if (null != this.video) {
                this.video.start();
            }

        } else {
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is not Visible Anymore: stop stuff from happening");
            // handler.removeCallbacksAndMessages(null);
            if (null != this.video) {
                Log.i("LOCAL", "setUserVisibleHint: ");
                this.video.stopPlayback();
            }
            // this.video.stopPlayback();
        }
    } */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        int delay = 0;
        if (isVisibleToUser){
            if (this.m_file_path == null) {
                playCompletionListener.onPlayCompleted(0); // first position
            }

            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is Visible: do Stuff");
            if (null != this.video) {
                this.video.start();
                delay = this.video.getDuration();
            }
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is Visible: do Stuff");
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
            }, video.getDuration());
        } else {
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is not Visible Anymore: stop stuff from happening");
            handler.removeCallbacksAndMessages(null);
            // this.video.stopPlayback();
            // this.vidControl.removeAllViews();
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
