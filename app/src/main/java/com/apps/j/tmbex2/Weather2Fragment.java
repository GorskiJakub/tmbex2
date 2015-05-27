package com.apps.j.tmbex2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Weather2Fragment extends Fragment {
    Weather weather;

    public Weather2Fragment(Weather weather) {
        this.weather = weather;
    }

    public Weather2Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_weather2, container, false);
    }

    public void updateContent(Weather weather) {
        this.weather = weather;
      //  System.out.println("view2 "+getView());
        updateView();
    }

    public void updateView() {
        if (weather!=null) {
            ((TextView) getView().findViewById(R.id.wind_power)).setText("wind - " + Integer.toString(weather.windSpeed));
            ((TextView) getView().findViewById(R.id.wind_direction)).setText("wind - " + Integer.toString(weather.windDirection)+"°");
            ((TextView) getView().findViewById(R.id.humidity)).setText("humidity - " + Integer.toString(weather.humidity)+"%");
            ((TextView) getView().findViewById(R.id.sunrise)).setText("sunrise - " + weather.sunrise);
            ((TextView) getView().findViewById(R.id.sunset)).setText("sunset - " + weather.sunset);
        }
    }

}
