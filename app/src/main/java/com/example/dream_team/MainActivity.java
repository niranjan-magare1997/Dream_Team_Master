package com.example.dream_team;

import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    Animation middle,bottom,up;
    TextView dreamTeam,welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initialization();

        //for splash screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginScreen.class);
                startActivity(intent);
            }
        }, 5000);
    }

    private void initialization() {
        imageView = findViewById(R.id.image);
        dreamTeam = findViewById(R.id.dreamTeam);
        welcome = findViewById(R.id.welcome);

        middle = AnimationUtils.loadAnimation(this, R.anim.middle_anim);
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        up = AnimationUtils.loadAnimation(this, R.anim.up_anim);

        imageView.setAnimation(middle);
        welcome.setAnimation(up);
        dreamTeam.setAnimation(bottom);

    }
}

