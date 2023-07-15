package easysent.in.Room.Messages;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import easysent.in.Interface.Messages.LiveData_Messages;

public class Message_View_Model extends AndroidViewModel {
    private final Chatepository repository;
    private final LiveData<List<Chats>> all_chat;

    public Message_View_Model(@NonNull Application application) {
        super(application);

        repository = new Chatepository(application);
        all_chat = repository.getAll();
    }


    public void insert(Chats chat) {
        repository.insert(chat);
    }

    public void update_seen(String status, String id) {
        repository.update_last_seen(status, id);
    }

    public void update_delete(String id, String value) {
        repository.update_delete(id, value);
    }

    public void update_seen_by_thread(String thread, String sender, String status) {
        repository.update_seen_By_Thread(thread, sender, status);
    }

    public void update_stseen_Recived_thread(String thread, String sender, String status) {
        repository.update_stseen_Recived_thread(thread, sender, status);
    }

    public void delete(String chat_id) {
        repository.delete(chat_id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Chats selectChat(String uid) {

        return repository.selectChat(uid);
    }

    public List<Chats> selectAttachment(String uid, String chat_id) {

        return repository.selectAttachment(uid, chat_id);
    }

    public LiveData<List<Chats>> getChat_By_User(String user, String me) {
        return repository.getChat_User(user, me);
    }

    public void getChat_By_Paged(String user, String me, LiveData_Messages<Chats> liveData_messages) {
         repository.getChat_byPaging(user, me,Message_View_Model.this,liveData_messages);
    }

    public LiveData<List<Chats>> getAll() {
        return all_chat;
    }


}
