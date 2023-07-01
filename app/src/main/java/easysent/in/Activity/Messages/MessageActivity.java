package easysent.in.Activity.Messages;



import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.BLOCK_USER;
import static easysent.in.Helper.Constants.CATCH_DIR;
import static easysent.in.Helper.Constants.CATCH_DIR2;
import static easysent.in.Helper.ExifUtils.getRotatedBitmap;
import static easysent.in.Helper.SyncData.SyncBlock;
import static easysent.in.Helper.SyncData.SyncThread;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.easy.pickfile.Interface.Onselect;
import com.easy.pickfile.PickFile;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import easysent.in.Adapter.MessageNewAdapter;
import easysent.in.Encription.Encripter;
import easysent.in.Firebase.Data;
import easysent.in.Firebase.Sender;
import easysent.in.Helper.Constants;
import easysent.in.Helper.CustomProgressbar;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.ShareData;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Login.User;
import easysent.in.Room.Block.BlockViewModel;
import easysent.in.Room.Messages.Chats;
import easysent.in.Room.Messages.Message_View_Model;
import easysent.in.Room.Threads.Thread_ViewModel;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;
import easysent.in.databinding.ActivityMessageBinding;
import easysent.in.databinding.AttachmentLayoutBinding;
import easysent.in.databinding.MainToolbarBinding;

public class MessageActivity extends AppCompatActivity {
    ActivityMessageBinding binding;


    String reciver = "";

    Message_View_Model message_view_model;
    private UserVewModel userVewModel;
    private Thread_ViewModel thread_viewModel;
    BlockViewModel blockViewModel;

    String token = "";
    private int total = 0;
    private String thread = "";
    private String filePath = "";
    private String fileType = "";

    boolean isblocked = false;
    boolean isloaded = false;
    public static Handler handler = new Handler();
    private ItemTouchHelper.SimpleCallback call_tocach;
    private String forward = "";
    private String[] storage_permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static MainToolbarBinding mainToolbarBinding;
    private Bitmap seleted_bitmap = null;
    private String reciver_name = "";
    Context context;
    Activity activity;
    PickFile pickFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        context = this;
        pickFile = new PickFile(this,handler);

        if (getIntent() != null) {
            reciver = getIntent().getStringExtra("reciver");
            if (reciver == null) {
                onBackPressed();
            }
        }
        userVewModel = ViewModelProviders.of(this).get(UserVewModel.class);
        thread_viewModel = ViewModelProviders.of(this).get(Thread_ViewModel.class);
        blockViewModel = new BlockViewModel(this.getApplication());
        message_view_model = ViewModelProviders.of(this).get(Message_View_Model.class);

        mainToolbarBinding = MainToolbarBinding.bind(binding.toolbar.getRoot());
        mainToolbarBinding.back.setVisibility(View.VISIBLE);
        mainToolbarBinding.menu.setVisibility(View.GONE);
        mainToolbarBinding.menuMessage.setVisibility(View.VISIBLE);


