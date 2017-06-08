package com.eqdd.imrong.listener;


import com.eqdd.common.base.AppManager;

import io.rong.imlib.RongIMClient;

/**
 * Created by lvzhihao on 17-4-6.
 */

public class ConnectionStatusListener implements RongIMClient.ConnectionStatusListener {

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {

        switch (connectionStatus){

            case CONNECTED://连接成功。

                break;
            case DISCONNECTED://断开连接。

                break;
            case CONNECTING://连接中。

                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                System.out.println("用户账户在其他设备登录，本机会被踢掉线");
                AppManager.getAppManager().finishAllActivity();
//                GreenDaoUtil.deleteAll();
//                Intent startTaobao = new Intent(getApplicationContext(), LoginActivity.class);
//                startTaobao.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(startTaobao);

                break;
        }
    }
}