package com.apps.j.tmbex2;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private boolean VERTICAL;

    String city = "";
    Weather weather;

    HashMap<Fragment,Boolean> fragments; //prosty związek klucz-wartość

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        weather = null;
        fragments = new HashMap<>();
        super.onCreate(savedInstanceState);


        city = PreferenceManager.getDefaultSharedPreferences(this).getString("city", "Lodz");

        setContentView(R.layout.activity_main);

        VERTICAL = !isXLargeTablet();

        if (VERTICAL) {

            mPager = (ViewPager) findViewById(R.id.vPager);
            mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            mPager.setOffscreenPageLimit(3);
            mPager.setAdapter(mPagerAdapter);

        }
        getActionBar().setTitle(city);
        getWeather();

    };

    @Override
    public void onAttachFragment(Fragment fragment) {
        System.out.println(fragments);
        if (fragment != null) {
            System.out.println("onattach " + fragment);
            fragments.put(fragment, false);
            System.out.println(fragments);
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // Jeśli użytkownik nie przesunął się jeszcze do innych stron i jest to pierwsza jaką przegląda,
            // to po wciśnięciu Back zostanie wywołane finish().
            super.onBackPressed();
        } else {
            // Jeśli  ma historie, cofa do poprzedniej strony.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Rozkłada menu; Dodaje opcje do rozwijanego menu.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    //Funkcja odpowiadając za obsługę przycisków odśwież i ustawienia.
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
    //parsowanie
    private void getWeather() {

        if (((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() == null) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            if ((weather = readFromCache(city)) != null) {
                Toast.makeText(this, "Read from cache", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            AsyncTask<String, Void, Void> execute = new AsyncTask<String, Void, Void>() {
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
                    if (VERTICAL)
                    {
                        updateFragments();
                    } else {
                        ((Weather1Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment1)).updateContent(weather);
                        ((Weather2Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment2)).updateContent(weather);
                        ((Weather3Fragment) getSupportFragmentManager().findFragmentById(R.id.fragment3)).updateContent(weather);
                    }

                }
            }.execute();

        }
    }

    private Weather readFromCache(String city) {
        String filename = getExternalCacheDir()+"/"+city+".txt";
        Weather weather = null;
        if (!((new File(filename)).exists())) {
            Toast.makeText(this, "No data for " + city, Toast.LENGTH_SHORT).show();
        } else {
            try {
                ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(filename)));
                weather = (Weather) input.readObject();
                System.out.println("read " + weather);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "No data for " + city, Toast.LENGTH_SHORT).show();
            }
        }
        return weather;
    }

    private void saveToCache(Weather weather, String city) {
        String filename = getExternalCacheDir()+"/"+city+".txt";
        try {
            ObjectOutput out = new ObjectOutputStream(new FileOutputStream(new File(filename)));
            out.writeObject(weather);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Saved to cache"+filename, Toast.LENGTH_SHORT).show();
    }

    private String getJSONfromUrl() {
        String country = PreferenceManager.getDefaultSharedPreferences(this).getString("country", "pl");
        String units = PreferenceManager.getDefaultSharedPreferences(this).getString("units", "imperial");

        System.out.println(city+" "+country+" "+units);

        final String url = "https://query.yahooapis.com/v1/public/yql?q=" +
                "select%20*%20from%20weather.forecast%20where%20woeid%20in%20(" +
                "select%20woeid%20from%20geo.places(1)%20where%20text%3D%22" +
                city.replaceAll(" ", "%20") + "%2C%20" + country.replaceAll(" ", "%20") +
                "%22)and%20u%3D%22" + units + "%22&format=json&env=store%3A%2F%2F" +
                "datatables.org%2Falltableswithkeys";

        System.out.println(url);

        InputStream is = null;
        String jsonStr = "";
        //pobranie url
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
        return jsonStr;
    }
    //zapisywanie danych pogodowych do konkretnych pól
    private Weather retrieveWeather (String jsonStr) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONObject channel = jsonObject.getJSONObject("query").getJSONObject("results")
                .getJSONObject("channel");
        String created = jsonObject.getJSONObject("query").getString("created");
        String format = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        DateFormat sdf = new SimpleDateFormat(format, Locale.US);
        Date date = null;
        try {
            date = sdf.parse(created);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String city = channel.getJSONObject("location").getString("city");
        String time = channel.getJSONObject("item").getJSONObject("condition").getString("date");
        String description = channel.getJSONObject("item").getJSONObject("condition").getString("text");
        String sunrise = channel.getJSONObject("astronomy").getString("sunrise");
        String sunset = channel.getJSONObject("astronomy").getString("sunset");

        int code = channel.getJSONObject("item").getJSONObject("condition").getInt("code");
        int temp = channel.getJSONObject("item").getJSONObject("condition").getInt("temp");
        int pressure = channel.getJSONObject("atmosphere").getInt("pressure");
        int windDirection = channel.getJSONObject("wind").getInt("direction");
        int windSpeed = channel.getJSONObject("wind").getInt("speed");
        int humidity = channel.getJSONObject("atmosphere").getInt("humidity");

        double lat = channel.getJSONObject("item").getDouble("lat");
        double lng = channel.getJSONObject("item").getDouble("long");
        double visibility = channel.getJSONObject("atmosphere").getDouble("visibility");


        Gson gson = new Gson();
        JSONArray forecast = channel.getJSONObject("item").getJSONArray("forecast");
        Type listType = new TypeToken<List<Weather.ShortWeather>>(){}.getType();
        List<Weather.ShortWeather> nextDays = gson.fromJson(forecast.toString(), listType);

        return new Weather(city, time, description, sunrise, sunset, code, temp, pressure, windDirection, windSpeed,
                humidity, lat, lng,visibility, nextDays);

    }

    private void updateFragments () {
        System.out.println("fragments update "+fragments);
        for (Map.Entry<Fragment, Boolean> entry: fragments.entrySet())
            updateFragment(entry.getKey());
    }

    private void updateFragment (Fragment fragment) {
        if (fragment instanceof Weather1Fragment) {
            ((Weather1Fragment) fragment).updateContent(weather);
            fragments.put(fragment, true);
        } else if (fragment instanceof Weather2Fragment) {
            ((Weather2Fragment) fragment).updateContent(weather);
            fragments.put(fragment, true);
        } else if (fragment instanceof Weather3Fragment) {
            ((Weather3Fragment) fragment).updateContent(weather);
            fragments.put(fragment, true);
        }
    }
    //klasa odpowiedzialna za ustawienia Pager'a
    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new Weather1Fragment();
                case 1:
                    return new Weather2Fragment();
                case 2:
                    return new Weather3Fragment();
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
