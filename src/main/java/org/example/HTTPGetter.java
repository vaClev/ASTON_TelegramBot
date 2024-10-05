package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPGetter {
    //TODO решить проблему использования русских букв одновременно с английскими
    //https://geocode-maps.yandex.ru/1.x/?apikey=xxxxxx&geocode=Нижний+Новгород&format=json
    public String HttpGETstring(String urlToRead)  {
        StringBuilder result = new StringBuilder();
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlToRead);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
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
