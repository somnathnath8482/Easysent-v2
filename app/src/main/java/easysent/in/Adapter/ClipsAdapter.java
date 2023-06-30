package easysent.in.Adapter;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.SyncData.sync_BY_SQL;

import android.app.Activity;
import android.app.Application;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Helper.TimeAgo2;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.OnMenuItemClick;
import easysent.in.R;
import easysent.in.Response.All_Clips.ClipsItem;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.Room.Users.Users;
import easysent.in.databinding.ClipActionMenueBinding;
import easysent.in.databinding.ClipItemBinding;
import io.github.muddz.styleabletoast.StyleableToast;

public class ClipsAdapter extends RecyclerView.Adapter<ClipsAdapter.ViewHolder> {
    Activity context;
    List<ClipsItem> clips = new ArrayList<>();
    private ViewPager2 viewPager2;
    private VideoView mp;
    List<ViewHolder> playlist = new ArrayList<>();

    AllInterFace allInterFace;
    int play = -1;
    private ViewHolder holder;

    Handler handler;

    public ClipsAdapter(Activity context, List<ClipsItem> clips, ViewPager2 viewPager2, Handler handler) {
        this.context = context;
        this.clips = clips;
        this.viewPager2 = viewPager2;
        this.handler = handler;
    }

    public void setAllInterFace(AllInterFace allInterFace) {
        this.allInterFace = allInterFace;
    }

