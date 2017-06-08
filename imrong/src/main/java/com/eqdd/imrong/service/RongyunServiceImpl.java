package com.eqdd.imrong.service;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.common.base.App;
import com.eqdd.common.bean.BaseBean;
import com.eqdd.common.http.HttpPresneter;
import com.eqdd.common.utils.GsonUtils;
import com.eqdd.common.utils.SPUtil;
import com.eqdd.library.base.Config;
import com.eqdd.library.bean.User;
import com.eqdd.library.bean.rongyun.GroupBean;
import com.eqdd.library.http.HttpConfig;
import com.eqdd.library.service.RongyunService;
import com.eqdd.library.service.UserService;
import com.eqdd.library.utils.GreenDaoUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Group;
import okhttp3.Call;
import okhttp3.Response;
import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

import static io.rong.imkit.utils.SystemUtils.getCurProcessName;

/**
 * Created by lvzhihao on 17-4-5.
 */
@Route(path = Config.IMRONG_SERVICE_RONGYUN_CONNECT)
public class RongyunServiceImpl implements RongyunService {
    private Subscription subscribe;
    private User user;
    private String token;
    @Autowired
    UserService userService;


    private void getToken() {
        ARouter.getInstance().inject(this);
        user = userService.getUser();
        token = SPUtil.getToken(user.getUname());
        if (!TextUtils.isEmpty(token)) {
            System.out.println("使用本地token" + token);
            connect(token);
        } else {
            subscribe = Observable.just(1)
                    .observeOn(Schedulers.newThread())
                    .subscribe((i) -> {
                        System.out.println("获取服务器token中。。。");
                        new HttpPresneter.Builder()
                                .Url(HttpConfig.GET_TOKEN)
                                .Params("uid", user.getUname())
                                .setOnHttpListener(new HttpPresneter.OnHttpListener() {
                                    @Override
                                    public void onError(Throwable e) {
                                        System.out.println(e.toString());
                                    }

                                    @Override
                                    public void onNext(Object o) {
                                        try {
                                            JSONObject jsonObject = new JSONObject((String) o);
                                            token = jsonObject.getString("token");
                                            if (!TextUtils.isEmpty(token)) {
                                                System.out.println("获取到token");
                                                connect(token);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .build()
                                .postString();
                    });
        }
    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #)} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @param
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(final String token) {

        if (App.INSTANCE.getApplicationInfo().packageName.equals(getCurProcessName(App.INSTANCE.getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 * 2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {
                    System.out.println("onTokenIncorrect");
                    SPUtil.savetToken(user.getUname(), "");
                    cancelSubscribe();
                    getToken();
                }

                /**
                 * 连接融云成功
                 *
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    System.out.println("###############################连接成功" + userid);
                    SPUtil.savetToken(userid, token);
                    cancelSubscribe();
                    RongIM.getInstance().enableNewComingMessageIcon(true);//显示新消息提醒
                    RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
                    OkHttpUtils
                            .post()
                            .url(HttpConfig.BASE_URL + HttpConfig.GROUP_LIST_INFO)
                            .addParams("uid", userService.getUser().getUname())
                            .build()
                            .execute(new Callback<BaseBean<GroupBean>>() {
                                @Override
                                public BaseBean<GroupBean> parseNetworkResponse(Response response, int id) throws Exception {
                                    return GsonUtils.changeGsonToBaseBean(response.body().string(), GroupBean.class);
                                }

                                @Override
                                public void onError(Call call, Exception e, int id) {

                                    System.out.println(e.toString());

                                }

                                @Override
                                public void onResponse(BaseBean<GroupBean> response, int id) {
                                    System.out.println(response);
                                    GreenDaoUtil.deleteAllGroup();
                                    for (GroupBean groupBean : response.getItems()) {
                                        GreenDaoUtil.insertGroup(groupBean);
                                        Group group = new Group(groupBean.getGroupid(), TextUtils.isEmpty(groupBean.getGroupname()) ? "未命名" : groupBean.getGroupname(), Uri.parse(HttpConfig.BASE_URL_NO + groupBean.getGroupphoto()));
                                        RongIM.getInstance().refreshGroupInfoCache(group);

                                    }
                                }
                            });

                }

                /**
                 * 连接融云失败
                 *
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    cancelSubscribe();
                    System.out.println("###############################连接失败，" + errorCode);
                }
            });


        }
    }

    private void cancelSubscribe() {
        if (subscribe != null) {
            subscribe.unsubscribe();
        }
    }

    @Override
    public void connect() {
        getToken();
    }

    @Override
    public void init(Context context) {
    }
}
