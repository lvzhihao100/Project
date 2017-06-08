package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.adapter.recycleadapter.BaseQuickAdapter;
import com.eqdd.common.adapter.recycleadapter.BaseViewHolder;
import com.eqdd.common.adapter.recycleadapter.ItemClickSupport;
import com.eqdd.common.utils.ImageUtil;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectFromPhoneActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.Person;
import com.eqdd.library.utils.ContactsUtil;
import com.eqdd.library.widget.PinItemDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
@Route(path = Config.LIBRARY_SELECT_FROM_PHONE)
public class SelectFromPhoneActivity extends CommonActivity {
    private SelectFromPhoneActivityCustom dataBinding;
    private int selectMaxNum;
    private ArrayList<Person> selectedBeans = new ArrayList<>();
    private ArrayList<Person> selectBeans;
    private BaseQuickAdapter<Person, BaseViewHolder> quickAdapter;
    private int selectNum;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_from_phone);
        initTopTitleBar(View.VISIBLE, "请选择");
        initTopRightText("提交", v -> {
            getSelectedBeans();
            finishAndResult();
        });
    }

    private void finishAndResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Config.CONTRACTS, selectedBeans);
        setResult(Config.SUCCESS, intent);
        finish();
    }

    private void getSelectedBeans() {
        selectedBeans.clear();
        Observable.from(quickAdapter.getData())
                .filter(person -> person.isCheck()&&person.isEnable())
                .subscribe(p -> selectedBeans.add(p),
                        e -> {
                        },
                        () -> {
                        });
    }

    @Override
    public void initData() {
        selectMaxNum = getIntent().getIntExtra(Config.MAX_NUM, Integer.MAX_VALUE);
        ArrayList<Person> filterPerson = getIntent().getParcelableArrayListExtra(Config.CONTRACTS);

        showLoading();
        Schedulers.io().createWorker().schedule(()-> {
                selectBeans = ContactsUtil.getQuickAllContacts(SelectFromPhoneActivity.this);
                for (int i = selectBeans.size() - 1; i >= 0; i--) {
                    for (int j = filterPerson.size() - 1; j >= 0; j--) {
                        if (selectBeans.get(i).getPhone().equals(filterPerson.get(j).getPhone())) {
                            selectBeans.get(i).setCheck(true);
                            selectBeans.get(i).setEnable(false);
                            break;
                        }
                    }
                }
                Collections.sort(selectBeans, PinyinComparator.getInstance());
                AndroidSchedulers.mainThread().createWorker().schedule(() -> {
                    if (quickAdapter != null) {
                        ArrayList<Person> temp = new ArrayList<>();
                        temp.addAll(selectBeans);
                        quickAdapter.notifyDataChanged(temp, true);
                        dataBinding.waveSideBar.setData(quickAdapter.getData(), (Person person) ->
                                person.getHeaderWord(), 0);
                    }
                    hideLoading();

                });
            }
        );

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Config.CHANGE) {
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
            dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            dataBinding.recyclerView.addItemDecoration(new PinItemDecoration(this, position -> {
                if (position >= 0 && position < quickAdapter.getData().size()) {
                    return quickAdapter.getItem(position).getHeaderWord();
                } else {
                    return null;
                }
            }
                    , 0));
            quickAdapter = new BaseQuickAdapter<Person, BaseViewHolder>(R.layout.library_list_item_5, new ArrayList()) {
                @Override
                protected void convert(BaseViewHolder helper, Person item) {
                    helper.setText(R.id.tv_content, item.getName() + "   " + item.getPhone());
                    ImageView imageView = helper.getView(R.id.iv_head);
                    ImageUtil.setCircleImage("0", imageView);
                    helper.setChecked(R.id.checkbox, item.isCheck());
                    helper.getView(R.id.checkbox).setEnabled(item.isEnable());
                }
            };

            dataBinding.recyclerView.setAdapter(quickAdapter);
            if (selectBeans != null) {
                ArrayList<Person> temp = new ArrayList<>();
                temp.addAll(selectBeans);
                quickAdapter.notifyDataChanged(temp, true);
                dataBinding.waveSideBar.setData(quickAdapter.getData(), (Person person) ->
                        person.getHeaderWord(), 0);
            }
            ItemClickSupport.addTo(dataBinding.recyclerView)
                    .setOnItemClickListener((rv, position, v) -> {
                        if (!quickAdapter.getItem(position).isEnable())
                            return;
                        selectNum = 0;
                        Observable.from(quickAdapter.getData())
                                .filter(person -> person.isCheck())
                                .subscribe(p -> {
                                    selectNum++;
                                }, e -> {

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
        dataBinding.floatingSearchView.setOnQueryChangeListener((oldQuery, newQuery) -> {
                    ArrayList<Person> temp = new ArrayList<>();
                    if (TextUtils.isEmpty(newQuery)) {
                        temp.addAll(selectBeans);
                        quickAdapter.notifyDataChanged(temp, true);
                    } else {
                        Observable.from(selectBeans)
                                .filter(person -> person.getPhone().contains(newQuery) || person.getName().contains(newQuery))
                                .subscribe(person -> temp.add(person)
                                        , (e) -> {

                                        },
                                        () ->
                                                quickAdapter.notifyDataChanged(temp, true)
                                );
                    }
                    dataBinding.waveSideBar.setData(quickAdapter.getData(), (Person person) ->
                            person.getHeaderWord(), 0);
                }
        );
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}
