package easysent.in.Room.Groups;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface Groups_dao {

    @Insert
    void insert(Groups group);

    @Query("UPDATE Groups SET name=:name, groupDesc=:desc,dp=:profile_pic,userCount=:user_count  WHERE groupId =:groupId")
    void update(String name,String desc,String user_count,String profile_pic,String groupId);

    @Query("DELETE  FROM Groups WHERE groupId =:id ")
    void delete(String id);

    @Query("DELETE FROM Groups")
    void deleteAll();

    @Query("SELECT * FROM Groups ORDER BY last_message_time DESC")
    LiveData<List<Groups>> getAll();

    @Query("SELECT * FROM Groups WHERE groupId = :id")
    Groups selectGroup(String id);
 @Query("SELECT * FROM Groups WHERE groupId = :id")
 LiveData<Groups> selectGroupLive(String id);

    @Query("SELECT * FROM Groups Where name like:name ORDER BY name")
    LiveData<List<Groups>> SearchGroup(String name);

    @Query("UPDATE Groups SET last_message =:message , last_message_type=:type, last_message_status =:status,last_message_time =:time,unread = unread+1   WHERE groupId =:groupId")
    void update_lastMessage(String message,String status,String type,String groupId,String time);


    @Query("UPDATE Groups set unread=0 where groupId=:groupId")
    void updateUnread(String groupId);

}
