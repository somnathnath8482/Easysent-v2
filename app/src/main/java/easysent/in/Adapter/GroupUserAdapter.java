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

import easysent.in.Helper.ImageGetter;
import easysent.in.Helper.MethodClass;
import easysent.in.Interface.AllInterFace;
import easysent.in.R;
import easysent.in.Response.GetGroupUser.UsersItem;
import easysent.in.databinding.GroupUserItemBinding;

public class GroupUserAdapter extends ListAdapter<UsersItem, GroupUserAdapter.ViewHolder> {
Context context;
Handler handler;
AllInterFace allInterface;
String creator;
    public GroupUserAdapter(Context context, Handler handler, String creator, AllInterFace allInterface) {
        super(Diff_callback);
        this.context = context;
        this.creator = creator;
        this.handler =handler;
        this.allInterface  = allInterface;
    }
    private static final DiffUtil.ItemCallback<UsersItem> Diff_callback = new DiffUtil.ItemCallback<UsersItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull UsersItem oldItem, @NonNull UsersItem newItem) {
            return oldItem.getEmail().equals(newItem.getEmail());
        }

        @Override
        public boolean areContentsTheSame(@NonNull UsersItem oldItem, @NonNull UsersItem newItem) {
            return oldItem.getId().equals(newItem.getId())
                    && oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.group_user_item,parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UsersItem user = getItem(position);
        holder.binding.name.setText(user.getName());
        String date =MethodClass.changeDateFormat(user.getDateOfJoin());

        holder.binding.about.setText("Joined on: "+date);



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






        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Bundle bundle = new Bundle();
                bundle.putString("reciver",user.getId());
                MainActivity.navController.navigate(R.id.messageFragment,bundle, MainActivity.options);*/
                if (allInterface!=null ){
                    allInterface.OnSelect(user.getId(),user.getName(),user.getToken());
                }
            }
        });



    }


    public static class ViewHolder extends RecyclerView.ViewHolder{

        GroupUserItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding =GroupUserItemBinding.bind(itemView);
        }
    }}
