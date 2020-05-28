package edu.monash.MovieMemoir;

public class MemoirItemAdapter {
    private String image;
    private String Mname;
    private String Ryear;
    private String UaddDate;
    private String Uopinion;
    private String Cpostcode;
    private String Genre;
    private float userRating;
    private float publicRating;
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setMname(String text) {
        this.Mname = text;
    }
    public String getMname() {
        return Mname;
    }
    public void setRyear(String text) {
        this.Ryear = text;
    }
    public String getRyear() {
        return Ryear;
    }
    public void setUaddDate(String text) {
        this.UaddDate = text;
    }
    public String getUaddDate() {
        return UaddDate;
    }
    public void setUopinion(String text) {
        this.Uopinion = text;
    }
    public String getUopinion() {
        return Uopinion;
    }
    public void setCpostcode(String text) {
        this.Cpostcode = text;
    }
    public String getCpostcode() {
        return Cpostcode;
    }
    public void setUserRating(float rating) {
        this.userRating = rating;
    }
    public float getUserRating() {
        return userRating;
    }
    public void setPublicRating(float rating) {
        this.publicRating = rating;
    }
    public float getPublicRating() {
        return publicRating;
    }
    public String getGenre() {
        return Genre;
    }
    public void setGenre(String text) {
        this.Genre   = text;
    }
}
