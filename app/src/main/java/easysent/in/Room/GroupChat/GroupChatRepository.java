package easysent.in.Room.GroupChat;

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

import easysent.in.Interface.Messages.LiveData_Item;
import easysent.in.Interface.Messages.LiveData_Messages;
import kotlinx.coroutines.CoroutineScope;

public class GroupChatRepository {


    private final Groups_Chats_dao Chat_dao;
    private final LiveData<List<Group_Chat>> all_chats;

    public GroupChatRepository(Application application) {
        GroupChatDataBase database = GroupChatDataBase.getInstance(application);
        Chat_dao = database.group_chat_dao();
        all_chats = Chat_dao.getAll();
    }


    public void insert(Group_Chat group_chat) {
        new InserAsync(Chat_dao).execute(group_chat);

    }

    public void update_delete(String id) {
        new UpdateDeleteAsync(Chat_dao).execute(id);

    }
    public void update_status(String id,String status) {
        new UpdateStatusAsync(Chat_dao).execute(id,status);

    }

    public void delete(String uid) {
        new DeleteAsync(Chat_dao).execute(uid);


    }

    public void deleteAll() {
        new DeleteAllAsync(Chat_dao).execute();


    }

    public LiveData<List<Group_Chat>> getAll() {
        return all_chats;
    }

    public Group_Chat selectChat(String uid) {
        Group_Chat user = null;
        try {
            user = new SelectAsync(Chat_dao).execute(uid).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void getChat_byPaging(String group_id, Groups_chat_ViewModel groups_chat_viewModel, boolean st, LiveData_Messages<Group_Chat> chatLiveData_list) {

            new  searchAsyncPaged(Chat_dao,groups_chat_viewModel,st,chatLiveData_list).execute(group_id);

    }

    public LiveData<List<Group_Chat>> getChat(String group_id) {
        LiveData<List<Group_Chat>> Chats = null;
        try {
            Chats = new getChat(Chat_dao).execute(group_id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Chats;
    }


    public List<Group_Chat> selectAttachment(String group_id, String chat_id) {
        List<Group_Chat> user = null;
        try {
            user = new SelectAttachmentAsync(Chat_dao).execute(group_id, chat_id).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


//----------------------------------------------------//

    private static class InserAsync extends AsyncTask<Group_Chat, Void, Void> {
        private final Groups_Chats_dao Chat_dao;

        private InserAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(Group_Chat... Chats) {
            try {
                Chat_dao.insert(Chats[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private static class UpdateDeleteAsync extends AsyncTask<String, Void, Void> {
        private final Groups_Chats_dao Chat_dao;

        private UpdateDeleteAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.update_deleted(strings[0]);
            return null;
        }
    }
   private static class UpdateStatusAsync extends AsyncTask<String, Void, Void> {
        private final Groups_Chats_dao Chat_dao;

        private UpdateStatusAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.update_status(strings[0], Integer.parseInt(strings[1]));
            return null;
        }
    }

    private static class DeleteAsync extends AsyncTask<String, Void, Void> {
        private final Groups_Chats_dao Chat_dao;

        private DeleteAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(String... strings) {
            Chat_dao.delete(strings[0]);
            return null;
        }
    }

    private static class DeleteAllAsync extends AsyncTask<Void, Void, Void> {
        private final Groups_Chats_dao Chat_dao;

        private DeleteAllAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Chat_dao.deleteAll();
            return null;
        }
    }

    private static class SelectAsync extends AsyncTask<String, Void, Group_Chat> {
        private final Groups_Chats_dao Chat_dao;

        private SelectAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }


        @Override
        protected Group_Chat doInBackground(String... strings) {

            return Chat_dao.selectChat(strings[0]);
        }
    }

    private static class SelectAttachmentAsync extends AsyncTask<String, Void, List<Group_Chat>> {
        private final Groups_Chats_dao Chat_dao;

        private SelectAttachmentAsync(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }


        @Override
        protected List<Group_Chat> doInBackground(String... strings) {

            return Chat_dao.selectAttachment(strings[0], strings[1]);
        }
    }

    public static class searchAsyncPaged extends AsyncTask<String, Void, LiveData<PagingData<Group_Chat>>> {
        private final Groups_Chats_dao Chat_dao;
        private final Groups_chat_ViewModel groups_chat_viewModel;
        private final boolean st;
        LiveData_Messages<Group_Chat> chatLiveData_list;
        private searchAsyncPaged(Groups_Chats_dao Chat_dao, Groups_chat_ViewModel groups_chat_viewModel, boolean st, LiveData_Messages<Group_Chat> chatLiveData_list) {
            this.Chat_dao = Chat_dao;
            this.groups_chat_viewModel = groups_chat_viewModel;
            this.st = st;
            this.chatLiveData_list = chatLiveData_list;
        }

        @Override
        protected LiveData<PagingData<Group_Chat>> doInBackground(String... strings) {

            LiveData<PagingData<Group_Chat>> all_chat_paged = null;

            Pager<Integer, Group_Chat> pager = new Pager(
                    // Create new paging config


                    new PagingConfig(    3000, //  Count of items in one page
                            9000, //  Number of items to prefetch
                            true, // Enable placeholders for data which is not yet loaded
                            30000, // initialLoadSize - Count of items to be loaded initially
                            PagingConfig.MAX_SIZE_UNBOUNDED),// maxSize - Count of total items to be shown in recyclerview
                    () -> st?Chat_dao.getMessageBy_FirstPage(strings[0]):Chat_dao.getMessageBy_paging(strings[0]));

            CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(groups_chat_viewModel);
            all_chat_paged = PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), coroutineScope);
            return all_chat_paged;


        }

        @Override
        protected void onPostExecute(LiveData<PagingData<Group_Chat>> pagingDataLiveData) {
            super.onPostExecute(pagingDataLiveData);
            chatLiveData_list.allMessage(pagingDataLiveData);
        }
    }

    public static class getChat extends AsyncTask<String, Void, LiveData<List<Group_Chat>>> {
        private final Groups_Chats_dao Chat_dao;

        private getChat(Groups_Chats_dao Chat_dao) {
            this.Chat_dao = Chat_dao;
        }

        @Override
        protected LiveData<List<Group_Chat>> doInBackground(String... strings) {

            LiveData<List<Group_Chat>> all_chat_paged = null;
            all_chat_paged = Chat_dao.getMessage(strings[0]);
            return all_chat_paged;
        }
    }

}
