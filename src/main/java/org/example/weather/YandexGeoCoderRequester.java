package org.example.weather;

import org.example.HTTPGetter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class YandexGeoCoderRequester {
    private static final String API_KEY = "3ae0fa69-793b-4757-a80c-d390d088d39a";
    private static final String REQUEST_URL_PART1 = "https://geocode-maps.yandex.ru/1.x/?apikey=" + API_KEY + "&geocode=";
    private static final String REQUEST_URL_PART2 = "&format=json";
    private final HTTPGetter httpGetter = new HTTPGetter();
    public boolean isAccuracy = true;

    public GeoCoordinates getCoordinatesByAddress(String address) {
        String requestAnswer = getStringAnswer(address);
        System.out.println(requestAnswer);
        String point = getPoint(requestAnswer);
        if (point.isEmpty()) {
            System.out.println("координаты не определены по адресу");
            return new GeoCoordinates("0", "0");
        }
        String[] coordinates = point.split(" ");
        return new GeoCoordinates(coordinates[1], coordinates[0]);
    }

    private String getPoint(String requestAnswer) {
        Object o = null;
        try {
            o = new JSONParser().parse(requestAnswer);
        } catch (ParseException e) {
            System.out.println("parse exception");
            return "";
        }
        JSONObject j = (JSONObject) o;
        JSONObject response = (JSONObject) j.get("response");
        JSONObject GeoObjectCollection = (JSONObject) response.get("GeoObjectCollection");
        JSONObject metaDataProperty = (JSONObject) GeoObjectCollection.get("metaDataProperty");
        JSONObject GeocoderResponseMetaData = (JSONObject) metaDataProperty.get("GeocoderResponseMetaData");
        String found = (String) GeocoderResponseMetaData.get("found");
        if (!found.equals("1")) {
            System.out.println("неточность в адресе");
            isAccuracy = false;
        }
        JSONArray featureMember = (JSONArray) GeoObjectCollection.get("featureMember");
        JSONObject featureMember1 = (JSONObject) featureMember.get(0);
        JSONObject GeoObject = (JSONObject) featureMember1.get("GeoObject");
        JSONObject Point = (JSONObject) GeoObject.get("Point");
        return (String) Point.get("pos");
    }

    private String getStringAnswer(String address) {
        String yandexAddress = address.trim().replaceAll(" ", "+");
        yandexAddress = URLEncoder.encode(yandexAddress, StandardCharsets.UTF_8); //для русских написаний адресов обязательно
        String url = REQUEST_URL_PART1 + yandexAddress + REQUEST_URL_PART2;
        return httpGetter.HttpGETstring(url);
    }
}
