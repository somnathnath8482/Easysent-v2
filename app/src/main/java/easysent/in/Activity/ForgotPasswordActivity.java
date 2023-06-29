package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import easysent.in.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewById(R.id.btn_sendOtp).setOnClickListener(view -> startActivity(new Intent(this,OtpActivity.class)));
    }
}