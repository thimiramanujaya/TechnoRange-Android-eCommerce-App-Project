package com.uniquestudio.technorange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CustomerSignup extends AppCompatActivity {

    EditText nameEdt, emailEdt, phoneEdt, addressEdt, usernameEdt, passwordEdt, cpasswordEdt;
    Button customerSignUpBtn, loginDirectBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_signup);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        customerSignUpBtn = findViewById(R.id.customer_signup_btn);
        loginDirectBtn = findViewById(R.id.login_direct_btn);

        nameEdt = findViewById(R.id.name_edt);
        emailEdt = findViewById(R.id.email_edt);
        phoneEdt = findViewById(R.id.phone_edt);
        addressEdt = findViewById(R.id.address_edt);
        usernameEdt = findViewById(R.id.username_edt);
        passwordEdt = findViewById(R.id.password_edt);
        cpasswordEdt = findViewById(R.id.c_password_edt);

        progressDialog = new ProgressDialog(this);

        customerSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupCustomer();
            }
        });

        loginDirectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logInActivity = new Intent(CustomerSignup.this, LoginActivity.class);
                startActivity(logInActivity);
            }
        });

    }

    private void signupCustomer() {
        String name = nameEdt.getText().toString();
        String email = emailEdt.getText().toString();
        String phone = phoneEdt.getText().toString();
        String address = addressEdt.getText().toString();
        String username = usernameEdt.getText().toString();
        String password = passwordEdt.getText().toString();
        String c_password = cpasswordEdt.getText().toString();

        if(name.isEmpty()) {
            nameEdt.setError("Name is Required");
            showErrorToastMsg();
        }
        else if(email.isEmpty()) {
            emailEdt.setError("Email is Required");
            showErrorToastMsg();
        }
        else if(phone.isEmpty()) {
            phoneEdt.setError("Phone is Required");
            showErrorToastMsg();
        }
        else if(username.isEmpty()) {
            usernameEdt.setError("Username is Required");
            showErrorToastMsg();
        }
        else if(password.isEmpty()) {
            passwordEdt.setError("Password is Required");
            showErrorToastMsg();
        }
        else if(c_password.isEmpty()) {
            cpasswordEdt.setError("Confirm Password is Required");
            showErrorToastMsg();
        }
        else if(!password.equals(c_password)) {
            Toast.makeText(getApplicationContext(), "Passwords are not matched. Try Again",Toast.LENGTH_LONG).show();
        }
        else {
            progressDialog.setTitle("Signing up is in Progress");
            progressDialog.setMessage("Please wait, while checking your credentials");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            saveCustomerInputs(name, email, phone, address, username, password);

        }
    }

    private void saveCustomerInputs(String name, String email, String phone, String address, String username, String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // check whether given email or username already exists on the db
                 if((snapshot.child("Users").child(username).exists())) {
                     Toast.makeText(getApplicationContext(), "Already SignUp using this Email or Username",Toast.LENGTH_SHORT).show();
                     progressDialog.dismiss();
                     Toast.makeText(getApplicationContext(), "Try Again with different Email or Username",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     HashMap<String, Object> customerDataMap = new HashMap<>();
                     customerDataMap.put("name", name);
                     customerDataMap.put("email", email);
                     customerDataMap.put("phone", phone);
                     customerDataMap.put("address", address);
                     customerDataMap.put("username", username);
                     customerDataMap.put("password", password);

                     RootRef.child("Users").child(username).updateChildren(customerDataMap)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful()) {
                                         Toast.makeText(getApplicationContext(), "Successfully SignedUp to TechnoRange",Toast.LENGTH_LONG).show();
                                         progressDialog.dismiss();
                                         Intent logInActivity = new Intent(CustomerSignup.this, LoginActivity.class);
                                         startActivity(logInActivity);
                                     }
                                     else {  // in the case of, not successful the completion due to some kinda network issues
                                         progressDialog.dismiss();
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