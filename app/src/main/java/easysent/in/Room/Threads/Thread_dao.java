package easysent.in.Room.Threads;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface Thread_dao {
    @Insert
    void insert(Message_Thread thread);

    @Query("UPDATE Message_Thread SET last_message =:message , last_message_type=:type, last_message_status =:status,last_message_time =:time,unread = unread+1   WHERE thread_id =:id")
    void update_lastseen(String message,String status,String type,String id,String time);

    @Query("DELETE  FROM Message_Thread WHERE thread_id =:id ")
    void delete(String id);

    @Query("DELETE FROM Message_Thread")
    void deleteAll();

    @Query("SELECT * FROM Message_Thread ORDER BY RID")
    LiveData<List<Message_Thread>> getAll();

    @Query("SELECT * FROM Message_Thread WHERE thread_id = :id")
    Message_Thread selectthread(String id);

    /*@Query("SELECT * FROM Message_Thread WHERE sender =:id Or reciver=:id")
    LiveData<List<Message_Thread>>  getActiveThread(String id);*/

    //@Query("select *  from Users  U   join message_thread T on u.user_id =:id AND (T.sender=:id or T.reciver = :id)")
    @Query("select *  from Users  U   join message_thread T on ( T.sender==:id AND   u.user_id = T.reciver  ) OR  ( T.reciver =:id AND   u.user_id = T.sender  ) order by last_message_time DESC")
    LiveData<List<Active_Thread>>  getActiveThread(String id);

 @Query("UPDATE message_thread set unread=0 where thread_id=:id")
    void updateUnread(String id);

    
}
