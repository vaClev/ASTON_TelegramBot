package org.example.weather;

import org.example.HTTPGetter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class OpenMeteoRequester {
    public static final String REQUEST_TEXT_PART1 = "http://api.open-meteo.com/v1/forecast?latitude=";
    public static final String REQUEST_TEXT_PART2 = "&longitude=";
    public static final String REQUEST_TEXT_PART3 = "&daily=temperature_2m_max,temperature_2m_min&timezone=GMT&forecast_days=1";
    private HTTPGetter httpGetter = new HTTPGetter();
    private String latitude;
    private String longitude;

    public OpenMeteoRequester(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
    public String getWeatherDescription() {
        String requestURL = REQUEST_TEXT_PART1 + latitude + REQUEST_TEXT_PART2 + longitude + REQUEST_TEXT_PART3;
        String requestAnswer = httpGetter.HttpGETstring(requestURL);
        System.out.println(requestAnswer);
        return parseDescription(requestAnswer);
    }
    private String parseDescription(String requestAnswer) {
        Object o = null;
        try {
            o = new JSONParser().parse(requestAnswer);
        } catch (ParseException e) {
            System.out.println("parse exception");
            return "";
        }
        JSONObject j = (JSONObject) o;
        JSONObject daily = (JSONObject)j.get("daily");
        JSONArray time = (JSONArray)daily.get("time");
        String date = (String) time.get(0);
        JSONArray temperature_2m_min = (JSONArray)daily.get("temperature_2m_min");
        JSONArray temperature_2m_max = (JSONArray)daily.get("temperature_2m_max");
        Object minTemperature = temperature_2m_min.get(0);
        Object maxTemperature = temperature_2m_max.get(0);
        return date+" минимальная температура ночью "+minTemperature+"C, максимальная температура днем " +maxTemperature+"C";
    }
}
