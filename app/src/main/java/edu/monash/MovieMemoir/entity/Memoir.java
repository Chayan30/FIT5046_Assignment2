
package edu.monash.MovieMemoir.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memoir {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    public String uid;
    @ColumnInfo(name = "personId")
    public String personId;
    @ColumnInfo(name = "cinemaId")
    public String cinemaId;
    @ColumnInfo(name = "movieName")
    public String movieName;
    @ColumnInfo(name = "releaseDate")
    public String releaseDate;
    @ColumnInfo(name = "userWatchDate")
    public String userWatchDate;
    @ColumnInfo(name = "userOpinion")
    public String userOpinion;
    @ColumnInfo(name = "userRating")
    public float userRating;
    //@ColumnInfo(name = "dob")
    //public Date dob;
    public Memoir(String uid, String personId, String cinemaId, String movieName,
                  String releaseDate, String userWatchDate, String userOpinion,
                  float userRating/*, Date dob*/) {
        this.uid = uid;
        this.personId=personId;
        this.cinemaId=cinemaId;
        this.movieName = movieName;
        this.releaseDate = releaseDate;
        this.userWatchDate = userWatchDate;
        this.userOpinion = userOpinion;
        this.userRating = userRating;
        //this.dob = dob;
    }

    public String getId() {
        return uid;
    }

    public String getPersonId() {
        return personId;
    }
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    public String getCinemaId() {
        return cinemaId;
    }
    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }
    public String getMovieName() {
        return movieName;
    }
    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
    public String getUserWatchDate() {
        return userWatchDate;
    }
    public void setUserWatchDate(String userWatchDate) {
        this.userWatchDate = userWatchDate;
    }
    public String getUserOpinion() {
        return userOpinion;
    }
    public void setUserOpinion(String userOpinion) {
        this.userOpinion = userOpinion;
    }
    public float getUserRating() {
        return userRating;
    }
    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }
//    public Date getDob() {
//        return dob;
//    }
//    public void setDob(Date dob) {
//        this.dob = dob;
//    }

}
