package easysent.in.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import easysent.in.Response.Add_Clip_response.AddClipResponse;
import easysent.in.databinding.FragmentAddClipBinding;
import io.github.muddz.styleabletoast.StyleableToast;


public class Add_Clip_Fragment extends Fragment {

    FragmentAddClipBinding binding;
Handler handler  = new Handler();
    private ProgressDialog progressDialog;
    private String out = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        progressDialog = new ProgressDialog(getContext());
/*
        filePicker.setFileSelectedListener(new FilePicker.OnFileSelectedListener() {
            @Override
            public void onFileSelectSuccess(@NonNull String s) {
                if (new File(s).exists()) {
                    binding.tvName.setText(new File(s).getName());
                    //String out = Environment.getExternalStorageDirectory().getPath()+"/"+Environment.DIRECTORY_DOWNLOADS+"/"+new File(s).getName();
                    out = getContext().getExternalCacheDir().getPath() + "/" + new File(s).getName();
                    File file = new File(s);
                    long fileSize = file.length();
                    long sixeinkb = fileSize / 1024;
                    long sizeinmb = sixeinkb / 1024;
                    if (sizeinmb > 10) {
                        out="";
                        StyleableToast.makeText(getContext(), "Cyrrently We Have A Limit Of 10MB File Size, Please Select A Smaller File", R.style.mytoast).show();
                        return;
                        //VideoCompress.compressVideoHigh(s, out, new listener(out));
                    } else {
                        out = s;
                        binding.video.setVideoPath(s);
                        MediaController mediaController = new MediaController(getContext());
                        binding.video.setMediaController(mediaController);
                        binding.video.setVisibility(View.VISIBLE);
                        binding.video.start();
                        binding.cross.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFileSelectFailure() {

            }
        });
*/
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
/*
        filePicker.onRequestPermissionsResult(requestCode, permissions, grantResults);
*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add__clip_, container, false);
        binding = FragmentAddClipBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());

        MediaController mediaController = new MediaController(getContext());
        binding.video.setMediaController(mediaController);

        Init();


    }

    private void Init() {
        binding.ivAddVideo.setOnClickListener(view -> {
          /*  filePicker.pickFile(false, true, false, false, false, false);*/
        });
        binding.cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                out = "";
                try {
                    binding.video.setVisibility(View.GONE);
                    binding.cross.setVisibility(View.GONE);
                    binding.video.stopPlayback();
                    binding.tvName.setText("Click to add video");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.btnSubmit.setOnClickListener(view -> {
            if (out.equalsIgnoreCase("")) {
                StyleableToast.makeText(getContext(), "Please Select A Video First", R.style.mytoast).show();
                return;
            }
            File file = new File(out);
            if (!file.exists()) {
                return;
            }
            long fileSize = file.length();
            long sixeinkb = fileSize / 1024;
            long sizeinmb = sixeinkb / 1024;
            if (sizeinmb > 10) {
                StyleableToast.makeText(getContext(), "Cyrrently We Have A Limit Of 10MB File Size, Please Select A Smaller File", R.style.mytoast).show();
                return;
            } else {
                Add();
            }
        });
    }





    public void Add() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("sender", PreferenceFile.getUser().getUser().getId());
        MethodClass.CallMultipart(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                out = "";
                Gson gson = new Gson();
                AddClipResponse loginResponse = gson.fromJson(res.toString(), AddClipResponse.class);
                if (loginResponse != null) {
                    StyleableToast.makeText(getContext(), "Post Created Successfully", Toast.LENGTH_SHORT, R.style.mytoast).show();
                    Navigation.findNavController(binding.getRoot()).popBackStack();
                }
                //StyleableToast.makeText(getContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();
            }
        }, Constants.BASE_URL + Constants.CREATE_CLIP, getActivity(),handler, map, out, "attachment");
        //binding.prograss.setVisibility(View.VISIBLE);

      /*  MethodClass.CallFileUpload(new Response() {
            @Override
            public void onResponse(JSONObject res) {

                if (res!=null){

                }

            }
        }, Constants.TEST, getActivity(), map, out, "attachment", binding.prograss);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        trimCache(out, getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        trimCache(out, getContext());
    }

    public static void trimCache(String path, Context context) {
        File file = new File(path);
        boolean bb = file.delete();
        if (bb) {
            //Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(context, "Not Deleted", Toast.LENGTH_SHORT).show();
        }

    }


}