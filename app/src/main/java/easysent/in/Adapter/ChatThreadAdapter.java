package easysent.in.Adapter;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR;
import static easysent.in.Helper.MethodClass.CashImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;


import easysent.in.Activity.Messages.MessageActivity;
import easysent.in.Helper.Constants;
import easysent.in.Helper.ImageGetter;
import easysent.in.Helper.MethodClass;
import easysent.in.R;
import easysent.in.Room.Threads.Active_Thread;
import easysent.in.databinding.ActiveThreadItemBinding;

public class ChatThreadAdapter extends ListAdapter<Active_Thread, ChatThreadAdapter.ViewHolder> {
    Context context;
    Handler handler;


    public ChatThreadAdapter(Context context, Handler handler) {
        super(Diff_callback);
        this.context = context;
        this.handler = handler;
    }


    private static final DiffUtil.ItemCallback<Active_Thread> Diff_callback = new DiffUtil.ItemCallback<Active_Thread>() {
        @Override
        public boolean areItemsTheSame(@NonNull Active_Thread oldItem, @NonNull Active_Thread newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Active_Thread oldItem, @NonNull Active_Thread newItem) {
            return oldItem.getRid() == newItem.getRid()
                    && oldItem.getId().equals(newItem.getId())
                    && oldItem.getSender().equals(newItem.getSender())
                    && oldItem.getReciver().equals(newItem.getReciver())
                    && oldItem.getLast_message().equals(newItem.getLast_message())
                    && oldItem.getLast_message_type().equals(newItem.getLast_message_type())
                    && oldItem.getLast_message_status().equals(newItem.getLast_message_status());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.active_thread_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Active_Thread item = getItem(position);
        holder.binding.name.setText(item.getName());
        if (item.getLast_message_type().equalsIgnoreCase("T")) {
            holder.binding.lastMesshge.setText(item.getLast_message());
        } else if (item.getLast_message_type().equalsIgnoreCase("I")) {


            holder.binding.lastMesshge.setText("Image");

        } else if (item.getLast_message_type().equalsIgnoreCase("V")) {

            holder.binding.lastMesshge.setText("Video");

        } else if (item.getLast_message_type().equalsIgnoreCase("D")) {

            holder.binding.lastMesshge.setText("DOC");

        } else if (item.getLast_message_type().equalsIgnoreCase("P")) {

            holder.binding.lastMesshge.setText("PDF");

        }

        if (item.getProfilePic() != null && !item.getProfilePic().equals("null")) {


            File file = new File(CATCH_DIR + "/" + item.getProfilePic());
            if (file.exists()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // String url  =   url = CATCH_DIR+"/"+item.getProfilePic();
                        // Bitmap bitmap = BitmapFactory.decodeFile(MethodClass.getRightAngleImage(url));
                        //Glide.with(context).load(url).into(holder.binding.profileImage);
                        ImageGetter imageGetter = new ImageGetter(holder.binding.profileImage);
                        imageGetter.execute(file);
                    }
                }).start();


            } else {


                Glide.with(context).load(Constants.BASE_URL + "profile_image/" + item.getProfilePic())
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .addListener(new RequestListener<Drawable>() {

                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.binding.profileImage.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(item.getName(), R.drawable.class)));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                holder.binding.profileImage.setImageDrawable(resource);
                                CashImage(BASE_URL + "profile_image/" + item.getProfilePic(), item.getProfilePic(), resource);
                                return false;
                            }
                        })
                        .into(holder.binding.profileImage);
            }
        } else {
            holder.binding.profileImage.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(item.getName(), R.drawable.class)));
        }

        if (item.getLast_message_time() != null) {
            holder.binding.time.setText(MethodClass.getTimeAgo(item.getLast_message_time()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(context, MessageActivity.class);
               intent.putExtra("reciver", item.getId());
                context.startActivity(intent);
            }
        });


        if (item.getUnread() > 0) {
            holder.binding.unread.setText(item.getUnread() + "");
            holder.binding.unread.setVisibility(View.VISIBLE);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ActiveThreadItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActiveThreadItemBinding.bind(itemView);
        }
    }
}
