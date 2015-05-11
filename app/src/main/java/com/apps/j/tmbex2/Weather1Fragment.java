package com.apps.j.tmbex2;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pwittchen.weathericonview.library.WeatherIconView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Weather1Fragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Weather weather;

    public static Weather1Fragment newInstance(Weather weather) {
        Weather1Fragment fragment = new Weather1Fragment(weather);
        Bundle args = new Bundle();
  //      args.putString(ARG_PARAM1, param1);
     //   args.putString(ARG_PARAM2, param2);
      //  fragment.setArguments(args);
        return fragment;
    }

    public Weather1Fragment(Weather weather) {
        this.weather = weather;
        // Required empty public constructor
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
        char unit = (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("units","f").equals("f") ? 'f' : 'c');

        //https://github.com/pwittchen/WeatherIconView <3
        View view = inflater.inflate(R.layout.fragment_weather1, container, false);
        ((TextView) view.findViewById(R.id.city)).setText(weather.city);
        ((TextView) view.findViewById(R.id.latlng)).setText(weather.lat+", "+weather.lng);
        ((TextView) view.findViewById(R.id.time)).setText(weather.time);
        ((TextView) view.findViewById(R.id.temp)).setText(Integer.toString(weather.temp) + (unit=='f' ? "F" : "°C"));
        ((TextView) view.findViewById(R.id.humidity)).setText(Integer.toString(weather.humidity) + "%");

        Document doc = Jsoup.parse(weather.description);
        ((TextView) view.findViewById(R.id.description)).setText(doc.text());
        String icon = setWeatherIcon(weather.code);
        System.out.println(weather.code+" "+icon);
        int resID = getResources().getIdentifier(""+icon, "string", getActivity().getPackageName());
        ((WeatherIconView) view.findViewById(R.id.my_weather_icon)).setIconResource(getString(resID));
        return view;
    }

    //https://gist.github.com/aloncarmel/8575527 <3
    private String setWeatherIcon(int code) {
        switch(code) {
            case 0: return "wi_tornado";
            case 1: return "wi_storm_showers";
            case 2: return "wi_tornado";
            case 3: return "wi_thunderstorm";
            case 4: return "wi_thunderstorm";
            case 5: return "wi_snow";
            case 6: return "wi_rain_mix";
            case 7: return "wi_rain_mix";
            case 8: return "wi_sprinkle";
            case 9: return "wi_sprinkle";
            case 10: return "wi_hail";
            case 11: return "wi_showers";
            case 12: return "wi_showers";
            case 13: return "wi_snow";
            case 14: return "wi_storm_showers";
            case 15: return "wi_snow";
            case 16: return "wi_snow";
            case 17: return "wi_hail";
            case 18: return "wi_hail";
            case 19: return "wi_cloudy_gusts";
            case 20: return "wi_fog";
            case 21: return "wi_fog";
            case 22: return "wi_fog";
            case 23: return "wi_cloudy_gusts";
            case 24: return "wi_cloudy_windy";
            case 25: return "wi_thermometer";
            case 26: return "wi_cloudy";
            case 27: return "wi_night_cloudy";
            case 28: return "wi_day_cloudy";
            case 29: return "wi_night_cloudy";
            case 30: return "wi_day_cloudy";
            case 31: return "wi_night_clear";
            case 32: return "wi_day_sunny";
            case 33: return "wi_night_clear";
            case 34: return "wi_day_sunny_overcast";
            case 35: return "wi_hail";
            case 36: return "wi_day_sunny";
            case 37: return "wi_thunderstorm";
            case 38: return "wi_thunderstorm";
            case 39: return "wi_thunderstorm";
            case 40: return "wi_storm_showers";
            case 41: return "wi_snow";
            case 42: return "wi_snow";
            case 43: return "wi_snow";
            case 44: return "wi_cloudy";
            case 45: return "wi_lightning";
            case 46: return "wi_snow";
            case 47: return "wi_thunderstorm";
            case 3200: return  "wi_cloud";
            default: return  "wi_cloud";
        }
    }

}
