package com.uniquestudio.technorange;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ImageView productImgV;
    public TextView productTitleTxt, productPriceTxt;

    ProductClickListener listener;


    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        productImgV = itemView.findViewById(R.id.product_imgv);
        productTitleTxt = itemView.findViewById(R.id.product_title_txt);
        productPriceTxt = itemView.findViewById(R.id.product_price_txt);
    }
    
    public void setProductClickListener(ProductClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view, getAdapterPosition(), false);

    }
}
