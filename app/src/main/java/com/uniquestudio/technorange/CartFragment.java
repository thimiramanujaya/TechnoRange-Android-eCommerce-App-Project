package com.uniquestudio.technorange;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CartFragment extends Fragment {

    TextView myCartTxt, cartTotalTxt, noProductTxt;
    RecyclerView cartItemsRecylV;
    RecyclerView.LayoutManager cartListLytManager;
    ImageView cartSettingImgV, cartMoreOptImgV;
    Button cartCheckoutBtn;

    float TotalPrice = 0.00F;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        myCartTxt = view.findViewById(R.id.mycart_txt);
        cartTotalTxt = view.findViewById(R.id.cart_total_txt);
        noProductTxt = view.findViewById(R.id.no_product_txt);

        cartItemsRecylV = view.findViewById(R.id.cartitems_recylv);
        cartListLytManager = new LinearLayoutManager(getContext());  // default vertical layout
        cartItemsRecylV.setLayoutManager(cartListLytManager);

        cartSettingImgV = view.findViewById(R.id.cart_setting_imgv);
        cartMoreOptImgV = view.findViewById(R.id.cart_more_opt_imgv);

        cartCheckoutBtn = view.findViewById(R.id.cart_checkout_btn);

        String TotalPriceTxt = "Total: Rs. " + String.valueOf(TotalPrice);
        cartTotalTxt.setText(TotalPriceTxt);

        cartCheckoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TotalPrice != 0.0) {
                    Intent checkoutActivity = new Intent(getActivity(), CheckoutActivity.class);
                    startActivity(checkoutActivity);
                }
                else {
                    Toast.makeText(getActivity(), "Please add products to cart before checkout", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (TotalPrice != 0.0) {
            noProductTxt.setVisibility(View.GONE);
        }
        else {
            noProductTxt.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("CartList");

        FirebaseRecyclerOptions<CartList> firebaseCartRecylOpts = new FirebaseRecyclerOptions.Builder<CartList>()
                .setQuery(CartListRef.child(CurrentUser.currentOnlineUser.getUsername()).child("CartProducts"), CartList.class)
                .build();

        FirebaseRecyclerAdapter<CartList, CartItemViewHolder> firebaseCartRecylAdapter =
                new FirebaseRecyclerAdapter<CartList, CartItemViewHolder>(firebaseCartRecylOpts) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartItemViewHolder holder, int position, @NonNull CartList model) {
                        holder.cartItemTitleTxt.setText(model.getPtitle());
                        holder.cartItemPriceTxt.setText("Rs. "+model.getPrice());
                        holder.cartItemQtyTxt.setText(model.getQuantity());
                        Picasso.get().load(model.getImage()).into(holder.cartItemImgV);

                        float ItemSubTotal = Float.parseFloat(model.getPrice()) * Integer.parseInt(model.getQuantity());
                        TotalPrice = TotalPrice + ItemSubTotal;

                        if (TotalPrice != 0.0) {
                            noProductTxt.setVisibility(View.GONE);
                        }
                        else {
                            noProductTxt.setVisibility(View.VISIBLE);
                        }

                        String TotalPriceTxt = "Total: Rs. " + String.valueOf(TotalPrice);
                        cartTotalTxt.setText(TotalPriceTxt);

                        holder.cartItemEditImgB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent productDetailsActivity = new Intent(getContext(), ProductDetailsActivity.class);
                                productDetailsActivity.putExtra("pid", model.getPid());
                                startActivity(productDetailsActivity);
                            }
                        });

                        holder.cartItemDelImgB.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(holder.cartItemDelImgB.getContext());
                                builder.setTitle("Are you sure?");
                                builder.setMessage("Do you want to remove this item?");

                                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        float ItemSubTotal = Float.parseFloat(model.getPrice()) * Integer.parseInt(model.getQuantity());
                                        TotalPrice = TotalPrice - ItemSubTotal;

                                        if (TotalPrice != 0.0) {
                                            noProductTxt.setVisibility(View.GONE);
                                        }
                                        else {
                                            noProductTxt.setVisibility(View.VISIBLE);
                                        }

                                        String TotalPriceTxt = "Total: Rs. " + String.valueOf(TotalPrice);
                                        cartTotalTxt.setText(TotalPriceTxt);

                                        CartListRef.child(CurrentUser.currentOnlineUser.getUsername()).child("CartProducts").child(model.getPid())
                                                .removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(getContext(), "Item removed", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        // nothing happen
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart_item, parent, false);
                        CartItemViewHolder cartItemViewHolder = new CartItemViewHolder(view);
                        return cartItemViewHolder;
                    }
                };

        cartItemsRecylV.setAdapter(firebaseCartRecylAdapter);
        firebaseCartRecylAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        TotalPrice = 0.00F;
    }
}