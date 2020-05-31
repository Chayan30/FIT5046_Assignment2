package edu.monash.MovieMemoir.dao;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import edu.monash.MovieMemoir.entity.Cinema;

import static androidx.room.OnConflictStrategy.REPLACE;
@Dao
public interface CinemaDAO {
    @Query("SELECT * FROM cinema")
    List<Cinema>  getAll();
    @Query("SELECT * FROM cinema WHERE cinemaName = :id LIMIT 1")
    Cinema findByID(String id);
    @Insert
    void insertAll(Cinema... cinema);
    @Insert
    long insert(Cinema cinema);
    @Delete
    void delete(Cinema cinema);
    @Update(onConflict = REPLACE)
    void updateCinema(Cinema... cinema);
    @Query("DELETE FROM cinema")
    void deleteAll();
    @Query("DELETE FROM cinema WHERE cinemaName = :name")
    void deleteById(String name);
}








