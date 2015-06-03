package com.apps.j.tmbex2;

import java.io.Serializable;
import java.util.List;


public class Weather implements Serializable{
    String city, time, description, sunrise, sunset;
    int code,temp, pressure,windDirection,windSpeed, humidity;
    double lat, lng, visibility;
    List<ShortWeather> nextDays;

    public Weather(String city, String time, String description, String sunrise, String sunset,
                   int code,int temp, int pressure,int windDirection, int windSpeed, int humidity,
                    double lat, double lng,double visibility,
                    List<ShortWeather> nextDays) {
        this.city = city;
        this.time = time;
        this.description = description;
        this.sunrise = sunrise;
        this.sunset = sunset;

        this.code = code;
        this.temp = temp;
        this.pressure = pressure;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.humidity = humidity;

        this.lat = lat;
        this.lng = lng;
        this.visibility = visibility;

        this.nextDays = nextDays;
    }

    class ShortWeather implements Serializable{
        String date, day, text;
        int low, high;

        ShortWeather(String date, String day , String text, int low, int high) {
            this.date = date;
            this.day = day;
            this.text = text;

            this.low = low;
            this.high = high;

        }

        @Override
        public String toString(){
            return date + " (" + day + "): " + low + "-" + high + " " + text;
        }
    }


}
