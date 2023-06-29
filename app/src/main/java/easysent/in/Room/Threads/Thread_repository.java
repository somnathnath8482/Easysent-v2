package easysent.in.Room.Threads;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import easysent.in.Room.Users.UserDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Thread_repository {

    private final Thread_dao thread_dao;
    private final LiveData<List<Message_Thread>> all_threads;
 
    public Thread_repository(Application application) {
        UserDatabase database = UserDatabase.getInstance(application);
        thread_dao = database.thread_dao();
        all_threads = thread_dao.getAll();
    }



    public void insert(Message_Thread user) {
        new InserAsync(thread_dao).execute(user);

    }

    public void update_last_seen(String message,String status,String type,String id,String time) {
        new UpdateAsync(thread_dao).execute(message,status,type,id,time);

    }

    public void delete(String uid) {
        new DeleteAsync(thread_dao).execute(uid);


    }
    public void UpdateUnread(String uid) {
        new UpdateUnreadAsync(thread_dao).execute(uid);


    }

    public void deleteAll() {
        new DeleteAllAsync(thread_dao).execute();


    }


    public Message_Thread selectThread(String uid) {
        Message_Thread user = new Message_Thread();
        try{ user = new SelectAsync(thread_dao).execute(uid).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return user ;
    }

    public LiveData<List<Active_Thread>> getActiveThread(String Name_Or_Email){
        LiveData<List<Active_Thread>>  Message_Thread = null;
        try {
            Message_Thread = new searchAsync(thread_dao).execute(Name_Or_Email).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Message_Thread;
    }
    public LiveData<List<Message_Thread>> getAll() {
        return all_threads;
    }
    
    
    
    
    
    //-------------------------//

    private static class InserAsync extends AsyncTask<Message_Thread, Void, Void> {
        private final Thread_dao Thread_dao;

        private InserAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }

        @Override
        protected Void doInBackground(Message_Thread... Message_Thread) {
            try {
                Thread_dao.insert(Message_Thread[0]);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdateAsync extends AsyncTask<String, Void, Void> {
        private final Thread_dao Thread_dao;

        private UpdateAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Thread_dao.update_lastseen(strings[0],strings[1],strings[2],strings[3],strings[4]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        private final Thread_dao Thread_dao;

        private DeleteAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Thread_dao.delete(strings[0]);
            return null;
        }
    }
private static class UpdateUnreadAsync extends AsyncTask<String, Void, Void> {
        private final Thread_dao Thread_dao;

        private UpdateUnreadAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Thread_dao.updateUnread(strings[0]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final Thread_dao Thread_dao;

        private DeleteAllAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Thread_dao.deleteAll();
            return null;
        }
    }
    private static class SelectAsync extends AsyncTask<String,Void,Message_Thread> {
        private final Thread_dao Thread_dao;

        private SelectAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }


        @Override
        protected Message_Thread doInBackground(String... strings) {

            return Thread_dao.selectthread(strings[0]);
        }
    }

    public static class searchAsync extends AsyncTask<String,Void,LiveData<List<Active_Thread>> >{
        private final Thread_dao Thread_dao;

        private searchAsync(Thread_dao Thread_dao) {
            this.Thread_dao = Thread_dao;
        }

        @Override
        protected LiveData<List<Active_Thread>>  doInBackground(String... strings) {

            return Thread_dao.getActiveThread(strings[0]);
        }
    }
    
}
