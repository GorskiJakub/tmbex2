package com.apps.j.tmbex2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Weather3Fragment extends Fragment {

    private  Weather weather;
    View view;
   // OnFragmentInteractionListener mListener;

    public Weather3Fragment() {
        // Required empty public constructor
    }

    public Weather3Fragment(Weather weather) {
        this.weather = weather;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("onCreateView3");
        view = inflater.inflate(R.layout.fragment_weather3, container, false);
        return view;
    }

    //@Override

    // TODO: Rename method, update argument and hook method into UI event
 //   public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }/*/
  //  }
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    /*    try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    public void updateContent(Weather weather) {
        this.weather = weather;
        updateView();
    }

    public void updateView() {
        if (weather!=null) {
            System.out.println("weather "+weather);
            System.out.println("view3 "+view);
            ((TextView) getView().findViewById(R.id.day1)).setText(weather.nextDays.get(0).toString());
            ((TextView) view.findViewById(R.id.day2)).setText(weather.nextDays.get(1).toString());
            ((TextView) view.findViewById(R.id.day3)).setText(weather.nextDays.get(2).toString());
            ((TextView) view.findViewById(R.id.day4)).setText(weather.nextDays.get(3).toString());
            ((TextView) view.findViewById(R.id.day5)).setText(weather.nextDays.get(4).toString());
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
