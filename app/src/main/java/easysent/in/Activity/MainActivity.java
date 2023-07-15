package easysent.in.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import java.io.File;

import easysent.in.BuildConfig;
import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Helper.SyncData;
import easysent.in.R;
import easysent.in.databinding.ActivityMainBinding;
import easysent.in.databinding.LeftMenuBinding;

public class MainActivity extends AppCompatActivity {
    public static ActivityMainBinding binding;
    LeftMenuBinding leftMenuBinding;
    Handler handler = new Handler();
    private NavOptions options;
    private NavController navController;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View header_view = binding.navigationView.getHeaderView(0);
        leftMenuBinding = LeftMenuBinding.bind(header_view);
        setContentView(binding.getRoot());

        Init();
        DrawaerClick();

        if (getIntent() != null && getIntent().getBooleanExtra("new_login", false)) {
            Syc();
        }

    }

    private void Syc() {
        SyncData.SyncThread(activity.getApplication(), handler);
        SyncData.SyncGroups(activity.getApplication(), handler);
        SyncData.SyncChat(activity.getApplication(), handler);
        SyncData.SyncBlock(activity.getApplication(), handler);
        SyncData.SyncAllGroupChats(activity.getApplication(), handler);
    }

    private void DrawaerClick() {
        leftMenuBinding.closeDrawer.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
        });
        leftMenuBinding.layLogout.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
            MethodClass.logout(MainActivity.this, handler, PreferenceFile.getUser().getUser().getId());
        });
        leftMenuBinding.layEditProfile.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.editProfileFragment, null, options);
        });
        leftMenuBinding.layChangePassword.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.chengePasswordFragment, null, options);
        });

        leftMenuBinding.laySettings.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.settengsFragment, null, options);
        });
        leftMenuBinding.layClip.setOnClickListener(view -> {
            binding.drawer.closeDrawer(GravityCompat.START);
            navController.navigate(R.id.clipsFragment, null, options);
        });
    }

    private void Init() {

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
        NavOptions.Builder navBuilder = new NavOptions.Builder();
        navBuilder.setEnterAnim(R.anim.slide_in)
                .setExitAnim(R.anim.fade_oout)
                /*.setPopEnterAnim(R.anim.fade_i_n)
               /* .setPopExitAnim(R.anim.slide_oout)*/;

        options = navBuilder
                .setLaunchSingleTop(true).build();
        navController = navHostFragment.getNavController();


        leftMenuBinding.version.setText(BuildConfig.VERSION_NAME);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Create();
    }

    private void Create() {
        String folder_main = "EasySent";
        String folder_secondary = "EasySent/Cash";


        File dir = new File(this.getFilesDir(), folder_main);
        File dir2 = new File(Environment.getExternalStorageDirectory() + "/Download/", folder_main);
        File dir3 = new File(this.getFilesDir(), folder_secondary);

        if (!dir.exists()) {
            boolean is = dir.mkdirs();
            if (is) {
                Constants.CATCH_DIR = dir.getAbsolutePath().toString();
            }
            Log.d("TAG", "Create Folder: " + is);
        } else {
            Constants.CATCH_DIR = dir.getAbsolutePath().toString();
        }

        if (!dir3.exists()) {
            boolean is = dir3.mkdirs();
            if (is) {
                Constants.CATCH_DIR3 = dir3.getAbsolutePath().toString();
            }
            Log.d("TAG", "Create Folder: " + is);
        } else {


            if (dir3.isDirectory()) {
                String[] children = dir3.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir3, children[i]).delete();
                }
            }
            boolean is = dir3.mkdirs();
            if (is) {
                Constants.CATCH_DIR3 = dir3.getAbsolutePath().toString();
            }
            Log.d("TAG", "Create Folder: " + is);
        }

        if (!dir2.exists()) {
            boolean is = dir2.mkdirs();
            if (is) {
                Constants.CATCH_DIR2 = dir2.getAbsolutePath().toString();
            }
            Log.d("TAG", "Create Folder: " + is);
        } else {
            Constants.CATCH_DIR2 = dir2.getAbsolutePath().toString();
        }


    }
}

