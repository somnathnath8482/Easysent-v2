package easysent.in.Room.Users;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class UserVewModel extends AndroidViewModel {
    private final LiveData<List<Users>> alluser;
    private final UserRepository repository;

    public UserVewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
        alluser = repository.getAll();
    }


    public void insert(Users user) {
        repository.insert(user);
    }

    public void update(String phone, String name, String profilePic, String gender, String about, String isVerifyed, String email, String token) {
        repository.update(phone,name,profilePic,gender,about,isVerifyed,email,token);
    }
 public void updateStatus(String status,String id) {
        repository.updateStatus(status,id);
    }

    public void delete(String user) {
        repository.delete(user);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void setIp(String uid,String ip){
        repository.setIp(uid, ip);
    }

    public String getIp(String uid){
        return repository.getIp(uid);
    }

    public Users selectUser(String uid) {

        return repository.selectUser(uid);
    }

    public   LiveData<Users>  selectUserLive(String uid) {
        return repository.selectUserLive(uid);
    }

    public  LiveData<List<Users>> search(String Name_Or_Email) {
        return repository.search(Name_Or_Email);
    }

    public LiveData<List<Users>> getAll() {
        return alluser;
    }


}
