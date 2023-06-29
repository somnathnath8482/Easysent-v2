package easysent.in.Room.Messages;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Chats.class},version = 3,exportSchema = false)
public abstract class MessageDataBase extends RoomDatabase {
    private static MessageDataBase instance;
    public abstract Chat_dao chat_dao();


    public static synchronized MessageDataBase  getInstance(Context context){
        if (instance== null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDataBase.class,"Message_databade")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
