package easysent.in.Room.Messages;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

import easysent.in.Room.GroupChat.Group_Chat;
import kotlinx.coroutines.CoroutineScope;

public class Chatepository {


    private final Chat_dao Chat_dao;
    private final LiveData<List<Chats>> all_chats;

    public Chatepository(Application application) {
        MessageDataBase database = MessageDataBase.getInstance(application);
        Chat_dao = database.chat_dao();
        all_chats = Chat_dao.getAll();
    }



    public void insert(Chats user) {
        new InserAsync(Chat_dao).execute(user);

    }

    public void update_last_seen(String status,String id) {
        new UpdateAsync(Chat_dao).execute(status,id);

    }public void update_delete(String id,String value) {
        new UpdateDeleteAsync(Chat_dao).execute(id,value);

    }
   public void update_seen_By_Thread(String thread,String sender,String status) {
        new UpdateSeenByThredaAsync(Chat_dao).execute(thread,sender,status);

    } public void update_stseen_Recived_thread(String thread,String sender,String status) {
        new update_stseen_Recived_thread(Chat_dao).execute(thread,sender,status);

    }

    public void delete(String uid) {
        new DeleteAsync(Chat_dao).execute(uid);


    }

    public void deleteAll() {
        new DeleteAllAsync(Chat_dao).execute();


    }


    public Chats selectChat(String uid) {
        Chats user = null;
        try{ user = new SelectAsync(Chat_dao).execute(uid).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return user ;
    }

    public List<Chats> selectAttachment(String uid,String chat_id) {
        List<Chats> user = null;
        try{ user = new SelectAttachmentAsync(Chat_dao).execute(uid,chat_id).get();}
        catch (Exception e) {
            e.printStackTrace();
        }
        return user ;
    }

    public LiveData<List<Chats>> getChat_User(String user,String me){
        LiveData<List<Chats>>  Chats = null;
        try {
            Chats = new searchAsync(Chat_dao).execute(user,me).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Chats;
    }

    public LiveData<PagingData<Chats>>getChat_byPaging(String user, String me, Message_View_Model message_view_model){
        LiveData<PagingData<Chats>> Chats = null;
        try {
            Chats = new searchAsyncPaged(Chat_dao,message_view_model).execute(user,me).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Chats;
    }






    public LiveData<List<Chats>> getAll() {
        return all_chats;
    }
//----------------------------------------------------//

    private static class InserAsync extends AsyncTask<Chats, Void, Void> {
        private final Chat_dao Chat_dao;

        private InserAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(Chats... Chats) {
            try {
                Chat_dao.insert(Chats[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class UpdateAsync extends AsyncTask<String, Void, Void> {
        private final Chat_dao Chat_dao;

        private UpdateAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.update_stseen(strings[0],strings[1]);
            return null;
        }
    } private static class UpdateDeleteAsync extends AsyncTask<String, Void, Void> {
        private final Chat_dao Chat_dao;

        private UpdateDeleteAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.update_deleted(strings[0],strings[1]);
            return null;
        }
    }
 private static class UpdateSeenByThredaAsync extends AsyncTask<String, Void, Void> {
        private final Chat_dao Chat_dao;

        private UpdateSeenByThredaAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.update_stseen_of_thread(strings[0],strings[1],strings[2]);
            return null;
        }
    }
    private static class update_stseen_Recived_thread extends AsyncTask<String, Void, Void> {
        private final Chat_dao Chat_dao;

        private update_stseen_Recived_thread(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.update_stseen_Recived_thread(strings[0],strings[1],strings[2]);
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        private final Chat_dao Chat_dao;

        private DeleteAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.delete(strings[0]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final Chat_dao Chat_dao;

        private DeleteAllAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Chat_dao.deleteAll();
            return null;
        }
    }
    private static class SelectAsync extends AsyncTask<String,Void,Chats> {
        private final Chat_dao Chat_dao;

        private SelectAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }


        @Override
        protected Chats doInBackground(String... strings) {

            return Chat_dao.selectChat(strings[0]);
        }
    }  private static class SelectAttachmentAsync extends AsyncTask<String,Void,List<Chats>> {
        private final Chat_dao Chat_dao;

        private SelectAttachmentAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }


        @Override
        protected List<Chats> doInBackground(String... strings) {

            return Chat_dao.selectAttachment(strings[0],strings[1]);
        }
    }

    public static class searchAsync extends AsyncTask<String,Void,LiveData<List<Chats>> >{
        private final Chat_dao Chat_dao;

        private searchAsync(Chat_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected LiveData<List<Chats>>  doInBackground(String... strings) {

            return Chat_dao.getMessageBy_User(strings[0],strings[1]);
        }
    }

    public static class searchAsyncPaged extends AsyncTask<String,Void, LiveData<PagingData<Chats>> >{
        private final Chat_dao Chat_dao;
        private final Message_View_Model message_view_model;

        private searchAsyncPaged(Chat_dao Chat_dao, Message_View_Model message_view_model) {
            this.Chat_dao = Chat_dao;
            this.message_view_model = message_view_model;
        }

        @Override
        protected LiveData<PagingData<Chats>>  doInBackground(String... strings) {

            LiveData<PagingData<Chats>>  all_chat_paged = null;

            Pager<Integer, Chats> pager = new Pager(
                    // Create new paging config


                    new PagingConfig(    3000, //  Count of items in one page
                            9000, //  Number of items to prefetch
                            true, // Enable placeholders for data which is not yet loaded
                            30000, // initialLoadSize - Count of items to be loaded initially
                            PagingConfig.MAX_SIZE_UNBOUNDED),// maxSize - Count of total items to be shown in recyclerview
                    () -> Chat_dao.getMessageBy_paging(strings[0],strings[1]));

            CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(message_view_model);
            all_chat_paged = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), coroutineScope);

            return all_chat_paged;
        }
    }

}
