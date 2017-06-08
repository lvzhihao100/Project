package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.adapter.MyFragmentPagerAdapter;
import com.eqdd.library.R;
import com.eqdd.library.SelectTongshiActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.SectionArcBean;
import com.eqdd.library.bean.SelectBean;


import java.util.ArrayList;
import java.util.List;
@Route(path = Config.LIBRARY_SELECT_TONGSHI)
public class SelectTongshiActivity extends CommonActivity {
    private SelectTongshiActivityCustom dataBinding;
    private List<String> title = new ArrayList<>();
    private List<Fragment> views = new ArrayList<>();
    private MyFragmentPagerAdapter fragmentPagerAdapter;
    private List<String> ids = new ArrayList<>();
    private ArrayList<SelectBean> selectedBeans;


    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_tongshi);
        initTopTitleBar(View.VISIBLE, "选择同事");
        initTopRightText("提交",v->{
            selectedBeans = ((SelectTongshiFragment) fragmentPagerAdapter.getItem(dataBinding.viewPager.getCurrentItem())).onSelect();
            finishAndResult();
        });
    }
    private void finishAndResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Config.SELECTED_BEANS, selectedBeans);
        setResult(Config.SUCCESS, intent);
        finish();
    }

    @Override
    public void initData() {
        fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), title, views);
        dataBinding.viewPager.setAdapter(fragmentPagerAdapter);
        dataBinding.slidingTabStrip.setViewPager(dataBinding.viewPager);
        SectionArcBean sectionArcBean = new SectionArcBean();
        sectionArcBean.setDname("公司");
        sectionArcBean.setId(user.getComid());
        sectionArcBean.setFid("");
        addFragment(sectionArcBean);
    }
    public void addFragment(SectionArcBean sectionArcBean) {
        for (int i = 0; i < ids.size(); i++) {
            if (sectionArcBean.getFid().equals(ids.get(i))) {

                int len = ids.size();
                for (int j = len - 1; j >= i + 1; j--) {
                    title.remove(j);
                    views.remove(j);
                    ids.remove(j);
                }
                break;
            }
        }
        title.add(sectionArcBean.getDname());
        SelectTongshiFragment fragment = new SelectTongshiFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bumen", sectionArcBean);
        fragment.setArguments(bundle);
        views.add(fragment);
        ids.add(sectionArcBean.getId()+"");
        fragmentPagerAdapter.notifyDataSetChanged();
        dataBinding.viewPager.setCurrentItem(views.size() - 1);
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}
