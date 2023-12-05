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
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    ImageButton checkoutBackImgB;
    EditText fullNameEdt, shippingAddrEdt, phoneEdt, emailEdt;
    RecyclerView checkoutItemsRecylV;
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

        checkoutBackImgB = findViewById(R.id.checkout_back_imgb);
        checkoutBackImgB.setOnClickListener(new View.OnClickListener() {
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

        checkoutItemsRecylV = findViewById(R.id.checkout_items_recylv);
        checkoutListLytManager = new LinearLayoutManager(this);  // default vertical layout
        checkoutItemsRecylV.setLayoutManager(checkoutListLytManager);

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

        String currentDate, currentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        currentDate =  dateFormat.format(calendar.getTime());

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        currentTime =  timeFormat.format(calendar.getTime());

        OrderIdentifierKey = currentDate +"-"+ currentTime;

        final DatabaseReference OrdersRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Orders");

        final HashMap<String, Object> orderMap = new HashMap<>();
            orderMap.put("order_id", OrderIdentifierKey);
            orderMap.put("date", currentDate);
            orderMap.put("time", currentTime);
            orderMap.put("ordered_by", fullNameEdt.getText().toString());
            orderMap.put("shipping_address", shippingAddrEdt.getText().toString());
            orderMap.put("phone", phoneEdt.getText().toString());
            orderMap.put("email", emailEdt.getText().toString());
            orderMap.put("order_total", ItemTotal);

            OrdersRef.child(CurrentUser.currentOnlineUser.getUsername()).child(OrderIdentifierKey).child("OrderInfo")
                    .updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        DatabaseReference fromPath = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("CartList/"+CurrentUser.currentOnlineUser.getUsername()+"/CartProducts");

                        DatabaseReference toPath = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                .getReference("OrderProducts");

                        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                toPath.updateChildren((Map<String, Object>) snapshot.getValue(), new DatabaseReference.CompletionListener() {
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
                                                                orderProgressDialog.dismiss();
                                                                Toast.makeText(CheckoutActivity.this, "Your order placed", Toast.LENGTH_LONG).show();
                                                                Intent paymentActivity = new Intent(CheckoutActivity.this, PaymentActivity.class);
                                                                paymentActivity.putExtra("OrderID", OrderIdentifierKey);
                                                                paymentActivity.putExtra("Total", TotalTxt);
                                                                startActivity(paymentActivity);
                                                            }
                                                            else {
                                                                orderProgressDialog.dismiss();
                                                                Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                            DatabaseReference OrderProductsRef1 = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                                    .getReference().child("OrderProducts");
                                            OrderProductsRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot orderProductSnapshot : snapshot.getChildren()) {
                                                        for(DataSnapshot orderProductsPropSnapshot : orderProductSnapshot.getChildren()) {
                                                            String orderProductsProp = orderProductsPropSnapshot.getKey();

                                                            if (orderProductsProp.equals("state")) {
                                                                orderProductsPropSnapshot.getRef().setValue("Order placed");
                                                            }
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    orderProgressDialog.dismiss();
                                                    Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            // use different Ref name, since same in inside
                                            DatabaseReference OrderProductsRef2 = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app")
                                                    .getReference().child("OrderProducts");

                                            OrderProductsRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    for (DataSnapshot orderProductSnapshot : snapshot.getChildren()) {

                                                        DatabaseReference OrderIdRef = orderProductSnapshot.child("order_id").getRef();
                                                        DatabaseReference OrderedByRef = orderProductSnapshot.child("ordered_by").getRef();
                                                        OrderIdRef.setValue(OrderIdentifierKey);
                                                        OrderedByRef.setValue(CurrentUser.currentOnlineUser.getUsername());
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {
                                                    orderProgressDialog.dismiss();
                                                    Toast.makeText(CheckoutActivity.this, "Something went wrong..Please try again", Toast.LENGTH_SHORT).show();
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

        checkoutItemsRecylV.setAdapter(firebaseCheckoutRecylAdapter);
        firebaseCheckoutRecylAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        ItemTotal = 0.00F;
    }
}