    public void stopMp(int pos) {
        if (mp != null && mp.isPlaying()) {
            mp.pause();
        }

        try {
            if (playlist != null) {
                ViewHolder viewHolder = playlist.get(pos);
                mp = viewHolder.binding.vvAttachemt;
                viewHolder.binding.vvAttachemt.start();
                viewHolder.binding.ivPause.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.clip_item, parent, false);
        view.setTag("VIEW" + viewType);
        holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ClipItemBinding binding = holder.binding;
        ClipActionMenueBinding clipActionMenue = holder.clipActionMenue;
        ClipsItem item = clips.get(position);


        clipActionMenue.radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.like) {
                    clipActionMenue.btnLike.setImageTintList(null);
                    clipActionMenue.btnDisLike.setImageTintList(null);

                    clipActionMenue.btnLike.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.thim_color)));
                    clipActionMenue.btnDisLike.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.input_box_border)));
                } else if (i == R.id.dislike) {
                    clipActionMenue.btnLike.setImageTintList(null);
                    clipActionMenue.btnDisLike.setImageTintList(null);

                    clipActionMenue.btnDisLike.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.thim_color)));
                    clipActionMenue.btnLike.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.input_box_border)));
                } else {
                    clipActionMenue.btnLike.setImageTintList(null);
                    clipActionMenue.btnDisLike.setImageTintList(null);

                    clipActionMenue.btnDisLike.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.input_box_border)));
                    clipActionMenue.btnLike.setImageTintList(ColorStateList.valueOf(context.getColor(R.color.input_box_border)));

                }
            }
        });
        clipActionMenue.btnLike.setOnClickListener(view -> {
            if (allInterFace != null) {
                allInterFace.OnclickedWithData(item.getData().getId(), "L");
                if (clipActionMenue.like.isChecked()) {
                    clipActionMenue.likeNone.setChecked(true);

                    int like = Integer.parseInt(clipActionMenue.likeCount.getText().toString().trim()) - 1;
                    clipActionMenue.likeCount.setText(String.valueOf(like));

                } else {
                    clipActionMenue.like.setChecked(true);
                    int like = Integer.parseInt(clipActionMenue.likeCount.getText().toString().trim()) + 1;
                    clipActionMenue.likeCount.setText(String.valueOf(like));
                }

            }

        });
        clipActionMenue.btnDisLike.setOnClickListener(view -> {
            allInterFace.OnclickedWithData(item.getData().getId(), "D");
            if (clipActionMenue.dislike.isChecked()) {
                clipActionMenue.likeNone.setChecked(true);
                int dlike = Integer.parseInt(clipActionMenue.disLikeCount.getText().toString().trim()) - 1;
                clipActionMenue.disLikeCount.setText(String.valueOf(dlike));

            } else {
                clipActionMenue.dislike.setChecked(true);
                int dlike = Integer.parseInt(clipActionMenue.disLikeCount.getText().toString().trim()) + 1;
                clipActionMenue.disLikeCount.setText(String.valueOf(dlike));
            }

        });

        clipActionMenue.commentCount.setText(item.getData().getComments());
        clipActionMenue.likeCount.setText(item.getData().getLikes());
        clipActionMenue.disLikeCount.setText(item.getData().getDis_likes());
        clipActionMenue.viewCount.setText(item.getData().getViews());

        if (item.getRect() != null) {

            if (item.getRect().getIsLike().equalsIgnoreCase("1")) {
                clipActionMenue.like.setChecked(true);

            } else if (item.getRect().getIsLike().equalsIgnoreCase("2")) {
                clipActionMenue.dislike.setChecked(true);

            } else {
                clipActionMenue.likeNone.setChecked(true);
            }

        } else {
            clipActionMenue.like.setChecked(false);
            clipActionMenue.dislike.setChecked(false);
        }

        holder.Setdata(item);
        if (play == position) {
            binding.vvAttachemt.start();
        }

        try {
            playlist.remove(position);
        } catch (Exception e) {

        }
        playlist.add(position, holder);


    }

    private void getName(ClipsItem item, ClipItemBinding binding) {
        UserVewModel userVewModel = new UserVewModel((Application) context.getApplicationContext());
        Users users = userVewModel.selectUser(item.getData().getUserId());
        String created_at = "";
        if (users != null) {
            binding.name.setText(users.getName());
            String me = PreferenceFile.getUser().getUser().getId();
            boolean isdeletable = users.getId().equalsIgnoreCase(me);
            binding.menu.btnMenu.setOnClickListener(view -> {
                MethodClass.show_popup_menu3(binding.menu.btnMenu, context, new OnMenuItemClick() {
                    @Override
                    public void OnClick(int res) {
                        if (res == R.id.menu_foroward) {

                           /* Intent intent = new Intent(context, ShareActivity.class);
                            intent.putExtra("from", "forowrd");
                            intent.putExtra("type", "T");
                            intent.putExtra("msg",Constants.Attachment_URL + "/Clip/" + item.getData().getFileName());
                            context.startActivity(intent);*/

                        } else if (res == R.id.menu_delete) {
                            String sql = "Delete * from `clips` Where `id` =" + item.getData().getId() + " AND `userid` =" + me;
                            sync_BY_SQL(sql, null, context.getApplication(), handler);
                            StyleableToast.makeText(context, "Clip will be deleted within 2 minutes", R.style.mytoast).show();
                        } else if (res == R.id.menu_report) {

                            MethodClass.report(context, new AllInterFace() {
                                @Override
                                public void isClicked(boolean is) {
                                    super.isClicked(is);
                                    if (is) {
                                        StyleableToast.makeText(context, "The user will be block within 2 minutes " +
                                                "and all content from the use will be hidden", R.style.mytoast).show();
                                    }
                                }
                            }, users.getName());

                        }
                    }
                }, isdeletable);
            });
            if (users.getProfilePic() != null && !users.getProfilePic().equals("null")) {
                Glide.with(context).load(BASE_URL + "profile_image/" + users.getProfilePic())
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.image.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(users.getName(), R.drawable.class)));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                binding.image.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(binding.image);
            } else {
                binding.image.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(users.getName(), R.drawable.class)));

            }


            String date = item.getData().getCreatedAt().replaceAll("-", "/");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

            try {
                DateFormat utcFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                Date d = utcFormat.parse(date);

                DateFormat pstFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //DateFormat pstFormat = new SimpleDateFormat("hh:mm a");;
                pstFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                assert d != null;
                String datess = pstFormat.format(d);
                String dd = datess.replaceAll("/", "-");

                created_at = TimeAgo2.convertAM_PM(dd);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        binding.time.setText(created_at);

    }


    @Override
    public int getItemCount() {
        return clips.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ClipItemBinding binding;
        ClipActionMenueBinding clipActionMenue;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            binding = ClipItemBinding.bind(itemView);
            clipActionMenue = ClipActionMenueBinding.bind(binding.menu.getRoot());


        }

        void Setdata(ClipsItem item) {
            String path = Constants.BASE_URL + "Attachment/Clip/" + item.getData().getFileName();
            binding.vvAttachemt.setVideoPath(path);
           /* MediaController mediaController = new MediaController(context);
            binding.vvAttachemt.setMediaController(mediaController);
            mediaController.setAnchorView( binding.vvAttachemt);*/
            binding.vvAttachemt.setOnClickListener(view -> {
                if (binding.vvAttachemt.isPlaying()) {
                    binding.vvAttachemt.pause();
                    binding.ivPause.setImageDrawable(context.getDrawable(R.drawable.ic_pause));
                    binding.ivPause.setVisibility(View.VISIBLE);
                } else {
                    mp = binding.vvAttachemt;
                    binding.vvAttachemt.start();
                    binding.ivPause.setVisibility(View.GONE);
                }
            });


            getName(item, binding);

            binding.vvAttachemt.setOnPreparedListener(mp -> {
                mp.seekTo(50);
                binding.progressbar.setVisibility(View.GONE);
                binding.ivPause.setVisibility(View.VISIBLE);
                mp.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                    @Override
                    public boolean onInfo(MediaPlayer mp, int what, int extra) {
                        switch (what) {
                            case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                            case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
                                binding.progressbar.setVisibility(View.GONE);
                                return true;
                            }
                            case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
                                binding.progressbar.setVisibility(View.VISIBLE);
                                return true;
                            }
                        }
                        return false;
                    }
                });

                mp.setOnCompletionListener(mediaPlayer -> {
                    binding.ivPause.setVisibility(View.VISIBLE);
                    binding.ivPause.setImageDrawable(context.getDrawable(R.drawable.ic_play));

                    String sql = "UPDATE `clips` SET `views`=clips.views+1  WHERE id =" + item.getData().getId();
                    sync_BY_SQL(sql, null, context.getApplication(), handler);
                });


            });


        }
    }


}
