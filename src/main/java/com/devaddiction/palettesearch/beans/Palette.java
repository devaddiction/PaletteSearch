package com.devaddiction.palettesearch.beans;

import java.util.List;

public class Palette {

    private int id;
    private String title;
    private List<String> colors;
    private String imageUrl;

    public Palette(int id, String title, List<String> colors, String imageUrl) {
        this.id = id;
        this.title = title;
        this.colors = colors;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}