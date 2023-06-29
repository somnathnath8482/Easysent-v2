package easysent.in.Room.Users;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(Users user);

    @Query("UPDATE Users  set phone =:phone, name =:name, profilePic =:profilePic, gender =:gender,about =:about, isverifyed =:isVerifyed,token =:token WHERE email =:email ")
    void update(String phone, String name, String profilePic, String gender, String about, String isVerifyed, String email, String token);


    @Query("UPDATE Users  set fstatus =:status WHERE user_id =:id ")
    void updateStatus(String status, String id);

    @Query("DELETE  FROM Users WHERE email =:email ")
    void delete(String email);

    @Query("DELETE FROM Users")
    void deleteAll();


    @Query("SELECT ip FROM USERS WHERE user_id=:uid")
    String getIp(String uid);

    @Query("UPDATE USERS SET ip=:ip WHERE user_id=:uid")
    void setIp(String uid, String ip);

    @Query("UPDATE USERS SET token =:token  WHERE user_id=:uid")
    void setToken(String uid, String token);

    @Query("SELECT * FROM Users ORDER BY RID")
    LiveData<List<Users>> getAll();

    @Query("SELECT * FROM Users WHERE user_id = :email")
    Users selectUser(String email);

    @Query("SELECT * FROM Users WHERE user_id = :uid")
    LiveData<Users> selectUserlive(String uid);

    @Query("SELECT * FROM Users WHERE name LIKE :name_or_Email || '%' OR email LIKE :name_or_Email || '%' ")
    LiveData<List<Users>> search(String name_or_Email);
    //  select *  from Users  U   join message_thread T on u.user_id=T.id


}
