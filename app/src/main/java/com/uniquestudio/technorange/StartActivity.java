package com.uniquestudio.technorange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StartActivity extends AppCompatActivity {

    private ViewPager viewPagerSlide;
    ViewPageAdapter viewPageAdapter;
    TabLayout tabLayout;
    ImageButton nextBtn;
    int position = 0;
    Button skipBtn, getStartedBtn;
    Animation getStartBtnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set activity to fullscreen *Note- Insert snippet before setting Activity layout xml
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_start);

        // hide action bar
        Objects.requireNonNull(getSupportActionBar()).hide();

        List<sliderLayoutContent> layoutContent = new ArrayList<>();
        // Add Contents to viewPagersSlides
        layoutContent.add(new sliderLayoutContent(R.string.onboard_heading_one, R.string.onboard_descript_one, R.drawable.onboarding_img_1));
        layoutContent.add(new sliderLayoutContent(R.string.onboard_heading_two, R.string.onboard_descript_two, R.drawable.onboarding_img_2));
        layoutContent.add(new sliderLayoutContent(R.string.onboard_heading_three, R.string.onboard_descript_three, R.drawable.onboarding_img_3));

        //setup viewpager
        viewPagerSlide = findViewById(R.id.view_pager_slide);
        viewPageAdapter = new ViewPageAdapter(this, layoutContent);
        viewPagerSlide.setAdapter(viewPageAdapter);

        tabLayout = findViewById(R.id.dot_layout);
        tabLayout.setupWithViewPager(viewPagerSlide);

        //add tabLayout change Listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == layoutContent.size()-1) {
                    loadLastPage();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        nextBtn = findViewById(R.id.next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = viewPagerSlide.getCurrentItem();
                if(position < layoutContent.size()) {
                    position++;
                    viewPagerSlide.setCurrentItem(position);
                }

            }
        });

        skipBtn = findViewById(R.id.skip_btn);
        getStartedBtn = findViewById(R.id.get_started_btn);
        getStartBtnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.get_started_btn_anim);

    }

    private void loadLastPage() {

        skipBtn.setVisibility(View.INVISIBLE);
        nextBtn.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        getStartedBtn.setVisibility(View.VISIBLE);

        // TODO : Set Animation to getStartedBtn

        //setup animation
        getStartedBtn.startAnimation(getStartBtnAnim);

    }
}