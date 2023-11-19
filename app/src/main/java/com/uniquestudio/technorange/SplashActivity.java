package com.uniquestudio.technorange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progresBar;
    TextView textV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        progresBar = findViewById(R.id.progr_bar);
        textV = findViewById(R.id.progress_text);

        progresBar.setMax(100);
        progresBar.setScaleY(2f);

        ProgressAnimation();
    }

    private void ProgressAnimation() {
        ProgressAnimation anim =new ProgressAnimation(this, textV, progresBar, 0f, 100f);
        anim.setDuration(4000);
        progresBar.setAnimation(anim);
    }

}