package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.adapter.recycleadapter.BaseQuickAdapter;
import com.eqdd.common.adapter.recycleadapter.BaseViewHolder;
import com.eqdd.common.adapter.recycleadapter.ItemClickSupport;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.ImageUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectQunzuActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.rongyun.GroupBean;
import com.eqdd.library.http.HttpConfig;

import java.util.ArrayList;
import java.util.List;
@Route(path = Config.LIBRARY_SELECT_QUNZU)
public class SelectQunzuActivity extends CommonActivity {
    private SelectQunzuActivityCustom dataBinding;
    private BaseQuickAdapter<GroupBean, BaseViewHolder> quickAdapter;
    private List<GroupBean> groupBeans = new ArrayList<>();
    private int num;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_qunzu);
        initTopTitleBar(View.VISIBLE, "选择群");
    }

    @Override
    public void initData() {
        num = getIntent().getIntExtra(Config.MAX_NUM, Integer.MAX_VALUE);
        new HttpPresneter.Builder()
                .Url(HttpConfig.GROUP_LIST_INFO)
                .Params("uid", user.getUname())
                .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        BaseBean<GroupBean> groupBeanBaseBean = GsonUtils.changeGsonToBaseBean((String) o, GroupBean.class);
                        if (groupBeanBaseBean.getItems() != null) {
                            groupBeans = groupBeanBaseBean.getItems();
                            if (quickAdapter != null) {
                                ArrayList<GroupBean> temp = new ArrayList<>();
                                temp.addAll(groupBeans);
                                quickAdapter.notifyDataChanged(temp, true);
                            }
                        }

                    }
                })
                .build()
                .postString();
    }

    @Override
    public void setView() {

        if (quickAdapter == null) {
            quickAdapter = new BaseQuickAdapter<GroupBean, BaseViewHolder>(R.layout.library_list_item_2, new ArrayList<>()) {

                @Override
                protected void convert(BaseViewHolder helper, GroupBean item) {
                    helper.setText(R.id.tv_name, item.getGroupname());
                    ImageView imageView = helper.getView(R.id.iv_head);
                    ImageUtil.setCircleImage(item.getGroupphoto(), imageView);
                }
            };
            dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            dataBinding.recyclerView.setAdapter(quickAdapter);
            ArrayList<GroupBean> temp = new ArrayList<>();
            temp.addAll(groupBeans);
            quickAdapter.notifyDataChanged(temp, true);
            ItemClickSupport.addTo(dataBinding.recyclerView)
                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("group", quickAdapter.getItem(position));
                            bundle.putInt(Config.MAX_NUM, num);
                            startActivityForResult(SelectQunMemberActivity.class, bundle, Config.SELECT_QUN_MEMBER);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Config.SUCCESS) {
            if (requestCode == Config.SELECT_QUN_MEMBER) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(Config.SELECTED_BEANS, data.getParcelableArrayListExtra(Config.SELECTED_BEANS));
                setResult(Config.SUCCESS, intent);
                finish();
            }
        }
    }
}
