package com.uniquestudio.technorange;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartItemViewHolder extends RecyclerView.ViewHolder {

    public ImageView cartItemImgV;
    public TextView cartItemTitleTxt, cartItemPriceTxt, cartItemQtyTxt;
    public ImageButton cartItemEditImgB, cartItemDelImgB;

    public CartItemViewHolder(@NonNull View itemView) {
        super(itemView);
        cartItemImgV = itemView.findViewById(R.id.cart_item_imgv);
        cartItemTitleTxt = itemView.findViewById(R.id.cart_item_title_txt);
        cartItemPriceTxt = itemView.findViewById(R.id.cart_item_price_txt);
        cartItemQtyTxt = itemView.findViewById(R.id.cart_item_qty_txt);
        cartItemEditImgB = itemView.findViewById(R.id.cart_item_edit_imgb);
        cartItemDelImgB = itemView.findViewById(R.id.cart_item_del_imgb);

    }

}