        MessageNewAdapter adapter = new MessageNewAdapter(activity, handler, userVewModel, message_view_model, binding.recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(false);
        binding.recycler.setLayoutManager(linearLayoutManager);
        binding.recycler.setNestedScrollingEnabled(false);
        binding.recycler.setHasFixedSize(true);
        binding.recycler.setAdapter(adapter);

        message_view_model.getChat_By_Paged(reciver, PreferenceFile.getUser().getUser().getId()).observe(this, new Observer<PagingData<Chats>>() {
            @Override
            public void onChanged(PagingData<Chats> chatsPagingData) {
                Log.e("TAG", "onChanged: ");
                adapter.submitData(getLifecycle(), chatsPagingData);
                String TAG = "NEW_ITEM";
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        Log.e(TAG, "onItemRangeInserted: start" + positionStart + " icount " + itemCount);

                        binding.recycler.smoothScrollToPosition(positionStart + itemCount);
                        total = adapter.snapshot().size();
                        thread = adapter.snapshot().get(0).getThread();

                        thread_viewModel.UpdateUnread(thread);
                    }

                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount) {
                        super.onItemRangeChanged(positionStart, itemCount);
                        Log.e(TAG, "onItemRangeChanged: start" + positionStart + " icount " + itemCount);


                    }


                });


            }
        });
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
                Chats chats = adapter.snapshot().get(viewHolder.getAbsoluteAdapterPosition());
                if (chats != null)
                    if (chats.getMessage() != null && !chats.getMessage().equals("")) {
                        binding.layReplayDoc.setVisibility(View.GONE);
                        binding.replayText.setVisibility(View.VISIBLE);
                        binding.replayText.setText(chats.getMessage());
                    } else {
                        if (chats.getType().equalsIgnoreCase("I")) {
                            binding.layReplayDoc.setVisibility(View.VISIBLE);
                            binding.ivReplayAttachmentTitle.setVisibility(View.GONE);
                            binding.replayText.setVisibility(View.GONE);
                            binding.ivReplayDoc.setVisibility(View.VISIBLE);

                            File file = new File(CATCH_DIR2 + "/" + chats.getAttachment());
                            if (file.exists()) {
                                Glide.with(activity).load(file.getPath()).override(150, 150).into(binding.ivReplayDoc);
                            } else {
                                Glide.with(activity).load(BASE_URL + "Attachment/Groups/" + chats.getAttachment()).override(150, 150).into(binding.ivReplayDoc);

                            }


                        } else if (!chats.getType().equalsIgnoreCase("T")) {
                            binding.layReplayDoc.setVisibility(View.VISIBLE);
                            binding.ivReplayAttachmentTitle.setVisibility(View.VISIBLE);
                            binding.ivReplayDoc.setVisibility(View.VISIBLE);
                            binding.replayText.setVisibility(View.GONE);

                            Glide.with(activity).load(R.drawable.ic_files).into(binding.ivReplayDoc);
                            binding.ivReplayAttachmentTitle.setText(chats.getAttachment());
                        }
                    }
                if (chats != null) {
                    forward = chats.getId();
                    if (chats.getSender().equalsIgnoreCase(PreferenceFile.getUser().getUser().getId())) {
                        binding.replaySender.setText("You");
                    } else {
                        Users user = userVewModel.selectUser(chats.getSender());
                        binding.replaySender.setText(user.getName());
                    }
                }

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(call_tocach);
        itemTouchHelper.attachToRecyclerView(binding.recycler);
        OnClick();
        Init();

    }

    private void SendMessage(String sender, String reciver, String message, Activity context, String email, String m_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("sender", sender);
                map.put("reciver", reciver);
                map.put("message", message);
                map.put("m_id", m_id);
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
                        Encripter encripter = new Encripter(sender);
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                        String date = utcFormat.format(new Date());

                        Chats chats = new Chats("", sender, date, m_id, total == 0 ? m_id : thread, encripter.decrepit(message), "T", reciver, "0", forward, "0");
                        message_view_model.insert(chats);
                        thread_viewModel.update_last_seen(encripter.decrepit(message), "0", "T", total == 0 ? m_id : thread, date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    MethodClass.CallHeader(new Response() {
                        @Override
                        public void onResponse(JSONObject res) {

                            if (!res.has("error")) {

                                binding.textSend.setText("");
                                forward = "";
                                String type = "";
                                if (total == 0) {
                                    type = "FM";
                                } else {
                                    type = "M";
                                }
                                Data data = new Data(type, thread, message);
                                data.setSender(PreferenceFile.getUser().getUser().getId());
                                Sender se = new Sender(data, token);
                                MethodClass.SendNotification(se, getApplication(), handler);

                                MessaSent(type, thread, message, total, m_id);

                            }

                        }
                    }, BASE_URL + Constants.SEND_MESSAGE, getApplication(), handler, email, map);
                } else {

                    map.put("type", fileType);
                    try {
                        Encripter encripter = new Encripter(sender);
                        DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                        String date = utcFormat.format(new Date());

                        Chats chats = new Chats(new File(filePath).getName() + "", sender, date, m_id, total == 0 ? m_id : thread, encripter.decrepit(message), fileType, reciver, "0", forward, "0");
                        message_view_model.insert(chats);
                        thread_viewModel.update_last_seen(encripter.decrepit(message), "0", fileType, total == 0 ? m_id : thread, date);
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
                                String type = "";
                                if (total == 0) {
                                    type = "FM";
                                } else {
                                    type = "M";
                                }
                                try {
                                    Data data = new Data(type, thread, new Encripter(sender).encrypt("Sent New Attachment"));
                                    data.setSender(PreferenceFile.getUser().getUser().getId());
                                    Sender se = new Sender(data, token);
                                    MethodClass.SendNotification(se, getApplication(), handler);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                MessaSent(type, thread, message, total, m_id);

                            }

                        }
                    }, BASE_URL + Constants.SEND_MESSAGE, getApplication(), handler, map, filePath, "attachment", binding.progress);

                    handler.post(() -> {
                        Log.e("TAG", "With Attachment: " + filePath);
                    });
                }
            }
        }).start();

    }

    private void MessaSent(String type, String thread, String message, int total, String m_id) {
        binding.dismissReplay.performClick();
        binding.dismiss.performClick();
        if (total == 1) {
            SyncThread(getApplication(), handler);
            //SyncChat(getApplication(), handler);
        } else {
            //SyncChatOFThread(thread, getApplication(), handler);
        }

        message_view_model.update_seen("1", m_id);
    }

    private void Init() {
        userVewModel.selectUserLive(reciver).observe(this, new Observer<Users>() {
            @Override
            public void onChanged(Users user) {
                updateImage(user.getProfilePic(), user.getName());
                reciver_name = user.getName();
                binding.btnSend.setOnClickListener(view14 -> {
                    String mid = Calendar.getInstance().getTimeInMillis() + "";
                    if (filePath.equals("") && binding.textSend.getText().toString().trim().equals("")) {
                        Toast.makeText(activity, "You Can't Send Empty Message", Toast.LENGTH_SHORT).show();
                    } else {
                        Encripter encripter = new Encripter(PreferenceFile.getUser().getUser().getId());

                        try {
                            String message = encripter.encrypt(binding.textSend.getText().toString().trim());
                            String sender = PreferenceFile.getUser().getUser().getId();
                            String email = PreferenceFile.getUser().getUser().getEmail();
                            if (fileType.equalsIgnoreCase("II")) {
                                CustomProgressbar.showProgressBar(activity, false);
                                MethodClass.cashattachmentImage2(seleted_bitmap, mid, handler, activity, new AllInterFace() {
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
                                            SendMessage(sender, reciver, message, activity, email, mid);
                                        }

                                    }
                                });
                                CustomProgressbar.hideProgressBar();

                            } else if (fileType.equalsIgnoreCase("P") || fileType.equalsIgnoreCase("V") || fileType.equalsIgnoreCase("D")) {

                                CustomProgressbar.showProgressBar(activity, false);
                                MethodClass.cashattachmentFILE2(new File(filePath), mid, handler, activity, new AllInterFace() {
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
                                            SendMessage(sender, reciver, message, activity, email, mid);
                                        }

                                    }
                                });
                                CustomProgressbar.hideProgressBar();


                            } else {
                                SendMessage(sender, reciver, message, activity, email, mid);
                            }
                            binding.textSend.setText("");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                ;
                binding.attach.setOnClickListener(view1 -> {
                    AddAttachMent();

                });


                if (user != null) {
                    token = user.getToken();
                    mainToolbarBinding.name.setText(user.getName());
                    mainToolbarBinding.email.setText(user.getFstatus());

                    if (user.getProfilePic() != null && !user.getProfilePic().equals("null")) {
                        Glide.with(activity).load(BASE_URL + "profile_image/" + user.getProfilePic()).thumbnail(0.05f).transition(DrawableTransitionOptions.withCrossFade()).addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                mainToolbarBinding.profileImage.setImageDrawable(activity.getResources().getDrawable(MethodClass.getResId(user.getName(), R.drawable.class)));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                mainToolbarBinding.profileImage.setImageDrawable(resource);
                                return false;
                            }
                        }).into(mainToolbarBinding.profileImage);
                    } else {
                        mainToolbarBinding.profileImage.setImageDrawable(activity.getResources().getDrawable(MethodClass.getResId(user.getName(), R.drawable.class)));
                    }

                } else {
                    mainToolbarBinding.back.performClick();
                    Toast.makeText(activity, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }


            }
        });


        if (pickFile != null) pickFile.setOnselect(new Onselect() {
            @Override
            public void onSelect(String... strings) {

                String path = strings != null ? strings[1] : null;
                String ty = strings != null ? strings[0] : null;
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


                        if (type.equalsIgnoreCase(".png") || type.equalsIgnoreCase(".JPEG") || type.equalsIgnoreCase(".JPG")) {
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
                                        FutureTarget<Bitmap> futureTarget = Glide.with(activity).asBitmap().override(600, 600).load(file.getAbsolutePath()).submit();

                                        try {
                                            Bitmap bi = futureTarget.get();
                                            handler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Glide.with(activity).load(bi).into(binding.ivDoc);
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
        });

        checkShare();
    }

    private void checkShare() {
        ShareData shareData = PreferenceFile.getShare();
        if (shareData == null) {
            return;
        }

        String fileType = shareData.getType();
        String msg = shareData.getMessage();
        String fi = shareData.getFile();

        PreferenceFile.setShare(new ShareData());
        if (fileType != null && !fileType.equalsIgnoreCase("")) {
            if (fileType.equalsIgnoreCase("T")) {
                binding.textSend.setText(msg);
            } else if (fileType.equalsIgnoreCase("I")) {
                binding.textSend.setText(msg);

                File file = new File(fi);
                try {
                    if (file.exists()) {

                        seleted_bitmap = getRotatedBitmap(activity, fi);
                        filePath = fi;
                        binding.ivAttachment.setImageBitmap(seleted_bitmap);
                        binding.idTvAttachment.setText(file.getName());
                        this.fileType = "I";

                        binding.ivAttachment.setVisibility(View.VISIBLE);
                        binding.layAttach.setVisibility(View.VISIBLE);
                        binding.layDoc.setVisibility(View.GONE);

                    } else {
                        Toast.makeText(MessageActivity.this, "not exist file", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void OnClick() {

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
            forward = "";
            TransitionManager.beginDelayedTransition((ViewGroup) binding.layReplay.getParent(), transition);
            binding.layReplay.setVisibility(View.GONE);
            binding.layReplayDoc.setVisibility(View.GONE);


        });

        binding.toolbar.back.setOnClickListener(view -> onBackPressed());
        binding.toolbar.menuMessage.setOnClickListener(view -> MethodClass.show_popup_menu(mainToolbarBinding.menuMessage, activity, isblocked, res -> {

            if (res == R.id.menu_background) {

            } else if (res == R.id.menu_block) {

                Block("B");


            } else if (res == R.id.menu_un_block) {
                Block("U");
            } else if (res == R.id.menu_report) {
                MethodClass.report(activity, new AllInterFace() {
                    @Override
                    public void isClicked(boolean is) {
                        super.isClicked(is);

                        if (is) {
                            Block("B");
                        }

                        Toast.makeText(context, "Report Submitted", Toast.LENGTH_SHORT).show();
                    }
                }, reciver_name);
            }


        }));
        binding.toolbar.container.setOnClickListener(view -> {

        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void updateImage(String path, String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (PreferenceFile.isLogged()) {


                    if (path != null && !path.equals("null")) {


                        File file = new File(CATCH_DIR + "/" + path);
                        if (file.exists()) {
                            String url = url = CATCH_DIR + "/" + path;
                            Bitmap bitmap = BitmapFactory.decodeFile(MethodClass.getRightAngleImage(url));
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mainToolbarBinding.profileImage.setImageBitmap(bitmap);
                                }
                            });

                        } else {

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(activity).load(BASE_URL + "profile_image/" + path).thumbnail(0.05f).transition(DrawableTransitionOptions.withCrossFade()).addListener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            mainToolbarBinding.profileImage.setImageDrawable(activity.getResources().getDrawable(MethodClass.getResId(name, R.drawable.class)));
                                            return true;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            mainToolbarBinding.profileImage.setImageDrawable(resource);
                                            return false;
                                        }
                                    }).into(mainToolbarBinding.profileImage);
                                }
                            });
                        }

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mainToolbarBinding.profileImage.setImageDrawable(activity.getResources().getDrawable(MethodClass.getResId(name, R.drawable.class)));

                            }
                        });
                    }
                }
            }
        }).start();

    }

    public void Block(String type) {
        User user = PreferenceFile.getUser().getUser();
        HashMap<String, Object> map = new HashMap<>();
        map.put("from_user", user.getId());
        map.put("to_user", reciver);
        map.put("type", type);
        map.put("thread", thread);

        MethodClass.CallHeader2(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                SyncBlock(getApplication(), handler);
                Data data = new Data("BLOCK", PreferenceFile.getUser().getUser().getId(), type);
                Sender se = new Sender(data, token);
                MethodClass.SendNotification(se, getApplication(), handler);

            }
        }, BASE_URL + BLOCK_USER, getApplication(), handler, user.getEmail(), map);

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
            pickFile.PickImage(false);


        });

        binding1.layPdf.setOnClickListener(view2 -> {
            dialog.dismiss();
            pickFile.PickPDF();


        });

        binding1.layDocs.setOnClickListener(view2 -> {
            dialog.dismiss();
            pickFile.PickDoc();

        });

        binding1.layVideo.setOnClickListener(view2 -> {
            dialog.dismiss();
            pickFile.PickVideo();


        });

        binding1.layCaptureImage.setOnClickListener(view2 -> {
            dialog.dismiss();


        });
        binding1.layCaptureVideo.setOnClickListener(view2 -> {
            dialog.dismiss();


        });


    }


}