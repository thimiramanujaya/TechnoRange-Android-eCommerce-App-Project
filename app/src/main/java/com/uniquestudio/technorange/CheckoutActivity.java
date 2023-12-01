package com.uniquestudio.technorange;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CheckoutActivity extends AppCompatActivity {

    ImageButton checkoutBackImgb;
    EditText fullNameEdt, shippingAddrEdt, phoneEdt, emailEdt;
    RecyclerView checkoutItemsRecylv;
    RecyclerView.LayoutManager checkoutListLytManager;
    TextView itemTotalTxt, deliveryFeeTxt, totalTxt;
    Button placeOrderBtn;

    ProgressDialog orderProgressDialog;

    String ItemTotalTxt, TotalTxt;

    float ItemTotal;
    String OrderIdentifierKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        checkoutBackImgb = findViewById(R.id.checkout_back_imgb);
        checkoutBackImgb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        fullNameEdt = findViewById(R.id.full_name_edt);
        shippingAddrEdt = findViewById(R.id.shipping_addr_edt);
        phoneEdt = findViewById(R.id.phone_edt);
        emailEdt = findViewById(R.id.email_edt);

        fullNameEdt.setText(CurrentUser.currentOnlineUser.getName());
        shippingAddrEdt.setText(CurrentUser.currentOnlineUser.getAddress());
        phoneEdt.setText(CurrentUser.currentOnlineUser.getPhone());
        emailEdt.setText(CurrentUser.currentOnlineUser.getEmail());

        checkoutItemsRecylv = findViewById(R.id.checkout_items_recylv);
        checkoutListLytManager = new LinearLayoutManager(this);  // default vertical layout
        checkoutItemsRecylv.setLayoutManager(checkoutListLytManager);

        itemTotalTxt = findViewById(R.id.item_total_txt);
        deliveryFeeTxt = findViewById(R.id.delivery_fee_txt);
        totalTxt = findViewById(R.id.total_txt);

        placeOrderBtn = findViewById(R.id.place_order_btn);

        placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateOrderInputs();
            }
        });

        orderProgressDialog = new ProgressDialog(this);

    }

    private void validateOrderInputs() {

        String full_name = fullNameEdt.getText().toString();
        String shipping_addr = shippingAddrEdt.getText().toString();
        String phone = phoneEdt.getText().toString();
        String email = emailEdt.getText().toString();

        if(full_name.isEmpty()) {
            fullNameEdt.setError("Name is Required");
            showErrorToastMsg();
        }
        else if(shipping_addr.isEmpty()) {
            shippingAddrEdt.setError("Shipping Address is Required");
            showErrorToastMsg();
        }
        else if(phone.isEmpty()) {
            phoneEdt.setError("Phone is Required");
            showErrorToastMsg();
        }
        else if(email.isEmpty()) {
            emailEdt.setError("Email is Required");
            showErrorToastMsg();
        }
        else {
            orderProgressDialog.setTitle("Placing Order...");
            orderProgressDialog.setMessage("Please wait, until complete the placing order");
            orderProgressDialog.setCanceledOnTouchOutside(false);
            orderProgressDialog.show();

            placeOrder();

        }
    }

    private void placeOrder() {

        String getCurrentDate, getCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        getCurrentDate =  currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        getCurrentTime =  currentTime.format(calendar.getTime());

        OrderIdentifierKey = getCurrentDate +"-"+ getCurrentTime;

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Orders");

        final HashMap<String, Object> orderMap = new HashMap<>();
            orderMap.put("order id", OrderIdentifierKey);
            orderMap.put("date", getCurrentDate);
            orderMap.put("time", getCurrentTime);
            orderMap.put("ordered by", fullNameEdt.getText().toString());
            orderMap.put("shipping address", shippingAddrEdt.getText().toString());
            orderMap.put("phone", phoneEdt.getText().toString());
            orderMap.put("email", emailEdt.getText().toString());
            orderMap.put("order total", ItemTotal);
            orderMap.put("state", "Order placed");

            OrdersRef.child(CurrentUser.currentOnlineUser.getUsername()).child(OrderIdentifierKey).child("OrderInfo")
                    .updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        DatabaseReference fromPath = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("CartList/"+CurrentUser.currentOnlineUser.getUsername()+"/CartProducts");

                        DatabaseReference toPath = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("Orders/"+CurrentUser.currentOnlineUser.getUsername()+"/"+OrderIdentifierKey+"/OrderProducts");

                        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                toPath.setValue(snapshot.getValue(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                        if (error != null) {
                                            orderProgressDialog.dismiss();
                                            Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            orderProgressDialog.dismiss();
                                            FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference()
                                                    .child("CartList").child(CurrentUser.currentOnlineUser.getUsername())
                                                    .removeValue()
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        Toast.makeText(CheckoutActivity.this, "Your order placed", Toast.LENGTH_LONG).show();
                                                                        Intent paymentActivity = new Intent(CheckoutActivity.this, PaymentActivity.class);
                                                                        paymentActivity.putExtra("OrderID", OrderIdentifierKey);
                                                                        paymentActivity.putExtra("Total", TotalTxt);
                                                                        startActivity(paymentActivity);
                                                                    }
                                                                    else {
                                                                        Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                orderProgressDialog.dismiss();
                                Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else {
                        orderProgressDialog.dismiss();
                        Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void showErrorToastMsg() {
        Toast.makeText(getApplicationContext(), "Required Fields cannot be Empty. Need relevant value",Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("CartList");

        FirebaseRecyclerOptions<CartList> firebaseCheckoutRecylOpts = new FirebaseRecyclerOptions.Builder<CartList>()
                .setQuery(CartListRef.child(CurrentUser.currentOnlineUser.getUsername()).child("CartProducts"), CartList.class)
                .build();

        FirebaseRecyclerAdapter<CartList, CheckoutItemViewHolder> firebaseCheckoutRecylAdapter =
                new FirebaseRecyclerAdapter<CartList, CheckoutItemViewHolder>(firebaseCheckoutRecylOpts) {
                    @Override
                    protected void onBindViewHolder(@NonNull CheckoutItemViewHolder holder, int position, @NonNull CartList model) {

                        holder.checkoutItemTitleTxt.setText(model.getPtitle());

                        float ItemSubTotal = Float.parseFloat(model.getPrice()) * Integer.parseInt(model.getQuantity());
                        String SubTotEqu = "Rs. " + model.getPrice() + " × " + model.getQuantity() + " = Rs. " + ItemSubTotal;
                        holder.checkoutItemSubTotTxt.setText(SubTotEqu);

                        ItemTotal = ItemTotal + ItemSubTotal;

                        ItemTotalTxt = "Rs. " + String.valueOf(ItemTotal);
                        itemTotalTxt.setText(ItemTotalTxt);

                        TotalTxt = "Total : Rs. " + String.valueOf(ItemTotal);
                        totalTxt.setText(TotalTxt);
                    }

                    @NonNull
                    @Override
                    public CheckoutItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_checkout_item, parent, false);
                        CheckoutItemViewHolder checkoutItemViewHolder = new CheckoutItemViewHolder(view);
                        return checkoutItemViewHolder;
                    }
                };

        checkoutItemsRecylv.setAdapter(firebaseCheckoutRecylAdapter);
        firebaseCheckoutRecylAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        ItemTotal = 0.00F;
    }
}