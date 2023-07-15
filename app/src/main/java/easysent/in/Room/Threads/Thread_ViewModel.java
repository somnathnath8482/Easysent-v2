package easysent.in.Room.Threads;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

import easysent.in.Interface.Messages.LiveData_Messages;
import easysent.in.Interface.Messages.LiveDatanonPage;

public class Thread_ViewModel  extends AndroidViewModel {

    private final LiveData<List<Message_Thread>> all_threads;
    private final Thread_repository repository;

    public Thread_ViewModel(@NonNull Application application) {
        super(application);
        repository = new Thread_repository(application);
        all_threads = repository.getAll();
    }


    public void insert(Message_Thread user) {
        repository.insert(user);
    }

    public void update_last_seen(String message,String status,String type,String id,String time) {
        repository.update_last_seen(message,status,type,id,time);
    }

    public void delete(String user) {
        repository.delete(user);
    }
 public void UpdateUnread(String thread_id) {
        repository.UpdateUnread(thread_id);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Message_Thread selectThread(String uid) {

        return repository.selectThread(uid);
    }

    public void getActiveThreds(String id, LiveDatanonPage<Active_Thread> live_data) {
         repository.getActiveThread(id,live_data);
    }

    public LiveData<List<Message_Thread>> getAll() {
        return all_threads;
    }



}
