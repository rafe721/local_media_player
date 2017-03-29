package com.gainxposure.local.ui.carousel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gainxposure.local.PlayCompleteListener;
import com.gainxposure.local.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CarouselExo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CarouselExo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarouselExo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_FILE_PATH = "param1";
    private static final String ARG_POSITION = "param2";

    // TODO: Rename and change types of parameters
    private String m_file_path;
    private int m_position;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "LOCAL";

    private Handler handler;

    private PlayCompleteListener playCompletionListener;

    private View view;

    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private ExoPlayer.EventListener exoPlayerEventListener;

    public CarouselExo() {
        // Required empty public constructor
        handler = new Handler();
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarouselExo.
     */
    // TODO: Rename and change types and number of parameters
    public static CarouselExo newInstance(String param1, int param2) {
        CarouselExo fragment = new CarouselExo();
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
        view = inflater.inflate(R.layout.fragment_carousel_exo, container, false);
        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
        TrackSelector trackSelector = new DefaultTrackSelector(mainHandler, videoTrackSelectionFactory);

        // 2. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this.getContext(), trackSelector, loadControl);
        simpleExoPlayerView = new SimpleExoPlayerView(this.getContext());
        simpleExoPlayerView = (SimpleExoPlayerView) this.view.findViewById(R.id.player_view);

        //Set media controller
        simpleExoPlayerView.setUseController(false);
        simpleExoPlayerView.requestFocus();

        // Bind the player to the view.
        simpleExoPlayerView.setPlayer(player);

        // VideoSource
        Uri mp4VideoUri = null;

        mp4VideoUri = Uri.parse(this.m_file_path);
        DefaultBandwidthMeter bandwidthMeterA = new DefaultBandwidthMeter();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this.getContext(), Util.getUserAgent(this.getContext(), String.valueOf(R.string.app_name)), bandwidthMeterA);

        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

        final MediaSource videoSource = new ExtractorMediaSource(mp4VideoUri, dataSourceFactory, extractorsFactory, null, null);

        player.addListener(new ExoPlayer.EventListener() {
            @Override
            public void onLoadingChanged(boolean isLoading) {
                // Log.v(TAG,"Listener-onLoadingChanged...");
            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                // Log.v(TAG,"Listener-onPlayerStateChanged...");
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    // Toast.makeText(getContext(), "Ended", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest) {
                // Log.v(TAG,"Listener-onTimelineChanged...");
            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {
                // Log.v(TAG,"Listener-onPlayerError...");
                player.stop();
                player.prepare(videoSource);
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPositionDiscontinuity() {
                // Log.v(TAG,"Listener-onPositionDiscontinuity...");
            }
        });

        player.prepare(videoSource);


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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            Long delay = 0L;
            if (null != player) {
                player.setPlayWhenReady(true);
                delay = player.getDuration();
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
            }, delay);
        } else {
            Log.i("LOCAL", "setUserVisibleHint: " + this.m_file_path + " is not Visible Anymore: stop stuff from happening");
            handler.removeCallbacksAndMessages(null);
            if (null != player) {
                player.release();
            }
        }
    }

    public void setPlayCompletionListener(PlayCompleteListener aListener) {
            this.playCompletionListener = aListener;
    }
}
