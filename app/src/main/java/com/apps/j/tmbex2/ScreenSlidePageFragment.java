package com.apps.j.tmbex2;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.TooManyListenersException;

public class ScreenSlidePageFragment extends Fragment {
    public static final String PREFS_NAME = "MyPrefsFile";



    public ScreenSlidePageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        final Context context = getActivity();
        view.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "clicked", Toast.LENGTH_SHORT).show();

                getWeather();
            }
        });




        //return inflater.inflate(R.layout.fragment_screen_slide_page, container, false);
        return view;
    }



    private void getWeather() {
        String city = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("city", "Lodz");
        String country = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("country", "pl");
        String units = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("units", "imperial");

        System.out.println(city+" "+country+" "+units);

        final String url = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(" +
                "select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                city + "%2C%20" + country +
                "%22)and%20u%3D%22" + units + "%22&format=json&env=store%3A%2F%2F" +
                "datatables.org%2Falltableswithkeys";

        System.out.println(url);


        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                return getJSONfromUrl(url);
            }

            @Override
            protected void onPostExecute(String jsonStr) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    JSONObject query = jsonObject.getJSONObject("query");
                    JSONObject results = query.getJSONObject("results");
                    System.out.println(results);
                 //   JSONObject query = jsonObject.getJSONObject("query");
                  /*
                    JSONArray routes = jsonObject.getJSONArray("routes");
                    JSONArray legs = routes.getJSONObject(0).getJSONArray("legs");
                    JSONArray steps = legs.getJSONObject(0).getJSONArray("steps");
                    //for (int i=0; i<steps.length(); i++) {
                        String path = steps.getJSONObject(i).getJSONObject("polyline").getString("points");
                        System.out.println(path);
                        //path[i] = path1;
                        List<LatLng> points = PolyUtil.decode(path);
                        polylines.add(map.addPolyline(new PolylineOptions().addAll(points).color(Color.BLUE)));*/
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //System.out.println(jsonStr);
            }

        }.execute();
    }

    private String getJSONfromUrl(String url) {
        InputStream is = null;
        String jsonStr = "";
        try {

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonStr = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
        System.out.println(jsonStr);
        return jsonStr;
    }

}