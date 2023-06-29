package easysent.in.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.os.Bundle;

import easysent.in.R;
import easysent.in.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
public static ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}