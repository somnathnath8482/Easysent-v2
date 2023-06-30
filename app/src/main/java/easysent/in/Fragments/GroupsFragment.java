package easysent.in.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import easysent.in.Activity.MainActivity;
import easysent.in.Adapter.GroupsAdapter;
import easysent.in.Helper.ShareData;
import easysent.in.Helper.SyncData;
import easysent.in.R;
import easysent.in.Room.Groups.Groups_ViewModel;
import easysent.in.databinding.FragmentGroupsBinding;

public class GroupsFragment extends Fragment {

FragmentGroupsBinding binding;
Handler handler = new Handler();
Groups_ViewModel groups_viewModel;
    private String users ="";
Activity activity;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity=getActivity();
        groups_viewModel = ViewModelProviders.of(this).get(Groups_ViewModel.class);
        if (getArguments() != null) {

        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_groups, container, false);
        binding =FragmentGroupsBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        binding.recycler.setHasFixedSize(true);
        GroupsAdapter adapter  =new GroupsAdapter(getContext(),handler);
        binding.recycler.setAdapter(adapter);
        groups_viewModel.getAll().observe(getViewLifecycleOwner(), adapter::submitList);


        binding.btnCreateGroup.setOnClickListener(view1 -> {

            Navigation.findNavController(binding.getRoot()).navigate(R.id.createGroupFragment);
        });
    }
}