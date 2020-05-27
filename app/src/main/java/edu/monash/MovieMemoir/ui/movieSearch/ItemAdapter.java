package edu.monash.MovieMemoir.ui.movieSearch;

import android.util.Log;

public class ItemAdapter {
    private String image;
    private String text;
    private String text2;
    private boolean isSelected;
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setText2(String text) {
        this.text2 = text;
    }
    public String getText2() {
        return text2;
    }
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }
    public boolean isSelected() {
        return isSelected;
    }
}
