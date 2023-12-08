package com.uniquestudio.technorange;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    ImageView orderImgV;
    TextView orderTitleTxt, orderQtyTxt, orderStateTxt;
    Button orderMoreBtn;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        orderImgV = itemView.findViewById(R.id.order_imgv);
        orderTitleTxt = itemView.findViewById(R.id.order_title_txt);
        orderQtyTxt = itemView.findViewById(R.id.order_qty_txt);
        orderStateTxt = itemView.findViewById(R.id.order_state_txt);
        orderMoreBtn = itemView.findViewById(R.id.order_more_btn);
    }
}
