package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.adapter.recycleadapter.BaseQuickAdapter;
import com.eqdd.common.adapter.recycleadapter.BaseViewHolder;
import com.eqdd.common.adapter.recycleadapter.ItemClickSupport;
import com.eqdd.common.utils.ImageUtil;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.SelectBean;
import com.eqdd.library.service.ConversitionListService;
import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import rx.Observable;

@Route(path = Config.LIBRARY_SELECT)
public class SelectActivity extends CommonActivity {
    @Autowired
    ConversitionListService conversitionListService;
    private SelectActivityCustom dataBinding;
    private ArrayList<SelectBean> selectBeans;
    private int selectMaxNum;
    private String[] headContents = new String[]{
            "同事",
            "好友",
            "群成员"
    };
    private Class[] classes = new Class[]{
            SelectTongshiActivity.class,
            SelectFriendActivity.class,
            SelectQunzuActivity.class
    };
    private ArrayList<SelectBean> selectedBeans = new ArrayList<>();
    private BaseQuickAdapter<SelectBean, BaseViewHolder> quickAdapter;
    private int selectNum;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select);
        initTopTitleBar(View.VISIBLE, "请选择");
        initTopRightText("提交", v -> {
            getSelectedBeans();
            finishAndResult();
        });
    }

    private void finishAndResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Config.SELECTED_BEANS, selectedBeans);
        setResult(Config.SUCCESS, intent);
        finish();
    }

    private void getSelectedBeans() {
        selectedBeans.clear();
        Observable.from(quickAdapter.getData())
                .filter(person -> person.isCheck())
                .subscribe(p -> selectedBeans.add(p),
                        e -> {
                        },
                        () -> {
                        });
    }

    @Override
    public void initData() {
        selectMaxNum = getIntent().getIntExtra(Config.MAX_NUM, Integer.MAX_VALUE);
        selectBeans = new ArrayList<>();

        initHeadView();
        conversitionListService.getConversitionListAsyn(selectBeen -> {
                    if (selectBeen != null) {
                        selectBeans.addAll(selectBeen);
                        quickAdapter.notifyDataChanged(selectBeans, true);
                    }
                }
        );
    }


    private void initHeadView() {
        for (int i = 0; i < headContents.length; i++) {
            int position = i;
            TextView tvContent = (TextView) dataBinding.llRoot.getChildAt(i).findViewById(R.id.tv_content);
            tvContent.setText(headContents[i]);
            RxView.clicks(dataBinding.llRoot.getChildAt(i))
                    .throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe((v) -> {
                        System.out.println(position);
                        getSelectedBeans();
                        Bundle bundle = new Bundle();
                        bundle.putInt(Config.MAX_NUM, selectMaxNum);
                        startActivityForResult(classes[position], bundle, Config.SELECT_FRIEND);
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Config.SUCCESS) {
            switch (requestCode) {
                case Config.SELECT_FRIEND:
                    selectedBeans.clear();
                    selectedBeans = data.getParcelableArrayListExtra(Config.SELECTED_BEANS);
                    finishAndResult();
                    break;

            }
        }
    }

    @Override
    public void setView() {
        if (quickAdapter == null) {
            quickAdapter = new BaseQuickAdapter<SelectBean, BaseViewHolder>(R.layout.library_list_item_5, new ArrayList<>()) {

                @Override
                protected void convert(BaseViewHolder helper, SelectBean item) {
                    helper.setText(R.id.tv_content, item.getContent());
                    ImageView imageView = helper.getView(R.id.iv_head);
                    ImageUtil.setCircleImage(item.getHead(), imageView);
                }
            };

            dataBinding.llParent.removeView(dataBinding.llRoot);
            AbsListView.LayoutParams lp
                    = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            dataBinding.llRoot.setLayoutParams(lp);
            quickAdapter.addHeaderView(dataBinding.llRoot);
            dataBinding.recyclerView.setAdapter(quickAdapter);
            ItemClickSupport.addTo(dataBinding.recyclerView)
                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            selectNum = 0;
                            Observable.from(quickAdapter.getData())
                                    .filter(person -> person.isCheck())
                                    .subscribe(p ->
                                            selectNum++, e -> {
                                    }, () -> {
                                        if (selectNum < selectMaxNum) {
                                            quickAdapter.getItem(position).setCheck(!quickAdapter.getItem(position).isCheck());
                                            quickAdapter.notifyDataSetChanged();
                                        } else {
                                            ToastUtil.showShort("超过最大可选人数");
                                        }
                                    });
                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}
