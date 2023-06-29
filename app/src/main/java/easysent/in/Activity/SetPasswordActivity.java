package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.databinding.ActivitySetPasswordBinding;
import io.github.muddz.styleabletoast.StyleableToast;

public class SetPasswordActivity extends AppCompatActivity {
ActivitySetPasswordBinding binding;
Handler handler = new Handler();
    String email = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
        }
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
                        StyleableToast.makeText(getApplicationContext(), res.getString("success"), Toast.LENGTH_SHORT, R.style.mytoast).show();
                        startActivity(new Intent(SetPasswordActivity.this,LoginActivity.class));
                        finishAffinity();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SetPasswordActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }


            }
        }, Constants.BASE_URL + Constants.SET_PASSWORD, SetPasswordActivity.this,handler, map);
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