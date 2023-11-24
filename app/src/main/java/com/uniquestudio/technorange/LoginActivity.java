package com.uniquestudio.technorange;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    EditText unameEdt, passwordEdt;
    Button loginBtn, signupDirectBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        unameEdt = findViewById(R.id.uname_edt);
        passwordEdt = findViewById(R.id.password_edt);

        loginBtn = findViewById(R.id.login_btn);
        signupDirectBtn = findViewById(R.id.signup_direct_btn);

        progressDialog = new ProgressDialog(this);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        signupDirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent customerSignUp = new Intent(LoginActivity.this, CustomerSignup.class);
                startActivity(customerSignUp);
            }
        });

    }

    private void loginUser() {
        String uname = unameEdt.getText().toString();
        String pwd = passwordEdt.getText().toString();

        // hide keyboard
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(passwordEdt.getWindowToken(), 0);

        if(uname.isEmpty()) {
            unameEdt.setError("Username cannot be Empty");
            Toast.makeText(getApplicationContext(), "Username is Required",Toast.LENGTH_LONG).show();
        }
        else if(pwd.isEmpty()) {
            passwordEdt.setError("Password cannot be Empty");
            Toast.makeText(getApplicationContext(), "Password is Required",Toast.LENGTH_LONG).show();
        }
        else if(uname.isEmpty() && pwd.isEmpty()) {
            unameEdt.setError("Username cannot be Empty");
            passwordEdt.setError("Password cannot be Empty");
            Toast.makeText(getApplicationContext(), "Username and Password are Required",Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.setTitle("Login to TechnoRange");
            progressDialog.setMessage("Please wait, while checking your credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            verifyUserLogin(uname, pwd);
        }
    }

    private void verifyUserLogin(String uname, String pwd) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // check whether given username is already exists on the db as a customer username
                if((snapshot.child("Customers").child(uname).exists())) {
                    Users usersData = snapshot.child("Customers").child(uname).getValue(Users.class);
                    // verify given username and password with firebase
                    if(usersData.getUsername().equals(uname) && usersData.getPassword().equals(pwd)) {
                        Toast.makeText(getApplicationContext(), "Welcome to TechnoRange!!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent customerActivity = new Intent(LoginActivity.this, CustomerActivity.class);
                        startActivity(customerActivity);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid Credentials!! Try Again",Toast.LENGTH_SHORT).show();
                    }
                }
                // check whether given username is already exists on the db as seller username
                else if ((snapshot.child("Sellers").child(uname).exists())) {
                    Users usersData = snapshot.child("Sellers").child(uname).getValue(Users.class);
                    // verify given username and password with firebase
                    if(usersData.getUsername().equals(uname) && usersData.getPassword().equals(pwd)) {
                        Toast.makeText(getApplicationContext(), "Welcome to TechnoRange!!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        Intent sellerActivity = new Intent(LoginActivity.this, SellerActivity.class);
                        startActivity(sellerActivity);
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Invalid Credentials!! Try Again",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Credentials!! Try Again",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Please verify your credentials or SignUp Now",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }
}