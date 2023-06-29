package easysent.in.Room.Users;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import easysent.in.Room.Block.Block;
import easysent.in.Room.Block.BlockDao;
import easysent.in.Room.Threads.Thread_dao;
import easysent.in.Room.Threads.Message_Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Users.class, Message_Thread.class, Block.class},version = 10,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase instance;
    public abstract UserDao  userDao();
    public abstract Thread_dao thread_dao();
    public abstract BlockDao blockDao();

    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);



    public static synchronized UserDatabase getInstance(Context context){
        if (instance== null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            UserDatabase.class,"User_databade")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
