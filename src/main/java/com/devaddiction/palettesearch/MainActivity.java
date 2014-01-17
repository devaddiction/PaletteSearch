package com.devaddiction.palettesearch;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.NumberPicker;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends Activity {

    private String API_URL = "http://www.colourlovers.com/api/patterns/?format=json";

    private int red = 0;
    private int green = 0;
    private int blue = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Begin color picking!
        startColorPicker();
    }

    // Control and execute the color picking process, start by initializing, setting defaults,
    // then listen for changes to each number picker, make updates to the color view as needed.
    private void startColorPicker() {
        // Initialize the red picker.
        NumberPicker red_picker = (NumberPicker) findViewById(R.id.red_picker);
        red_picker = initializeDefaults(red_picker);

        // Initialize the green picker.
        NumberPicker green_picker = (NumberPicker) findViewById(R.id.green_picker);
        green_picker = initializeDefaults(green_picker);

        // Initialize the red picker.
        NumberPicker blue_picker = (NumberPicker) findViewById(R.id.blue_picker);
        blue_picker = initializeDefaults(blue_picker);

        // Hide keyboard / keypad.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(red_picker.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(green_picker.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(blue_picker.getWindowToken(), 0);

        // Listen for changes, make updates if necessary.
        pickerListener(red_picker, "r");
        pickerListener(green_picker, "g");
        pickerListener(blue_picker, "b");
    }

    // Initialize a number picker with a range of 0-255, and a scroll interval of 20ms.
    private NumberPicker initializeDefaults(NumberPicker picker) {
        picker.setMaxValue(255);
        picker.setMinValue(0);
        picker.setOnLongPressUpdateInterval(20);
        return picker;
    }

    // Listen for changes to each 'picker', if changes are detected, update the global color,
    // then the background using RGB.
    private void pickerListener(NumberPicker picker, final String colorType) {

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {

            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateColor(colorType, newVal);
                updateBackground();
            }
        });
    }

    // Update the global color variable, which color is updated is based on the 'colorType' 
    // which is a flag determined by the original picker listener type.
    private void updateColor(String colorType, int color) {
        if (colorType == "r")
            red = color;
        if (colorType == "g")
            green = color;
        if (colorType == "b")
            blue = color;
    }

    // Set the background color to the current red, green, and blue values.
    private void updateBackground() {
        View view = (View) findViewById(R.id.view);
        view.setBackgroundColor(Color.rgb(red, green, blue));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void searchPattern(View view) throws IOException {
        new ColourLoversApi().execute(this.API_URL);
    }

    // Class with extends AsyncTask class
    private class ColourLoversApi extends AsyncTask<String, Void, Void> {

        // Required initialization

        private final HttpClient Client = new DefaultHttpClient();
        private String Content;
        private String Error = null;
        private ProgressDialog Dialog = new ProgressDialog(MainActivity.this);

        NumberPicker red_picker = (NumberPicker) findViewById(R.id.red_picker);
        NumberPicker green_picker = (NumberPicker) findViewById(R.id.green_picker);
        NumberPicker blue_picker = (NumberPicker) findViewById(R.id.blue_picker);

        String hex = String.format(
                "%02x%02x%02x",
                red_picker.getValue(), green_picker.getValue(), blue_picker.getValue()
        );

        String data = "";

        protected void onPreExecute() {
            Dialog.setMessage("Please wait..");
            Dialog.show();
            data += "&hex=" + hex.toString();
        }

        // Call after onPreExecute method
        protected Void doInBackground(String... urls) {
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);

                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();

                // Get the server response
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    // Append server response in string
                    sb.append(line + " ");
                }

                // Append Server Response To Content String
                Content = sb.toString();
            } catch (Exception ex) {
                Error = ex.getMessage();
            } finally {
                try {
                    reader.close();
                } catch (Exception ex) {
                }
            }
            return null;
        }

        protected void onPostExecute(Void unused) {
            // Close progress dialog
            Dialog.dismiss();

            //Starting a new Intent
            Intent nextScreen = new Intent(getApplicationContext(), SearchPaletteActivity.class);

            //Sending data to another Activity
            nextScreen.putExtra("response", Content);

            startActivity(nextScreen);
        }

    }
}