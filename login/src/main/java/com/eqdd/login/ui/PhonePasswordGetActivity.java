package com.eqdd.login.ui;

import android.databinding.DataBindingUtil;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.ToastUtil;
import com.eqdd.library.base.CommonActivity;
import com.eqdd.library.base.Config;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.login.PhonePasswordGetActivityCustom;
import com.eqdd.login.R;
import com.github.yoojia.inputs.AndroidNextInputs;
import com.github.yoojia.inputs.LazyLoaders;
import com.github.yoojia.inputs.StaticScheme;
import com.github.yoojia.inputs.ValueScheme;
import com.github.yoojia.inputs.WidgetAccess;
@Route(path = Config.LOGIN_PHONE_PASSWORD_GET)
public class PhonePasswordGetActivity extends CommonActivity {
    private PhonePasswordGetActivityCustom dataBinding;
    private String phone;
    private AndroidNextInputs inputs;
    private WidgetAccess access;

    @Override
    public void initBinding() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.login_activity_phone_password_get);
        initTopTitleBar(View.VISIBLE, "密码找回");
    }

    @Override
    public void initData() {
        phone = getIntent().getStringExtra("phone");
        inputs = new AndroidNextInputs();
        access = new WidgetAccess(this);
        // 必选，与邮件相同
        LazyLoaders loader = new LazyLoaders(this);
        inputs
                .add(access.findEditText(R.id.et_password))
                .with(StaticScheme.Required(), ValueScheme.RangeLength(8, 18))// 必选 ，密码8~18
                .add(access.findEditText(R.id.et_password_again))
                .with(StaticScheme.Required(), ValueScheme.EqualsTo(loader.fromEditText(R.id.et_password)));
    }

    @Override
    public void setView() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.btn_submit) {
            if (inputs.test()) {
                new HttpPresneter.Builder()
                        .Url(HttpConfig.USER_EMAIL_GET_PASSWORD)
                        .showProgress(PhonePasswordGetActivity.this)
                        .Params("aname", phone)
                        .Params("apass", dataBinding.etPassword.getText().toString())
                        .setOnHttpListener(new HttpPresneter.OnHttpListener<String>() {
                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String o) {
                                BaseBean baseBean = GsonUtils.changeGsonToBean(o, BaseBean.class);
                                ToastUtil.showShort(baseBean.getMsg());
                                if (baseBean.getStatus()==200) {
                                    finish();
                                }
                            }
                        })
                        .build()
                        .postString();
            }

        }
    }
}
