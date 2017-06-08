package com.eqdd.library.ui.select;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.eqdd.common.adapter.recycleadapter.BaseMultiItemQuickAdapter;
import com.eqdd.common.adapter.recycleadapter.BaseViewHolder;
import com.eqdd.common.adapter.recycleadapter.ItemClickSupport;
import com.eqdd.common.adapter.recycleadapter.entity.MultiItemEntity;
import com.eqdd.common.base.BaseActivity;
import com.eqdd.common.base.BaseFragment;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.ImageUtil;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.BumenListFragmentCustom;
import com.eqdd.library.R;
import com.eqdd.library.bean.EmploymentBean;
import com.eqdd.library.bean.SectionArcBean;
import com.eqdd.library.bean.SelectBean;
import com.eqdd.library.http.HttpConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

/**
 * Created by lvzhihao on 17-5-15.
 */

public class SelectTongshiFragment extends BaseFragment {
    private SectionArcBean bumen;
    private List<MultiItemEntity> sectionsTemp;
    private BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> quickAdapter;
    private BumenListFragmentCustom databinding;

    private BaseBean<SectionArcBean> sectionsCar;
    private List<SectionArcBean> sectionsSec;

    private Subscription subscribe;
    private BaseBean<EmploymentBean> sectionsEmp;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();
    private ArrayList<SelectBean> selectedBeans = new ArrayList<>();

