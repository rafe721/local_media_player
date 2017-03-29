package com.gainxposure.local.ui.carousel;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gainxposure.local.PlayCompleteListener;
import com.gainxposure.local.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Carousel.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Carousel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Carousel extends Fragment implements PlayCompleteListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private View view;
    private ViewPager pager;

    private CarouselAdapter carouselAdapter;

    private int mCurrentPosition;

    private int currentPage = 0;

    public Carousel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Carousel.
     */
    // TODO: Rename and change types and number of parameters
    public static Carousel newInstance(String param1, String param2) {
        Carousel fragment = new Carousel();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PagerAdapter getAdaptor() {
        return this.carouselAdapter;
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
        this.view = inflater.inflate(R.layout.fragment_carousel, container, false);
        final ViewPager pager = (ViewPager)this.view.findViewById(R.id.pager);
        this.pager = pager;
        final CarouselAdapter carouselAdapter = new CarouselAdapter(view.getContext(), this.getChildFragmentManager(), this);
        this.carouselAdapter = carouselAdapter;
        pager.setAdapter(this.carouselAdapter);
        pager.setCurrentItem(this.currentPage);
        pager.setOffscreenPageLimit(1);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if ((mCurrentPosition == carouselAdapter.getCount() - 1) && (position == carouselAdapter.getCount() - 1)) {
                    Log.i("LOCAL", "onPageSelected: moving to Position " + (1));
                    pager.setCurrentItem(1, false);
                    mCurrentPosition = 1;
                    Log.i("LOCAL", "onPageSelected: mCurrentPosition: " + mCurrentPosition);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("LOCAL", "onPageSelected: Curr_Position " + position);
                mCurrentPosition = position; // Declare mCurrentPosition as a global variable to track the current position of the item in the ViewPager
                if (position == carouselAdapter.getCount() - 1) {
                    carouselAdapter.instantiateItem(pager, 1);
                    /* Log.i("LOCAL", "onPageSelected: moving to Position " + (1));
                    pager.setCurrentItem(1, false);
                    mCurrentPosition = 1;
                    Log.i("LOCAL", "onPageSelected: mCurrentPosition: " + mCurrentPosition);*/
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("LOCAL", "onPageScrollStateChanged: state " + state + " mCurrentPosition: " + mCurrentPosition);
                if (mCurrentPosition >= carouselAdapter.getCount() && state ==0) {
                    mCurrentPosition = 1;
                    pager.setCurrentItem(mCurrentPosition);
                }
                if (carouselAdapter.getCount() > 3) {
                    // For going from the first item to the last item, when the 1st A goes to 1st C on the left, again we let the ViewPager do it's job until the movement is completed, we then set the current item to the 2nd C.
                    // Set the current item to the item before the last item if the current position is 0
                    if (mCurrentPosition == 0) {
                        pager.setCurrentItem(carouselAdapter.getCount() - 2, false); // lastPageIndex is the index of the last item, in this case is pointing to the 2nd A on the list. This variable should be declared and initialzed as a global variable
                        mCurrentPosition = carouselAdapter.getCount() - 2;
                    }

                    // For going from the last item to the first item, when the 2nd C goes to the 2nd A on the right, we let the ViewPager do it's job for us, once the movement is completed, we set the current item to the 1st A.
                    // Set the current item to the second item if the current position is on the last
                    if (mCurrentPosition == carouselAdapter.getCount() - 1) {
                        Log.i("LOCAL", "onPageScrollStateChanged: moving to Position " + (1));
                        mCurrentPosition = 1;
                        // pager.setCurrentItem(1, false);
                        Log.i("LOCAL", "onPageScrollStateChanged: mCurrentPosition: " + mCurrentPosition);
                    }

                }
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

    @Override
    public void onPlayCompleted(int position) {
        // this.pager.setCurrentItem(++mCurrentPosition);
        /* if (position == (this.carouselAdapter.getCount() - 1)) {
            Log.i("LOCAL", "onPlayCompleted: Go From final Position " + position + " to position " + 1);
            mCurrentPosition = 1;
            this.pager.setCurrentItem(mCurrentPosition);
        } else */
        if (mCurrentPosition == 1) {
            // move to 1;
            pager.setCurrentItem(1, false);
            pager.setCurrentItem(++mCurrentPosition);
        }   else if (mCurrentPosition == position) {
            Log.i("LOCAL", "onPlayCompleted: Go From cur_Position " + mCurrentPosition + " to position " + (mCurrentPosition + 1));
            /* if (mCurrentPosition == (this.carouselAdapter.getCount() - 1)) {
                mCurrentPosition = 0;
            } */

            this.pager.setCurrentItem(++mCurrentPosition);
        } else {
            Log.i("LOCAL", "onPlayCompleted: Curr_Position " + mCurrentPosition + " Obtained position: " + position);
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

    public void showPresent() {
        Log.i("LOCAL", "showPresent: Display: " + this.mCurrentPosition);

        this.pager.setCurrentItem(this.mCurrentPosition, false);
    }

}
