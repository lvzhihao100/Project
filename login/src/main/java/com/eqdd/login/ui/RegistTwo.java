package com.eqdd.login.ui;

import android.databinding.DataBindingUtil;
import android.view.View;


import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.EncryptUtil;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.login.R;
import com.eqdd.login.RegistTwoCustom;
import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.StaticScheme;
import com.github.yoojia.inputs.ValueScheme;
import com.github.yoojia.inputs.WidgetAccess;

import java.util.HashMap;

@Route(path = Config.LOGIN_REGISTER_TWO)
public class RegistTwo extends CommonActivity {
    private RegistTwoCustom dataBinding;
    private String phone;
    private AndroidNextInputs inputs;
    private WidgetAccess access;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_regist_two);
        initTopTitleBar(View.VISIBLE, "注册");
    }

    @Override
    public void initData() {
        phone = getIntent().getStringExtra("phone");
        inputs = new AndroidNextInputs();
        access = new WidgetAccess(this);
        inputs  // 必选，密码
                .add(access.findEditText(R.id.et_reg_username))
                .with(StaticScheme.Required(), ValueScheme.RangeLength(8, 18));
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_regeist) {
            if (inputs.test()) {
                registNormal(dataBinding.etRegUsername.getText().toString());
            } else {
                ToastUtil.showShort("密码格式有误");
            }
        }
    }

    public void registNormal(String pass) {
        HashMap<String, String> map = new HashMap<>();
        map.put("u1", phone);
        map.put("u2", EncryptUtil.sha1(pass + "EQD"));
        new HttpPresneter.Builder()
                .Url(HttpConfig.REGISTER)
                .Params(map)
                .showProgress(RegistTwo.this)
                .setOnHttpListener(new HttpPresneter.OnHttpListener<String>() {
                    @Override
                    public void onError(Throwable e) {

                        ToastUtil.showShort("服务器异常");
                    }

                    @Override
                    public void onNext(String o) {
                        BaseBean baseBean = GsonUtils.changeGsonToBean(o, BaseBean.class);
                        ToastUtil.showLong(baseBean.getMsg());
                        if (baseBean.getStatus() == 200) {
                            finish();
                        }
                    }
                })
                .build()
                .postString();
    }
}
