package com.devaddiction.palettesearch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import com.devaddiction.palettesearch.beans.Palette;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SearchPaletteActivity extends Activity {

    List<Palette> palettes = new ArrayList<Palette>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.palette_result);

        processPalettes();
        printPalettes();
    }

    private void printPalettes() {
        int paletteNumber = 0;
        for (paletteNumber = 0; paletteNumber < palettes.size(); paletteNumber++) {
            drawPalette(palettes.get(paletteNumber));
        }
    }

    private void drawPalette(Palette palette) {
        InputStream is = null;
        try {
            is = (InputStream) this.fetch((String) palette.getImageUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Drawable d = Drawable.createFromStream(is, "src");

        ImageView paletteView = (ImageView) findViewById(R.id.palette);
        paletteView.setImageDrawable(d);
    }


    public Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
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
