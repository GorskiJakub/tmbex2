package com.apps.j.tmbex2;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Weather34Fragment extends Fragment {
    Weather weather;

    public Weather34Fragment(Weather weather) {
        this.weather = weather;
    }

    public Weather34Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        char unit = (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("units","f").equals("f") ? 'f' : 'c');

        //https://github.com/pwittchen/WeatherIconView <3
        View view = inflater.inflate(R.layout.fragment_weather34, container, false);
        if (weather!=null) {
            ((TextView) view.findViewById(R.id.day1)).setText(weather.nextDays.get(0).toString());
            ((TextView) view.findViewById(R.id.day2)).setText(weather.nextDays.get(1).toString());
            ((TextView) view.findViewById(R.id.day3)).setText(weather.nextDays.get(2).toString());
            ((TextView) view.findViewById(R.id.day4)).setText(weather.nextDays.get(3).toString());
            ((TextView) view.findViewById(R.id.day5)).setText(weather.nextDays.get(4).toString());

        }
        return view;
    }

}
