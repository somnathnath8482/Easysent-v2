package easysent.in.Helper;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.GET_ALL_CHATS;
import static easysent.in.Helper.Constants.GET_ALL_CHATS_BY_THREAD;
import static easysent.in.Helper.Constants.GET_ALL_GROUP_MESSAGE;
import static easysent.in.Helper.Constants.GET_ALL_THREAD;
import static easysent.in.Helper.Constants.GET_ALL_USER;
import static easysent.in.Helper.Constants.GET_BLOCK_USER;
import static easysent.in.Helper.Constants.GET_MESSAGE_By_GROUP;
import static easysent.in.Helper.Constants.Get_group;
import static easysent.in.Helper.Constants.UPDATE_BY_SQL;

import android.app.Application;
import android.os.Handler;

import easysent.in.Firebase.Data;
import easysent.in.Firebase.Message;
import easysent.in.Firebase.Sender;
import easysent.in.Firebase.SubscribetoTopic;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.Response;
import easysent.in.Response.AllThreads.AllThreadsResponse;
import easysent.in.Response.AllThreads.ThreadsItem;
import easysent.in.Response.AllUsers.AllUsersResponse;
import easysent.in.Response.AllUsers.UsersItem;
import easysent.in.Response.Chats_By_Thread.AllChatResponse;
import easysent.in.Response.Chats_By_Thread.ChatsItem;
import easysent.in.Response.GetAllBlocks.AllBlockResponse;
import easysent.in.Response.GetAllBlocks.Block_item;
import easysent.in.Response.GetGroups.GetgroupsResponse;
import easysent.in.Response.GetGroups.GroupsItem;
import easysent.in.Response.GroupChats.GetGroupChatsResponse;
import easysent.in.Response.GroupChats.MessagesItem;
import easysent.in.Response.Login.User;
import easysent.in.Room.Block.Block;
import easysent.in.Room.Block.BlockViewModel;
import easysent.in.Room.GroupChat.Group_Chat;
import easysent.in.Room.GroupChat.Groups_chat_ViewModel;
import easysent.in.Room.Groups.Groups;
import easysent.in.Room.Groups.Groups_ViewModel;
import easysent.in.Room.Messages.Chats;
import easysent.in.Room.Messages.Message_View_Model;
import easysent.in.Room.Threads.Message_Thread;
import easysent.in.Room.Threads.Thread_ViewModel;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SyncData {


    public static void SyncThread(Application application, Handler handler) {
        Thread_ViewModel thread_viewModel = new Thread_ViewModel(application);
        String email = PreferenceFile.getUser().getUser().getEmail();
        String id = PreferenceFile.getUser().getUser().getId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);

                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            AllThreadsResponse response = gson.fromJson(res.toString(), AllThreadsResponse.class);
                            if (response != null) {
                                for (int i = 0; i < response.getThreads().size(); i++) {
                                    try {
                                        ThreadsItem uitem = response.getThreads().get(i);
                                        Message_Thread message_thread = new Message_Thread(uitem.getSender(), uitem.getId(), uitem.getCreateAt(), uitem.getReciver()
                                                , "", "", "", uitem.getCreateAt());
                                        thread_viewModel.insert(message_thread);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }, BASE_URL + GET_ALL_THREAD, application, handler, email, map);
            }
        }).start();

    }

    public static void SyncGroups(Application application, Handler handler) {
        String id = PreferenceFile.getUser().getUser().getId();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);

                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            GetgroupsResponse response = gson.fromJson(res.toString(), GetgroupsResponse.class);
                            if (response != null) {
                                if (response != null) {
                                    Groups_ViewModel groups_viewModel = new Groups_ViewModel(application);
                                    UserVewModel userVewModel = new UserVewModel(application);
                                    SubscribetoTopic subscribetoTopic = new SubscribetoTopic();
                                    for (int i = 0; i < response.getGroups().size(); i++) {
                                        try {
                                            GroupsItem groupsItem = response.getGroups().get(i);
                                            subscribetoTopic.Subscribetotopic(groupsItem.getGroupId(), new ArrayList<>());
                                            Groups groups = new Groups(groupsItem.getDateOfJoin(), groupsItem.getCreator(),
                                                    groupsItem.getGroupPrimaryId(), groupsItem.getUserCount(), groupsItem.getUserId(),
                                                    groupsItem.getGroupId(), id, groupsItem.getJoindBy(), groupsItem.getName(),
                                                    groupsItem.getGroupDesc(), groupsItem.getCreatedAt(), groupsItem.getGroupMasterId(),
                                                    0, groupsItem.getDp());
                                            Users u = userVewModel.selectUser(groupsItem.getCreator());
                                            if (u != null) {
                                                groups.setLast_message("Group Created by " + u.getName());
                                            } else {
                                                groups.setLast_message("New Group Created");
                                            }

                                            groups.setLast_message_status("");
                                            groups.setLast_message_time(groupsItem.getCreatedAt());
                                            groups.setLast_message_type("T");

                                            Groups isss = groups_viewModel.selectgroup(groups.getGroupId());
                                            if (isss != null) {
                                                if (
                                                        !groupsItem.getName().equals(isss.getName())
                                                                || !groupsItem.getDp().equals(isss.getDp())
                                                                || !groupsItem.getGroupDesc().equals(isss.getGroupDesc())
                                                                || !groupsItem.getUserCount().equals(isss.getUserCount())


                                                ) {
                                                    groups_viewModel.update(groupsItem.getName(), groupsItem.getGroupDesc(), groupsItem.getUserCount(),
                                                            groupsItem.getDp(),groupsItem.getGroupId());
                                                }


                                            } else {
                                                groups_viewModel.insert(groups);
                                            }

                                            // userVewModel.insert(users);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }

                    }
                }, BASE_URL + Get_group, application, handler, id, map);
            }
        }).start();

    }
    public static void SyncBlock(Application activity, Handler handler) {
        User user = PreferenceFile.getUser().getUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", user.getId());


        MethodClass.CallHeader2(new Response() {
            @Override
            public void onResponse(JSONObject res) {

                try {
                    if (res != null) {
                        BlockViewModel blockViewModel = new BlockViewModel(activity);
                        blockViewModel.deleteAll();
                        Gson gson = new Gson();
                        AllBlockResponse response = gson.fromJson(res.toString(), AllBlockResponse.class);
                        for (Block_item item : response.getblocks()) {
                            Block block = new Block(item.getToUser(), item.getId(), item.getThread(), item.getFromUser());
                            blockViewModel.insert(block);
                        }

                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }

            }
        }, BASE_URL + GET_BLOCK_USER, activity, handler, user.getEmail(), map);

    }
    public static void SyncAllGroupChats(Application application, Handler handler) {
        String id = PreferenceFile.getUser().getUser().getId();


        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", id);

                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            GetGroupChatsResponse response = gson.fromJson(res.toString(), GetGroupChatsResponse.class);

                            if (response != null) {
                                Groups_chat_ViewModel chat_viewModel = new Groups_chat_ViewModel(application);
                                Groups_ViewModel groups_viewModel = new Groups_ViewModel(application);
                                if (response != null) {
                                    for (int i = 0; i < response.getMessages().size(); i++) {
                                        try {
                                            MessagesItem chatitem = response.getMessages().get(i);
                                            Group_Chat isss = chat_viewModel.selectChat(chatitem.getId());

                                            if (isss != null) {

                                                if (!chatitem.getIsDeleted().equals(isss.getIsDeleted())) {
                                                    chat_viewModel.update_deleted(chatitem.getId());
                                                }
                                            } else {
                                                Group_Chat group_chat = new Group_Chat(chatitem.getIsDeleted(), chatitem.getAttachment(), chatitem.getGroupId()
                                                        , chatitem.getSender(), chatitem.getAttachmentTitle(), chatitem.getCreatedAt(), chatitem.getId(), chatitem.getReplayOf()
                                                        , chatitem.getMessage(), chatitem.getType(), chatitem.getSeen());


                                                chat_viewModel.insert(group_chat);
                                                groups_viewModel.update_last_message(chatitem.getMessage(), "", group_chat.getType()
                                                        , group_chat.getGroupId(), group_chat.getCreatedAt());
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        }

                    }
                }, BASE_URL + GET_ALL_GROUP_MESSAGE, application, handler, id, map);
            }
        }).start();

    }

    public static void SyncChatsByGroup(Application application, Handler handler,String id) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("group", id);

                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            GetGroupChatsResponse response = gson.fromJson(res.toString(), GetGroupChatsResponse.class);

                            if (response != null) {
                                Groups_chat_ViewModel chat_viewModel = new Groups_chat_ViewModel(application);
                                Groups_ViewModel groups_viewModel = new Groups_ViewModel(application);
                                if (response != null) {
                                    for (int i = 0; i < response.getMessages().size(); i++) {
                                        try {
                                            MessagesItem chatitem = response.getMessages().get(i);
                                            Group_Chat isss = chat_viewModel.selectChat(chatitem.getId());

                                            if (isss != null) {

                                                if (!chatitem.getIsDeleted().equals(isss.getIsDeleted())) {
                                                    chat_viewModel.update_deleted(chatitem.getId());
                                                }
                                            } else {
                                                Group_Chat group_chat = new Group_Chat(chatitem.getIsDeleted(), chatitem.getAttachment(), chatitem.getGroupId()
                                                        , chatitem.getSender(), chatitem.getAttachmentTitle(), chatitem.getCreatedAt(), chatitem.getId(), chatitem.getReplayOf()
                                                        , chatitem.getMessage(), chatitem.getType(), chatitem.getSeen());


                                                chat_viewModel.insert(group_chat);
                                                groups_viewModel.update_last_message(chatitem.getMessage(), "", group_chat.getType()
                                                        , group_chat.getGroupId(), group_chat.getCreatedAt());
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        }

                    }
                }, BASE_URL + GET_MESSAGE_By_GROUP, application, handler, PreferenceFile.getUser().getUser().getId(), map);
            }
        }).start();

    }

    public static void SyncUser(Application context, Handler handler, String Header) {

        UserVewModel userVewModel = new UserVewModel(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            AllUsersResponse response = gson.fromJson(res.toString(), AllUsersResponse.class);
                            if (response != null) {
                                for (int i = 0; i < response.getUsers().size(); i++) {
                                    try {
                                        UsersItem uitem = response.getUsers().get(i);
                                        Users users = new Users(uitem.getPhone(), uitem.getName(), uitem.getProfilePic(), uitem.getId(), "offline", uitem.getGender(),
                                                uitem.getAbout(), uitem.getIsVerifyed(), uitem.getEmail(), uitem.getToken());
                                        Users isss = userVewModel.selectUser(uitem.getId());
                                        if (isss != null) {

                                            if (uitem.getToken() != null) {
                                                if (
                                                        !uitem.getToken().equals(isss.getToken())
                                                                || !uitem.getProfilePic().equals(isss.getProfilePic())
                                                                || !uitem.getName().equals(isss.getName())
                                                ) {
                                                    userVewModel.update(uitem.getPhone(), uitem.getName(), uitem.getProfilePic(), uitem.getGender(),
                                                            uitem.getAbout(), uitem.getIsVerifyed(), uitem.getEmail(), uitem.getToken());
                                                }
                                            }

                                        } else {
                                            userVewModel.insert(users);
                                        }

                                        // userVewModel.insert(users);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }, BASE_URL + GET_ALL_USER, context, handler, Header, new HashMap<>());
            }
        }).start();
    }


    public static void SyncChat(Application application, Handler handler) {


        Thread_ViewModel thread_viewModel = new Thread_ViewModel(application);
        Message_View_Model message_view_model = new Message_View_Model(application);
        String Header = PreferenceFile.getUser().getUser().getEmail();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", PreferenceFile.getUser().getUser().getId());
        new Thread(new Runnable() {
            @Override
            public void run() {
                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            AllChatResponse response = gson.fromJson(res.toString(), AllChatResponse.class);
                            if (response != null) {
                                for (int i = 0; i < response.getChats().size(); i++) {
                                    try {
                                        ChatsItem item = response.getChats().get(i);
                                        Chats chats = new Chats(item.getAttachment() + "", item.getSender(), item.getCreatedAt(), item.getId(), item.getThread()
                                                , item.getMessage(), item.getType(), item.getReciver(), item.getSeen(), item.getReplay_of(), item.getIs_deleted());

                                        Chats isss = message_view_model.selectChat(item.getId());
                                        if (isss != null) {
                                            if (!item.getSeen().equals(isss.getSeen()) && (Integer.parseInt(item.getSeen()) > Integer.parseInt(isss.getSeen()))) {
                                                message_view_model.update_seen(item.getSeen(), item.getId());
                                            }
                                            if (!item.getIs_deleted().equals(isss.getIs_deleted())) {
                                                message_view_model.update_delete(item.getId(), item.getIs_deleted());
                                            }

                                        } else {
                                            message_view_model.insert(chats);
                                            thread_viewModel.update_last_seen(chats.getMessage(), chats.getSeen(), chats.getType(), chats.getThread(), chats.getCreatedAt());
                                            if (i == response.getChats().size() - 1) {
                                                String id = PreferenceFile.getUser().getUser().getId();
                                                String sql = "update `chats` set `seen` = '2' where `seen` = '1'  AND `reciver` =" + id;
                                                sync_BY_SQL(sql, chats, application, handler);
                                            }
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }, BASE_URL + GET_ALL_CHATS, application, handler, Header, map);
            }
        }).start();
    }

    public static void SyncChatOFThread(String id, Application application, Handler handler) {

        Message_View_Model message_view_model = new Message_View_Model(application);
        Thread_ViewModel thread_viewModel = new Thread_ViewModel(application);
        String Header = PreferenceFile.getUser().getUser().getEmail();
        HashMap<String, Object> map = new HashMap<>();
        map.put("thread", id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            AllChatResponse response = gson.fromJson(res.toString(), AllChatResponse.class);
                            if (response != null) {
                                for (int i = 0; i < response.getChats().size(); i++) {
                                    try {
                                        ChatsItem item = response.getChats().get(i);
                                        Chats chats = new Chats(item.getAttachment() + "", item.getSender(), item.getCreatedAt(), item.getId(), item.getThread()
                                                , item.getMessage(), item.getType(), item.getReciver(), item.getSeen(), item.getReplay_of(), item.getIs_deleted());

                                        Chats isss = message_view_model.selectChat(item.getId());
                                        if (isss != null) {
                                            if (!item.getSeen().equals(isss.getSeen()) && (Integer.parseInt(item.getSeen()) > Integer.parseInt(isss.getSeen()))) {
                                                message_view_model.update_seen(item.getSeen(), item.getId());
                                            }
                                            if (!item.getIs_deleted().equals(isss.getIs_deleted())) {

                                                message_view_model.update_delete(item.getId(), item.getIs_deleted());
                                            }

                                        } else {
                                            message_view_model.insert(chats);
                                            if (i == response.getChats().size() - 1) {
                                                thread_viewModel.update_last_seen(chats.getMessage(), chats.getSeen(), chats.getType(), chats.getThread(), chats.getCreatedAt());


                                                String sql = "update `chats` set `seen` = '2' where `seen` = '1'  AND  `thread` =" + chats.getThread() + " AND `reciver` =" + id;

                                                sync_BY_SQL(sql, chats, application, handler);

                                            }
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                    }
                }, BASE_URL + GET_ALL_CHATS_BY_THREAD, application, handler, Header, map);
            }
        }).start();
    }

    public static void UpdateSeen(Chats chats, Application application, Handler handler) {
        UserVewModel userVewModel = new UserVewModel(application);

        String token = "";
        Users reciver = null;
        if (PreferenceFile.getUser().getUser().getId().equals(chats.getSender())) {
            reciver = userVewModel.selectUser(chats.getReciver());

        } else if (PreferenceFile.getUser().getUser().getId().equals(chats.getReciver())) {
            reciver = userVewModel.selectUser(chats.getSender());

        }

        if (reciver != null && application != null) {
            token = reciver.getToken();
            Data data = new Data("RECIVED", chats.getThread(), "");
            ///Message message1 = new Message(data, token);

            Sender se = new Sender(data,token);
            MethodClass.SendNotification(se, application, handler);
        }


    }

    public static void sync_BY_SQL(String sql, Chats chats, Application application, Handler handler) {
        String Header = PreferenceFile.getUser().getUser().getEmail();
        HashMap<String, Object> map = new HashMap<>();

        map.put("sql", sql);
        new Thread(new Runnable() {
            @Override
            public void run() {
                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {

                        }

                    }
                }, BASE_URL + UPDATE_BY_SQL, application, handler, Header, map);
            }
        }).start();

        if (chats != null) {
            UpdateSeen(chats, application, handler);
        }
    }


}
