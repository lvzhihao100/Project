package com.eqdd.yiqidian;

import android.content.Context;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavigationCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.eqdd.library.base.Config;

import java.util.List;

/**
 * Created by lvzhihao on 17-5-26.
 */

public class EnterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        System.out.println(getInfo());
        ARouter.getInstance().build(Config.LOGIN_SPLASH).navigation(this, new NavigationCallback() {
            @Override
            public void onFound(Postcard postcard) {
                System.out.println("当前类=EnterActivity.onFound()");
                finish();
            }

            @Override
            public void onLost(Postcard postcard) {
                System.out.println("当前类=EnterActivity.onLost()");
            }

            @Override
            public void onArrival(Postcard postcard) {
                System.out.println("当前类=EnterActivity.onArrival()");

            }

            @Override
            public void onInterrupt(Postcard postcard) {
                System.out.println("当前类=EnterActivity.onInterrupt()");
            }
        });
    }

    private String getInfo() {
        WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String bssid = info.getBSSID();
        System.out.println(bssid);
        String maxText = info.getMacAddress();
        String ipText = intToIp(info.getIpAddress());
        String status = "";
        if (wifi.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            status = "WIFI_STATE_ENABLED";
        }
        String ssid = info.getSSID();
        int networkID = info.getNetworkId();
        int speed = info.getLinkSpeed();
        return "mac：" + maxText + "\n\r"
                + "ip：" + ipText + "\n\r"
                + "wifi status :" + status + "\n\r"
                + "ssid :" + ssid + "\n\r"
                + "net work id :" + networkID + "\n\r"
                + "connection speed:" + speed + "\n\r"
                ;
    }

    private String intToIp(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "."
                + ((ip >> 24) & 0xFF);
    }
}
