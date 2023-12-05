package com.uniquestudio.technorange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView productTitleTxt2, productPriceTxt2, productAvailQtyTxt2, quantityTxt2, productDescriptTxt2;
    ImageButton backImgB, qtyAddImgB, qtyRemoveImgB, homeImgB, cartImgB;
    Button addCartBtn, buynowBtn;
    ImageView productImgV2;
    int quantity, AvailQty;
    private String ProductID = null;
    private Products products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // to hide Action bar
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        productTitleTxt2 = findViewById(R.id.product_title_txt2);
        productPriceTxt2 = findViewById(R.id.product_price_txt2);
        productAvailQtyTxt2 = findViewById(R.id.product_avail_qty_txt2);
        quantityTxt2 = findViewById(R.id.quantity_txt2);
        productDescriptTxt2 = findViewById(R.id.product_descript_txt2);

        backImgB = findViewById(R.id.back_imgb);
        qtyAddImgB = findViewById(R.id.qty_add_imgb);
        qtyRemoveImgB = findViewById(R.id.qty_remove_imgb);
        homeImgB = findViewById(R.id.home_imgb);
        cartImgB = findViewById(R.id.cart_imgb);

        addCartBtn = findViewById(R.id.add_cart_btn);
        buynowBtn = findViewById(R.id.buynow_btn);

        productImgV2 = findViewById(R.id.product_imgv2);

        ProductID = getIntent().getStringExtra("pid");
        getProductDetails(ProductID);


        backImgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        qtyAddImgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(quantityTxt2.getText().toString());
                if(quantity < 25) {
                    quantity++;
                    quantityTxt2.setText(String.valueOf(quantity));
                }
                else {
                    Toast.makeText(ProductDetailsActivity.this, "Oops!! the maximum limit of quantity can purchase, is reached", Toast.LENGTH_LONG).show();
                }
            }
        });

        qtyRemoveImgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity = Integer.parseInt(quantityTxt2.getText().toString());
                if(quantity > 0) {
                    quantity--;
                    if(quantity != 0) {
                        quantityTxt2.setText(String.valueOf(quantity));
                    }
                    else {
                        Toast.makeText(ProductDetailsActivity.this, "Please select at least one item", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(ProductDetailsActivity.this, "Oops!! quantity cannot be minus value", Toast.LENGTH_LONG).show();
                }
            }
        });

        homeImgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent1 = new Intent(ProductDetailsActivity.this, CustomerActivity.class);
                startActivity(homeIntent1);
            }
        });

        cartImgB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent2 = new Intent(ProductDetailsActivity.this, CustomerActivity.class);
                // use putExtra method to identify an intent specifically & implement relevant function according to that
                // method to implement navigation from activity to a fragment in another activity
                homeIntent2.putExtra("isNavigToCartFragment", 1);
                startActivity(homeIntent2);
            }
        });

        addCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProductToCartList();
            }
        });
    }

    private void addProductToCartList() {

        String getCurrentDate, getCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        getCurrentDate =  currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        getCurrentTime =  currentTime.format(calendar.getTime());

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("CartList");

        final HashMap<String, Object> cartMap = new HashMap<>();
        if (quantity <= AvailQty) {
            cartMap.put("pid", ProductID);
            cartMap.put("date", getCurrentDate);
            cartMap.put("time", getCurrentTime);
            cartMap.put("image", products.getImage());
            cartMap.put("ptitle", productTitleTxt2.getText().toString());
            cartMap.put("price", products.getPrice());
            cartMap.put("quantity", quantityTxt2.getText().toString());
            cartMap.put("discount", "");
            cartMap.put("state", "added to cart");

            CartListRef.child(CurrentUser.currentOnlineUser.getUsername()).child("CartProducts").child(ProductID)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ProductDetailsActivity.this, "Product added to Cart Successfully", Toast.LENGTH_LONG).show();

                                Intent homeIntent3 = new Intent(ProductDetailsActivity.this, CustomerActivity.class);
                                homeIntent3.putExtra("isNavigToCartFragment", 1);
                                startActivity(homeIntent3);

                            }
                            else {
                                Toast.makeText(ProductDetailsActivity.this, "Error occurred! Please try again later", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Oops!!, given quantity is higher than available quantity of the product", Toast.LENGTH_LONG).show();
        }

    }

    private void getProductDetails(String productID) {

        DatabaseReference ProductRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Products");
        ProductRef.child(ProductID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    products = snapshot.getValue(Products.class);

                    productTitleTxt2.setText(products.getPtitle());
                    productPriceTxt2.setText("Rs. "+products.getPrice());
                    productAvailQtyTxt2.setText("Available Quantity : "+products.getAvail_quantity());
                    productDescriptTxt2.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImgV2);

                    AvailQty = Integer.parseInt(products.getAvail_quantity());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}