package org.example.weather.badAPIServices;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherRequest {
    //сервис http://api.openweathermap.org   почему-то нестабильно отвечает на запросы -- вероятно блокирован
    private static final String MY_API_KEY = "d7511337c6e3f780718cfa4ee3a228ee";
    private static final String REQUEST_CORDINATES_PART1 = "http://api.openweathermap.org/geo/1.0/direct?q=";
    private static final String REQUEST_CORDINATES_PART2 = "&limit=5&appid=" + MY_API_KEY;
    private static final String REQUEST_WEATHER_NOW_PART1 = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static final String REQUEST_WEATHER_NOW_PART2 = "&APPID=" + MY_API_KEY;
    private String latitude;
    private String longitude;


    public String getWeatherNowStringDescription(String city) {
        String url = REQUEST_WEATHER_NOW_PART1 + city + REQUEST_WEATHER_NOW_PART2;
        System.out.println(url);
        String requestAnswer;
        try {
            requestAnswer = getHTML(url);
            System.out.println(requestAnswer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Object o = null;
        try {
            o = new JSONParser().parse(requestAnswer);
        } catch (ParseException e) {
            System.out.println("parse exception");
        }

        JSONObject j = (JSONObject) o;
        String temp = (String) j.get("temp");
        String temp_min = (String ) j.get("temp_min");
        String temp_max = (String ) j.get("temp_max");

        System.out.println(temp+" " +temp_min+" "+temp_max);
        return "";
    }

    private void setCoordinates(String city) {

        //get coordinates
        String url = REQUEST_CORDINATES_PART1 + city + REQUEST_CORDINATES_PART2;
        String requestText;
        try {
            requestText = getHTML(url);
            System.out.println(requestText);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // set coordinates to latitude and longitude fields

    }

    public String getHTML(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(100000);
        conn.setReadTimeout(100000);

        try (final BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String inputLine;
            final StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            return content.toString();
        } catch (final Exception ex) {
            System.out.println("exeption");
            ex.printStackTrace();
            return "";
        }
    }
}
