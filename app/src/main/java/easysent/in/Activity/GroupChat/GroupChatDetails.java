package easysent.in.Activity.GroupChat;


import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR;
import static easysent.in.Helper.Constants.CATCH_DIR2;
import static easysent.in.Helper.Constants.LEFT_GROUP;
import static easysent.in.Helper.ExifUtils.getRotatedBitmap;
import static easysent.in.Helper.MethodClass.CashImage;
import static easysent.in.Helper.SyncData.SyncAllGroupChats;
import static easysent.in.Helper.SyncData.SyncGroups;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagingData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import easysent.in.Adapter.GroupChatNewAdapter;
import easysent.in.Encription.Encripter;
import easysent.in.Firebase.Data;
import easysent.in.Firebase.Sender;
import easysent.in.Helper.Constants;
import easysent.in.Helper.CustomProgressbar;
import easysent.in.Helper.ImageGetter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.ShareData;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.OnMenuItemClick;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Error.Error;
import easysent.in.Response.Login.User;
import easysent.in.Room.GroupChat.Group_Chat;
import easysent.in.Room.GroupChat.Groups_chat_ViewModel;
import easysent.in.Room.Groups.Groups;
import easysent.in.Room.Groups.Groups_ViewModel;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;
import easysent.in.databinding.ActivityGroupChatDetailsBinding;
import easysent.in.databinding.AttachmentLayoutBinding;
import easysent.in.databinding.MainToolbarBinding;

public class GroupChatDetails extends AppCompatActivity {
    ActivityGroupChatDetailsBinding binding;
    MainToolbarBinding mainToolbarBinding;
    Groups_ViewModel groups_viewModel;
    UserVewModel userVewModel;
    Groups_chat_ViewModel groups_chat_viewModel;
    Application application;
    Context context;
    Activity activity;
    String id = "";
    private String filePath = "";
    private String fileType = "";
    private String forward = "";

