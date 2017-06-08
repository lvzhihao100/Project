package com.eqdd.login.ui;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.base.BaseActivity;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.library.base.Config;
import com.eqdd.login.GuideActivityCustom;
import com.eqdd.login.R;

@Route(path = Config.LOGIN_GUIDE)
public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {
    private GuideActivityCustom dataBinding;
    private int[] resIds = {R.mipmap.login_slide, R.mipmap.login_slide1,
            R.mipmap.login_slide2, R.mipmap.login_slide3,
            R.mipmap.login_slide4};

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_guide);
    }

    @Override
    public void initData() {
        dataBinding.pager.setAdapter(new GuideAdapter(resIds));
        dataBinding.pager.addOnPageChangeListener(this);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int arg0) {
        if (arg0 == resIds.length - 1) {
            dataBinding.stvStart.setVisibility(View.VISIBLE);
        } else {
            dataBinding.stvStart.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.stv_start) {
            SPUtil.savetFirst(Config.GUIDE_FIRST);
            ARouter.getInstance().build(Config.LOGIN_LOGIN)
                    .navigation();
            finish();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class GuideAdapter extends PagerAdapter {

        private int[] resIds;

        public GuideAdapter(int[] resIds) {
            this.resIds = resIds;
        }

        @Override
        public int getCount() {
            return resIds == null ? 0 : resIds.length;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setBackground(getResources().getDrawable(resIds[position]));
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            container.addView(iv, params);
            return iv;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

    }

}
