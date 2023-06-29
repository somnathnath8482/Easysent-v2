package easysent.in.Room.Groups;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Groups.class},version = 1,exportSchema = false)
public abstract class GroupsDatabase extends RoomDatabase {
    private static GroupsDatabase instance;
    public abstract Groups_dao groups_dao();


    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);



    public static synchronized GroupsDatabase getInstance(Context context){
        if (instance== null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            GroupsDatabase.class,"Groups Database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
