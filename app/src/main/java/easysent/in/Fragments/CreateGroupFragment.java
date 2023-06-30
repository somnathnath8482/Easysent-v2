package easysent.in.Fragments;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.Create_group;
import static easysent.in.Helper.Constants.JOIN_USERS;

import android.app.AlertDialog;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import easysent.in.Activity.MainActivity;
import easysent.in.Adapter.CreateGroupUserAdapter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Helper.SyncData;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Register.RegisterResponse;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.databinding.ActivityShareBinding;
import easysent.in.databinding.CreateGroupBinding;
import easysent.in.databinding.Toolbar2Binding;
import io.github.muddz.styleabletoast.StyleableToast;

public class CreateGroupFragment extends Fragment {


    private ActivityShareBinding binding;
    private Toolbar2Binding toolbar2Binding;
    private UserVewModel userVewModel;
    private CreateGroupUserAdapter adapter;
    Handler handler = new Handler();
Application application;
    String from = "";
    String id = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application= getActivity().getApplication();
        userVewModel = ViewModelProviders.of(this).get(UserVewModel.class);
        if (getArguments() != null) {
            id = getArguments().getString("id", "");
            from = getArguments().getString("from", "");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_create_group, container, false);
        binding = ActivityShareBinding.inflate(getLayoutInflater());
        toolbar2Binding = Toolbar2Binding.bind(binding.toolbar.getRoot());
        return binding.getRoot();

    }

    private void Init() {
        toolbar2Binding.back.setOnClickListener(view -> back());
        toolbar2Binding.search.setOnClickListener(view -> {
            show();
        });
        binding.ivNext.setOnClickListener(view -> {
            List<String> list = adapter.getUsers();
            if (list.size() < 1) {
                StyleableToast.makeText(getContext(), "Please Select At-least 1 user to create group", R.style.mytoast).show();
                return;
            } else {
                String users = "";
                for (int i = 0; i < list.size(); i++) {
                    if (i==0){
                        users += list.get(i);
                    }else{
                        users += ","+list.get(i);
                    }

                }

                if (from.equalsIgnoreCase("add")) {
                    AddUsers(users);
                } else {
                    ShowGetGroupDEtails(users);
                }

            }
        });

        toolbar2Binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence != null)
                    userVewModel.search(charSequence.toString().trim()).observe(getViewLifecycleOwner(), adapter::submitList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void AddUsers(String users) {

        HashMap<String, Object> map = new HashMap<>();
        map.put("group",id);
        map.put("users",users);
        Log.e("TAG", "AddUsers: "+map.toString() );
        new Thread(new Runnable() {
            @Override
            public void run() {
                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {
                        SyncData.SyncGroups(application, handler);
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                    }
                }, BASE_URL + JOIN_USERS, application, handler, "",map);
            }
        }).start();


    }

    private void ShowGetGroupDEtails(String list) {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.create_group, null);
        CreateGroupBinding binding = CreateGroupBinding.bind(dialogView);
        // AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setView(dialogView);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setStatusBarColor(getResources().getColor(R.color.thim_color));
        if (dialog.getWindow() != null)
            dialog.getWindow().getAttributes().windowAnimations = R.style.SlidingDialogAnimation;
        dialog.show();

        binding.ok.setOnClickListener(view -> {
            Log.e("TAG", "Create Group users: " + list);
            String me = PreferenceFile.getUser().getUser().getId();
            try {
                HashMap<String, Object> map = new HashMap<>();
                map.put("creator", PreferenceFile.getUser().getUser().getId());
                map.put("name", MethodClass.CheckEmpty(binding.title));
                map.put("desc", MethodClass.CheckEmpty(binding.desc));
                map.put("users", list + me);

                MethodClass.Call(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {
                        dialog.dismiss();
                        Gson json = new Gson();
                        RegisterResponse registerResponse = json.fromJson(res.toString(), RegisterResponse.class);
                        if (registerResponse != null) {
                            StyleableToast.makeText(getContext(), registerResponse.getSuccess(), Toast.LENGTH_SHORT, R.style.mytoast).show();


                            SyncData.SyncGroups(application, handler);
                            Navigation.findNavController(binding.getRoot()).popBackStack();
                        }

                    }
                }, BASE_URL + Create_group, getActivity(), handler, map);


            } catch (Exception e) {
                EditText editText = binding.getRoot().findViewById(Integer.parseInt(e.getMessage()));
                editText.requestFocus();
                e.printStackTrace();
                return;
            }

        });

        binding.close.setOnClickListener(view -> {
            dialog.dismiss();
        });
    }

    private void back() {
        if (toolbar2Binding.edSearch.getVisibility() == View.VISIBLE) {
            show();
        } else {
            getActivity().onBackPressed();
        }
    }

    void show() {
        Transition transition = new Slide(Gravity.END);
        transition.setDuration(300);
        transition.addTarget(toolbar2Binding.edSearch);

        if (toolbar2Binding.edSearch.getVisibility() == View.VISIBLE) {
            MethodClass.hideKeyboard(getActivity(), toolbar2Binding.edSearch);
        } else {
            MethodClass.showKeyboard(getActivity(), toolbar2Binding.edSearch);
            toolbar2Binding.edSearch.requestFocus();
        }

        TransitionManager.beginDelayedTransition((ViewGroup) toolbar2Binding.edSearch.getParent(), transition);
        toolbar2Binding.edSearch.setVisibility(toolbar2Binding.edSearch.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Init();
        binding.recycler.setHasFixedSize(true);
        adapter = new CreateGroupUserAdapter(getContext(), handler, new AllInterFace() {
            @Override
            public void IsClicked(String s) {
                super.IsClicked(s);
            }
        }, binding.ivNext);
        binding.recycler.setAdapter(adapter);
        userVewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);


    }

    @Override
    public void onResume() {
        super.onResume();
        if (from.equalsIgnoreCase("add")) {
            toolbar2Binding.title.setText("Select  Users to Add ");
        } else {
            toolbar2Binding.title.setText("Select  Users to Create Group");
        }

    }
}