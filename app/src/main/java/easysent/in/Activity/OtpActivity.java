package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import easysent.in.Helper.Constants;
import easysent.in.Helper.GenericTextWatcher;
import easysent.in.Helper.MethodClass;
import easysent.in.Interface.Response;
import easysent.in.R;
import easysent.in.Response.OtpResend.ResendOtpResponse;
import easysent.in.databinding.ActivityOtpBinding;
import io.github.muddz.styleabletoast.StyleableToast;

public class OtpActivity extends AppCompatActivity {
ActivityOtpBinding binding;
    String code,email,from;
    Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityOtpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent()!=null){
            code = getIntent().getStringExtra("otp");
            email = getIntent().getStringExtra("email");
            from = getIntent().getStringExtra("from");

            binding.tvOtp.setText("Otp has been send to "+email+" Please verify");

        }


        EditText[] edit = { binding.edt1,  binding.edt2,  binding.edt3,  binding.edt4};

        binding.edt1.addTextChangedListener(new GenericTextWatcher( binding.edt1, edit,0));
        binding.edt2.addTextChangedListener(new GenericTextWatcher( binding.edt2, edit,1));
        binding.edt3.addTextChangedListener(new GenericTextWatcher( binding.edt3, edit,2));
        binding.edt4.addTextChangedListener(new GenericTextWatcher( binding.edt4, edit,3));
        binding.btnVerify.setOnClickListener(view -> {
            // startActivity(new Intent(this,SetPasswordActivity.class));

            String OTP = binding.edt1.getText().toString().trim()+
                    binding.edt2.getText().toString().trim()+
                    binding.edt3.getText().toString().trim()+
                    binding.edt4.getText().toString().trim();

            if (OTP.length()<4){
                Toast.makeText(getApplicationContext(), "Please Enter 4 Digit Valid Otp", Toast.LENGTH_SHORT).show();
                return;
            }

            VeRyfy(OTP);



        });

        binding.btnSendAgain.setOnClickListener(view -> {
            Resend();
        });
    }

    private void Resend() {
        HashMap<String ,Object> map =new HashMap<>();
        map.put("email", email);

        MethodClass.Call(new Response() {
            @Override
            public void onResponse(JSONObject res) {
                if (res!=null){
                    Gson json = new Gson();
                    ResendOtpResponse registerResponse = json.fromJson(res.toString(), ResendOtpResponse.class);
                    Toast.makeText(OtpActivity.this, ""+registerResponse.getSuccess(), Toast.LENGTH_SHORT).show();
                    code = registerResponse.getOtp();
                    //binding.tvOtp.setText(registerResponse.getOtp());
                }
            }
        }, Constants.BASE_URL+ Constants.RESEND_OTP,OtpActivity.this,handler,map);

    }

    private void VeRyfy(String new_otp) {

        if (new_otp.equals(code)){
            HashMap<String ,Object> map =new HashMap<>();
            map.put("email", email);

            MethodClass.Call(new Response() {
                @Override
                public void onResponse(JSONObject res) {

                    try {
                        if (res!=null){
                            StyleableToast.makeText(getApplicationContext(),res.getString("success"), Toast.LENGTH_SHORT, R.style.mytoast).show();

                            if (from.equals("register")){
                                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                                finishAffinity();
                            }else if (from.equals("forgot")){

                                Intent intent  = new Intent(getApplicationContext(),SetPasswordActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, Constants.BASE_URL+ Constants.EMAIL_VERIFY,OtpActivity.this,handler,map);



        }else{
            Toast.makeText(getApplicationContext(), "Please Enter Correct Code", Toast.LENGTH_SHORT).show();
        }


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