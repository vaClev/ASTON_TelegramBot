import org.example.weather.GeoCoordinates;
import org.example.weather.YandexGeoCoderRequester;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class YandexGeoTests {
    public static void main(String[] args) {
        YandexGeoCoderRequester yandex = new YandexGeoCoderRequester();
        String city ="москва";
        String myCity = URLEncoder.encode(city, StandardCharsets.UTF_8);
        GeoCoordinates coord = yandex.getCoordinatesByAddress(myCity);

        System.out.println(coord);
    }
}
