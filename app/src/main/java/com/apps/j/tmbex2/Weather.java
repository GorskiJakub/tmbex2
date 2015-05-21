package com.apps.j.tmbex2;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by j on 06.05.15.
 */
public class Weather implements Serializable{
    String city;
    int code;
    double lat;
    double lng;
    String time;
    int temp;
    int pressure;
    String description;
    int windDirection;
    int windSpeed;
    int humidity;
    double visibility;
    String sunrise;
    String sunset;
    List<ShortWeather> nextDays;

    public Weather(String city, int code, double lat, double lng, String time, int temp, int pressure,
                   String description, int windDirection, int windSpeed, int humidity,
                   double visibility, String sunrise, String sunset, List<ShortWeather> nextDays) {
        this.city = city;
        this.code = code;
        this.lat = lat;
        this.lng = lng;
        this.time = time;
        this.temp = temp;
        this.pressure = pressure;
        this.description = description;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.nextDays = nextDays;
    }

    class ShortWeather implements Serializable{
        String date;
        String day;
        int high;
        int low;
        String text;

        //pon11-13.15 wtorek10
        ShortWeather(String date, String day, int high, int low, String text) {
            this.date = date;
            this.day = day;
            this.high = high;
            this.low = low;
            this.text = text;
        }

        @Override
        public String toString(){
            return date + " (" + day + "): " + low + "-" + high + " " + text;
        }
    }


}