    private int total = 0;
    Groups groups = null;
    Handler handler = new Handler();
    private Bitmap seleted_bitmap = null;
    private String[] storage_permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private ItemTouchHelper.SimpleCallback call_tocach;
    private SoundPool soundPool;
    private int success;
    private int count = 0;
    private String namme = "";
    private String total_user = "";
    private String p_pic = "";
    private String my_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_group_chat_details);
        binding = ActivityGroupChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        application = getApplication();
        my_id = PreferenceFile.getUser().getUser().getId();
        activity = this;
        context = this;
        id = getIntent().getStringExtra("id");


        mainToolbarBinding = MainToolbarBinding.bind(binding.toolbar.getRoot());
        mainToolbarBinding.back.setVisibility(View.VISIBLE);
        mainToolbarBinding.menu.setVisibility(View.GONE);
        mainToolbarBinding.menuMessage.setVisibility(View.VISIBLE);


        groups_viewModel = ViewModelProviders.of(this).get(Groups_ViewModel.class);
        userVewModel = ViewModelProviders.of(this).get(UserVewModel.class);
        groups_chat_viewModel = ViewModelProviders.of(this).get(Groups_chat_ViewModel.class);


        GroupChatNewAdapter adapter = new GroupChatNewAdapter(activity, handler, userVewModel, groups_chat_viewModel, binding.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(false);
        binding.recycler.setLayoutManager(linearLayoutManager);
        binding.recycler.setNestedScrollingEnabled(false);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(adapter);

        groups_chat_viewModel.getMessageBy_paging(id, false).observe(this, new Observer<PagingData<Group_Chat>>() {
            @Override
            public void onChanged(PagingData<Group_Chat> group_chats) {
                adapter.submitData(getLifecycle(), group_chats);
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);

                        groups_viewModel.updateUnread(id);
                    }
                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount) {
                        super.onItemRangeChanged(positionStart, itemCount);
                        Log.e("TAG", "onItemRangeChanged: start"+positionStart+" icount "+itemCount );
                        binding.recycler.smoothScrollToPosition(positionStart+itemCount);
                        int curr = adapter.snapshot().size();
                        total = curr;
                    }


                });
            }
        });

        groups = groups_viewModel.selectgroup(id);
        if (groups != null) {
            namme = groups.getName();
            total_user = groups.getUserCount();
            p_pic = groups.getDp();

        }
        mainToolbarBinding.name.setText(namme);
        mainToolbarBinding.email.setText(total_user + " Active users");
        if (p_pic != null && !p_pic.equalsIgnoreCase("") && !p_pic.equalsIgnoreCase("null")) {
            File file = new File(CATCH_DIR + "/" + p_pic);
            if (file.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ImageGetter imageGetter = new ImageGetter(mainToolbarBinding.profileImage);
                        imageGetter.execute(file);
                    }
                }).start();


            } else {
                Glide.with(context).load(Constants.BASE_URL + "profile_image/" + p_pic)
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .addListener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mainToolbarBinding.profileImage.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(namme, R.drawable.class)));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                mainToolbarBinding.profileImage.setImageDrawable(resource);
                                CashImage(BASE_URL + "profile_image/" + p_pic, p_pic, resource);
                                return false;
                            }
                        })
                        .into(mainToolbarBinding.profileImage);
            }
        }
        call_tocach = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                binding.layReplay.setVisibility(View.VISIBLE);
                binding.dismiss.performClick();
                Group_Chat chats = adapter.snapshot().get(viewHolder.getAbsoluteAdapterPosition());
                if (chats!=null)
                    if (chats.getMessage()!=null &&  !chats.getMessage().equals("")){
                        binding.layReplayDoc.setVisibility(View.GONE);
                        binding.replayText.setVisibility(View.VISIBLE);
                        binding.replayText.setText(chats.getMessage());
                    }else{
                        if (chats.getType().equalsIgnoreCase("I")){
                            binding.layReplayDoc.setVisibility(View.VISIBLE);
                            binding.ivReplayAttachmentTitle.setVisibility(View.GONE);
                            binding.replayText.setVisibility(View.GONE);
                            binding.ivReplayDoc.setVisibility(View.VISIBLE);

                            File file = new File(CATCH_DIR2 + "/" + chats.getAttachment());
                            if (file.exists()) {
                                Glide.with(activity).load(file.getPath()).override(150, 150).into(binding.ivReplayDoc);
                            } else {
                                Glide.with(activity).load(BASE_URL + "Attachment/Groups/"  + chats.getAttachment())
                                        .override(150, 150).into(binding.ivReplayDoc);

                            }


                        }else if(!chats.getType().equalsIgnoreCase("T")){
                            binding.layReplayDoc.setVisibility(View.VISIBLE);
                            binding.ivReplayAttachmentTitle.setVisibility(View.VISIBLE);
                            binding.ivReplayDoc.setVisibility(View.VISIBLE);
                            binding.replayText.setVisibility(View.GONE);

                            Glide.with(activity).load(R.drawable.ic_files).into(binding.ivReplayDoc);
                            binding.ivReplayAttachmentTitle.setText(chats.getAttachment());
                        }
                    }
                if (chats!=null) {
                    forward = chats.getId();
                    if (chats.getSender().equalsIgnoreCase(my_id)) {
                        binding.replaySender.setText("You");
                    }else{
                        Users user = userVewModel.selectUser(chats.getSender());
                        binding.replaySender.setText(user.getName());
                    }
                }

            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(call_tocach);
        itemTouchHelper.attachToRecyclerView(binding.recycler);

        Init();
        OnClick();
        InitPool();
    }

    private void Init() {
      /*  if (pickFile != null)
            pickFile.setAllInterface(new AllInterface() {
                @Override
                public void OnSelect(String path) {
                    super.OnSelect(path);
                    File file = new File(path);
                    try {
                        if (file.exists()) {
                            seleted_bitmap = getRotatedBitmap(activity, path);
                            binding.ivAttachment.setImageBitmap(seleted_bitmap);
                            binding.idTvAttachment.setText(file.getName());
                            fileType = "I";
                            filePath = path;

                            Transition transition = new Slide(Gravity.START);
                            transition.setDuration(10);
                            transition.addTarget(binding.layAttach);

                            TransitionManager.beginDelayedTransition((ViewGroup) binding.layAttach.getParent(), transition);
                            binding.layAttach.setVisibility(View.VISIBLE);
                            binding.ivAttachment.setVisibility(View.VISIBLE);
                            binding.layDoc.setVisibility(View.GONE);
                            binding.dismissReplay.performClick();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void OnSelect(String ty, String path) {
                    super.OnSelect(ty, path);
                    if (ty != null && path != null) {


                        File file = new File(path);
                        if (file.exists()) {
                            String name = ty;

                            int lastIndexOf = name.lastIndexOf(".");

                            String type = "";
                            if (lastIndexOf == -1) {
                                Toast.makeText(activity, "Failed to select file", Toast.LENGTH_SHORT).show();
                                return;

                            } else {
                                type = name.substring(lastIndexOf);
                            }


                            if (type.equalsIgnoreCase(".png")
                                    || type.equalsIgnoreCase(".JPEG")
                                    || type.equalsIgnoreCase(".JPG")) {
                                filePath = MethodClass.getRightAngleImage(path);
                                binding.ivAttachment.setImageBitmap(BitmapFactory.decodeFile(filePath));
                                binding.idTvAttachment.setText(file.getName());
                                fileType = "I";

                                binding.ivAttachment.setVisibility(View.VISIBLE);
                                binding.layDoc.setVisibility(View.GONE);


                            } else if (type.equalsIgnoreCase(".PDF")) {
                                binding.ivDoc.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf));
                                binding.idTvAttachment.setText(file.getName());
                                fileType = "P";

                                binding.ivAttachment.setVisibility(View.GONE);
                                binding.layDoc.setVisibility(View.VISIBLE);
                            } else if (type.equalsIgnoreCase(".mp4")) {
                                binding.ivAttachment.setImageDrawable(getResources().getDrawable(R.drawable.ic_video));
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (file.exists()) {
                                            FutureTarget<Bitmap> futureTarget = Glide.with(activity)
                                                    .asBitmap()
                                                    .override(600, 600)
                                                    .load(file.getAbsolutePath())
                                                    .submit();

                                            try {
                                                Bitmap bi = futureTarget.get();
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        Glide.with(activity).load(bi)
                                                                .into(binding.ivDoc);
                                                    }
                                                });

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }).start();


                                binding.idTvAttachment.setText(file.getName());
                                fileType = "V";
                                binding.ivAttachment.setVisibility(View.GONE);
                                binding.layDoc.setVisibility(View.VISIBLE);
                            } else {
                                binding.ivDoc.setImageDrawable(getResources().getDrawable(R.drawable.ic_files));
                                binding.idTvAttachment.setText(file.getName());
                                fileType = "D";
                                binding.ivAttachment.setVisibility(View.GONE);
                                binding.layDoc.setVisibility(View.VISIBLE);
                            }

                            Transition transition = new Slide(Gravity.START);
                            transition.setDuration(10);
                            transition.addTarget(binding.layAttach);

                            filePath = path;


                            TransitionManager.beginDelayedTransition((ViewGroup) binding.layAttach.getParent(), transition);
                            binding.layAttach.setVisibility(View.VISIBLE);
                            binding.dismissReplay.performClick();
                        }


                    }


                }
            });*/
        checkShare();
    }
    private void checkShare() {
        ShareData shareData = PreferenceFile.getShare();
        if (shareData==null){
            return;
        }

        String fileType = shareData.getType();
        String msg = shareData.getMessage();
        String fi = shareData.getFile();

        PreferenceFile.setShare(new ShareData());
        if (fileType!=null && !fileType.equalsIgnoreCase("")){
            if (fileType.equalsIgnoreCase("T")) {
                binding.textSend.setText(msg);
            }else if (fileType.equalsIgnoreCase("I")) {
                binding.textSend.setText(msg);

                File file = new File(fi);
                try {
                    if (file.exists()) {

                        seleted_bitmap  =  getRotatedBitmap(activity, fi);
                        filePath = fi;
                        binding.ivAttachment.setImageBitmap(seleted_bitmap);
                        binding.idTvAttachment.setText(file.getName());
                        this.fileType = "I";

                        binding.ivAttachment.setVisibility(View.VISIBLE);
                        binding.layAttach.setVisibility(View.VISIBLE);
                        binding.layDoc.setVisibility(View.GONE);

                    }else{
                        Toast.makeText(GroupChatDetails.this, "not exist file",Toast.LENGTH_SHORT ).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void OnClick() {

        binding.btnSend.setOnClickListener(view14 -> {
            String mid =  Calendar.getInstance().getTimeInMillis()+"";
            if (filePath.equals("") && binding.textSend.getText().toString().trim().equals("")) {
                Toast.makeText(activity, "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
            } else {
                Encripter encripter = new Encripter(PreferenceFile.getUser().getUser().getId());

                try {
                    String message = encripter.encrypt(binding.textSend.getText().toString().trim());
                    String sender = PreferenceFile.getUser().getUser().getId();
                    String email = PreferenceFile.getUser().getUser().getEmail();
                    if (fileType.equalsIgnoreCase("I")) {
                        CustomProgressbar.showProgressBar(activity, false);
                        MethodClass.cashattachmentImage2(seleted_bitmap,mid ,handler, activity, new AllInterFace() {
                            @Override
                            public void IsClicked(String s) {
                                super.IsClicked(s);
                                if (s == null) {
                                    binding.layAttach.setVisibility(View.GONE);
                                    filePath = "";
                                    fileType = "";
                                    binding.textSend.setText("");
                                    Toast.makeText(activity, "Unable to sent attachment", Toast.LENGTH_SHORT).show();

                                } else {
                                    filePath = s;
                                    //Toast.makeText(activity,filePath, Toast.LENGTH_SHORT).show();
                                    SendMessage(sender, id, message, activity, email,mid);
                                }

                            }
                        });
                        CustomProgressbar.hideProgressBar();

                    }else if (fileType.equalsIgnoreCase("P")
                            || fileType.equalsIgnoreCase("V")
                            || fileType.equalsIgnoreCase("D") ){

                        CustomProgressbar.showProgressBar(activity, false);
                        MethodClass.cashattachmentFILE2(new File(filePath),mid ,handler, activity, new AllInterFace() {
                            @Override
                            public void IsClicked(String s) {
                                super.IsClicked(s);
                                if (s == null) {

                                    binding.layAttach.setVisibility(View.GONE);
                                    filePath = "";
                                    fileType = "";
                                    binding.textSend.setText("");
                                    Toast.makeText(activity, "Unable to sent attachment", Toast.LENGTH_SHORT).show();

                                } else {
                                    filePath = s;
                                    //Toast.makeText(activity,filePath, Toast.LENGTH_SHORT).show();
                                    SendMessage(sender, id, message, activity, email,mid);
                                }

                            }
                        });
                        CustomProgressbar.hideProgressBar();


                    }

                    else {
                        SendMessage(sender, id, message, activity, email,mid);
                    }
                    binding.textSend.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        binding.attach.setOnClickListener(view -> AddAttachMent());
        binding.dismiss.setOnClickListener(view12 -> {
            Transition transition = new Slide(Gravity.START);
            transition.setDuration(10);
            transition.addTarget(binding.layAttach);

            filePath = "";
            fileType = "";
            forward = "";

            TransitionManager.beginDelayedTransition((ViewGroup) binding.layAttach.getParent(), transition);
            binding.layAttach.setVisibility(View.GONE);

            binding.ivAttachment.setVisibility(View.GONE);
            binding.layDoc.setVisibility(View.GONE);
        });
        binding.dismissReplay.setOnClickListener(view12 -> {
            Transition transition = new Slide(Gravity.START);
            transition.setDuration(10);
            transition.addTarget(binding.layReplay);
            forward="";
            TransitionManager.beginDelayedTransition((ViewGroup) binding.layReplay.getParent(), transition);
            binding.layReplay.setVisibility(View.GONE);
            binding.layReplayDoc.setVisibility(View.GONE);


        });

        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        binding.toolbar.menuMessage.setOnClickListener(view -> Showmenu());
        binding.toolbar.container.setOnClickListener(view -> {
            Intent intent = new Intent(GroupChatDetails.this,GroupDetailsActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        });
    }

    private void Showmenu() {
        MethodClass.show_popup_menu_Group(mainToolbarBinding.menuMessage, activity, new OnMenuItemClick() {
            @Override
            public void OnClick(int res) {

                if (res == R.id.menu_left_group) {
                    leftFGroup();
                }

                else if (res == R.id.menu_report) {
                    MethodClass.reportGroup(activity, new AllInterFace(){
                        @Override
                        public void isClicked(boolean is) {
                            super.isClicked(is);

                            if (is){
                                leftFGroup();
                            }

                            Toast.makeText(activity, "Report Submitted", Toast.LENGTH_SHORT).show();
                        }
                    },groups.getName());
                }

            }
        });
    }

    private void SendMessage(String sender, String id, String message, Activity context, String email,String mid) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String m_id = mid;
                HashMap<String, Object> map = new HashMap<>();
                map.put("sender", sender);
                map.put("id", id);
                map.put("message", message);
                map.put("m_id",m_id );
                if (!forward.equals("")) {
                    map.put("replay_of", forward);
                }


                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.layAttach.setVisibility(View.GONE);
                    }
                });
                if (filePath == null || filePath.equals("")) {
                    map.put("type", "T");

                    try {
                        Encripter encripter = new Encripter(PreferenceFile.getUser().getUser().getId());
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                       String date = utcFormat.format(new Date());
                        Group_Chat group_chat = new Group_Chat("0", "", id
                                ,sender, "", date, m_id, forward
                                , encripter.decrepit(message), "T", 0);
                        groups_chat_viewModel.insert(group_chat);
                        groups_viewModel.update_last_message( encripter.decrepit(message),"1","T", id,date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    MethodClass.CallHeader(new Response() {
                        @Override
                        public void onResponse(JSONObject res) {

                            if (!res.has("error")) {

                                binding.textSend.setText("");
                                forward = "";

                                try {
                                    GroupMessageSend(total, id, message,m_id);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            } else {
                                try {
                                    Error error = new Error(res.getString("code"), res.getString("error"));
                                    MethodClass.hasError(application, error, handler);

                                    if (res.getString("code") != null && res.getString("code").equalsIgnoreCase("-101")) {
                                        leftFGroup();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, BASE_URL + Constants.SEND_GROUP_MESSAGE, activity.getApplication(), handler, email, map);
                } else {

                    map.put("type", fileType);
                    try {
                        Encripter encripter = new Encripter(PreferenceFile.getUser().getUser().getId());
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                        String date = utcFormat.format(new Date());
                        Group_Chat group_chat = new Group_Chat("0", new File(filePath).getName()+"", id
                                ,sender,"" , date, m_id, forward
                                , encripter.decrepit(message), fileType, 0);
                        groups_chat_viewModel.insert(group_chat);
                        groups_viewModel.update_last_message( encripter.decrepit(message),"1",fileType, id,date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    MethodClass.CallMultipart_NO_ProgressBarWithProgress(new Response() {

                        @Override
                        public void onResponse(JSONObject res) {
                            filePath = "";
                            fileType = "";

                            if (!res.has("error")) {

                                binding.textSend.setText("");
                                forward = "";
                                try {
                                    GroupMessageSend(total, id, new Encripter(sender).encrypt("Sent New Attachment"),m_id);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                try {
                                    Error error = new Error(res.getString("code"), res.getString("error"));
                                    MethodClass.hasError(application, error, handler);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }, BASE_URL + Constants.SEND_GROUP_MESSAGE, activity.getApplication(), handler, map, filePath, "attachment", binding.progress);

                    handler.post(() -> {
                        Log.e("TAG", "With Attachment: " + filePath);
                    });
                }
            }
        }).start();

    }

    private void leftFGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = PreferenceFile.getUser().getUser();
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("group", id);


                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MethodClass.showStyleToast(activity,"Left from group");
                                //FirebaseMessaging.getInstance().unsubscribeFromTopic(id);
                                groups_viewModel.delete(id);
                                binding.toolbar.back.performClick();
                            }
                        });


                    }
                }, BASE_URL + LEFT_GROUP, application, handler, user.getEmail(), map);

            }
        }).start();

    }

    private void GroupMessageSend(int total, String group_id, String message,String m_id) {
        binding.dismissReplay.performClick();
        binding.dismiss.performClick();
       // SyncChatsByGroup(application, handler, id);
        groups_chat_viewModel.update_status(m_id,"1");


        if (total == 0) {
            SyncGroups(application,handler);
            SyncAllGroupChats(application,handler);
        } else {
            //SyncChatsByGroup(application,handler,id);
        }
        try {
            soundPool.play(success, 1, 1, 1, 0, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String type = "";
        if (total == 0) {
            type = "FGM";
        } else {
            type = "GM";
        }

        Data data = new Data(type, id, message);
        data.setSender(PreferenceFile.getUser().getUser().getId());

        Sender se = new Sender(data, "/topics/"+id);
        MethodClass.SendNotificationOnTopic(se, application,handler);

    }

    private void AddAttachMent() {
        BottomSheetDialog dialog = new BottomSheetDialog(activity, R.style.SheetDialog);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.attachment_layout, null);

        AttachmentLayoutBinding binding1 = AttachmentLayoutBinding.bind(dialogView);


        dialog.setContentView(dialogView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        binding1.layImages.setOnClickListener(view2 -> {
            dialog.dismiss();


        });

        binding1.layPdf.setOnClickListener(view2 -> {
            dialog.dismiss();

        });

        binding1.layDocs.setOnClickListener(view2 -> {
            dialog.dismiss();

        });

        binding1.layVideo.setOnClickListener(view2 -> {
            dialog.dismiss();

        });

        binding1.layCaptureImage.setOnClickListener(view2 -> {
            dialog.dismiss();


        });
        binding1.layCaptureVideo.setOnClickListener(view2 -> {
            dialog.dismiss();


        });


    }
    private void InitPool() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(1)
                    .setAudioAttributes(audioAttributes)
                    .build();

        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        }

        success = soundPool.load(context, R.raw.beep1, 1);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
        Constants.ACTIVE = "";
    }


}