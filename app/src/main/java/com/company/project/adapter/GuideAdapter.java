package com.company.project.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.activity.MainActivity;
import com.company.project.base.BaseActivity;
import com.company.project.config.CollectionConfig;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

/**
 * @author Tinlone
 * @date 2018/3/27.
 */

public class GuideAdapter extends PagerAdapter {

    private BaseActivity mActivity;

    public GuideAdapter(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public int getCount() {
        return CollectionConfig.GUIDE_IMAGES.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.item_guide_image, container, false);
        ImageView imageView = view.findViewById(R.id.ivGuide);
        TextView tvToUse = view.findViewById(R.id.tvToUse);
        if (position == getCount() - 1) {
            tvToUse.setVisibility(View.VISIBLE);
            tvToUse.setOnClickListener(v -> {
                mActivity.startActivity(MainActivity.class);
                mActivity.finish();
            });
        }
        imageView.setImageResource(CollectionConfig.GUIDE_IMAGES.get(position));
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        /* super.destroyItem(container, position, object); */
        container.removeView((View) object);
    }
}
