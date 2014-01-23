package com.devaddiction.palettesearch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import com.devaddiction.palettesearch.beans.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchPaletteActivity extends Activity {

    List<Palette> palettes = new ArrayList<Palette>();
    PaletteListAdapter adapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.palette_list);

        processPalettes();
        printPalettes();
    }

    private void printPalettes() {
        // add data in contact image adapter
        adapter = new PaletteListAdapter(this, R.layout.palette_list, palettes);
        ListView dataList = (ListView) findViewById(R.id.list);
        dataList.setAdapter(adapter);
    }

    protected void processPalettes() {
        Intent i = getIntent();

        JSONArray jsonResponse = null;
        int paletteNumber = 0;

        try {
            jsonResponse = new JSONArray(i.getStringExtra("response"));

            for (paletteNumber = 0; paletteNumber < jsonResponse.length(); paletteNumber++) {
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
        for (int i = 0; i < colorsArray.length(); i++) {
            colors.add(colorsArray.getString(i));
        }
        return new Palette(id, title, colors, imageUrl);
    }


}
