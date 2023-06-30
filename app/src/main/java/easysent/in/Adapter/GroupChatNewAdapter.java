package easysent.in.Adapter;

import static easysent.in.Helper.Constants.Attachment_URL;
import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR2;
import static easysent.in.Helper.MethodClass.CashImage2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.FileProvider;
import androidx.paging.ItemSnapshotList;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;


import easysent.in.BuildConfig;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SaveWithProgress;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.OnMenuItemClick;
import easysent.in.R;
import easysent.in.Room.GroupChat.Group_Chat;
import easysent.in.Room.GroupChat.Groups_chat_ViewModel;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;

/**
 * Created by Somnath nath on 22,May,2023
 * Artix Development,
 * India.
 */

public class GroupChatNewAdapter extends PagingDataAdapter<Group_Chat, GroupChatNewAdapter.ViewHolder> {
    Handler handler;
    String my_id;
    UserVewModel userVewModel;
    Groups_chat_ViewModel groups_chat_viewModel;
    Context context;
    Activity activity;
    RecyclerView recyclerView;

    public GroupChatNewAdapter(Activity activity, Handler handler, UserVewModel userVewModel, Groups_chat_ViewModel groups_chat_viewModel,
                               RecyclerView recyclerView) {
        super(Diff_async);
        this.activity = activity;
        this.handler = handler;
        this.groups_chat_viewModel = groups_chat_viewModel;
        this.userVewModel = userVewModel;
        this.recyclerView = recyclerView;
        my_id = PreferenceFile.getUser().getUser().getId();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private static final DiffUtil.ItemCallback<Group_Chat> Diff_async = new DiffUtil.ItemCallback<Group_Chat>() {
        @Override
        public boolean areItemsTheSame(@NonNull Group_Chat oldItem, @NonNull Group_Chat newItem) {

            boolean b = oldItem.getRid() == newItem.getRid();
            Log.e("TAG", "submitData: " + "areItemsTheSame- " + b);
            return b;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Group_Chat oldItem, @NonNull Group_Chat newItem) {

            boolean b = oldItem.getIsDeleted().equalsIgnoreCase(newItem.getIsDeleted())
                    && oldItem.getSeen()== newItem.getSeen();
            Log.e("TAG", "submitData: " + "areContentsTheSame- " + b);
            return b;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        Group_Chat item = getItem(viewType);
        int res;
        if (item != null) {
            if (item.getSender().equalsIgnoreCase(my_id)) {

                switch (item.getType()) {
                    case "T":
                        if (item.getReplayOf() != null &&
                                !item.getReplayOf().equalsIgnoreCase("null") &&
                                !item.getReplayOf().equalsIgnoreCase("")) {
                            res = R.layout.chat_right_m;
                        } else {
                            res = R.layout.chat_right_t;
                        }
                        break;
                    case "I":
                        if (item.getReplayOf() != null &&
                                !item.getReplayOf().equalsIgnoreCase("null") &&
                                !item.getReplayOf().equalsIgnoreCase("")) {
                            res = R.layout.chat_right_m;
                        } else {
                            res = R.layout.chat_right_i;
                        }
                        break;
                    default:
                        res = R.layout.chat_right_m;
                        break;
                }

            } else {

                switch (item.getType()) {
                    case "T":
                        if (item.getReplayOf() != null &&
                                !item.getReplayOf().equalsIgnoreCase("null") &&
                                !item.getReplayOf().equalsIgnoreCase("")) {
                            res = R.layout.chat_left_m;
                        } else {
                            res = R.layout.chat_left_t;
                        }

                        break;
                    case "I":
                        if (item.getReplayOf() != null &&
                                !item.getReplayOf().equalsIgnoreCase("null") &&
                                !item.getReplayOf().equalsIgnoreCase("")) {
                            res = R.layout.chat_left_m;
                        } else {
                            res = R.layout.chat_left_i;
                        }
                        break;
                    default:
                        res = R.layout.chat_left_m;
                        break;
                }


            }
        } else {
            res = R.layout.chat_shimmer;
        }


        View view = LayoutInflater.from(parent.getContext()).inflate(res, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        final int position = pos;
        Log.e("SOMNATH", "onBindViewHolder: " + position + " size-" + this.snapshot().size());

        final String[] time = {""};
        final String[] message = {""};
        final String[] is_forowoard = {String.valueOf(false)};

        final Users[] user = new Users[1];

        Group_Chat item = getItem(position);

        if (item != null)  {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    time[0] = MethodClass.changeDateFormat2(item.getCreatedAt());
                    if (item.getSender().equalsIgnoreCase(my_id)) {
                    //todo
                    } else {
                        user[0] = userVewModel.selectUser(item.getSender());
                    }

                    message[0] = item.getMessage();

                    is_forowoard[0] = item.getReplayOf();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                holder.mesage.setText(message[0]);
                                holder.time.setText(time[0]);


                                if (!item.getType().equalsIgnoreCase("T") && !item.getType().equalsIgnoreCase("I")) {
                                    holder.iv_download.setOnClickListener(view -> {
                                        Download_file(item, holder.iv_download, holder.iv_download_progress, position);
                                    });
                                }

                                holder.itemView.setOnLongClickListener(view -> {ShowPopupMenu(holder.main_lay,item);
                                return true;});

                                if (!item.getType().equalsIgnoreCase("T") &&
                                        !item.getType().equalsIgnoreCase("I") &&
                                        !item.getType().equalsIgnoreCase("V")) {
                                    holder.lay_doc.setOnClickListener(view -> {
                                        File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
                                        if (file.exists()) {

                                            /// if file already downloaded

                                            //Uri path = Uri.fromFile(file);
                                            Uri path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                                            Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                                            pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            pdfOpenintent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            pdfOpenintent.setData(path);
                                            try {
                                                context.startActivity(pdfOpenintent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            ///

                                        } else {
                                            holder.iv_download.performClick();
                                        }
                                    });
                                }

                                if (item.getType().equalsIgnoreCase("T")) {
//todo
                                } else if (item.getType().equalsIgnoreCase("I")) {
                                    AttachmentImage(item, holder);
                                } else if (item.getType().equalsIgnoreCase("V")) {
                                    AttachmentVideo(item, holder);
                                } else if (item.getType().equalsIgnoreCase("P")) {
                                    AttachmentPDF(item, holder);
                                } else {
                                    AttachmentDoc(item, holder);
                                }

                                if (item.getReplayOf() != null &&
                                        !item.getReplayOf().equalsIgnoreCase("null") &&
                                        !item.getReplayOf().equalsIgnoreCase("")) {
                                    Hasreplay(item, holder);
                                }


                                if (!item.getSender().equalsIgnoreCase(my_id)) {
                                    holder.sender.setText(user[0].getName());
                                }else{
                                    if (item.getSeen()==1){
                                        holder.status.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_check));
                                    }else{
                                        holder.status.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_clock));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }).start();
        }


    }

    private void Hasreplay(Group_Chat item, ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Group_Chat repla = groups_chat_viewModel.selectChat(item.getReplayOf());
                if (repla!=null) {
                    Users sender = userVewModel.selectUser(repla.getSender());
                    String name = sender.getId().equalsIgnoreCase(my_id) ? "You" : sender.getName();
                    int posis = findPosi(GroupChatNewAdapter.this.snapshot(), repla);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            holder.replay_sender.setText(name);
                            holder.replay_text.setText(repla.getMessage());
                            if (repla.getType().equalsIgnoreCase("T")) {

                            } else if (repla.getType().equalsIgnoreCase("I") && (repla.getMessage() == null || repla.getMessage().equalsIgnoreCase("") || repla.getMessage().equalsIgnoreCase("null"))) {
                                holder.replay_text.setVisibility(View.GONE);
                                File file = new File(CATCH_DIR2 + "/" + repla.getAttachment());
                                if (file.exists()) {
                                    Glide.with(context).load(file.getAbsolutePath())
                                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.iv_replay_doc);

                                } else {
                                    Glide.with(context).load(BASE_URL + "Attachment/Groups/" + repla.getAttachment())
                                            .thumbnail(0.05f)
                                            .transition(DrawableTransitionOptions.withCrossFade())
                                            .addListener(new RequestListener<Drawable>() {
                                                @SuppressLint("UseCompatLoadingForDrawables")
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    holder.iv_replay_doc.setImageDrawable(context.getDrawable(R.drawable.ic_x));
                                                    return true;
                                                }

                                                @Override
                                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                                    holder.iv_replay_doc.setImageDrawable(resource);
                                                    return false;
                                                }
                                            })
                                            .into(holder.iv_replay_doc);
                                }
                                holder.iv_replay_attachment_title.setVisibility(View.GONE);
                                holder.lay_replay_doc.setVisibility(View.VISIBLE);
                            } else if (repla.getType().equalsIgnoreCase("V") && (repla.getMessage() == null || repla.getMessage().equalsIgnoreCase("") || repla.getMessage().equalsIgnoreCase("null"))) {
                                holder.replay_text.setVisibility(View.GONE);
                                if (repla.getAttachment() != null && !repla.getAttachment().equals("null")) {
                                    File file = new File(CATCH_DIR2 + "/" + repla.getAttachment());


                                    if (file.exists()) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                FutureTarget<Bitmap> futureTarget = Glide.with(context)
                                                        .asBitmap()
                                                        .override(300, 300)
                                                        .load(file.getAbsolutePath())
                                                        .submit();
                                                try {
                                                    Bitmap bitmap = futureTarget.get();

                                                    handler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Glide.with(context).load(bitmap).override(300, 300).into(holder.iv_replay_doc);


                                                        }
                                                    });

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();

