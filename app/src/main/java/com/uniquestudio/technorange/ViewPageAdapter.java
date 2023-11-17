package com.uniquestudio.technorange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class ViewPageAdapter extends PagerAdapter {

    Context context;
    List<sliderLayoutContent> layoutContent;

    public ViewPageAdapter(Context context, List<sliderLayoutContent> layoutContent) {
        this.context = context;
        this.layoutContent = layoutContent;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View screenLayout = layoutInflater.inflate(R.layout.slider_layout, null);

        ImageView onboardingImgV = screenLayout.findViewById(R.id.onboarding_imgv);
        TextView headingTxt = screenLayout.findViewById(R.id.heading_txt);
        TextView descriptTxt = screenLayout.findViewById(R.id.descript_txt);

        onboardingImgV.setImageResource(layoutContent.get(position).getScreenImg());
        headingTxt.setText(layoutContent.get(position).getHeading());
        descriptTxt.setText(layoutContent.get(position).getDescript());

        container.addView(screenLayout);
        return screenLayout;
    }

    @Override
    public int getCount() {
        return layoutContent.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
