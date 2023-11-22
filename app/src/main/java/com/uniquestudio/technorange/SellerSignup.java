package com.uniquestudio.technorange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class SellerSignup extends AppCompatActivity {

    EditText sNameEdt, sEmailEdt, sPhoneEdt, sAddressEdt, sUsernameEdt, sPasswordEdt, scPasswordEdt;
    Button sellerSignUpBtn, sLoginDirectBtn;
    ProgressDialog sProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_signup);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sellerSignUpBtn = findViewById(R.id.seller_signup_btn);
        sLoginDirectBtn = findViewById(R.id.s_login_direct_btn);

        sNameEdt = findViewById(R.id.s_name_edt);
        sEmailEdt = findViewById(R.id.s_email_edt);
        sPhoneEdt = findViewById(R.id.s_phone_edt);
        sAddressEdt = findViewById(R.id.s_address_edt);
        sUsernameEdt = findViewById(R.id.s_username_edt);
        sPasswordEdt = findViewById(R.id.s_password_edt);
        scPasswordEdt = findViewById(R.id.s_c_password_edt);

        sProgressDialog = new ProgressDialog(this);

        sellerSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupSeller();
            }
        });

        sLoginDirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInActivity = new Intent(SellerSignup.this, LoginActivity.class);
                startActivity(logInActivity);
            }
        });
    }

    private void signupSeller() {
        String s_name = sNameEdt.getText().toString();
        String s_email = sEmailEdt.getText().toString();
        String s_phone = sPhoneEdt.getText().toString();
        String s_address = sAddressEdt.getText().toString();
        String s_username = sUsernameEdt.getText().toString();
        String s_password = sPasswordEdt.getText().toString();
        String s_c_password = scPasswordEdt.getText().toString();

        if(s_name.isEmpty()) {
            sNameEdt.setError("Name is Required");
            showErrorToastMsg();
        }
        else if(s_email.isEmpty()) {
            sEmailEdt.setError("Email is Required");
            showErrorToastMsg();
        }
        else if(s_phone.isEmpty()) {
            sPhoneEdt.setError("Phone is Required");
            showErrorToastMsg();
        }
        else if(s_username.isEmpty()) {
            sUsernameEdt.setError("Username is Required");
            showErrorToastMsg();
        }
        else if(s_password.isEmpty()) {
            sPasswordEdt.setError("Password is Required");
            showErrorToastMsg();
        }
        else if(s_c_password.isEmpty()) {
            scPasswordEdt.setError("Confirm Password is Required");
            showErrorToastMsg();
        }
        else if(!s_password.equals(s_c_password)) {
            Toast.makeText(getApplicationContext(), "Passwords are not matched. Try Again",Toast.LENGTH_LONG).show();
        }
        else {
            sProgressDialog.setTitle("Signing up is in Progress");
            sProgressDialog.setMessage("Please wait, while checking your credentials");
            sProgressDialog.setCanceledOnTouchOutside(false);
            sProgressDialog.show();

            saveCustomerInputs(s_name, s_email, s_phone, s_address, s_username, s_password);

        }
    }

    private void saveCustomerInputs(String s_name, String s_email, String s_phone, String s_address, String s_username, String s_password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check whether given email or username already exists on the db
                if((snapshot.child("Sellers").child(s_username).exists())) {
                    Toast.makeText(getApplicationContext(), "Already SignUp using this Username",Toast.LENGTH_SHORT).show();
                    sProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again with different Username",Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String, Object> sellerDataMap = new HashMap<>();
                    sellerDataMap.put("name", s_name);
                    sellerDataMap.put("email", s_email);
                    sellerDataMap.put("phone", s_phone);
                    sellerDataMap.put("address", s_address);
                    sellerDataMap.put("username", s_username);
                    sellerDataMap.put("password", s_password);

                    RootRef.child("Sellers").child(s_username).updateChildren(sellerDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "Successfully SignedUp to TechnoRange",Toast.LENGTH_LONG).show();
                                        sProgressDialog.dismiss();
                                        Intent logInActivity = new Intent(SellerSignup.this, LoginActivity.class);
                                        startActivity(logInActivity);
                                    }
                                    else {  // in the case of, not successful the completion due to some kinda network issues
                                        sProgressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Network Error: Please Try Again Later",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showErrorToastMsg() {
        Toast.makeText(getApplicationContext(), "Required Fields cannot be Empty. Need relevant value",Toast.LENGTH_LONG).show();
    }
}