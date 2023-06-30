package easysent.in.Fragments;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.CATCH_DIR;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import easysent.in.Activity.MainActivity;
import easysent.in.Adapter.ViewPagerAdapter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.R;
import easysent.in.Response.Login.LoginResponse;
import easysent.in.databinding.FragmentMainBinding;
import easysent.in.databinding.MainToolbarBinding;


public class MainFragment extends Fragment {

FragmentMainBinding binding;
MainToolbarBinding mainToolbarBinding;
Handler handler = new Handler();
Activity activity;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater,container, false);
        mainToolbarBinding  = MainToolbarBinding.bind(binding.toolbar.getRoot());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(),getLifecycle());
            viewPagerAdapter.addFragment(new ChatsFragment(), "Chats");
            viewPagerAdapter.addFragment(new GroupsFragment(), "Groups");
            viewPagerAdapter.addFragment(new UsersFragment(), "Users");
            binding.viewpager.setAdapter(viewPagerAdapter);
            binding.viewpager.setOffscreenPageLimit(3);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Init();
    }

    private void Init() {



        //tabLayout.setupWithViewPager(viewPager);
        resetTab(binding.btnChats,activity);

        binding.btnUsers.setOnClickListener(view -> {
            //navController.navigate(R.id.usersFragment, null, options);
            binding.viewpager.setCurrentItem(2, true);
        });
        binding.btnGroups.setOnClickListener(view -> {
            //navController.navigate(R.id.chatsFragment, null, options);
            binding.viewpager.setCurrentItem(1, true);
        });
        binding.btnChats.setOnClickListener(view -> {
            //navController.navigate(R.id.chatsFragment, null, options);
            binding.viewpager.setCurrentItem(0, true);
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position==0){
                    resetTab(binding.btnChats,activity);
                }
                else if (position==1){
                    resetTab(binding.btnGroups,activity);
                }
                else if (position==2){
                    resetTab(binding.btnUsers,activity);
                }
            }
        });

        mainToolbarBinding.menu.setOnClickListener(view -> {
            MainActivity.binding.drawer.openDrawer(GravityCompat.START);
        });


        updateData(getActivity());
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    public  void updateData(Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (PreferenceFile.isLogged()) {
                    LoginResponse user = PreferenceFile.getUser();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mainToolbarBinding.name.setText(user.getUser().getName());
                            mainToolbarBinding.email.setText(user.getUser().getEmail());


                        }
                    });


                    if (user.getUser().getProfilePic() != null && !user.getUser().getProfilePic().equals("null")) {


                        File file = new File(CATCH_DIR + "/" + user.getUser().getProfilePic());
                        if (file.exists()) {
                            String url = url = CATCH_DIR + "/" + user.getUser().getProfilePic();
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
                                    Glide.with(activity).load(BASE_URL + "profile_image/" + user.getUser().getProfilePic())
                                            .thumbnail(0.05f)
                                            .transition(DrawableTransitionOptions.withCrossFade())
                                            .addListener(new RequestListener<Drawable>() {
                                                @Override
                                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                                    mainToolbarBinding.profileImage.setImageDrawable(activity.getResources().getDrawable(MethodClass.getResId(user.getUser().getName(), R.drawable.class)));
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
                                mainToolbarBinding.profileImage.setImageDrawable(activity.getResources().getDrawable(MethodClass.getResId(user.getUser().getName(), R.drawable.class)));

                            }
                        });
                    }
                }
            }
        }).start();

    }


    @SuppressLint("UseCompatLoadingForDrawables")
    public  void resetTab(TextView textView, Activity activity) {

        binding.btnChats.setBackground(null);
        binding.btnChats.setBackground(activity.getResources().getDrawable(R.drawable.green_border));
        binding.btnChats.setTextColor(activity.getResources().getColor(R.color.thim_color));


        binding.btnGroups.setBackground(null);
        binding.btnGroups.setBackground(activity.getResources().getDrawable(R.drawable.green_border));
        binding.btnGroups.setTextColor(activity.getResources().getColor(R.color.thim_color));

        binding.btnUsers.setBackground(null);
        binding.btnUsers.setBackground(activity.getResources().getDrawable(R.drawable.green_border));
        binding.btnUsers.setTextColor(activity.getResources().getColor(R.color.thim_color));


        textView.setBackground(null);
        textView.setBackgroundColor(activity.getResources().getColor(R.color.thim_color));
        textView.setTextColor(activity.getResources().getColor(R.color.white));


    }
}