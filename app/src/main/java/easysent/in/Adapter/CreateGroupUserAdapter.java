package easysent.in.Adapter;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR;
import static easysent.in.Helper.MethodClass.CashImage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
import java.util.ArrayList;
import java.util.List;

import easysent.in.Helper.ImageGetter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.R;
import easysent.in.Room.Users.Users;
import easysent.in.databinding.CreateGroupUserItemBinding;

public class CreateGroupUserAdapter extends ListAdapter<Users, CreateGroupUserAdapter.ViewHolder> {
Context context;
Handler handler;
AllInterFace allInterface;
List<String> users = new ArrayList<>();
ImageView imageView;
    public CreateGroupUserAdapter(Context context, Handler handler, AllInterFace allInterface, ImageView imageView) {
        super(Diff_callback);
        this.context = context;
        this.handler =handler;
        this.imageView = imageView;
        this.allInterface  = allInterface;
    }

    public List<String> getUsers() {
        return users;
    }

    private static final DiffUtil.ItemCallback<Users> Diff_callback = new DiffUtil.ItemCallback<Users>() {
        @Override
        public boolean areItemsTheSame(@NonNull Users oldItem, @NonNull Users newItem) {
            return oldItem.getEmail().equals(newItem.getEmail());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Users oldItem, @NonNull Users newItem) {
            return oldItem.getRid() == newItem.getRid()
                    && oldItem.getId().equals(newItem.getId())
                    && oldItem.getFstatus().equals(newItem.getFstatus())
                    && oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.create_group_user_item,parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users user = getItem(position);

        holder.binding.name.setText(user.getName());
        holder.binding.about.setText(user.getAbout()==null ? "Hi! Happy To Use EasySent \uD83D\uDC95"  : user.getAbout());




        if (user.getProfilePic()!=null && !user.getProfilePic().equals("null")) {

            String url = BASE_URL + "profile_image/" + user.getProfilePic();
            File file = new File(CATCH_DIR+"/"+user.getProfilePic());
            if (file.exists()){


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                       // String url  =   url = CATCH_DIR+"/"+user.getProfilePic();
                       // Bitmap bitmap = BitmapFactory.decodeFile(MethodClass.getRightAngleImage(url));
                        ImageGetter imageGetter =new ImageGetter(holder.binding.profileImage);
                        imageGetter.execute(file);

                    }
                }).start();


            }else {
                Glide.with(context).load(url)
                        .thumbnail(0.05f)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                holder.binding.profileImage.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(user.getName(), R.drawable.class)));
                                return true;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                holder.binding.profileImage.setImageDrawable(resource);
                                CashImage(BASE_URL + "profile_image/" + user.getProfilePic(), user.getProfilePic(), resource);
                                return false;
                            }
                        })
                        .into(holder.binding.profileImage);
            }
        }else{
            holder.binding.profileImage.setImageDrawable(context.getResources().getDrawable(MethodClass.getResId(user.getName(), R.drawable.class)));
        }



        if (user.getFstatus().equalsIgnoreCase("Online")){
            holder.binding.status.setBackground(null);
            holder.binding.status.setBackground(context.getResources().getDrawable(R.drawable.active));
            holder.binding.status.setVisibility(View.VISIBLE);

        } else if (user.getFstatus().equalsIgnoreCase("a")){
            holder.binding.status.setBackground(null);
            holder.binding.status.setBackground(context.getResources().getDrawable(R.drawable.away));
            holder.binding.status.setVisibility(View.VISIBLE);
        } else{
            holder.binding.status.setVisibility(View.GONE);
        }
        holder.binding.check.setOnCheckedChangeListener(null);
        if (isChecked(user.getId())){
            holder.binding.check.setChecked(true);
        }else{
            holder.binding.check.setChecked(false);
        }


        holder.binding.check.setOnCheckedChangeListener((compoundButton, b) -> {

            if (!b){
                users.remove(user.getId());
            }else{
                users.remove(user.getId());
                users.add(user.getId());
            }

            if (users.size()>0){
                imageView.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.GONE);
            }
        });



        if (user.getId().equalsIgnoreCase(PreferenceFile.getUser().getUser().getId())){
            holder.binding.name.setText("You");
            holder.binding.check.setVisibility(View.INVISIBLE);
            holder.binding.check.setOnCheckedChangeListener((compoundButton, b) -> {
                holder.binding.check.setChecked(false);
            });

        }else{
            holder.itemView.setOnClickListener(view -> {
                if (holder.binding.check.isChecked()){
                    holder.binding.check.setChecked(false);
                }else {
                    holder.binding.check.setChecked(true);
                }
            });

        }




    }

boolean isChecked(String id){
    for (int i = 0; i < users.size(); i++) {
        if (id.equalsIgnoreCase(users.get(i))){
            return true;
        }

    }
    return false;

}
    public static class ViewHolder extends RecyclerView.ViewHolder{

        CreateGroupUserItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding =CreateGroupUserItemBinding.bind(itemView);
        }
    }}
