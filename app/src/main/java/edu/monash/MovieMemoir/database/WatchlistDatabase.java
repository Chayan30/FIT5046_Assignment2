package edu.monash.MovieMemoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import edu.monash.MovieMemoir.dao.WatchlistDAO;
import edu.monash.MovieMemoir.entity.Watchlist;

@Database(entities = {Watchlist.class}, version = 2, exportSchema = false)
public abstract class WatchlistDatabase extends RoomDatabase {
    public abstract WatchlistDAO watchlistDao();
    private static WatchlistDatabase INSTANCE;
    public static synchronized WatchlistDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WatchlistDatabase.class, "WatchlistDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}
