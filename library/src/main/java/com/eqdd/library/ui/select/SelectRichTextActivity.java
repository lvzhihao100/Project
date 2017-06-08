package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.adapter.recycleadapter.BaseItemDraggableAdapter;
import com.eqdd.common.adapter.recycleadapter.BaseViewHolder;
import com.eqdd.common.adapter.recycleadapter.ItemClickSupport;
import com.eqdd.common.adapter.recycleadapter.callback.ItemDragAndSwipeCallback;
import com.eqdd.common.adapter.recycleadapter.listener.OnItemDragListener;
import com.eqdd.common.utils.ImageUtil;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectRichTextActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.luck.picture.lib.compress.Luban;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.FunctionOptions;
import com.luck.picture.lib.model.PictureConfig;
import com.yalantis.ucrop.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = Config.LIBRARY_SELECT_RICH_TEXT)
public class SelectRichTextActivity extends CommonActivity {
    private SelectRichTextActivityCustom dataBinding;
    private List<LocalMedia> imagePath = new ArrayList<>();
    private BaseItemDraggableAdapter<LocalMedia, BaseViewHolder> quickAdapter;
    private Map<Integer, Integer> map = new HashMap<>();
    private boolean isShow = false;
    private List<LocalMedia> localList = new ArrayList<>();

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_rich_text);
        initTopRightText("完成", v -> {
            if (!TextUtils.isEmpty(dataBinding.etContent.getText())) {
                RichTextResult richTextResult = new RichTextResult();
                richTextResult.setContent(dataBinding.etContent.getText().toString().trim());
                richTextResult.setSelectMedia(localList);
                Intent intent = new Intent();
                intent.putExtra(Config.RICH_TEXT_RESULT, richTextResult);
                setResult(Config.SUCCESS, intent);
                finish();
            } else {
                ToastUtil.showShort("请先写些东西吧");
            }
        });
    }

    @Override
    public void initData() {

        String title = getIntent().getStringExtra(Config.TITLE);
        initTopTitleBar(View.VISIBLE, TextUtils.isEmpty(title) ? "请填写" : title);
        RichTextResult richTextResult = (RichTextResult) getIntent().getSerializableExtra(Config.RICH_TEXT_RESULT);
        if (richTextResult != null) {
            localList = richTextResult.getSelectMedia();
            imagePath.addAll(localList);
            dataBinding.etContent.setText(TextUtils.isEmpty(richTextResult.getContent()) ? "" : richTextResult.getContent());
        }
        imagePath.add(new LocalMedia());
    }

    @Override
    public void setView() {
        dataBinding.recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        quickAdapter = new BaseItemDraggableAdapter<LocalMedia, BaseViewHolder>(R.layout.library_list_item_image_1, imagePath) {
            @Override
            protected void convert(BaseViewHolder helper, LocalMedia item) {
                System.out.println(helper.getPosition());
                int position = helper.getPosition();
                System.out.println(position);
                if (helper.getPosition() == quickAdapter.getData().size() - 1) {
                    System.out.println("现实");
                    ImageUtil.setImage(R.mipmap.library_image_add, helper.getView(R.id.image));
                    helper.setVisible(R.id.iv_delete, false);
                    return;
                }
                ImageUtil.setImage(new File(item.getCompressPath() == null ? item.getPath() : item.getCompressPath()), helper.getView(R.id.image));
                helper.setVisible(R.id.iv_delete, isShow);
                map.put(position, position);
                helper.setOnClickListener(R.id.iv_delete, v -> {

                    quickAdapter.remove(map.get(position));
                    localList.remove((int) map.get(position));
                    for (int i = position; i < map.size(); i++) {
                        map.put(i, map.get(i) - 1);
                    }
                });
            }
        };
        ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(quickAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        itemTouchHelper.attachToRecyclerView(dataBinding.recyclerView);

// 开启拖拽
        quickAdapter.enableDragItem(itemTouchHelper);
        quickAdapter.setOnItemDragListener(new OnItemDragListener() {
            @Override
            public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

            }

            @Override
            public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

            }

            @Override
            public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

            }
        });

// 开启滑动删除
//        quickAdapter.enableSwipeItem();
//        quickAdapter.setOnItemSwipeListener(new OnItemSwipeListener() {
//            @Override
//            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//
//            }
//
//            @Override
//            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//
//            }
//
//            @Override
//            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
//
//            }
//
//            @Override
//            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
//
//            }
//        });
        dataBinding.recyclerView.setAdapter(quickAdapter);

        ItemClickSupport.addTo(dataBinding.recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                if (position == quickAdapter.getData().size() - 1) {
                    FunctionOptions options = new FunctionOptions.Builder()
                            .setType(FunctionConfig.TYPE_IMAGE)
                            .setCompress(true)
                            .setMaxSelectNum(9)
                            .setGrade(Luban.THIRD_GEAR)
                            .setImageSpanCount(3)
                            .setSelectMedia(localList)
                            .create();
                    PictureConfig.getInstance().init(options).openPhoto(SelectRichTextActivity.this, new PictureConfig.OnSelectResultCallback() {
                        @Override
                        public void onSelectSuccess(List<LocalMedia> list) {
                            localList = list;
                            imagePath.clear();
                            imagePath.addAll(localList);
                            imagePath.add(new LocalMedia());
                            quickAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onSelectSuccess(LocalMedia localMedia) {

                            System.out.println("123123");
                        }
                    });
//                    MultiImageSelector.create()
//                            .showCamera(true) // show camera or not. true by default
//                            .count(9)
//                            .multi() // single mode
//                            .origin(temp) // original select data set, used width #.multi()
//                            .start(SelectRichTextActivity.this, Config.IMAGE_SELECT);
                } else {
                    PictureConfig.getInstance().externalPicturePreview(SelectRichTextActivity.this, position, localList);
                }
            }
        })
                .setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {
                        if (position != quickAdapter.getData().size() - 1) {
                            isShow = true;
                            quickAdapter.notifyDataSetChanged();
                        }
                        return true;
                    }
                });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}
