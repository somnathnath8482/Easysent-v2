package easysent.in.Room.GroupChat;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;


@Database(entities = {Group_Chat.class},version = 1,exportSchema = false)
public abstract class GroupChatDataBase extends RoomDatabase {
    private static GroupChatDataBase instance;
    public abstract Groups_Chats_dao group_chat_dao();


    public static synchronized GroupChatDataBase getInstance(Context context){
        if (instance== null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            GroupChatDataBase.class,"Group_chat_databade")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
