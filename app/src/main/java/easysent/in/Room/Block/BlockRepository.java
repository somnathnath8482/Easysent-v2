package easysent.in.Room.Block;

import android.app.Application;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.lifecycle.LiveData;
import easysent.in.Room.Users.UserDatabase;

import java.util.List;

public class BlockRepository {
    private  BlockDao blockDao;
    private  LiveData<List<Block>> all_blocks;
    Handler handler;

    public BlockRepository(Application application) {
        UserDatabase database = UserDatabase.getInstance(application);
        blockDao = database.blockDao();
        all_blocks = blockDao.getAll();
    }

    public void insert(Block user) {
        new InserAsync(blockDao).execute(user);

    }
    

    public void delete(String uid) {
        new DeleteAsync(blockDao).execute(uid);


    } public void deleteFromTo(String from,String to) {
        new DeleteFromTOAsync(blockDao).execute(from,to);


    }

    public void deleteAll() {
        new DeleteAllAsync(blockDao).execute();


    }


    public  LiveData<Block>   selectBlock(String me,String reciver) {
        LiveData<Block>   user = null;
        try{ user = new SelectAsync(blockDao).execute(me,reciver).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return user ;
    }

   
    public LiveData<List<Block>> getAll() {
        return all_blocks;
    }

    private static class InserAsync extends AsyncTask<Block, Void, Void> {
        private final BlockDao userDao;

        private InserAsync(BlockDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(Block... users) {
            try {
                userDao.insert(users[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    
    
    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        private final BlockDao blockDao;

        private DeleteAsync(BlockDao userDao) {
            this.blockDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            blockDao.delete(strings[0]);
            return null;
        }
    }
 private static class DeleteFromTOAsync extends AsyncTask<String, Void, Void> {
        private final BlockDao blockDao;

        private DeleteFromTOAsync(BlockDao userDao) {
            this.blockDao = userDao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            blockDao.delete_from_TO(strings[0],strings[1]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final BlockDao blockDao;

        private DeleteAllAsync(BlockDao userDao) {
            this.blockDao = userDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            blockDao.deleteAll();
            return null;
        }
    }
    private static class SelectAsync extends AsyncTask<String,Void, LiveData<Block>> {
        private final BlockDao blockDao;

        private SelectAsync(BlockDao userDao) {
            this.blockDao = userDao;
        }


        @Override
        protected  LiveData<Block>   doInBackground(String... strings) {

            return blockDao.getBlock(strings[0],strings[1]);
        }
    }



}
