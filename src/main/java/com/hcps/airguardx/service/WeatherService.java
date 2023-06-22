package com.hcps.airguardx.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class WeatherService {

    public float getTemperature() {
        return getParameter("temperature_2m");
    }

    public float getHumidity() {
        return getParameter("relativehumidity_2m");
    }

    private float getParameter(String param) {

        JSONObject obj = getJson();

        if (obj != null) {
            String time = obj.getJSONObject("current_weather").getString("time");
            JSONArray times = obj.getJSONObject("hourly").getJSONArray("time");

            for (int i = 0; i < times.length(); i++) {
                if (times.get(i).equals(time)) {
                    return obj.getJSONObject("hourly").getJSONArray(param).getFloat(i);
                }
            }
        }

        return 0.0F;
    }

    private JSONObject getJson() {
        try {
            // URL der Wetter-API
            String apiUrl = "https://api.open-meteo.com/v1/forecast?latitude=50.26&longitude=10.96&hourly=temperature_2m,relativehumidity_2m,windspeed_10m&daily=sunrise,sunset&current_weather=true&forecast_days=1&timezone=Europe%2FBerlin";

            // Verbindung zur API herstellen
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Antwort der API lesen
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // JSON-Objekt weiterleiten
                return new JSONObject(response.toString());

            } else {
                System.out.println("Fehler: " + responseCode);
            }
            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
