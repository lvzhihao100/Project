package com.eqdd.common.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

/**
 * Created by lvzhihao on 17-5-25.
 */

public class BaseActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        AppManager.getAppManager().addActivity(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        AppManager.getAppManager().finishActivity(activity);

    }
}
