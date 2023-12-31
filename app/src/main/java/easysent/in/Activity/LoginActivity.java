package easysent.in.Activity;

import static easysent.in.Helper.Constants.FIREBASE_TOKEN;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Helper.Constants;
import easysent.in.Helper.MethodClass;
import easysent.in.Helper.SharePref.PreferenceFile;
import easysent.in.Helper.SyncData;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.Login.LoginResponse;
import easysent.in.databinding.ActivityLoginBinding;
import io.github.muddz.styleabletoast.StyleableToast;

public class LoginActivity extends AppCompatActivity {
ActivityLoginBinding binding;
Handler handler  = new Handler();
Activity activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        activity = this;
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
                    map.put("device", Build.MANUFACTURER);
                    map.put("model", Build.MODEL);
                    map.put("device_id", Build.ID);
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


                            SyncData.SyncUser(activity.getApplication(),handler,loginResponse.getUser().getEmail());

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("new_login", true);
                            startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();

        try {
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null && !task.getResult().equals("")) {
                    PreferenceFile.setData(task.getResult(), FIREBASE_TOKEN);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (PreferenceFile.isLogged()) {
            if (PreferenceFile.getFingirAuth()) {
                //Enable();
            } else {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finishAffinity();
            }
        }
    }
}