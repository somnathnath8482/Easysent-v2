package easysent.in.Room.Users;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private final UserDao userDao;
    private final LiveData<List<Users>> allUser;
    Handler handler;

    public UserRepository(Application application) {
        UserDatabase database = UserDatabase.getInstance(application);
        userDao = database.userDao();
        allUser = userDao.getAll();
    }

    public void insert(Users user) {
        new InserAsync(userDao).execute(user);

    }

    public void update(String phone, String name, String profilePic, String gender, String about, String isVerifyed, String email, String token) {
        new UpdateAsync(userDao).execute(phone,name,profilePic,gender,about,isVerifyed,email,token);

    }

    public void setIp(String uid, String ip){

        new SetIpAsync(userDao).execute(uid,ip);

    }

    public String getIp(String uid){
        String ip="";
        try {
            ip = new GetIpAsync(userDao).execute(uid).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ip;
    }


  public void updateStatus(String status,String id) {
        new UpdateStatusAsync(userDao).execute(status,id);

    }

    public void delete(String uid) {
        new DeleteAsync(userDao).execute(uid);


    }

    public void deleteAll() {
        new DeleteAllAsync(userDao).execute();


    }


    public Users selectUser(String uid) {
        Users user = null;
        try{ user = new SelectAsync(userDao).execute(uid).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return user ;
    }
    public   LiveData<Users>  selectUserLive(String uid) {
        LiveData<Users>  user = null;
        try{ user = new SelectUserLiveAsync(userDao).execute(uid).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return user ;
    }

    public  LiveData<List<Users>> search(String Name_Or_Email){
        LiveData<List<Users>> users = null;
        try {
            users = new searchAsync(userDao).execute(Name_Or_Email).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return users;
    }
    public LiveData<List<Users>> getAll() {
        return allUser;
    }

    private static class InserAsync extends AsyncTask<Users, Void, Void> {
        private final UserDao userDao;

        private InserAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Users... users) {
            try {
                userDao.insert(users[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdateAsync extends AsyncTask<String, Void, Void> {
        private final UserDao userDao;

        private UpdateAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            //phone,name,profilePic,gender,about,isVerifyed,email,token
            userDao.update(strings[0],strings[1],strings[2],strings[3],strings[4],strings[5],strings[6],strings[7]);
            return null;
        }
    }

    private static class SetIpAsync extends AsyncTask<String, Void, Void> {
        private final UserDao userDao;

        private SetIpAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            //phone,name,profilePic,gender,about,isVerifyed,email,token
            if (strings[1].length()>100){
                userDao.setToken(strings[0],strings[1]);
            }else{
                userDao.setIp(strings[0],strings[1]);
            }

            return null;
        }
    }

        private static class GetIpAsync extends AsyncTask<String, Void, String> {
        private final UserDao userDao;

        private GetIpAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected String doInBackground(String... strings) {
            //phone,name,profilePic,gender,about,isVerifyed,email,token
            userDao.getIp(strings[0]);
            return null;
        }
    }




    private static class UpdateStatusAsync extends AsyncTask<String, Void, Void> {
        private final UserDao userDao;

        private UpdateStatusAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            //phone,name,profilePic,gender,about,isVerifyed,email,token
            userDao.updateStatus(strings[0],strings[1]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        private final UserDao userDao;

        private DeleteAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            userDao.delete(strings[0]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final UserDao userDao;

        private DeleteAllAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDao.deleteAll();
            return null;
        }
    }


    private static class SelectAsync extends AsyncTask<String,Void,Users> {
        private final UserDao userDao;

        private SelectAsync(UserDao userDao) {
            this.userDao = userDao;
        }


        @Override
        protected Users doInBackground(String... strings) {

            return userDao.selectUser(strings[0]);
        }
    }
    private static class SelectUserLiveAsync extends AsyncTask<String,Void,  LiveData<Users> > {
        private final UserDao userDao;

        private SelectUserLiveAsync(UserDao userDao) {
            this.userDao = userDao;
        }


        @Override
        protected   LiveData<Users>  doInBackground(String... strings) {

            return userDao.selectUserlive(strings[0]);
        }
    }

    public static class searchAsync extends AsyncTask<String,Void, LiveData<List<Users>>>{
        private final UserDao userDao;

        private searchAsync(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected  LiveData<List<Users>> doInBackground(String... strings) {

            return userDao.search(strings[0]);
        }
    }


}
