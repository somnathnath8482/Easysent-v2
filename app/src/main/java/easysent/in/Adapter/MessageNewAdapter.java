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
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.paging.ItemSnapshotList;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.concurrent.ExecutionException;

import easysent.in.BuildConfig;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SaveWithProgress;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.OnMenuItemClick;
import easysent.in.R;
import easysent.in.Room.Messages.Chats;
import easysent.in.Room.Messages.Message_View_Model;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;

/**
 * Created by Somnath nath on 08,June,2023
 * Artix Development,
 * India.
 */
public class MessageNewAdapter extends PagingDataAdapter<Chats, MessageNewAdapter.ViewHolder> {
    private String my_id = "";
    Activity activity;
    Context context;
    UserVewModel userVewModel;
    Handler handler;
    Message_View_Model message_view_model;
    RecyclerView recycler;

    public MessageNewAdapter(Activity activity, Handler handler, UserVewModel userVewModel, Message_View_Model message_view_model, RecyclerView recycler) {
        super(Diff_callback);
        this.activity = activity;
        this.context = activity;
        this.handler = handler;
        this.userVewModel = userVewModel;
        this.message_view_model = message_view_model;
        this.recycler = recycler;
        my_id = PreferenceFile.getUser().getUser().getId();
    }

    private static final DiffUtil.ItemCallback<Chats> Diff_callback = new DiffUtil.ItemCallback<Chats>() {
        @Override
        public boolean areItemsTheSame(@NonNull Chats oldItem, @NonNull Chats newItem) {
            boolean b = oldItem.getRid() == newItem.getRid();
            return b;
        }


        @Override
        public boolean areContentsTheSame(@NonNull Chats oldItem, @NonNull Chats newItem) {
            boolean b = oldItem.getRid() == newItem.getRid() && oldItem.getSeen().equalsIgnoreCase(newItem.getSeen())
                    && oldItem.getIs_deleted().equalsIgnoreCase(newItem.getIs_deleted());
            return b;
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Chats item = getItem(viewType);

        int res;
        if (item != null) {
            if (item.getSender().equalsIgnoreCase(my_id)) {

                switch (item.getType()) {
                    case "T":
                        if (item.getReplay_of() != null &&
                                !item.getReplay_of().equalsIgnoreCase("null") &&
                                !item.getReplay_of().equalsIgnoreCase("")) {
                            res = R.layout.chat_right_m;
                        } else {
                            res = R.layout.chat_right_t;
                        }
                        break;
                    case "I":
                        if (item.getReplay_of() != null &&
                                !item.getReplay_of().equalsIgnoreCase("null") &&
                                !item.getReplay_of().equalsIgnoreCase("")) {
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
                        if (item.getReplay_of() != null &&
                                !item.getReplay_of().equalsIgnoreCase("null") &&
                                !item.getReplay_of().equalsIgnoreCase("")) {
                            res = R.layout.chat_left_m;
                        } else {
                            res = R.layout.chat_left_t;
                        }

                        break;
                    case "I":
                        if (item.getReplay_of() != null &&
                                !item.getReplay_of().equalsIgnoreCase("null") &&
                                !item.getReplay_of().equalsIgnoreCase("")) {
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


        View view = LayoutInflater.from(activity).inflate(res, parent, false);
        ViewHolder hol = new ViewHolder(view);
        return hol;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        final int position = pos;
        Log.e("SOMNATH", "onBindViewHolder: " + position + " size-" + this.snapshot().size());

        final String[] time = {""};
        final String[] message = {""};
        final String[] is_forowoard = {String.valueOf(false)};

        final Users[] user = new Users[1];


        Chats item = getItem(position);
        if (item != null) {
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

                    is_forowoard[0] = item.getReplay_of();
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

                                holder.itemView.setOnLongClickListener(view -> {
                                    ShowPopupMenu(holder.main_lay, item);
                                    return true;
                                });

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

                                if (item.getReplay_of() != null &&
                                        !item.getReplay_of().equalsIgnoreCase("null") &&
                                        !item.getReplay_of().equalsIgnoreCase("")) {
                                    Hasreplay(item, holder);
                                }


                                if (!item.getSender().equalsIgnoreCase(my_id)) {
                                    holder.sender.setText(user[0].getName());
                                } else {


                                    if (item.getSeen().equals("2")) {
                                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_check));
                                        holder.status.setColorFilter(null);
                                        holder.status.setColorFilter(ContextCompat.getColor(context, R.color.white));
                                    } else if (item.getSeen().equals("1")) {
                                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_check));
                                        holder.status.setColorFilter(null);
                                        holder.status.setColorFilter(ContextCompat.getColor(context, R.color.white));
                                    } else if (item.getSeen().equals("0")) {
                                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_clock));
                                        holder.status.setColorFilter(null);
                                        holder.status.setColorFilter(ContextCompat.getColor(context, R.color.thim_color));
                                    } else {
                                        holder.status.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_double_check));
                                        holder.status.setColorFilter(null);
                                        holder.status.setColorFilter(ContextCompat.getColor(context, R.color.thim_color));
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


    private void Hasreplay(Chats item, ViewHolder holder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Chats repla = message_view_model.selectChat(item.getReplay_of());
                if (repla != null) {
                    Users sender = userVewModel.selectUser(repla.getSender());
                    String name = sender.getId().equalsIgnoreCase(my_id) ? "You" : sender.getName();
                    int posis = findPosi(MessageNewAdapter.this.snapshot(), repla);
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
                                    Glide.with(context).load(BASE_URL + "Attachment/" + repla.getAttachment())
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
                                    recycler.smoothScrollToPosition(posis);
                                    ViewHolder viewHolder = (ViewHolder) recycler.findViewHolderForAdapterPosition(posis);

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

    private int findPosi(ItemSnapshotList<Chats> snapshot, Chats repla) {
        int posi = -1;

        for (int i = 0; i < snapshot.size(); i++) {

            if (repla.getId().equalsIgnoreCase(snapshot.get(i).getId())) {
                posi = i;
                return posi;
            }

        }
        return posi;
    }


    private void Download_file(Chats item, ImageView iv_download, ProgressBar iv_download_progress, int position) {

        iv_download.setVisibility(View.GONE);
        iv_download_progress.setVisibility(View.VISIBLE);
        //MethodClass.downloadVideo(Attachment_URL + item.getAttachment(), context, item.getAttachment());


        File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
        SaveWithProgress saveWithProgress = new SaveWithProgress(Attachment_URL + "/" + item.getAttachment(), file, iv_download_progress, handler);
        saveWithProgress.save();
        saveWithProgress.setAllInterface(new AllInterFace() {
            @Override
            public void isClicked(boolean is_success) {
                super.isClicked(is_success);
                if (is_success) {
                    MessageNewAdapter.this.notifyItemChanged(position);
                }
            }
        });


    }

    private void AttachmentDoc(Chats item, ViewHolder holder) {
        holder.lay_doc.setVisibility(View.VISIBLE);
        holder.iv_attachment_title.setText(item.getAttachment());
        holder.iv_doc.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_files));
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {
            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());


            if (file.exists()) {
                holder.iv_download.setVisibility(View.GONE);
            }
        }
    }

    private void AttachmentPDF(Chats item, ViewHolder holder) {
        holder.lay_doc.setVisibility(View.VISIBLE);
        holder.iv_attachment_title.setText(item.getAttachment());
        holder.iv_doc.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_pdf));
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {
            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
            if (file.exists()) {
                holder.iv_download.setVisibility(View.GONE);
            }
        }
    }

    private void AttachmentVideo(Chats item, ViewHolder holder) {
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
                holder.iv_doc.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ic_video));

            }

            holder.iv_attachment.setOnClickListener(view -> {
                MethodClass.showFullScreen(activity, handler, item.getThread(), item.getId());
            });


        }
    }

    private void ShowPopupMenu(LinearLayout chatMe, Chats item) {

        MethodClass.show_popup_menu2(chatMe, activity, new OnMenuItemClick() {
            @Override
            public void OnClick(int res) {
                if (res == R.id.menu_delete) {
                    if (item.getSender().equalsIgnoreCase(my_id)) {
                        message_view_model.update_delete(item.getId(), "1");
                    } else {
                        message_view_model.update_delete(item.getId(), "1");
                    }

                } else if (res == R.id.menu_foroward) {

                    /*Intent intent = new Intent(activity, ShareActivity.class);
                    intent.putExtra("from", "forowrd");
                    intent.putExtra("type", item.getType());
                    intent.putExtra("msg", item.getMessage() == null ? "" : item.getMessage());
                    if (item.getAttachment() != null) {
                        intent.putExtra("file", CATCH_DIR2 + "/" + item.getAttachment());
                    } else {
                        intent.putExtra("file", "");
                    }

                    context.startActivity(intent);*/
                } else if (res == R.id.menu_report) {
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

    private void AttachmentImage(Chats item, ViewHolder holder) {
        if (item.getAttachment() != null && !item.getAttachment().equals("null")) {

            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
            if (file.exists()) {
            new MethodClass.GetFileBitmap(file.getAbsolutePath(),holder.iv_attachment,context).execute();

            } else {
                Glide.with(context).load(BASE_URL + "Attachment/" + item.getAttachment())
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
                CashImage2(BASE_URL + "Attachment/" + item.getAttachment(), item.getAttachment());
            }
            holder.iv_attachment.setVisibility(View.VISIBLE);
            holder.lay_attachment.setVisibility(View.VISIBLE);

            holder.iv_attachment.setOnClickListener(view -> {
                MethodClass.showFullScreen(activity, handler, item.getThread(), item.getId());
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mesage, time, sender, iv_attachment_title, replay_sender, replay_text, iv_replay_attachment_title;
        LinearLayout main_lay, lay_doc, lay_replay, lay_replay_doc;
        RelativeLayout lay_attachment;
        ImageView iv_download, iv_doc, btn_play, iv_attachment, iv_replay_doc, status;
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
