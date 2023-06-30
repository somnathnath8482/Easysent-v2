package easysent.in.Adapter;

import static easysent.in.Helper.Constants.CATCH_DIR2;
import static easysent.in.Helper.MethodClass.CashImage2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import easysent.in.Helper.Constants;
import easysent.in.R;
import easysent.in.Room.GroupChat.Group_Chat;
import easysent.in.databinding.AttachmentItemBinding;

public class Group_Attachment_viewpager extends RecyclerView.Adapter<Group_Attachment_viewpager.ViewHolder> {
    List<Group_Chat> chats = new ArrayList<>();
    Context context;
    private ExoPlayer player;
    Handler handler;

    List<ExoPlayer> playerList  = new ArrayList<>();
    public Group_Attachment_viewpager(List<Group_Chat> chats, Context context, Handler handler) {
        this.chats = chats;

        this.handler = handler;
    }

    public void pauseAll() {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).isPlaying()){
                playerList.get(i).pause();
            }
        }
    }

    public ExoPlayer getPlayer() {
        return player;
    }

    public void releasePlayer() {
        this.player.release();
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
context = parent.getContext();
        View view  = LayoutInflater.from(context).inflate(R.layout.attachment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttachmentItemBinding binding = holder.binding;



        Group_Chat item = chats.get(position);
        if (item.getType().equalsIgnoreCase("I")) {


            File file = new File(CATCH_DIR2 + "/" + item.getAttachment());
            if (file.exists() && file.isAbsolute()) {
                Glide.with(context).load(file.getAbsolutePath())
                        .into(binding.ivAttachment);
            }else {
                Glide.with(context).load(Constants.BASE_URL + "Attachment/Groups/" + item.getAttachment())
                        .thumbnail(0.02f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .addListener(new RequestListener<Drawable>() {
                            @SuppressLint("UseCompatLoadingForDrawables")
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                binding.ivAttachment.setImageDrawable(context.getDrawable(R.drawable.ic_x));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                binding.ivAttachment.setImageDrawable(resource);
                                return false;
                            }
                        })
                        .into(binding.ivAttachment);
                CashImage2(Constants.BASE_URL + "Attachment/Groups/"+item.getAttachment(), item.getAttachment());
            }
            binding.ivAttachment.setVisibility(View.VISIBLE);
        }
        if (item.getType().equalsIgnoreCase("V")) {
            if (player!=null) {
                player.pause();
            }
            player = new ExoPlayer.Builder(context).build();
            String path = Constants.BASE_URL + "Attachment/Groups/" + item.getAttachment();
            File file  = new File(CATCH_DIR2 + "/"+item.getAttachment());
            if (file.exists()){
                path = file.getAbsolutePath();
            }
            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(path));
            player.setMediaItem(mediaItem);
            player.prepare();
            binding.vvAttachemt.setPlayer(player);
            player.setPlayWhenReady(false);
            binding.vvAttachemt.setVisibility(View.VISIBLE);
            playerList.add(player);
        }


    }

    @Override
    public int getItemCount() {
        return chats ==null ? 0 : chats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        AttachmentItemBinding binding;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           binding = AttachmentItemBinding.bind(itemView);
       }
   }
}
