package com.apps.j.tmbex2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Weather2Fragment extends Fragment {
    Weather weather;
    View view;

    public Weather2Fragment(Weather weather) {
        this.weather = weather;
    }

    public Weather2Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

      //  System.out.println("onCreateView2");
        view = inflater.inflate(R.layout.fragment_weather2, container, false);
        return view;
    }

    public void updateContent(Weather weather) {
        this.weather = weather;
        updateView();
    }

    public void updateView() {
  //      System.out.println("view2 "+view);
        if (weather!=null) {
            ((TextView) view.findViewById(R.id.wind_power)).setText(Integer.toString(weather.windSpeed));
            ((TextView) view.findViewById(R.id.wind_direction)).setText(Integer.toString(weather.windDirection)+"°");
            ((TextView) view.findViewById(R.id.humidity)).setText(Integer.toString(weather.humidity)+"%");
            ((TextView) view.findViewById(R.id.sunrise)).setText(weather.sunrise);
            ((TextView) view.findViewById(R.id.sunset)).setText(weather.sunset);
        }
    }

}
