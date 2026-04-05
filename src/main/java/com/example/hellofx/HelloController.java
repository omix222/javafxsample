package com.example.hellofx;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HelloController {

    // 今日
    @FXML private Label locationLabel;
    @FXML private Label conditionLabel;
    @FXML private Label temperatureLabel;
    @FXML private Label windLabel;

    // 明日
    @FXML private Label tomorrowConditionLabel;
    @FXML private Label tomorrowTempLabel;
    @FXML private Label tomorrowWindLabel;

    @FXML private Label statusLabel;
    @FXML private Button refreshButton;

    private final WeatherService weatherService = new WeatherService();

    @FXML
    public void initialize() {
        fetchWeather();
    }

    @FXML
    protected void onRefreshButtonClick() {
        fetchWeather();
    }

    private void fetchWeather() {
        setLoading(true);

        Task<WeatherService.WeatherInfo> task = new Task<>() {
            @Override
            protected WeatherService.WeatherInfo call() throws Exception {
                WeatherService.LocationInfo location = weatherService.fetchLocation();
                return weatherService.fetchWeather(location);
            }
        };

        task.setOnSucceeded(e -> {
            WeatherService.WeatherInfo w = task.getValue();
            locationLabel.setText(w.location());
            conditionLabel.setText(w.condition());
            temperatureLabel.setText(String.format("%.1f°C", w.temperature()));
            windLabel.setText(String.format("風速: %.1f km/h", w.windSpeed()));

            WeatherService.TomorrowForecast t = w.tomorrow();
            tomorrowConditionLabel.setText(t.condition());
            tomorrowTempLabel.setText(String.format("最高 %.1f°C / 最低 %.1f°C", t.tempMax(), t.tempMin()));
            tomorrowWindLabel.setText(String.format("最大風速: %.1f km/h", t.windSpeedMax()));

            statusLabel.setText("");
            setLoading(false);
        });

        task.setOnFailed(e -> {
            statusLabel.setText("取得に失敗しました: " + task.getException().getMessage());
            setLoading(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void setLoading(boolean loading) {
        refreshButton.setDisable(loading);
        if (loading) {
            statusLabel.setText("取得中...");
            locationLabel.setText("--");
            conditionLabel.setText("--");
            temperatureLabel.setText("--°C");
            windLabel.setText("風速: -- km/h");
            tomorrowConditionLabel.setText("--");
            tomorrowTempLabel.setText("最高 --°C / 最低 --°C");
            tomorrowWindLabel.setText("最大風速: -- km/h");
        }
    }
}