                                        holder.iv_replay_attachment_title.setVisibility(View.GONE);
                                    } else {
                                        holder.iv_replay_attachment_title.setText(repla.getAttachment());
                                    }
                                    holder.lay_replay_doc.setVisibility(View.VISIBLE);
                                }
                            } else if (repla.getType().equalsIgnoreCase("P") || repla.getType().equalsIgnoreCase("P") && (repla.getMessage() == null || repla.getMessage().equalsIgnoreCase("") || repla.getMessage().equalsIgnoreCase("null"))) {
                                holder.replay_text.setVisibility(View.GONE);
                                holder.iv_replay_attachment_title.setText(repla.getAttachment());
                                holder.lay_replay_doc.setVisibility(View.VISIBLE);
                            }


                            // Log.e("SOMNATH", "run: "+repla );

                            if (posis != -1) {
                                holder.lay_replay.setOnClickListener(view -> {
                                    recyclerView.smoothScrollToPosition(posis);
                                    ViewHolder viewHolder = (ViewHolder) recyclerView.findViewHolderForAdapterPosition(posis);

                                    if (viewHolder != null) {
                                        MethodClass.chengeBackground(viewHolder.main_lay);
                                    }
                                });

                            }
                            holder.lay_replay.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }).start();

    }

    private int findPosi(ItemSnapshotList<Group_Chat> snapshot, Group_Chat repla) {
        int posi = -1;

        for (int i = 0; i < snapshot.size(); i++) {

            if (repla.getId().equalsIgnoreCase(snapshot.get(i).getId())) {
                posi = i;
                return posi;
            }

        }
        return posi;
    }


    private void Download_file(Group_Chat item, ImageView iv_download, ProgressBar iv_download_progress, int position) {

        iv_download.setVisibility(View.GONE);
        iv_download_progress.setVisibility(View.VISIBLE);
        //MethodClass.downloadVideo(Attachment_URL + item.getAttachment(), context, item.getAttachment());


        File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
        SaveWithProgress saveWithProgress = new SaveWithProgress(Attachment_URL + "/Groups/" + item.getAttachment(), file, iv_download_progress, handler);
        saveWithProgress.save();
        saveWithProgress.setAllInterface(new AllInterFace() {
            @Override
            public void isClicked(boolean is) {
                super.isClicked(is);
                if (is) {
                    GroupChatNewAdapter.this.notifyItemChanged(position);
                }
            }
        });


    }

    private void AttachmentDoc(Group_Chat item, ViewHolder holder) {
        holder.lay_doc.setVisibility(View.VISIBLE);
        holder.iv_attachment_title.setText(item.getAttachment());
        holder.iv_doc.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_files));
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {
            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());


            if (file.exists()) {
                holder.iv_download.setVisibility(View.GONE);
            }
        }
    }

    private void AttachmentPDF(Group_Chat item, ViewHolder holder) {
        holder.lay_doc.setVisibility(View.VISIBLE);
        holder.iv_attachment_title.setText(item.getAttachment());
        holder.iv_doc.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_pdf));
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {
            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
            if (file.exists()) {
                holder.iv_download.setVisibility(View.GONE);
            }
        }
    }

    private void AttachmentVideo(Group_Chat item, ViewHolder holder) {
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {
            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());


            if (file.exists()) {
                holder.iv_attachment.setVisibility(View.VISIBLE);
                holder.lay_attachment.setVisibility(View.VISIBLE);
                holder.btn_play.setVisibility(View.VISIBLE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FutureTarget<Bitmap> futureTarget = Glide.with(context)
                                .asBitmap()
                                .override(600, 600)
                                .load(file.getAbsolutePath())
                                .submit();
                        try {
                            Bitmap bitmap = futureTarget.get();

                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(context).load(bitmap).override(600, 600).into(holder.iv_attachment);
                                    holder.iv_attachment.setVisibility(View.VISIBLE);
                                    holder.lay_attachment.setVisibility(View.VISIBLE);

                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            } else {

                holder.lay_doc.setVisibility(View.VISIBLE);
                holder.iv_download.setVisibility(View.VISIBLE);
                holder.iv_attachment_title.setText(item.getAttachment());
                holder.iv_doc.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_video));

            }

            holder.iv_attachment.setOnClickListener(view -> {
                MethodClass.showFullScreenGroup(activity, handler, item.getGroupId(), item.getId());
            });


        }
    }
    private void ShowPopupMenu(LinearLayout chatMe, Group_Chat item) {

        MethodClass.show_popup_menu2(chatMe, activity, new OnMenuItemClick() {
            @Override
            public void OnClick(int res) {
                if (res == R.id.menu_delete) {
                    if (item.getSender().equalsIgnoreCase(my_id)) {
                        groups_chat_viewModel.update_deleted(item.getId());
                    } else {
                        groups_chat_viewModel.update_deleted(item.getId());
                    }

                }
                else if (res == R.id.menu_foroward) {

                 /*   Intent intent = new Intent(activity, ShareActivity.class);
                    intent.putExtra("from", "forowrd");
                    intent.putExtra("type", item.getType());
                    intent.putExtra("msg", item.getMessage() == null ? "" : item.getMessage());
                    if (item.getAttachment() != null) {
                        intent.putExtra("file", CATCH_DIR2 + "/" + item.getAttachment());
                    } else {
                        intent.putExtra("file", "");
                    }

                    context.startActivity(intent);*/
                }
                else if (res == R.id.menu_report) {
                    MethodClass.report(activity, new AllInterFace() {
                        @Override
                        public void isClicked(boolean is) {
                            super.isClicked(is);

                            if (is) {

                            }

                            Toast.makeText(context, "Report Submitted", Toast.LENGTH_SHORT).show();
                        }
                    }, "This Group");
                }
            }
        });


    }

    private void AttachmentImage(Group_Chat item, ViewHolder holder) {
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {

            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
            if (file.exists()) {
                Glide.with(context).load(file.getAbsolutePath())
                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).into(holder.iv_attachment);

            } else {
                Glide.with(context).load(BASE_URL + "Attachment/Groups/" + item.getAttachment())
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .addListener(new RequestListener<Drawable>() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.iv_attachment.setImageDrawable(context.getDrawable(R.drawable.ic_x));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                holder.iv_attachment.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(holder.iv_attachment);
                CashImage2(BASE_URL + "Attachment/Groups/" + item.getAttachment(), item.getAttachment());
            }
            holder.iv_attachment.setVisibility(View.VISIBLE);
            holder.lay_attachment.setVisibility(View.VISIBLE);

            holder.iv_attachment.setOnClickListener(view -> {
                MethodClass.showFullScreenGroup(activity, handler, item.getGroupId(), item.getId());
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mesage, time, sender, iv_attachment_title, replay_sender, replay_text, iv_replay_attachment_title;
        LinearLayout main_lay, lay_doc, lay_replay, lay_replay_doc;
        RelativeLayout lay_attachment;
        ImageView iv_download, iv_doc, btn_play, iv_attachment, iv_replay_doc,status;
        ProgressBar iv_download_progress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            main_lay = itemView.findViewById(R.id.main_lay);

            mesage = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time);
            sender = itemView.findViewById(R.id.sender);

            iv_attachment_title = itemView.findViewById(R.id.iv_attachment_title);
            lay_doc = itemView.findViewById(R.id.lay_doc);
            lay_attachment = itemView.findViewById(R.id.lay_attachment);
            iv_download = itemView.findViewById(R.id.iv_download);
            iv_doc = itemView.findViewById(R.id.iv_doc);
            btn_play = itemView.findViewById(R.id.btn_play);
            iv_attachment = itemView.findViewById(R.id.iv_attachment);
            iv_download_progress = itemView.findViewById(R.id.iv_download_progress);
            status = itemView.findViewById(R.id.status);

            replay_sender = itemView.findViewById(R.id.replay_sender);
            replay_text = itemView.findViewById(R.id.replay_text);
            iv_replay_attachment_title = itemView.findViewById(R.id.iv_replay_attachment_title);
            lay_replay = itemView.findViewById(R.id.lay_replay);
            lay_replay_doc = itemView.findViewById(R.id.lay_replay_doc);
            iv_replay_doc = itemView.findViewById(R.id.iv_replay_doc);


        }
    }
}