    @Override
    protected void setView() {
        if (quickAdapter == null) {
            quickAdapter = new BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder>(new ArrayList<MultiItemEntity>()) {
                @Override
                protected void convert(BaseViewHolder helper, MultiItemEntity multiItemEntity) {
                    if (multiItemEntity.getItemType() == MultiItemEntity.CONTENT) {
                        if (multiItemEntity instanceof SectionArcBean) {
                            SectionArcBean item = (SectionArcBean) multiItemEntity;
                            if (item.getDataType() == SectionArcBean.CAREER) {
                                helper.setText(R.id.tv_name, item.getCareename());
                            } else if (item.getDataType() == SectionArcBean.SECTION) {
                                helper.setText(R.id.tv_name, item.getDname());
                            }
                        }
                    } else if (multiItemEntity.getItemType() == MultiItemEntity.TITLE) {
                        SectionArcBean item = (SectionArcBean) multiItemEntity;
                        helper.setVisible(R.id.rl_top, true);
                        helper.setText(R.id.tv_top_title, item.getTitle());
                    } else if (multiItemEntity.getItemType() == MultiItemEntity.EMPLOYMENT) {
                        EmploymentBean item = (EmploymentBean) multiItemEntity;
                        helper.setText(R.id.tv_upper_left_content, item.getName())
                                .setText(R.id.tv_upper_right_content, item.getEntertimes())
                                .setText(R.id.tv_down_left_content, item.getPostname())
                                .setText(R.id.tv_down_right_content, item.getCareername())
                                .setChecked(R.id.cb_left, sparseBooleanArray.get(helper.getPosition(), false));
                        ImageView imageView = helper.getView(R.id.iv_head);
                        ImageUtil.setCircleImage(HttpConfig.BASE_URL_NO + item.getPhoto(), imageView);

                    }
                }
            };
            ItemClickSupport.addTo(databinding.recyclerView)
                    .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                        @Override
                        public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                            if (quickAdapter.getItem(position) instanceof SectionArcBean) {
                                SectionArcBean sectionArcBean = (SectionArcBean) quickAdapter.getItem(position);
                                if (sectionArcBean.getDataType() == SectionArcBean.SECTION && sectionArcBean.getItemType() == SectionArcBean.CONTENT) {
                                    ((SelectTongshiActivity) getActivity()).addFragment(sectionArcBean);
                                }
                            } else if (quickAdapter.getItem(position) instanceof EmploymentBean) {
                                int selectNum = 0;
                                sparseBooleanArray.put(position, !sparseBooleanArray.get(position, false));
                                for (int i = 0; i < sparseBooleanArray.size(); i++) {
                                    if (sparseBooleanArray.valueAt(i)) {
                                        selectNum++;
                                    }
                                }
                                if (selectNum >= 6) {
                                    sparseBooleanArray.delete(position);
                                    ToastUtil.showShort("可选人数最大为5");
                                } else {
                                    quickAdapter.notifyItemChanged(position);
                                }
                            }
                        }
                    });
            quickAdapter.addItemType(SectionArcBean.CONTENT, R.layout.library_list_item_section);
            quickAdapter.addItemType(SectionArcBean.TITLE, R.layout.library_item_edit_title_type);
            quickAdapter.addItemType(MultiItemEntity.EMPLOYMENT, R.layout.library_list_item_11);
            databinding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            databinding.recyclerView.setAdapter(quickAdapter);
        }
    }

    public void refresh() {
        ((BaseActivity) getActivity()).showLoading();
        sectionsTemp = new ArrayList<>();
        Observable bumenObservable = new HttpPresneter.Builder()
                .Url(HttpConfig.Select_SECTION)
                .Params("pparent", bumen.getId() + "")
                .build()
                .postListObserver();
        Observable careerObservable = new HttpPresneter.Builder()
                .Url(HttpConfig.SELECT_CAREER)
                .Params("pid", bumen.getId() + "")
                .build()
                .postStringObserver();
        Observable tongshiObservable = new HttpPresneter.Builder()
                .Url(HttpConfig.SELECT_EMPLOYMENT)
                .Params("postnum", bumen.getId() + "")
                .build()
                .postStringObserver();
        subscribe = Observable.combineLatest(bumenObservable, careerObservable, tongshiObservable, new Func3() {
            @Override
            public Object call(Object o, Object o2, Object o3) {
                sectionsSec = new Gson().fromJson((String) o, new TypeToken<List<SectionArcBean>>() {
                }.getType());
                sectionsCar = GsonUtils.changeGsonToBaseBean((String) o2, SectionArcBean.class);
                if (sectionsCar.getItems() != null) {
                    for (int i = 0; i < sectionsCar.getItems().size(); i++) {
                        sectionsCar.getItems().get(i).setDataType(SectionArcBean.CAREER);
                    }
                }
                sectionsEmp = GsonUtils.changeGsonToBaseBean((String) o3, EmploymentBean.class);
                return null;
            }
        })
                .compose(((BaseActivity) getActivity()).bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((s) -> {
                    SectionArcBean sectionArcBeanbumen = new SectionArcBean();
                    sectionArcBeanbumen.setTitle("部门");
                    sectionArcBeanbumen.setLayoutType(SectionArcBean.TITLE);
                    sectionsTemp.add(sectionArcBeanbumen);
                    if (sectionsSec != null) {
                        sectionsTemp.addAll(sectionsSec);
                    }
                    SectionArcBean sectionArcBeancar = new SectionArcBean();
                    sectionArcBeancar.setTitle("岗位");
                    sectionArcBeancar.setLayoutType(SectionArcBean.TITLE);
                    sectionsTemp.add(sectionArcBeancar);
                    if (sectionsCar.getItems() != null) {
                        sectionsTemp.addAll(sectionsCar.getItems());
                    }
                    sectionsTemp.addAll(sectionsEmp.getItems());
                    quickAdapter.notifyDataChanged(sectionsTemp, true);
                    ((BaseActivity) getActivity()).hideLoading();
                });
    }

    @Override
    protected void initData() {
        Bundle arguments = getArguments();
        bumen = (SectionArcBean) arguments.getSerializable("bumen");
        refresh();
    }

    @Override
    public ViewDataBinding initBinding(LayoutInflater inflater) {
        return databinding = DataBindingUtil.inflate(inflater, R.layout.library_fragment_bumen_list, null, false);
    }

    @Override
    public void onClick(View v) {

    }

    public ArrayList<SelectBean> onSelect(){
        for (int i = 0; i < sparseBooleanArray.size(); i++) {
            if (sparseBooleanArray.valueAt(i)){
                EmploymentBean item = (EmploymentBean) quickAdapter.getItem(sparseBooleanArray.keyAt(i));
                SelectBean selectBean = new SelectBean();
                selectBean.setType(SelectBean.PRIVITE);
                selectBean.setHead(HttpConfig.BASE_URL_NO+item.getPhoto());
                selectBean.setId(item.getUid());
                selectBean.setContent(item.getName());
                selectedBeans.add(selectBean);
            }
        }
        return selectedBeans;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscribe.unsubscribe();
    }
}
