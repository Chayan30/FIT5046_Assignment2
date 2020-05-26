package edu.monash.MovieMemoir.dao;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import static androidx.room.OnConflictStrategy.REPLACE;

import edu.monash.MovieMemoir.entity.Person;

@Dao
public interface PersonDAO {
    @Query("SELECT * FROM person")
    List<Person> getAll();
    @Query("SELECT * FROM person WHERE uid = :personId LIMIT 1")
    Person findByID(int personId);

    @Insert
    void insertAll(Person... person);
    @Insert
    long insert(Person person);
    @Delete
    void delete(Person person);
    @Update(onConflict = REPLACE)
    void updateCustomers(Person... person);
    @Query("DELETE FROM person")
    void deleteAll();

}
