package easysent.in.Fragments;

import static easysent.in.Helper.Constants.BASE_URL;
import static easysent.in.Helper.Constants.GATE_ALL_CLIP;
import static easysent.in.Helper.Constants.LIKE_CLIPS;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import easysent.in.Adapter.ClipsAdapter;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.AllInterFace;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.All_Clips.AllClipResponse;
import easysent.in.databinding.FragmentClipsBinding;


public class ClipsFragment extends Fragment {
    FragmentClipsBinding binding;
    ClipsAdapter clipsAdapter;
    Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_clips, container, false);
        binding = FragmentClipsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clipsAdapter = new ClipsAdapter(getActivity(), new ArrayList<>(), binding.recycler, handler);
        Init();
    }

    private void Init() {

        binding.btnAdd.setOnClickListener(view1 -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.add_Clip_Fragment);
        });

        binding.recycler.setAdapter(clipsAdapter);


        GetAll();
    }

    private void GetAll() {

        String Header = PreferenceFile.getUser().getUser().getId();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", PreferenceFile.getUser().getUser().getId());
        MethodClass.CallHeader(new Response() {
            @Override
            public void onResponse(JSONObject res) {

                if (!res.has("error")) {
                    Gson gson = new Gson();
                    AllClipResponse response = gson.fromJson(res.toString(), AllClipResponse.class);
                    if (response != null) {
                        Collections.reverse(response.getClips());
                        clipsAdapter = new ClipsAdapter(getActivity(), response.getClips(), binding.recycler, handler);
                        binding.recycler.setAdapter(clipsAdapter);
                        binding.recycler.setOffscreenPageLimit(3);

                        binding.recycler.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                            @Override
                            public void onPageSelected(int position) {
                                super.onPageSelected(position);
                                clipsAdapter.stopMp(position);
                            }
                        });

                        clipsAdapter.setAllInterFace(new AllInterFace() {
                            @Override
                            public void OnclickedWithData(String id, String value) {
                                super.OnclickedWithData(id, value);
                                if (value.equalsIgnoreCase("L")) {

                                    Like(value, id);
                                } else if (value.equalsIgnoreCase("D")) {
                                    Like(value, id);
                                }

                            }
                        });

                    }
                }

            }
        }, BASE_URL + GATE_ALL_CLIP, getActivity().getApplication(), handler, Header, map);
    }

    private void Like(String value, String id) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, Object> map = new HashMap<>();
                map.put("type", value);
                map.put("id", id);
                map.put("user_id", PreferenceFile.getUser().getUser().getId());
                MethodClass.CallUpdate(res -> {


                }, BASE_URL + LIKE_CLIPS, getActivity().getApplication(), handler, "", map);

            }
        }).start();

    }

}