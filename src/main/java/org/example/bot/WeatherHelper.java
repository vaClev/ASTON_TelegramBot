package org.example.bot;

import org.example.weather.GeoCoordinates;
import org.example.weather.OpenMeteoRequester;
import org.example.weather.YandexGeoCoderRequester;

public class WeatherHelper {
    public static String getTodayTemperature(String address)
    {
        YandexGeoCoderRequester yandex = new YandexGeoCoderRequester();
        GeoCoordinates coord = yandex.getCoordinatesByAddress(address);
        if(coord.getLatitude().equals("0") && coord.getLongitude().equals("0"))
        {
            return "не могу найти это место на карте";
        }
        OpenMeteoRequester openMeteoRequester = new OpenMeteoRequester(coord.getLatitude(),coord.getLongitude());
        return openMeteoRequester.getWeatherDescription();
    }
}
