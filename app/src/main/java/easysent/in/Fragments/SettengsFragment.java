package easysent.in.Fragments;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.GET_ALL_USER;
import static easysent.in.Helper.Constants.LOGIN_HISTORY;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Adapter.LoginHistoryAdapter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Helper.SyncData;
import easysent.in.Interface.Response;
import easysent.in.Response.AllUsers.AllUsersResponse;
import easysent.in.Response.AllUsers.UsersItem;
import easysent.in.Response.LoginHistory.HistoryItem;
import easysent.in.Response.LoginHistory.LoginHistoryResponse;
import easysent.in.Room.Users.Users;
import easysent.in.databinding.FragmentSettengsBinding;

public class SettengsFragment extends Fragment {

FragmentSettengsBinding binding;
Application context;
Handler handler = new Handler();
LoginHistoryAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       context = getActivity().getApplication();

       adapter = new LoginHistoryAdapter(new DiffUtil.ItemCallback<HistoryItem>() {
           @Override
           public boolean areItemsTheSame(@NonNull HistoryItem oldItem, @NonNull HistoryItem newItem) {
               return newItem.getId().equals(oldItem.getId());
           }

           @Override
           public boolean areContentsTheSame(@NonNull HistoryItem oldItem, @NonNull HistoryItem newItem) {
               return newItem.getId().equals(oldItem.getId());
           }
       });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_settengs, container, false);
        binding  =FragmentSettengsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.enableFingireprint.setOnCheckedChangeListener(null);
        if (PreferenceFile.getFingirAuth()){
            binding.enableFingireprint.setChecked(true);
        }else{
            binding.enableFingireprint.setChecked(false);
        }
        binding.enableFingireprint.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b){
                Enable();
            }else {
                PreferenceFile.setFingirAuth(false);
            }
        });
        binding.recycler.setLayoutManager(new LinearLayoutManager(context));
        binding.recycler.setAdapter(adapter);

        getHistory();

    }

    private void Enable() {
       /* Authenticate authenticate = new Authenticate(getContext(), getActivity(), "Add 2 Step Verification",
                "While Adding Two Step, You Should Know You Need to Verify Yourself EveryTime While Autologin", "Easysent");
        authenticate.setIssuccessListener(new Authenticate.isSuccess() {
            @Override
            public boolean isSuccess(boolean isS) {
                if (isS){
                    PreferenceFile.setFingirAuth(true);
                }else{
                    PreferenceFile.setFingirAuth(false);
                }
                return false;
            }
        });*/

    }
    private void getHistory(){
        String Header =  PreferenceFile.getUser().getUser().getEmail();
        new Thread(new Runnable() {
            @Override
            public void run() {
                MethodClass.CallHeader(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {

                        if (!res.has("error")) {
                            Gson gson = new Gson();
                            LoginHistoryResponse response = gson.fromJson(res.toString(), LoginHistoryResponse.class);
                            if (response != null) {

                                adapter.submitList(response.getHistory());
                            }
                        }

                    }
                }, BASE_URL + LOGIN_HISTORY, context, handler, Header, new HashMap<>());
            }
        }).start();
    }
}