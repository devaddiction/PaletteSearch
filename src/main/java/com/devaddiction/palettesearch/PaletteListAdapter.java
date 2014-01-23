package com.devaddiction.palettesearch;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devaddiction.palettesearch.beans.Palette;

public class PaletteListAdapter extends ArrayAdapter<Palette> {

    Context context;
    int layoutResourceId;
    LinearLayout linearMain;
    List<Palette> data = new ArrayList<Palette>();

    public PaletteListAdapter(Context context, int layoutResourceId, List<Palette> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            linearMain = (LinearLayout) row.findViewById(R.id.linearMain);

            Palette palette = data.get(position);

            TextView label = new TextView(context);
            label.setText(palette.getTitle());
            linearMain.addView(label);

            ImageView image = new ImageView(context);
            if (this.loadImageFromURL(palette.getImageUrl(), image)) {
                linearMain.addView(image);
            }
        }

        return row;
    }

    public boolean loadImageFromURL(String fileUrl, ImageView iv){
        try {

            URL myFileUrl = new URL (fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            iv.setImageBitmap(BitmapFactory.decodeStream(is));

            return true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}