package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import easysent.in.R;

public class OtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        findViewById(R.id.btn_verify).setOnClickListener(view -> startActivity(new Intent(this,SetPasswordActivity.class)));
    }
}