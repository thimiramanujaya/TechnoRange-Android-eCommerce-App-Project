package com.uniquestudio.technorange;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    TextView greetingTxt, usernameTxt;
    private RecyclerView.Adapter bannerAdapter;
    private RecyclerView bannerRecylV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_home, container, false);  default line

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        greetingTxt = (TextView) view.findViewById(R.id.greeting_txt);
        usernameTxt = (TextView) view.findViewById(R.id.username_txt);
        bannerRecylV = (RecyclerView) view.findViewById(R.id.banner_recylv);

        setupBannerRecylV();

        usernameTxt.setText(CurrentUser.currentOnlineUser.getName());

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int hour = cal.get(Calendar.HOUR_OF_DAY);

        String greeting = null;
        if(hour < 12) {
            greeting = "Good Morning!";
        }
        else if (hour < 15) {
            greeting = "Good Afternoon!";
        }
        else if (hour < 19) {
            greeting = "Good Evening!";
        }
        else {
            greeting = "Good Night!";
        }
        greetingTxt.setText(greeting);

        return view;

    }

    private void setupBannerRecylV() {
        LinearLayoutManager linearLytManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        bannerRecylV.setLayoutManager(linearLytManager);

        ArrayList<BannerContent> banner = new ArrayList<>();
        banner.add(new BannerContent(R.string.banner_heading_one, R.string.banner_descript_one, "", R.drawable.banner_img_1));
        banner.add(new BannerContent(R.string.banner_heading_two, R.string.banner_descript_two, "", R.drawable.banner_img_2));
        banner.add(new BannerContent(R.string.banner_heading_three, R.string.banner_descript_three, "", R.drawable.banner_img_3));

        bannerAdapter = new BannerRecylVAdapter(banner);
        bannerRecylV.setAdapter(bannerAdapter);
    }
}