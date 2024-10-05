import org.example.weather.GeoCoordinates;
import org.example.weather.YandexGeoCoderRequester;

public class YandexGeoTests {
    public static void main(String[] args) {
        YandexGeoCoderRequester yandex = new YandexGeoCoderRequester();
        GeoCoordinates coord = yandex.getCoordinatesByAddress("moscow");
        System.out.println(coord);
    }
}
