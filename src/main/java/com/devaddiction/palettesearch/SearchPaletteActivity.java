package com.devaddiction.palettesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.devaddiction.palettesearch.beans.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchPaletteActivity extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.palette_result);

        processPatterns();
    }

    protected void processPatterns() {
        Intent i = getIntent();

        JSONArray jsonResponse = null;
        int paletteNumber = 0;

        List<Palette> palettes = new ArrayList<Palette>();

        try {
            jsonResponse = new JSONArray(i.getStringExtra("response"));

            for (paletteNumber = 0; paletteNumber<jsonResponse.length(); paletteNumber++) {
                palettes.add(getPaletteFromJson(jsonResponse.getJSONObject(paletteNumber)));
            }

    
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private Palette getPaletteFromJson(JSONObject jsonObject) throws JSONException {

        int id = Integer.valueOf(jsonObject.getString("id"));
        String title = jsonObject.getString("title");
        String imageUrl = jsonObject.getString("imageUrl");
        JSONArray colorsArray = jsonObject.getJSONArray("colors");

        List<String> colors = new ArrayList<String>();
        for (int i=0; i<colorsArray.length(); i++) {
            colors.add( colorsArray.getString(i) );
        }
        return new Palette(id, title, colors, imageUrl);
    }


}
