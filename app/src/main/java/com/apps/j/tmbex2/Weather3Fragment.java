package com.apps.j.tmbex2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.apps.j.tmbex2.R;

public class Weather3Fragment extends Fragment {

    Weather weather;

    public Weather3Fragment() {
    }

    public Weather3Fragment(Weather weather) {
        this.weather = weather;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather3, container, false);
    }

    public void updateContent(Weather weather) {
        this.weather = weather;
        updateView();
    }

    public void updateView() {
        if (weather!=null) {
            ((TextView) getView().findViewById(R.id.day1)).setText(weather.nextDays.get(0).toString());
            ((TextView) getView().findViewById(R.id.day2)).setText(weather.nextDays.get(1).toString());
            ((TextView) getView().findViewById(R.id.day3)).setText(weather.nextDays.get(2).toString());
            ((TextView) getView().findViewById(R.id.day4)).setText(weather.nextDays.get(3).toString());
            ((TextView) getView().findViewById(R.id.day5)).setText(weather.nextDays.get(4).toString());
        }
    }
}
