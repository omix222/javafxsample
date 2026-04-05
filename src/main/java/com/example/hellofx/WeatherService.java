package com.example.hellofx;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class WeatherService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public record LocationInfo(double latitude, double longitude, String city, String country) {}

    public record TomorrowForecast(String condition, double tempMax, double tempMin, double windSpeedMax) {}

    public record WeatherInfo(
            String location,
            String condition,
            double temperature,
            double windSpeed,
            int windDirection,
            TomorrowForecast tomorrow
    ) {}

    public LocationInfo fetchLocation() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://ip-api.com/json/"))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        String status = json.get("status").getAsString();
        if (!"success".equals(status)) {
            throw new RuntimeException("位置情報の取得に失敗しました");
        }

        double lat = json.get("lat").getAsDouble();
        double lon = json.get("lon").getAsDouble();
        String city = json.get("city").getAsString();
        String country = json.get("country").getAsString();

        return new LocationInfo(lat, lon, city, country);
    }

    public WeatherInfo fetchWeather(LocationInfo location) throws Exception {
        String url = String.format(
                "https://api.open-meteo.com/v1/forecast"
                + "?latitude=%.4f&longitude=%.4f"
                + "&current_weather=true"
                + "&daily=weathercode,temperature_2m_max,temperature_2m_min,windspeed_10m_max"
                + "&forecast_days=2"
                + "&timezone=auto",
                location.latitude(), location.longitude()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();

        // 現在の天気
        JsonObject current = json.getAsJsonObject("current_weather");
        double temperature = current.get("temperature").getAsDouble();
        double windSpeed = current.get("windspeed").getAsDouble();
        int windDirection = current.get("winddirection").getAsInt();
        int weatherCode = current.get("weathercode").getAsInt();

        // 明日の予報（インデックス 1 = 明日）
        JsonObject daily = json.getAsJsonObject("daily");
        JsonArray codes = daily.getAsJsonArray("weathercode");
        JsonArray tempMax = daily.getAsJsonArray("temperature_2m_max");
        JsonArray tempMin = daily.getAsJsonArray("temperature_2m_min");
        JsonArray windMax = daily.getAsJsonArray("windspeed_10m_max");

        TomorrowForecast tomorrow = new TomorrowForecast(
                toConditionLabel(codes.get(1).getAsInt()),
                tempMax.get(1).getAsDouble(),
                tempMin.get(1).getAsDouble(),
                windMax.get(1).getAsDouble()
        );

        String locationStr = location.city() + ", " + location.country();
        return new WeatherInfo(locationStr, toConditionLabel(weatherCode), temperature, windSpeed, windDirection, tomorrow);
    }

    private String toConditionLabel(int code) {
        return switch (code) {
            case 0  -> "快晴";
            case 1  -> "晴れ";
            case 2  -> "一部曇り";
            case 3  -> "曇り";
            case 45, 48 -> "霧";
            case 51, 53, 55 -> "霧雨";
            case 61, 63, 65 -> "雨";
            case 71, 73, 75 -> "雪";
            case 77 -> "霰（あられ）";
            case 80, 81, 82 -> "にわか雨";
            case 85, 86 -> "にわか雪";
            case 95 -> "雷雨";
            case 96, 99 -> "激しい雷雨";
            default -> "不明 (コード: " + code + ")";
        };
    }
}
