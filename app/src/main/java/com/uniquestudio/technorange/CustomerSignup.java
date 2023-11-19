package com.uniquestudio.technorange;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CustomerSignup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signup);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}