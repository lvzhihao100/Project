package com.eqdd.library.ui.select;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bigkoo.pickerview.TimePickerView;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.R;
import com.eqdd.library.SelectStartEndTimeActivityCustom;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
@Route(path = Config.LIBRARY_SELECT_START_END_TIME)
public class SelectStartEndTimeActivity extends CommonActivity {
    private SelectStartEndTimeActivityCustom dataBinding;
    private int type;
    private TimePickerView startTimePicker;
    private TimePickerView endTimePicker;
    private String msg;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.library_activity_select_start_end_time);
        initTopTitleBar(View.VISIBLE, "请选择");
        initTopRightText("完成", v -> {
            if (checkInfo()) {
                Intent intent = new Intent();
                intent.putExtra(Config.START_TIME, dataBinding.tvStartTime.getText().toString());
                intent.putExtra(Config.END_TIME, dataBinding.tvEndTime.getText().toString());
                setResult(Config.SUCCESS, intent);
                finish();
            } else {
                ToastUtil.showShort(msg);
            }
        });

    }

    private boolean checkInfo() {
        if (TextUtils.isEmpty(dataBinding.tvStartTime.getText())) {
            msg = "请选择开始时间";
            return false;
        }
        if (TextUtils.isEmpty(dataBinding.tvEndTime.getText())) {
            msg = "请选择结束时间";
            return false;
        }

        return true;
    }

    @Override
    public void initData() {
        type = getIntent().getIntExtra(Config.TIME_TYPE, -1);
    }

    @Override
    public void setView() {
        initStartTimePicker();
        initEndTimePicker();

    }

    private void initStartTimePicker() {
        startTimePicker = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat sf = null;
            switch (type) {
                case Config.YEAR_MONTH_DAY:
                    sf = new SimpleDateFormat("yyyy-MM-dd");
                    break;
                case Config.ALL:
                    sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
            }
            dataBinding.tvStartTime.setText(sf.format(date));
        })
                .setOutSideCancelable(true)
                .setType(type == Config.YEAR_MONTH_DAY ? TimePickerView.Type.YEAR_MONTH_DAY : TimePickerView.Type.ALL)
                .setDate(Calendar.getInstance())
                .setRange(1920, 2050)
                .build();
    }

    private void initEndTimePicker() {
        endTimePicker = new TimePickerView.Builder(this, (date, v) -> {
            SimpleDateFormat sf = null;
            switch (type) {
                case Config.YEAR_MONTH_DAY:
                    sf = new SimpleDateFormat("yyyy-MM-dd");
                    break;
                case Config.ALL:
                    sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    break;
            }
            dataBinding.tvEndTime.setText(sf.format(date));
        })
                .setOutSideCancelable(true)
                .setType(type == Config.YEAR_MONTH_DAY ? TimePickerView.Type.YEAR_MONTH_DAY : TimePickerView.Type.ALL)
                .setDate(Calendar.getInstance())
                .setRange(1920, 2050)
                .build();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.tv_start_time) {
            startTimePicker.show();

        } else if (i == R.id.tv_end_time) {
            endTimePicker.show();

        }
    }
}
