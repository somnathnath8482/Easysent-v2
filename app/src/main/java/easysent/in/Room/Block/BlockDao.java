package easysent.in.Room.Block;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



import java.util.List;

@Dao
public interface BlockDao {

    @Insert
    void insert(Block chatsItem);

    @Query("DELETE  FROM block WHERE id =:id ")
    void delete(String id);

    @Query("DELETE  FROM block WHERE fromUser=:from AND toUser=:to ")
    void delete_from_TO(String from,String to);

    @Query("DELETE FROM block")
    void deleteAll();

    @Query("SELECT * FROM block ORDER BY RID")
    LiveData<List<Block>> getAll();

    @Query("SELECT * FROM block WHERE (fromUser =:me and toUser =:reciver) OR (fromUser =:reciver and toUser =:me)")
    LiveData<Block>  getBlock(String me , String reciver);


}
