package org.example.weather;

public class GeoCoordinates {
    private String latitude;
    private String longitude;

    public GeoCoordinates(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
    @Override
    public String toString() {
        return "GeoCoordinates{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
