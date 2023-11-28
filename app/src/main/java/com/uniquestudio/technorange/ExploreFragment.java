package com.uniquestudio.technorange;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class ExploreFragment extends Fragment {

    private DatabaseReference ProductRef;
    RecyclerView productsRecylV;
    RecyclerView.LayoutManager productsLytManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        ProductRef = FirebaseDatabase.getInstance("https://technorange-3b319-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("Products");

        productsRecylV = view.findViewById(R.id.products_recylv);
        //productsRecylV.setHasFixedSize(true);

        productsLytManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        productsRecylV.setLayoutManager(productsLytManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductRef, Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, ProductViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(firebaseRecyclerOptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Products model) {
                        holder.productTitleTxt.setText(model.getPtitle());
                        holder.productPriceTxt.setText("Rs. "+model.getPrice());
                        Picasso.get().load(model.getImage()).into(holder.productImgV);

                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_product, parent, false);
                        ProductViewHolder productViewHolder = new ProductViewHolder(view);
                        return productViewHolder;
                    }
                };

        productsRecylV.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }
}