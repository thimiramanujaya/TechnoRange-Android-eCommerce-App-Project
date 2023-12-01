package com.uniquestudio.technorange;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutItemViewHolder extends RecyclerView.ViewHolder {

    public TextView checkoutItemTitleTxt, checkoutItemSubTotTxt;

    public CheckoutItemViewHolder(@NonNull View itemView) {
        super(itemView);
        checkoutItemTitleTxt = itemView.findViewById(R.id.checkout_item_title_txt);
        checkoutItemSubTotTxt = itemView.findViewById(R.id.checkout_item_subtot_txt);
    }
}
