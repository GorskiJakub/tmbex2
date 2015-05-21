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
    Weather weather;
    char unit = 'a';
    OnFragmentInteractionListener mListener;

    public Weather1Fragment(Weather weather) {
        this.weather = weather;
    }


    public Weather1Fragment() {
        System.out.println("new Weather1Fragment");
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unit = (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("units","f").equals("f") ? 'f' : 'c');

        System.out.println("onCreate1");
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        System.out.println("onActivityCreated1");
    }

    @Override
    public void onStart () {
        super.onStart();
        System.out.println("onStart1 "+getView());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        //https://github.com/pwittchen/WeatherIconView <3
      //  System.out.println("unit "+unit);
        //System.out.println("onCreateView1 "+getView());
        return inflater.inflate(R.layout.fragment_weather1, container, false);
    }

   public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
  }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
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

    public void updateContent(Weather weather) {
        System.out.println("updateContent1 "+getView());
        this.weather = weather;
        updateView();
    }

    public void updateView() {
        if (weather!=null) {
            System.out.println("view1 " + getView());
            ((TextView) getView().findViewById(R.id.city)).setText(weather.city);
            ((TextView) getView().findViewById(R.id.latlng)).setText(weather.lat + ", " + weather.lng);
            ((TextView) getView().findViewById(R.id.time)).setText(weather.time);
            ((TextView) getView().findViewById(R.id.temp)).setText(Integer.toString(weather.temp) + (unit == 'f' ? "F" : "°C"));
            ((TextView) getView().findViewById(R.id.humidity)).setText(Integer.toString(weather.humidity) + "%");

            Document doc = Jsoup.parse(weather.description);
            ((TextView) getView().findViewById(R.id.description)).setText(doc.text());
            String icon = setWeatherIcon(weather.code);
          //  System.out.println(weather.code + " " + icon);
            int resID = getResources().getIdentifier("" + icon, "string", getActivity().getPackageName());
            ((WeatherIconView) getView().findViewById(R.id.my_weather_icon)).setIconResource(getString(resID));
        }
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
