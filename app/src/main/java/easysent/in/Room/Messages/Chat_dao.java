package easysent.in.Room.Messages;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface Chat_dao {

    @Insert
    void insert(Chats thread);

    @Query("UPDATE Chats SET seen =:status  WHERE chat_id =:id")
    void update_stseen(String status,String id);

     @Query("UPDATE Chats SET is_deleted=:value , replay_of='', message='This message is deleted', type='T' WHERE chat_id =:id")
    void update_deleted(String id,String value);


 @Query("UPDATE Chats SET seen =:status  WHERE thread  =:tid AND sender =:sender")
    void update_stseen_of_thread(String tid,String sender,String status);


 @Query("UPDATE Chats SET seen =:status  WHERE thread  =:tid AND sender =:sender AND seen= '0' ")
    void update_stseen_Recived_thread(String tid,String sender,String status);


    @Query("DELETE  FROM Chats WHERE chat_id =:id ")
    void delete(String id);

    @Query("DELETE FROM Chats")
    void deleteAll();

    @Query("SELECT * FROM Chats ORDER BY RID")
    LiveData<List<Chats>> getAll();

    @Query("SELECT * FROM Chats WHERE chat_id = :id")
    Chats selectChat(String id);

    @Query("SELECT * FROM Chats WHERE (sender =:user AND reciver=:me) OR (sender =:me AND reciver=:user)")
    LiveData<List<Chats>>  getMessageBy_User(String user,String me);

    //todo  paging3
    @Query("SELECT * FROM Chats WHERE (sender =:user AND reciver=:me) OR (sender =:me AND reciver=:user)")
    PagingSource<Integer,Chats> getMessageBy_paging(String user, String me);


   // @Query("SELECT * FROM Chats WHERE thread = :Tid AND (type = 'I' OR type = 'V') ")
   @Query("SELECT * FROM Chats WHERE thread = :Tid AND (type = 'I' OR type = 'V')  ORDER BY CASE WHEN chat_id =:chat_id THEN 0 ELSE 1 END")
    List<Chats> selectAttachment(String Tid,String chat_id);



}
