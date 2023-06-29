package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Register.RegisterResponse;
import easysent.in.databinding.ActivityForgotPasswordBinding;
import io.github.muddz.styleabletoast.StyleableToast;

public class ForgotPasswordActivity extends AppCompatActivity {
    ActivityForgotPasswordBinding binding;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Init();
    }
    private void Init() {
        binding.btnSendOtp.setOnClickListener(view -> {
            //  startActivity(new Intent(this,OtpActivity.class));


            HashMap<String, Object> map = new HashMap<>();
            try {
                map.put("email", MethodClass.CheckEmpty(binding.edEmail));

            } catch (Exception e) {
                EditText editText = binding.getRoot().findViewById(Integer.parseInt(e.getMessage()));
                editText.requestFocus();
                e.printStackTrace();
                return;
            }
            Send(map);
        });

        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignupActivity.class));
            }
        });
    }

    private void Send(HashMap<String, Object> map) {
        MethodClass.Call(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                if (res != null) {
                    Gson json = new Gson();

                    RegisterResponse registerResponse = json.fromJson(res.toString(), RegisterResponse.class);

                    if (registerResponse!=null){
                        StyleableToast.makeText(getApplicationContext(), registerResponse.getSuccess(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                        Intent intent   = new Intent(getApplicationContext(),OtpActivity.class);
                        intent.putExtra("otp", registerResponse.getOtp());
                        intent.putExtra("from", "forgot");
                        intent.putExtra("email", binding.edEmail.getText().toString().trim());
                        startActivity(intent);
                    }
                }
            }
        }, Constants.BASE_URL + Constants.FORGOT_PASSWORD, ForgotPasswordActivity.this, handler, map);

    }

}