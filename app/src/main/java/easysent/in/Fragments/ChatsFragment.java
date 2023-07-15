package easysent.in.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;

import java.util.List;

import easysent.in.Adapter.ChatThreadAdapter;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.Messages.LiveDatanonPage;
import easysent.in.Room.Threads.Active_Thread;
import easysent.in.Room.Threads.Thread_ViewModel;
import easysent.in.databinding.FragmentChatsBinding;

public class ChatsFragment extends Fragment {
    FragmentChatsBinding binding;
    Thread_ViewModel thread_viewModel;
    Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread_viewModel =new Thread_ViewModel(getActivity().getApplication());
      }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        binding = FragmentChatsBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.recycler.setHasFixedSize(true);
        ChatThreadAdapter adapter  =new ChatThreadAdapter(getContext(),handler);
        binding.recycler.setAdapter(adapter);
        thread_viewModel.getActiveThreds(PreferenceFile.getUser().getUser().getId(), new LiveDatanonPage<Active_Thread>() {
            @Override
            public void allMessage(LiveData<List<Active_Thread>> messages) {
           messages.observe(getViewLifecycleOwner(), adapter::submitList);
            }
        });
    }


}