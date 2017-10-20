package edu.orangecoastcollege.cs273.sbadajoz.cs273superheroes;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class loads Country data from a formatted JSON (JavaScript Object Notation) file.
 * Populates data model (Country) with data.
 */
public class JSONLoader {

    /**
     * Loads JSON data from a file in the assets directory.
     *
     * @param context The activity from which the data is loaded.
     * @throws IOException If there is an error reading from the JSON file.
     */
    public static List<SuperHero> loadJSONFromAsset(Context context) throws IOException {
        List<SuperHero>allSuperHeroes = new ArrayList<>();
        String json = null;
        InputStream is = context.getAssets().open("cs273superheroes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allHeroesJSON = jsonRootObject.getJSONArray("Superheroes");

            // TODO: Loop through all the countries in the JSON data, create a Country
            // TODO: object for each and add the object to allSuperHeroes

            for (int i = 0; i < allHeroesJSON.length(); i++) {
                JSONObject countryJSON = allHeroesJSON.getJSONObject(i);
                allSuperHeroes.add(new SuperHero(countryJSON.getString("Username"), countryJSON.getString("Name"), countryJSON.getString("Superpower"), countryJSON.getString("One Thing")));
            }


        } catch (JSONException e) {
            Log.e("Superheroes Quiz", e.getMessage());
        }

        return allSuperHeroes;
    }
}
