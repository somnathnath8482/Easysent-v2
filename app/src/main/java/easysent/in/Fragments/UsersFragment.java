package easysent.in.Fragments;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;


import easysent.in.Adapter.UserAdapter;
import easysent.in.Interface.AllInterFace;
import easysent.in.Room.Users.UserVewModel;
import easysent.in.databinding.FragmentUsersBinding;

public class UsersFragment extends Fragment {


FragmentUsersBinding binding;
UserVewModel userVewModel;
Handler handler = new Handler();
Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        userVewModel = ViewModelProviders.of(UsersFragment.this).get(UserVewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_users, container, false);

        binding = FragmentUsersBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recycler.setHasFixedSize(true);
        UserAdapter adapter  =new UserAdapter(getContext(),handler,new AllInterFace(){
            @Override
            public void IsClicked(String path) {
                super.IsClicked(path);
                  Bundle bundle = new Bundle();
                bundle.putString("reciver",path);
               // Intent intent = new Intent(activity, MessageActivity.class);
                //intent.putExtra("reciver", path);
                //activity.startActivity(intent);
            }
        });
        binding.recycler.setAdapter(adapter);
        userVewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);
        binding.edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence!=null)
                    userVewModel.search(charSequence.toString().trim()).observe(getViewLifecycleOwner(), adapter::submitList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        //MainFragment.resetTab(MainFragment.binding.btnUsers,getActivity());
    }
}