package easysent.in.Fragments;

import static easysent.in.Helper.ExifUtils.getRotatedBitmap;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.easy.pickfile.Interface.Onselect;
import com.easy.pickfile.PickFile;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import easysent.in.Activity.MainActivity;
import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Login.LoginResponse;
import easysent.in.Response.Login.User;
import easysent.in.databinding.FragmentEditProfileBinding;
import io.github.muddz.styleabletoast.StyleableToast;


public class EditProfileFragment extends Fragment {
    FragmentEditProfileBinding binding;
    private String profile_pic_path="";

Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnSubmit.setOnClickListener(view1 -> {
            try {
                HashMap<String, Object> map = new HashMap<>();
                map.put("name", MethodClass.CheckEmpty(binding.name));
                map.put("email", MethodClass.CheckEmpty(binding.email));
                map.put("phone", MethodClass.CheckEmpty(binding.phone));
                map.put("about", MethodClass.CheckEmpty(binding.about));
                if (binding.male.isChecked()) {
                    map.put("gender","M");
                }else if (binding.female.isChecked()){
                    map.put("gender","F");
                }else if (binding.other.isChecked()){
                    map.put("gender","N");
                }else{
                    Toast.makeText(getContext(), "Please Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                Update(map);

            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        });
        binding.ivProfile.setOnClickListener(view1 -> {
           MainActivity.pickFile.PickImage(false);

        });
        GetUser();
        manageFile();
    }

    private void manageFile() {
        MainActivity.pickFile.setOnselect(new Onselect() {
            @Override
            public void onSelect(String... strings) {
                if (strings!=null){
                    Log.e("PhotoPicker", "onSelect: "+strings[0]+" "+strings[1] );
                    String s = strings[1];
                    profile_pic_path = s;
                    File file =new File(s);
                    if (file.exists()){
                        binding.ivProfile.setImageBitmap(BitmapFactory.decodeFile(s));
                    }
                }
            }
        });
    }

    private void GetUser() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("email", PreferenceFile.getUser().getUser().getEmail());
        MethodClass.Call(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res.toString(), LoginResponse.class);

                PreferenceFile.setLogged(true);
                PreferenceFile.setUser(loginResponse);



                User user = loginResponse.getUser();
                binding.tvName.setText(user.getName());
                binding.tvEmail.setText(user.getEmail());
                binding.name.setText(user.getName());
                binding.email.setText(user.getEmail());
                binding.phone.setText(user.getPhone());
                binding.about.setText(user.getAbout());

                if (user.getGender().equals("M")){
                    binding.male.setChecked(true);
                }   else if (user.getGender().equals("F")){
                    binding.female.setChecked(true);
                }  else if (user.getGender().equals("N")) {
                    binding.other.setChecked(true);
                }


                Glide.with(getContext()).load(Constants.BASE_URL+"profile_image/"+user.getProfilePic()).addListener(new RequestListener<Drawable>() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.ivProfile.setImageDrawable(getContext().getResources().getDrawable(MethodClass.getResId(user.getName(), R.drawable.class)));
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        binding.ivProfile.setImageDrawable(resource);
                        return false;
                    }
                }).into( binding.ivProfile);

            }
        }, Constants.BASE_URL+ Constants.GET_USER, getActivity(),handler,map );
    }

    private void Update(HashMap<String, Object> map) {

        if (profile_pic_path.equals(""))
        MethodClass.Call(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                profile_pic_path = "";
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res.toString(), LoginResponse.class);
                StyleableToast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();

                PreferenceFile.setLogged(true);
                PreferenceFile.setUser(loginResponse);
                GetUser();

            }
        }, Constants.BASE_URL+ Constants.EDIT_PROFILE,  getActivity(),handler, map);
        else{
            MethodClass.CallMultipart(new Response() {
                @Override
                public void onResponse(JSONObject res) {
                    profile_pic_path = "";
                    Gson gson = new Gson();
                    LoginResponse loginResponse = gson.fromJson(res.toString(), LoginResponse.class);
                    StyleableToast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    GetUser();

                }
            }, Constants.BASE_URL+ Constants.EDIT_PROFILE,  getActivity(),handler, map,profile_pic_path,"img");
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.pickFile.setOnselect(null);
        handler = null;
    }
}