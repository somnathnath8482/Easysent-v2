package easysent.in.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.databinding.FragmentChengePasswordBinding;
import io.github.muddz.styleabletoast.StyleableToast;


public class ChengePasswordFragment extends Fragment {
FragmentChengePasswordBinding binding;
    private String email;
Handler handler = new Handler();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            email = PreferenceFile.getUser().getUser().getEmail();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_chenge_password, container, false);
        binding = FragmentChengePasswordBinding.inflate(inflater,container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
Init();
    }

    private void Init() {
        binding.btnSubmit.setOnClickListener(view -> {

            if (Validate()) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("password", binding.password.getText().toString().trim());
                map.put("email", email);
                Submit(map);
            }

        });
    }

    private void Submit(HashMap<String, Object> map) {

        MethodClass.Call(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                try {
                    if (res != null) {
                        StyleableToast.makeText(getContext(), res.getString("success"), Toast.LENGTH_SHORT, R.style.mytoast).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }


            }
        }, Constants.BASE_URL + Constants.SET_PASSWORD, getActivity(),handler, map);
    }

    private boolean Validate() {
        if (!MethodClass.checkEditText(binding.password)) {
            binding.password.setError("Please Enter Password");
            binding.password.requestFocus();
            return false;
        } else if (binding.password.getText().toString().trim().length() < 8) {
            binding.password.setError("Password Must be 8 Character Long");
            binding.password.requestFocus();
            return false;

        }

        if (!MethodClass.checkEditText(binding.conformPassword)) {
            binding.conformPassword.setError("Please  Re Enter Your Password");
            binding.conformPassword.requestFocus();
            return false;
        } else if (!binding.password.getText().toString().trim().equals(binding.conformPassword.getText().toString().trim())) {
            binding.conformPassword.setError("Conform Password Must Same as New Password");
            binding.conformPassword.requestFocus();
            return false;
        }
        return true;
    }



}