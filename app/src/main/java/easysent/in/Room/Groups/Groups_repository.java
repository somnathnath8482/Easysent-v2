package easysent.in.Room.Groups;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class Groups_repository {

    private final Groups_dao groups_dao;
    private final LiveData<List<Groups>> all_groups;
 
    public Groups_repository(Application application) {
        GroupsDatabase database = GroupsDatabase.getInstance(application);
        groups_dao = database.groups_dao();
        all_groups = groups_dao.getAll();
    }



    public void insert(Groups groups) {
        new InserAsync(groups_dao).execute(groups);

    }

    public void update_last_message(String message,String status,String type,String groupId,String time) {
        new UpdatelastmessageAsync(groups_dao).execute(message,status,type,groupId,time);
    }

  public void upadet(String name,String desc,String user_count,String profile_pic,String groupId) {
        new UpdateAsync(groups_dao).execute(name,desc,user_count,profile_pic,groupId);
    }


    public void delete(String uid) {
        new DeleteAsync(groups_dao).execute(uid);


    }
    public void UpdateUnread(String uid) {
        new UpdateUnreadAsync(groups_dao).execute(uid);


    }

    public void deleteAll() {
        new DeleteAllAsync(groups_dao).execute();


    }


    public Groups selectGroup(String uid) {
        Groups groups = new Groups();
        try{ groups = new SelectAsync(groups_dao).execute(uid).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return groups ;
    }
 public LiveData<Groups> selectGroupLive(String uid) {
     LiveData<Groups> groups = null;
        try{ groups = new SelectAsyncLive(groups_dao).execute(uid).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return groups ;
    }

    public LiveData<List<Groups>> searchGroups(String Name_Or_Email){
        LiveData<List<Groups>>  Groups = null;
        try {
            Groups = new searchAsync(groups_dao).execute(Name_Or_Email).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Groups;
    }
    public LiveData<List<Groups>> getAll() {
        return all_groups;
    }
    
    
    
    
    
    //-------------------------//

    private static class InserAsync extends AsyncTask<Groups, Void, Void> {
        private final Groups_dao groups_dao;

        private InserAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected Void doInBackground(Groups... Groups) {
            try {
                groups_dao.insert(Groups[0]);
            } catch (Exception e) {
                //e.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdatelastmessageAsync extends AsyncTask<String, Void, Void> {
        private final Groups_dao groups_dao;

        private UpdatelastmessageAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            groups_dao.update_lastMessage(strings[0],strings[1],strings[2],strings[3],strings[4]);
            return null;
        }
    }
    private static class UpdateAsync extends AsyncTask<String, Void, Void> {
        private final Groups_dao groups_dao;

        private UpdateAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            groups_dao.update(strings[0],strings[1],strings[2],strings[3],strings[4]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        private final Groups_dao groups_dao;

        private DeleteAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            groups_dao.delete(strings[0]);
            return null;
        }
    }
private static class UpdateUnreadAsync extends AsyncTask<String, Void, Void> {
        private final Groups_dao groups_dao;

        private UpdateUnreadAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            groups_dao.updateUnread(strings[0]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final Groups_dao groups_dao;

        private DeleteAllAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            groups_dao.deleteAll();
            return null;
        }
    }
    private static class SelectAsync extends AsyncTask<String,Void,Groups> {
        private final Groups_dao groups_dao;

        private SelectAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }


        @Override
        protected Groups doInBackground(String... strings) {

            return groups_dao.selectGroup(strings[0]);
        }
    }

    private static class SelectAsyncLive extends AsyncTask<String,Void,LiveData<Groups>> {
        private final Groups_dao groups_dao;

        private SelectAsyncLive(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }


        @Override
        protected LiveData<Groups> doInBackground(String... strings) {

            return groups_dao.selectGroupLive(strings[0]);
        }
    }

    public static class searchAsync extends AsyncTask<String,Void,LiveData<List<Groups>> >{
        private final Groups_dao groups_dao;

        private searchAsync(Groups_dao groups_dao) {
            this.groups_dao = groups_dao;
        }

        @Override
        protected LiveData<List<Groups>>  doInBackground(String... strings) {

            return groups_dao.SearchGroup(strings[0]);
        }
    }
    
}
