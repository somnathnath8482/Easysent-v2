package easysent.in.Activity.GroupChat;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR;
import static easysent.in.Helper.Constants.LEFT_GROUP;
import static easysent.in.Helper.MethodClass.CashImage;
import static easysent.in.Helper.SyncData.SyncGroups;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import easysent.in.Activity.Messages.MessageActivity;
import easysent.in.Adapter.GroupUserAdapter;
import easysent.in.Firebase.Data;
import easysent.in.Firebase.Sender;
import easysent.in.Helper.Constants;
import easysent.in.Helper.CustomProgressbar;
import easysent.in.Helper.ImageGetter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.GetGroupUser.GetGroupResponse;
import easysent.in.Response.Login.User;
import easysent.in.Room.Groups.Groups;
import easysent.in.Room.Groups.Groups_ViewModel;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.databinding.ActivityGroupDetailsBinding;

public class GroupDetailsActivity extends AppCompatActivity {
    ActivityGroupDetailsBinding binding;
    String id = "0";
    Groups_ViewModel groups_viewModel;
    UserVewModel userVewModel;
    Context context;
    Activity activity;
    Handler handler = new Handler();
    private Bitmap seleted_bitmap = null;
    private String filePath = "";
    GroupUserAdapter groupUserAdapter;
    private Groups groups = null;
    Application application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        context = this;
        activity = this;
        application = activity.getApplication();
        groups_viewModel = ViewModelProviders.of(this).get(Groups_ViewModel.class);
        userVewModel = ViewModelProviders.of(this).get(UserVewModel.class);
        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }


        groupUserAdapter = new GroupUserAdapter(context, handler, "", new AllInterFace() {
            @Override
            public void OnSelect(String... ids) {
                super.OnSelect(ids[0], ids[1], ids[2]);
                if (!PreferenceFile.getUser().getUser().getId().equals(ids[0]) && groups.getCreator().equals(PreferenceFile.getUser().getUser().getId())) {
                    RemoveUser(ids[0], ids[1], ids[2]);

                }

            }


        });


        groups_viewModel.selectgroupLive(id).observe(this, item -> {
            if (item != null) {
                groups = item;
                binding.toolbar.setTitle(item.getName());
                binding.desc.setText(item.getGroupDesc());
                binding.tvDesc.setText(item.getGroupDesc());
                binding.title.setText(item.getName());
                binding.tvTitle.setText(item.getName());
                String date = MethodClass.changeDateFormat(item.getCreatedAt());
                binding.createdAt.setText(date);
                binding.totalUser.setText(item.getUserCount());
                binding.createdBy.setText(MethodClass.getUserName(userVewModel, item.getCreator()));

                if (item.getDp() != null && !item.getDp().equals("null") && !item.getDp().equals("") && !item.getDp().equals(" ")) {


                    File file = new File(CATCH_DIR + "/" + item.getDp());
                    if (file.exists()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // String url  =   url = CATCH_DIR+"/"+item.getProfilePic();
                                // Bitmap bitmap = BitmapFactory.decodeFile(MethodClass.getRightAngleImage(url));
                                //Glide.with(context).load(url).into(holder.binding.profileImage);
                                ImageGetter imageGetter = new ImageGetter(binding.toolbarImage);
                                imageGetter.execute(file);
                            }
                        }).start();


                    } else {


                        Glide.with(context).load(Constants.BASE_URL + "profile_image/" + item.getDp())
                                .thumbnail(0.05f)
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .addListener(new RequestListener<Drawable>() {

                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                                        Drawable d = context.getResources().getDrawable(MethodClass.getResId(item.getName(), R.drawable.class));
                                        binding.toolbar.setCollapseIcon(d);
                                        binding.toolbarImage.setImageDrawable(d);
                                        return true;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                        binding.toolbarImage.setImageDrawable(resource);
                                        binding.toolbar.setCollapseIcon(resource);
                                        CashImage(BASE_URL + "profile_image/" + item.getDp(), item.getDp(), resource);
                                        return false;
                                    }
                                })
                                .into(binding.toolbarImage);
                    }
                } else {
                    Drawable d = context.getResources().getDrawable(MethodClass.getResId(item.getName(), R.drawable.class));
                    binding.toolbar.setCollapseIcon(d);
                    binding.toolbarImage.setImageDrawable(d);
                }

                binding.radio.setOnCheckedChangeListener((radioGroup, i) -> {
                    if (i == R.id.editor) {
                        binding.layView.setVisibility(View.GONE);
                        binding.layEdit.setVisibility(View.VISIBLE);
                        MethodClass.showKeyboard(activity, binding.title);
                        binding.title.requestFocus();
                        binding.btnSubmit.setVisibility(View.VISIBLE);
                    } else if (i == R.id.non_editor) {
                        binding.layView.setVisibility(View.VISIBLE);
                        binding.layEdit.setVisibility(View.GONE);
                        binding.btnSubmit.setVisibility(View.GONE);

                    }
                });

                binding.nonEditor.setChecked(true);

                if (PreferenceFile.isLogged())
                    if (PreferenceFile.getUser().getUser().getId().equalsIgnoreCase(item.getCreator())) {

                        binding.edtDesc.setOnClickListener(view2 -> {
                            if (binding.editor.isChecked()) {
                                binding.nonEditor.setChecked(true);
                            } else {
                                binding.editor.setChecked(true);
                            }

                        });

                        binding.edtImg.setOnClickListener(view2 -> {
                            binding.editor.setChecked(true);
                            //pickFile.pickImage(false);

                        });
                        binding.addUser.setOnClickListener(view2 -> {
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "add");
                            bundle.putString("id", id);
                            Navigation.findNavController(binding.getRoot()).navigate(R.id.createGroupFragment, bundle, null);

                        });

                        binding.btnSubmit.setOnClickListener(view1 -> {

                            HashMap<String, Object> map = new HashMap<>();
                            try {
                                map.put("name", MethodClass.CheckEmpty(binding.title));
                                map.put("desc", MethodClass.CheckEmpty(binding.desc));
                                map.put("group", item.getGroupId());
                                map.put("id", PreferenceFile.getUser().getUser().getId());
                            } catch (Exception e) {
                                return;
                            }


                            if (!filePath.equals("")) {
                                CustomProgressbar.showProgressBar(activity, false);
                                MethodClass.cashattachmentImage(seleted_bitmap, handler, activity, new AllInterFace() {
                                    @Override
                                    public void IsClicked(String s) {
                                        super.IsClicked(s);
                                        if (s == null) {
                                            Toast.makeText(activity, "Unable to sent Picture", Toast.LENGTH_SHORT).show();
                                        } else {

                                            Update(map, s);
                                        }

                                    }
                                });
                                CustomProgressbar.hideProgressBar();

                            } else {
                                Update(map, "");
                            }


                        });


                    } else {
                        binding.edtDesc.setVisibility(View.GONE);
                        binding.edtImg.setVisibility(View.GONE);
                        binding.addUser.setVisibility(View.GONE);

                    }


            }
        });

        binding.back.setOnClickListener(view1 -> {
            onBackPressed();
        });

       /* pickFile.setAllInterface(new AllInterface() {
            @Override
            public void OnSelect(String path) {
                super.OnSelect(path);


                seleted_bitmap = getRotatedBitmap(activity, path);
                binding.toolbarImage.setImageBitmap(seleted_bitmap);
                filePath = path;

            }

            @Override
            public void OnSelect(String type, String path) {
                super.OnSelect(type, path);
            }
        });*/


        getUsers();
        Colaps();
    }

    private void Colaps() {

    }


    private void RemoveUser(String ids, String name, String token) {
        final CharSequence[] items = {"Remove " + name, "Message " + name, "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //builder.setTitle("Select Attachments");
        builder.setItems(items, (dialogInterface, i) -> {

            if (i == 0) {
                leftFGroup(ids, token);
            } else if (i == 1) {

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("reciver", ids);
                context.startActivity(intent);

            } else {
                dialogInterface.dismiss();
            }
        });
        builder.show();

    }

    private void leftFGroup(String ids, String token) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = PreferenceFile.getUser().getUser();
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", ids);
                map.put("group", id);


                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                MethodClass.showStyleToast(activity, "user Removed");
                                getUsers();

                                Data data = new Data("RFG", id, "removed from group");
                                data.setSender(PreferenceFile.getUser().getUser().getId());

                                Sender se = new Sender(data, token);
                                MethodClass.SendNotificationOnTopic(se, application, handler);

                            }
                        });


                    }
                }, BASE_URL + LEFT_GROUP, activity.getApplication(), handler, user.getEmail(), map);

            }
        }).start();

    }

    private void Update(HashMap<String, Object> map, String s) {
        CustomProgressbar.showProgressBar(activity, false);
        new Thread(new Runnable() {
            @Override
            public void run() {

                if (s == null || s.equals("")) {
                    MethodClass.CallHeader(new Response() {
                        @Override
                        public void onResponse(JSONObject res) {
                            filePath = "";
                            handler.post(CustomProgressbar::hideProgressBar);

                            if (!res.has("error")) {

                                SyncGroups(activity.getApplication(), handler);

                            }

                        }
                    }, BASE_URL + Constants.UPDATE_Group, activity.getApplication(), handler, "", map);
                } else {
                    MethodClass.CallMultipart_NO_ProgressBar(new Response() {

                        @Override
                        public void onResponse(JSONObject res) {
                            filePath = "";
                            handler.post(CustomProgressbar::hideProgressBar);
                            if (!res.has("error")) {

                                SyncGroups(activity.getApplication(), handler);

                            }

                        }
                    }, BASE_URL + Constants.UPDATE_Group, activity.getApplication(), handler, map, filePath, "attachment");

                    handler.post(() -> {
                        Log.e("TAG", "With Attachment: " + filePath);
                    });
                }


            }
        }).start();

    }

    private void getUsers() {
        HashMap<String, Object> map = new HashMap<>();

        map.put("group", id);

        new Thread(() -> {
            MethodClass.CallHeader(new Response() {
                @Override
                public void onResponse(JSONObject res) {
                    if (res != null)
                        if (!res.has("error")) {
                            try {
                                Gson gson = new Gson();
                                GetGroupResponse getGroupResponse = gson.fromJson(res.toString(), GetGroupResponse.class);
                                groupUserAdapter.submitList(getGroupResponse.getUsers());
                                binding.recycler.setHasFixedSize(true);
                                binding.recycler.setLayoutManager(new LinearLayoutManager(context));
                                binding.recycler.setAdapter(groupUserAdapter);
                                groupUserAdapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                }
            }, BASE_URL + Constants.GET_GROUP_USERS, activity.getApplication(), handler, PreferenceFile.getUser().getUser().getId(), map);

        }).start();
    }

}