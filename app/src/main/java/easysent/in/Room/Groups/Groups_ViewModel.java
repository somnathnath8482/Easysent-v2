package easysent.in.Room.Groups;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class Groups_ViewModel extends AndroidViewModel {

    private final LiveData<List<Groups>> all_groups;
    private final Groups_repository repository;

    public Groups_ViewModel(@NonNull Application application) {
        super(application);
        repository = new Groups_repository(application);
        all_groups = repository.getAll();
    }


    public void insert(Groups groups) {
        repository.insert(groups);
    }

    public void update_last_message(String message, String status, String type, String groupId, String time) {
        repository.update_last_message(message, status, type, groupId, time);
    }

    public void delete(String group_id) {
        repository.delete(group_id);
    }

    public void updateUnread(String group_id) {
        repository.UpdateUnread(group_id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Groups selectgroup(String group_id) {

        return repository.selectGroup(group_id);
    }
    public LiveData<Groups> selectgroupLive(String group_id) {

        return repository.selectGroupLive(group_id);
    }

    public LiveData<List<Groups>> searchGroup(String name) {
        return repository.searchGroups(name);
    }

    public void update(String name, String desc, String user_count, String profile_pic,String groupId) {
        repository.upadet(name, desc, user_count, profile_pic,groupId);
    }

    public LiveData<List<Groups>> getAll() {
        return all_groups;
    }


}
