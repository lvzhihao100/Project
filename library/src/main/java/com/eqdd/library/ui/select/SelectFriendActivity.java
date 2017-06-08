package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.adapter.recycleadapter.BaseQuickAdapter;
import com.eqdd.common.adapter.recycleadapter.BaseViewHolder;
import com.eqdd.common.adapter.recycleadapter.ItemClickSupport;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.PinYinUtil;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectFriendActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.FriendBean;
import com.eqdd.library.bean.SelectBean;
import com.eqdd.library.http.HttpConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;

@Route(path = Config.LIBRARY_SELECT_FRIEND)
public class SelectFriendActivity extends CommonActivity {
    private SelectFriendActivityCustom dataBinding;
    private ArrayList<SelectBean> selectBeans;
    private int selectMaxNum;
    private ArrayList<SelectBean> selectedBeans = new ArrayList<>();
    private List<Integer> ints = new ArrayList<>();
    private BaseQuickAdapter<SelectBean, BaseViewHolder> quickAdapter;
    private int selectNum;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_friend);
        initTopTitleBar(View.VISIBLE, "选择好友");
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
        new HttpPresneter.Builder()
                .Url(HttpConfig.FRIEND_LIST_INFO)
                .Params("uid", user.getUname())
                .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {
                        BaseBean<FriendBean> friendBeanBaseBean = GsonUtils.changeGsonToBaseBean((String) o, FriendBean.class);
                        if (friendBeanBaseBean.getItems() != null) {
                            for (FriendBean friendBean : friendBeanBaseBean.getItems()) {
                                SelectBean selectBean = new SelectBean();
                                selectBean.setId(friendBean.getUname());
                                selectBean.setContent(friendBean.getPname());
                                selectBean.setType(SelectBean.PRIVITE);
                                selectBean.setHead(friendBean.getIphoto());
                                String sortString = "#";
                                //汉字转换成拼音
                                String pinyin = PinYinUtil.getPinyin(selectBean.getContent());
                                if (pinyin != null) {
                                    if (pinyin.length() > 0) {
                                        sortString = pinyin.substring(0, 1).toUpperCase();
                                    }
                                }
                                // 正则表达式，判断首字母是否是英文字母
                                if (sortString.matches("[A-Z]")) {
                                    selectBean.setLetter(sortString.toUpperCase());
                                } else {
                                    selectBean.setLetter("#");
                                }
                                selectBeans.add(selectBean);
                            }
                            Collections.sort(selectBeans, PinyinComparator.getInstance());
                            if (quickAdapter != null) {
                                ArrayList<SelectBean> temp = new ArrayList<>();
                                temp.addAll(selectBeans);
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
                dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
                quickAdapter = new BaseQuickAdapter<SelectBean, BaseViewHolder>(R.layout.library_list_item_5, new ArrayList<>()) {

                    @Override
                    protected void convert(BaseViewHolder helper, SelectBean item) {

                    }
                };
                dataBinding.recyclerView.setAdapter(quickAdapter);
                if (selectBeans != null) {
                    ArrayList<SelectBean> temp = new ArrayList<>();
                    temp.addAll(selectBeans);
                    quickAdapter.notifyDataChanged(temp, true);
                }
                ItemClickSupport.addTo(dataBinding.recyclerView)
                        .setOnItemClickListener((rv, position, v) -> {
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
                        });
            }
            ArrayList<SelectBean> temp = new ArrayList<>();
            temp.addAll(selectBeans);
            quickAdapter.notifyDataChanged(temp, true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}
