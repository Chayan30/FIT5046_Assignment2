
package edu.monash.MovieMemoir.database;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import edu.monash.MovieMemoir.dao.PersonDAO;
import edu.monash.MovieMemoir.entity.Person;
@Database(entities = {Person.class}, version = 2, exportSchema = false)
public abstract class PersonDatabase extends RoomDatabase{
    public abstract PersonDAO personDao();
    private static PersonDatabase INSTANCE;
    public static synchronized PersonDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    PersonDatabase.class, "PersonDatabase")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }
}