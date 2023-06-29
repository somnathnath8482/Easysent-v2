package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.content.Intent;
import android.os.Bundle;

import easysent.in.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SplashScreen.installSplashScreen(this);
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_register).setOnClickListener(view -> startActivity(new Intent(this,SignupActivity.class)));
        findViewById(R.id.btn_login).setOnClickListener(view -> startActivity(new Intent(this,MainActivity.class)));
 findViewById(R.id.btn_forgot).setOnClickListener(view -> startActivity(new Intent(this,ForgotPasswordActivity.class)));
    }


}