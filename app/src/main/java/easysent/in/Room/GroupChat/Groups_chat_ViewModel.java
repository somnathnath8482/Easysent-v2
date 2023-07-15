package easysent.in.Room.GroupChat;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.PagingData;

import java.util.List;

import easysent.in.Interface.Messages.LiveData_Item;
import easysent.in.Interface.Messages.LiveData_Messages;

public class Groups_chat_ViewModel extends AndroidViewModel {

    private final LiveData<List<Group_Chat>> all_groups;
    private final GroupChatRepository repository;

    public Groups_chat_ViewModel(@NonNull Application application) {
        super(application);
        repository = new GroupChatRepository(application);
        all_groups = repository.getAll();
    }


    public void insert(Group_Chat groups) {
        repository.insert(groups);
    }


    public  void update_deleted(String chat_id){
        repository.update_delete(chat_id);
    };
    public  void update_status(String chat_id,String status){
        repository.update_status(chat_id,status);
    };
    public void delete(String chat_id) {
        repository.delete(chat_id);
    }


    public void deleteAll() {
        repository.deleteAll();
    }
    public LiveData<List<Group_Chat>> getAll() {
        return all_groups;
    }

   public Group_Chat selectChat(String id){
        return repository.selectChat(id);
   };
    public void getMessageBy_paging(String group_id, boolean is_fistpage, LiveData_Messages<Group_Chat> chatLiveData_list) {
         repository.getChat_byPaging(group_id,this, is_fistpage,chatLiveData_list);
    }
    public  LiveData<List<Group_Chat>> getMessage(String group_id) {
        return repository.getChat(group_id);
    }
    public List<Group_Chat> selectAttachment(String group_id,String chat_id){
        return repository.selectAttachment(group_id, chat_id);
    }


}
