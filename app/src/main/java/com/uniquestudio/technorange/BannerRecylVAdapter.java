package com.uniquestudio.technorange;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class BannerRecylVAdapter extends RecyclerView.Adapter<BannerRecylVAdapter.ViewHolder> {

    ArrayList<BannerContent> banner;

    public BannerRecylVAdapter(ArrayList<BannerContent> banner) {
        this.banner = banner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_banner, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bannerHeadingTxt.setText(banner.get(position).getBannerHeading());
        holder.bannerDescriptTxt.setText(banner.get(position).getBannerDescript());
        holder.bannerImgV.setImageResource(banner.get(position).getBannerImg());
    }

    @Override
    public int getItemCount() {
        return banner.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bannerHeadingTxt, bannerDescriptTxt;
        ImageView bannerImgV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerHeadingTxt = itemView.findViewById(R.id.banner_heading_txt);
            bannerDescriptTxt = itemView.findViewById(R.id.banner_descript_txt);
            bannerImgV = itemView.findViewById(R.id.banner_imgv);
        }
    }
}
