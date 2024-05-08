package com.fiza;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class CityList {
    Map<String, City> cities = new HashMap<String, City>();

    public CityList(String fileNameString) {
        try {
            // Read JSON file content
            String jsonContent = new String(Files.readAllBytes(Paths.get(fileNameString)));

            // Parse JSON array
            JSONArray jsonArray = new JSONArray(jsonContent);

            // Iterate over JSON array and print each object
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONObject coorJsonObject = jsonObject.getJSONObject("coord");
                Map<String, Double> coord = Map.of("lon", coorJsonObject.getDouble("lon"), "lat",
                        coorJsonObject.getDouble("lat"));
                City city = new City(Integer.valueOf(jsonObject.getInt("id")), jsonObject.getString("name"),
                        jsonObject.getString("country"), coord);
                cities.put(city.getName(), city);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
