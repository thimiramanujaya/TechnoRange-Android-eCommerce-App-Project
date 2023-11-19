package com.uniquestudio.technorange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button joinNowBtn, loginBtn, sellerSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        joinNowBtn = findViewById(R.id.join_now_btn);
        loginBtn = findViewById(R.id.login_btn);
        sellerSignupBtn = findViewById(R.id.seller_signup_btn);

        joinNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customerSignUp = new Intent(getApplicationContext(), CustomerSignup.class);
                startActivity(customerSignUp);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(logInActivity);
            }
        });

        sellerSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sellerSignUp = new Intent(getApplicationContext(), SellerSignup.class);
                startActivity(sellerSignUp);
            }
        });
    }
}