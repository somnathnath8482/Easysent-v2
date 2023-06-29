package easysent.in.Activity;

import static easysent.in.Helper.Constants.Home_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import easysent.in.databinding.ActivitySignupBinding;
import io.github.muddz.styleabletoast.StyleableToast;


public class SignupActivity extends AppCompatActivity {
ActivitySignupBinding binding;
Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!binding.accept.isChecked()){
                    StyleableToast.makeText(SignupActivity.this, "Please Read AND Accept The Terms & Condition", R.style.mytoast).show();
                    return;
                }

                HashMap<String,Object> map = new HashMap<>();
                try {
                    map.put("name",MethodClass.CheckEmpty(binding.edFillname));
                    map.put("email",MethodClass.CheckEmpty(binding.email));
                    map.put("phone",MethodClass.CheckEmpty(binding.phone));
                    map.put("password",MethodClass.CheckEmpty(binding.edPassword));
                    map.put("username", MethodClass.CheckEmpty(binding.edUsername));
                    map.put("c_password",MethodClass.CheckEmpty(binding.edConPassword));
                    if(!Validate()){
                        return;
                    }
                } catch (Exception e) {
                    EditText editText = binding.getRoot().findViewById(Integer.parseInt(e.getMessage()));
                    editText.requestFocus();
                    e.printStackTrace();
                    return;
                }
                MethodClass.Call(new Response() {
                    @Override
                    public void onResponse(JSONObject res) {
                        if (res!=null){
                            Gson json = new Gson();

                            RegisterResponse registerResponse = json.fromJson(res.toString(), RegisterResponse.class);

                            if (registerResponse!=null){
                                StyleableToast.makeText(getApplicationContext(), registerResponse.getSuccess(), Toast.LENGTH_SHORT, R.style.mytoast).show();
                                Intent intent   = new Intent(getApplicationContext(),OtpActivity.class);
                                intent.putExtra("otp", registerResponse.getOtp());
                                intent.putExtra("from", "register");
                                intent.putExtra("email", binding.email.getText().toString().trim());
                                startActivity(intent);
                            }
                        }
                    }
                }, Constants.BASE_URL+ Constants.REGISTER_USER, SignupActivity.this,handler, map);
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });



        SpannableString ss = new SpannableString(getResources().getString(R.string.accepTc));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(Home_URL+"Terms"));
                startActivity(i);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.blue));
            }
        };
        ss.setSpan(clickableSpan, 10, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvAccep.setText(ss);
        binding.tvAccep.setMovementMethod(LinkMovementMethod.getInstance());


    }

    private boolean Validate() {
        if (!MethodClass.checkEditText(binding.email)) {
            binding.email.setError("Please Enter Email Id");
            binding.email.requestFocus();
            return false;
        } else if (!MethodClass.isValidEmailId(binding.email.getText().toString().trim())) {
            binding.email.setError("Please Enter A Valid Email Id");
            binding.email.requestFocus();
            return false;
        }
        if (!MethodClass.checkEditText(binding.edUsername)) {
            binding.edUsername.setError("Please Enter Username");
            binding.edUsername.requestFocus();
            return false;
        }


        if (!MethodClass.checkEditText(binding.edPassword)) {
            binding.edPassword.setError("Please Enter Password");
            binding.edPassword.requestFocus();
            return false;
        } else if (binding.edPassword.getText().toString().trim().length() < 8) {
            binding.edPassword.setError("Password Must be 8 Character Long");
            binding.edPassword.requestFocus();
            return false;

        }

        if (!MethodClass.checkEditText(binding.edConPassword)) {
            binding.edConPassword.setError("Please  Re Enter Your Password");
            binding.edConPassword.requestFocus();
            return false;
        } else if (!binding.edPassword.getText().toString().trim().equals(binding.edConPassword.getText().toString().trim())) {
            binding.edConPassword.setError("Conform Password Must Same as New Password");
            binding.edConPassword.requestFocus();
            return false;
        }
        return true;
    }



    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.thim_color));
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(getContext().getResources().getColor(R.color.white));


            int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            switch (currentNightMode) {
                case Configuration.UI_MODE_NIGHT_NO:
                    // Night mode is not active on device
                    Log.e("TAG", "onStop: theme" + "Light");
                    window.setStatusBarColor(getResources().getColor(R.color.white));
                    break;
                case Configuration.UI_MODE_NIGHT_YES:
                    // Night mode is active on device
                    Log.e("TAG", "onStop: theme" + "night");
                    window.setStatusBarColor(getResources().getColor(R.color.derk));
                    break;
            }


        }


    }
}
