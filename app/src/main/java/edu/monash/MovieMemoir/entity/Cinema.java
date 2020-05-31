
package edu.monash.MovieMemoir.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Cinema {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "cinemaName")
    public String cinemaName;
    @ColumnInfo(name = "Latitude")
    public String lat;
    @ColumnInfo(name = "Longitude")
    public String lng;
    //@ColumnInfo(name = "dob")
    //public Date dob;
    public Cinema(String cinemaName,String lat,String lng) {
        this.cinemaName = cinemaName;
        this.lat=lat;
        this.lng=lng;
    }

    public String getCinemaName() {
        return cinemaName;
    }
    public void setCinemaName(String cinemaName){ this.cinemaName = cinemaName;}
    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }
    public String getLng() {
        return lng;
    }
    public void setLng(String lng) {
        this.lng = lng;
    }

}
