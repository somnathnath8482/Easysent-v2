package easysent.in.Activity;

import static easysent.in.Helper.Constants.FIREBASE_TOKEN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Login.LoginResponse;
import easysent.in.databinding.ActivityLoginBinding;
import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
Handler handler  = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Init();

    }

    private void Init() {
        binding.btnRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
        binding.joinEasysent.setOnClickListener(view -> {
            binding.btnRegister.performClick();
        });

        binding.btnForgot.setOnClickListener(view -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> map = new HashMap<>();
                try {
                    map.put("password", MethodClass.CheckEmpty(binding.edPassword));
                    map.put("username", MethodClass.CheckEmpty(binding.edUsername));
                    map.put("token", PreferenceFile.getData(FIREBASE_TOKEN));
                } catch (Exception e) {
                    EditText editText = binding.getRoot().findViewById(Integer.parseInt(e.getMessage()));
                    editText.requestFocus();
                    e.printStackTrace();
                    return;
                }

                Login(map);
            }
        });
    }

    private void Login(HashMap<String, Object> map) {

        MethodClass.Call(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                try {
                    if (res != null) {
                        if (res.getString("code").equals("-100")) {
                            StyleableToast.makeText(getApplicationContext(), res.getString("message"), Toast.LENGTH_SHORT, R.style.mytoast).show();
                            Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
                            intent.putExtra("otp", "");
                            intent.putExtra("from", "register");
                            intent.putExtra("email", res.getString("email"));
                            startActivity(intent);
                        } else if (res.getString("code").equals("-200")) {
                            Gson gson = new Gson();
                            LoginResponse loginResponse = gson.fromJson(res.toString(), LoginResponse.class);
                            StyleableToast.makeText(getApplicationContext(), loginResponse.getMessage(), Toast.LENGTH_SHORT, R.style.mytoast).show();

                            PreferenceFile.setLogged(true);
                            PreferenceFile.setUser(loginResponse);



                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finishAffinity();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(LoginActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            }
        }, Constants.BASE_URL + Constants.LOGIN, LoginActivity.this, handler, map);
    }
}