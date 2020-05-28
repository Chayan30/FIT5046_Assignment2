package edu.monash.MovieMemoir.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity
public class Watchlist
{
    @NonNull
    @ColumnInfo(name = "Title")
    public String title;
    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "Id")
    public String id;
    @ColumnInfo(name = "release_Date")
    public String rel_date;
    @ColumnInfo(name = "add_date")
    public String add_date;
    public Watchlist(String title, String rel_date, String add_date, String id) {
        this.title = title;
        this.rel_date=rel_date;
        this.add_date=add_date;
        this.id=id;
    }

    @NotNull
    public String getId() {
        return id;
    }
    @NotNull
    public String getTitle() {
        return title;
    }

    public String getRel_date() {
        return rel_date;
    }
    public void setRel_date(String rel_date) {
        this.rel_date = rel_date;
    }
    public String getAdd_date() {
        return add_date;
    }
    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }
}


