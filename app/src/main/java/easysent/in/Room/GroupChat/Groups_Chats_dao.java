package easysent.in.Room.GroupChat;

import androidx.lifecycle.LiveData;
import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface Groups_Chats_dao {

    @Insert
    void insert(Group_Chat group_Chat);

    @Query("UPDATE Group_Chat SET isDeleted=1 , message='This message is deleted',replayOf='', type='T' WHERE id =:id")
    void update_deleted(String id);

    @Query("UPDATE Group_Chat SET seen=:status WHERE id =:id")
    void update_status(String id,int status);


    @Query("DELETE  FROM Group_Chat WHERE id =:id ")
    void delete(String id);

    @Query("DELETE FROM Group_Chat")
    void deleteAll();

    @Query("SELECT * FROM Group_Chat ORDER BY RID")
    LiveData<List<Group_Chat>> getAll();

    @Query("SELECT * FROM Group_Chat WHERE id = :id")
    Group_Chat selectChat(String id);

    //todo for paging
    @Query("SELECT * FROM Group_Chat WHERE groupId =:groupid ORDER BY createdAt ASC ")
    PagingSource<Integer,Group_Chat> getMessageBy_paging(String groupid);

    //todo for paging
    @Query("SELECT * FROM Group_Chat WHERE groupId =:groupid ORDER BY createdAt DESC Limit 10")
    PagingSource<Integer,Group_Chat> getMessageBy_FirstPage(String groupid);

  @Query("SELECT * FROM Group_Chat WHERE groupId =:groupid")
  LiveData<List<Group_Chat>>  getMessage(String groupid);


    // @Query("SELECT * FROM Chats WHERE thread = :Tid AND (type = 'I' OR type = 'V') ")
    @Query("SELECT * FROM Group_Chat WHERE groupId =:group_id AND (type = 'I' OR type = 'V')  ORDER BY CASE WHEN id =:chat_id THEN 0 ELSE 1 END")
    List<Group_Chat> selectAttachment(String group_id,String chat_id);



}
