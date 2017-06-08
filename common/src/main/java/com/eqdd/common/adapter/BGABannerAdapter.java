package com.eqdd.common.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.eqdd.common.R;
import com.squareup.picasso.Picasso;

import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by XY on 2016/9/14.
 */
public class BGABannerAdapter implements BGABanner.Adapter {
    private Context context;

    public BGABannerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void fillBannerItem(BGABanner banner, View view, Object model, int position) {
//        ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context)
                .load((String) model)
                .placeholder(R.mipmap.ic_launcher)
                .into((ImageView) view);
    }

}
