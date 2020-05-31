package edu.monash.MovieMemoir.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import edu.monash.MovieMemoir.dao.CinemaDAO;
import edu.monash.MovieMemoir.dao.WatchlistDAO;
import edu.monash.MovieMemoir.entity.Cinema;

@Database(entities = {Cinema.class}, version = 2, exportSchema = false)
public abstract class CinemaDatabase extends RoomDatabase {
    public abstract CinemaDAO cinemaDAO();
    private static CinemaDatabase INSTANCE;
    public static synchronized CinemaDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    CinemaDatabase.class, "CinemaDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}

