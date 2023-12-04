package com.uniquestudio.technorange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    ImageButton paymentBackImgB;
    RadioGroup paymentRadioGrp;
    RadioButton paymentRadioBtn1, paymentRadioBtn2;
    EditText cardNoEdt, nameOnCardEdt, cardExpireEdt, cvvEdt;
    TextView totalPayTxt;
    Button payNowBtn;
    ProgressDialog paymentProgressDialog;

    String PaymentMethod, OrderId, TotalPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        OrderId = getIntent().getStringExtra("OrderID");
        TotalPay = getIntent().getStringExtra("Total");

        paymentBackImgB = findViewById(R.id.payment_back_imgb);
        paymentBackImgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        paymentRadioGrp = findViewById(R.id.payment_radio_grp);

        paymentRadioBtn1 = findViewById(R.id.payment_radio_btn1);
        paymentRadioBtn2 = findViewById(R.id.payment_radio_btn2);


        cardNoEdt = findViewById(R.id.card_no_edt);
        nameOnCardEdt = findViewById(R.id.name_on_card_edt);
        cardExpireEdt = findViewById(R.id.card_expire_edt);
        cvvEdt = findViewById(R.id.cvv_edt);

        paymentRadioGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (paymentRadioBtn1.isChecked()) {

                    PaymentMethod = "CARD";

                    cardNoEdt.setEnabled(true);
                    cardNoEdt.setFocusable(true);
                    cardNoEdt.setFocusableInTouchMode(true);

                    nameOnCardEdt.setEnabled(true);
                    cardExpireEdt.setEnabled(true);
                    cvvEdt.setEnabled(true);

                }
                else if (paymentRadioBtn2.isChecked()) {

                    PaymentMethod = "COD";

                    cardNoEdt.setEnabled(false);
                    cardNoEdt.setFocusable(false);
                    cardNoEdt.setFocusableInTouchMode(false);

                    nameOnCardEdt.setEnabled(false);
                    cardExpireEdt.setEnabled(false);
                    cvvEdt.setEnabled(false);
                }
                else {
                    cardNoEdt.setEnabled(false);
                    cardNoEdt.setFocusable(false);
                    cardNoEdt.setFocusableInTouchMode(false);

                    nameOnCardEdt.setEnabled(false);
                    cardExpireEdt.setEnabled(false);
                    cvvEdt.setEnabled(false);
                }
            }
        });

        totalPayTxt = findViewById(R.id.total_pay_txt);
        totalPayTxt.setText(TotalPay);

        payNowBtn = findViewById(R.id.pay_now_btn);

        payNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePaymentInputs();
            }
        });

        paymentProgressDialog = new ProgressDialog(this);

    }

    private void validatePaymentInputs() {

        if (Objects.equals(PaymentMethod, "CARD")) {

            String card_num = cardNoEdt.getText().toString();
            String name_on_card = nameOnCardEdt.getText().toString();
            String card_expire = cardExpireEdt.getText().toString();
            String cvv = cvvEdt.getText().toString();

            if(card_num.isEmpty()) {
                cardNoEdt.setError("Card Number is Mandatory");
                showErrorToastMsg();
            }
            else if(name_on_card.isEmpty()) {
                nameOnCardEdt.setError("Name on card is Mandatory");
                showErrorToastMsg();
            }
            else if(card_expire.isEmpty()) {
                cardExpireEdt.setError("Expiration is Mandatory");
                showErrorToastMsg();
            }
            else if(cvv.isEmpty()) {
                cvvEdt.setError("CVV is Mandatory");
                showErrorToastMsg();
            }
            else {
                paymentProgressDialog.setTitle("Processing Payment...");
                paymentProgressDialog.setMessage("Please wait, until complete your payment");
                paymentProgressDialog.setCanceledOnTouchOutside(false);
                paymentProgressDialog.show();

                processPayment();
            }
        }
        else if (Objects.equals(PaymentMethod, "COD")) {

            paymentProgressDialog.setTitle("Processing Payment...");
            paymentProgressDialog.setMessage("Please wait, until complete your payment");
            paymentProgressDialog.setCanceledOnTouchOutside(false);
            paymentProgressDialog.show();

            processPayment();

        }
        else {
            Toast.makeText(this, "No payment method is selected", Toast.LENGTH_LONG).show();
        }

    }

    private void processPayment() {
        final DatabaseReference OrdersRef =
                FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Orders");

        final HashMap<String, Object> paymentMap = new HashMap<>();
            paymentMap.put("payment method", PaymentMethod);
            paymentMap.put("card number", cardNoEdt.getText().toString());
            paymentMap.put("name on card", nameOnCardEdt.getText().toString());
            paymentMap.put("expiration", cardExpireEdt.getText().toString());
            paymentMap.put("cvv", cvvEdt.getText().toString());

        OrdersRef.child(CurrentUser.currentOnlineUser.getUsername()).child(OrderId).child("OrderPayment")
                .updateChildren(paymentMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            paymentProgressDialog.dismiss();

                            DatabaseReference StateRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                    .getReference().child("Orders").child(CurrentUser.currentOnlineUser.getUsername()).child(OrderId).child("OrderInfo");

                            StateRef.child("state").setValue("Payment completed").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PaymentActivity.this, "Payment Successful ✅ Thank you for Shopping with Us", Toast.LENGTH_LONG).show();

                                        Intent homeIntent4 = new Intent(PaymentActivity.this, CustomerActivity.class);
                                        homeIntent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(homeIntent4);
                                        Toast.makeText(getApplicationContext(), "Your Order will be Delivered soon..Happy Shopping 🎉", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(PaymentActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else {
                            paymentProgressDialog.dismiss();
                            Toast.makeText(PaymentActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showErrorToastMsg() {
        Toast.makeText(getApplicationContext(), "Required Fields cannot be Empty. Need relevant value",Toast.LENGTH_LONG).show();
    }
}