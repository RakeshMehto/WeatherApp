package com.fiza;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class APIResponse {
    private Map<String, String> responseMap = new HashMap<>();

    public APIResponse(Integer cityId) {
        // Replace {city id} and {API key} with your actual values
        try {

            String apiKey = "3eac7fb0342038e24ffd2a1654ed7466";

            // Create URL
            String urlString = "https://api.openweathermap.org/data/2.5/weather?id=" +
                    cityId + "&appid=" + apiKey;
            URL url = new URL(urlString);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read response body
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONObject mainObject = jsonResponse.getJSONObject("main");
            JSONObject sysObject = jsonResponse.getJSONObject("sys");
            JSONObject windObject = jsonResponse.getJSONObject("wind");
            Long datetime = jsonResponse.getLong("dt");
            Integer timezone = jsonResponse.getInt("timezone");
            double temperature = mainObject.getDouble("temp");
            double temp_min = mainObject.getDouble("temp_min");
            double humidity = mainObject.getDouble("humidity");
            double pressure = mainObject.getDouble("pressure");
            double feels_like = mainObject.getDouble("feels_like");
            double temp_max = mainObject.getDouble("temp_max");
            double wind_speed = windObject.getDouble("speed");
            Long sunset = sysObject.getLong("sunset");
            Long sunrise = sysObject.getLong("sunrise");

            responseMap.put("temperature", kelvinToCelcius(temperature));
            responseMap.put("temp_min", kelvinToCelcius(temp_min));
            responseMap.put("humidity", humidity + "%");
            responseMap.put("pressure", pressure + "hPa");
            responseMap.put("feels_like", kelvinToCelcius(feels_like));
            responseMap.put("temp_max", kelvinToCelcius(temp_max));
            responseMap.put("wind_speed", wind_speed + "km/h");
            responseMap.put("sunset", converDateTime(sunset.toString(), timezone).toString());
            responseMap.put("sunrise", converDateTime(sunrise.toString(), timezone).toString());
            responseMap.put("datetime", converDateTime(datetime.toString(), timezone).toString());

            // Close connection
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Double round(double value, int places) {
        if (places < 0)
            throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static LocalDateTime converDateTime(String timestamp, Integer timezone) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestamp)),
                ZoneOffset.ofTotalSeconds(timezone).normalized());
        return dateTime;
    }

    public static String kelvinToCelcius(Double kelvin) {
        return round(kelvin - 273.15, 2) + "°C";
    }

    public Map<String, String> getApiResponse() {
        return responseMap;
    }
}
