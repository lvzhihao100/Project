package com.eqdd.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.eqdd.common.base.App;


public class SPUtil {

    private static final String ACCOUNT = "account";
    private static final String PASSWORD = "password";
    private static final String FIRST = "first";
    private static final String UUID = "uuid";
    private static final String TOKEN = "token";
    private static final String ID = "id";
    private static final String JPUSH = "jpush";


    public static void saveJPushAlias(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(JPUSH, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, true);
        edit.commit();
    }

    public static boolean isSaveJPushAlias(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(JPUSH, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static void savetID(String key, Long id) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(ID, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear();
        edit.putLong(key, id);
        edit.commit();
    }

    public static Long getID(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(ID, Context.MODE_PRIVATE);
        return preferences.getLong(key, -1);
    }


    public static void savetToken(String key, String uuid) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, uuid);
        edit.commit();
    }

    public static String getToken(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(TOKEN, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void savetUUID(String key, String uuid) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(UUID, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, uuid);
        edit.commit();
    }

    public static String getUUID(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(UUID, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void savetFirst(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(FIRST, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putBoolean(key, false);
        edit.commit();
    }

    public static boolean getFirst(String key) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(FIRST, Context.MODE_PRIVATE);
        return preferences.getBoolean(key, true);
    }

    public static void saveAccount(String key, String account) {
        clearAccount();
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, account);
        edit.commit();
    }

    public static String getPassword(String key, String defalut) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE);
        return preferences.getString(key, defalut);
    }

    public static void savetPassword(String key, String password) {
        clearPassword();
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, password);
        edit.commit();
    }

    public static String getAccount(String key, String defalut) {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
        return preferences.getString(key, defalut);
    }


    public static void clearAccount() {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear().commit();
    }

    public static void clearPassword() {
        SharedPreferences preferences = App.INSTANCE.getSharedPreferences(PASSWORD, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.clear().commit();
    }
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param
     * @param key
     * @param object
     */
    public static void setParam(String key, Object object){

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = App.INSTANCE.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if("String".equals(type)){
            editor.putString(key, (String)object);
        }
        else if("Integer".equals(type)){
            editor.putInt(key, (Integer)object);
        }
        else if("Boolean".equals(type)){
            editor.putBoolean(key, (Boolean)object);
        }
        else if("Float".equals(type)){
            editor.putFloat(type, (Float)object);
        }
        else if("Long".equals(type)){
            editor.putLong(type, (Long)object);
        }

        editor.commit();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = App.INSTANCE.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(type, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(type, (Long)defaultObject);
        }
        return null;
    }

    public static void clearData(){
        SharedPreferences sp = App.INSTANCE.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().commit();
    }
}

