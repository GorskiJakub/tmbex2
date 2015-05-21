package com.apps.j.tmbex2;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends FragmentActivity implements Weather1Fragment.OnFragmentInteractionListener {

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private boolean VERTICAL;

    String city = "";
    Weather weather;

   // Weather1Fragment fragment1;
  //  Weather2Fragment fragment2;
   // Weather3Fragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        weather = null;

        city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "Lodz");


        System.out.println("---------------");
        setContentView(R.layout.activity_main);

        VERTICAL = !isXLargeTablet();
        System.out.println("ON CREATE " + VERTICAL);

        if (VERTICAL) {

            mPager = (ViewPager) findViewById(R.id.pager);
            mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            mPager.setAdapter(mPagerAdapter);

        } else {
     //       fragment1 = ;
            //      fragment2 = (Weather2Fragment)getSupportFragmentManager().findFragmentById(R.id.fragment2);
            //      fragment3 = (Weather3Fragment)getSupportFragmentManager().findFragmentById(R.id.fragment3);
        }

       // System.out.println("fragment1");
   //     System.out.println(fragment1);
     //   System.out.println(fragment1.getView());
        getActionBar().setTitle(city);
        System.out.println("---------------");

        getWeather();

    }



    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.action_refresh:
                getWeather();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private boolean isXLargeTablet() {
        //return (getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
        if (((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation() % 180 != 0)
            return true;
        return false;
    }

    private void getWeather() {

        if (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            if ((weather = readFromCache(city)) != null) {
                //setTextViews(view, weather);
                Toast.makeText(this, "Read from cache", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... params) {

                    try {
                        weather = retrieveWeather(getJSONfromUrl());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void v) {
                    try {
                        saveToCache(weather, city);
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    System.out.println("weather " + weather);


                  //  System.out.println(fragment1);
                //    System.out.println(fragment1.getView());
                //    System.out.println(fragment2.view);
                    if (VERTICAL)
                        ((ViewPagerAdapter)mPagerAdapter).getFragment1().updateContent(weather);
                    else
                        ((Weather1Fragment)getSupportFragmentManager().findFragmentById(R.id.fragment1)).updateContent(weather);
                    //fragment1.updateContent(weather);
            //        fragment2.updateContent(weather);
           //         System.out.println(fragment1.view);
                //    fragment3.updateContent(weather);

                }
            }.execute();

        }
    }

    private Weather readFromCache(String city) {
        String filename = getExternalCacheDir()+"/"+city+".txt";
        Weather weather = null;
        if (!((new File(filename)).exists())) {
            Toast.makeText(this, "No cached data for " + city, Toast.LENGTH_SHORT).show();
        } else {
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(filename)));
                weather = (Weather) input.readObject();
                System.out.println("read " + weather);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No cached data for " + city, Toast.LENGTH_SHORT).show();
            }
        }
        return weather;
    }

    private void saveToCache(Weather weather, String city) {
        //String filename = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+city+".txt";
        String filename = getExternalCacheDir()+"/"+city+".txt";
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(filename)));
            out.writeObject(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Saved to cache as "+filename, Toast.LENGTH_SHORT).show();
    }

    private String getJSONfromUrl() {
        String country = PreferenceManager.getDefaultSharedPreferences(this).getString("country", "pl");
        String units = PreferenceManager.getDefaultSharedPreferences(this).getString("units", "imperial");

        System.out.println(city+" "+country+" "+units);

        final String url = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(" +
                "select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                city + "%2C%20" + country +
                "%22)and%20u%3D%22" + units + "%22&format=json&env=store%3A%2F%2F" +
                "datatables.org%2Falltableswithkeys";

        System.out.println(url);

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
     //   System.out.println(jsonStr);
        return jsonStr;
    }

    private Weather retrieveWeather (String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject channel = jsonObject.getJSONObject("query").getJSONObject("results")
                .getJSONObject("channel");
  //      System.out.println(channel);
        String created = jsonObject.getJSONObject("query").getString("created");
        String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        DateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(created);

        } catch (ParseException e) {
            e.printStackTrace();
        }
   //     System.out.println(date.toString());
        String city = channel.getJSONObject("location").getString("city");
        int code = channel.getJSONObject("item").getJSONObject("condition").getInt("code");
        double lat = channel.getJSONObject("item").getDouble("lat");
        double lng = channel.getJSONObject("item").getDouble("long");
        String time = channel.getJSONObject("item").getJSONObject("condition").getString("date");
        int temp = channel.getJSONObject("item").getJSONObject("condition").getInt("temp");
        int pressure = channel.getJSONObject("atmosphere").getInt("pressure");
        String description = channel.getJSONObject("item").getString("description");
        int windDirection = channel.getJSONObject("wind").getInt("direction");
        int windSpeed = channel.getJSONObject("wind").getInt("speed");
        int humidity = channel.getJSONObject("atmosphere").getInt("humidity");
        double visibility = channel.getJSONObject("atmosphere").getDouble("visibility");
        String sunrise = channel.getJSONObject("astronomy").getString("sunrise");
        String sunset = channel.getJSONObject("astronomy").getString("sunset");

        Gson gson = new Gson();
        JSONArray forecast = channel.getJSONObject("item").getJSONArray("forecast");
        Type listType = new TypeToken<List<Weather.ShortWeather>>(){}.getType();
        List<Weather.ShortWeather> nextDays = gson.fromJson(forecast.toString(), listType);

        return new Weather(city, code, lat, lng, time, temp, pressure, description, windDirection, windSpeed,
                humidity, visibility, sunrise, sunset, nextDays);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public Weather1Fragment fragment1;
        public Weather2Fragment fragment2;
        public Weather3Fragment fragment3;

        public Weather1Fragment getFragment1() {
            return fragment1;
        }

        public Weather2Fragment getFragment2() {
            return fragment2;
        }

        public Weather3Fragment getFragment3() {
            return fragment3;
        }

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragment1 = new Weather1Fragment();
            fragment2 = new Weather2Fragment();
            fragment3 = new Weather3Fragment();
        }

        @Override
        public Fragment getItem(int position) {
            System.out.println("getItem - "+position);
            switch (position) {
                case 0:
                    return fragment1;
                case 1:
                    return fragment2;
                case 2:
                    return fragment3;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }




}
