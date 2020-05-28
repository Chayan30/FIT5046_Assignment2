package edu.monash.MovieMemoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.monash.MovieMemoir.entity.Watchlist;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface WatchlistDAO {
    @Query("SELECT * FROM watchlist")
    LiveData<List<Watchlist>> getAll();
    @Query("SELECT * FROM watchlist WHERE id = :id LIMIT 1")
    Watchlist findByID(String id);

    @Insert
    void insertAll(Watchlist... watchlist);
    @Insert
    long insert(Watchlist watchlist);
    @Delete
    void delete(Watchlist watchlist);
    @Update(onConflict = REPLACE)
    void updateCustomers(Watchlist... watchlist);
    @Query("DELETE FROM watchlist")
    void deleteAll();
    @Query("DELETE FROM watchlist WHERE title = :title")
    void deleteById(String title);
}


