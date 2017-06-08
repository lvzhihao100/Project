/**
 *
 */
package com.eqdd.library.base;


import android.net.Uri;

/**
 * app 静态变量
 */
public class Config {

    public static final String APP_BASE_URL="app://eqdd.com";

    /**
     * 网络请求失败，显示字样
     */
    public static final CharSequence SERVER_ERROR = "服务器异常";

    /**
     * 单图片选择，展示，图片路径
     */
    public static final String IMAGE_PATH = "imagePath";
    public static final String AREA_CONTENT = "areaContent";

    public static final String PASS = "pass";
    public static final String PERSON = "person";

    public static final String SELECTED_BEANS = "selectedBeans";


    public static final int UNDONE = 0;
    public static final int SUCCESS = 1;
    public static final int CHANGE = 2;

    public static final int MODE_REGISTER = 0;
    public static final int MODE_REGISTER_QIYE = 1;
    public static final int MODE_PASS_FORGET = 2;


    public static final int YEAR_MONTH_DAY = 0;
    public static final int ALL = 1;


    public static final int ADD = 1;
    public static final int UPDATE = 2;

    public static final int MAP_TYPE_MY_LOCATION = 0;
    public static final int MAP_TYPE_PERFECT_LOCATION = 1;
    public static final int MAP_TYPE_LOCATION_NOCITY = 2;


    //具体activity
    public static final int QUN_GUANLI = 100;
    public static final int MINE_AU = 102;
    public static final int TEAM_NAME_SET = 103;
    public static final int ADD_CAREER = 104;
    public static final int PERSON_INFO = 105;
    public static final int SELECT_FRIEND = 106;
    public static final int SELECT = 107;
    public static final int SELECT_QUN_MEMBER = 108;
    public static final int INVITE_PERSON_LIST = 109;
    public static final int SELECT_CONTRACT = 110;
    public static final int AC_COM_INVITE_INFO = 111;
    public static final int MINE_AU_PERFECT = 112;
    public static final int USER_DOCUMENT = 113;
    public static final int SELECT_RICH_TEXT = 114;
    public static final int SELECT_START_END_TIME = 115;
    public static final int SCAN = 116;


    public static final int IMAGE_PATH_ID_CARD_FRONT = 200;
    public static final int IMAGE_PATH_ID_CARD_BEHAND = 201;
    public static final int IMAGE_PATH_ID_CARD_WITH = 202;
    public static final int IMAGE_PATH_QIYEDAIMA = 203;
    public static final int IMAGE_PATH_YINGYEZHIZHAO = 204;
    public static final int IMAGE_PATH_SHENCHANXUKE = 205;
    public static final int IMAGE_PATH_ZHENGJIANZHAO = 206;

    //某一类型的activity
    public static final int IMAGE_SELECT = 300;
    public static final int INTENT_CONTENT = 301;
    public static final int AREA = 302;
    public static final String MAX_NUM = "maxNum";
    public static final String CAREER_INFO = "careerInfo";
    public static final String CONTRACTS = "contracts";
    public static final String COM_INVITE_INFO = "comInviteInfo";
    public static final String MODE = "mode";
    public static final String RICH_TEXT_CONTENT = "richTextContent";
    public static final String RICH_TEXT_IMAGE_PATHS = "richTextImagePaths";
    public static final String TITLE = "title";
    public static final String TIME_TYPE = "timeType";

    public static final String START_TIME = "startTime";
    public static final String END_TIME = "endTime";
    public static final String URL = "url";
    public static final String URL_ABOUT_US = "file:///android_asset/about_us.html";
    public static final String KEHU_BEAN = "kehuBean";
    public static final String FROM_ACTIVITY = "fromActivity";
    public static final String ACTION = "action";
    public static final String RICH_TEXT_IMAGE_URLS = "richTextImageUrls";
    public static final String RICH_TEXT_RESULT = "richTextResult";
    public static final String SELECT_MAP_TYPE = "selectMapType";
    public static final String UNDO = "该模块暂未开发";
    public static final String MAP_RESULT = "mapResult";
    public static final String GUIDE_FIRST = "guideFirst";


    public static final String APP = "/app";
    public static final String LOGIN = "/login";
    public static final String TONGXUNLU = "/tongxunlu";
    public static final String LIBRARY = "/library";
    public static final String IMRONG = "/imrong";


    public static final String LOGIN_SERVICE = "/loginService";
    public static final String IMRONG_SERVICE = "/imrongService";

    private static final String IMRONG_FRAGMENT = "/imrongFragment";

    public static final String APP_SCAN = APP+"/scan";

    public static final String LOGIN_REGISTER=LOGIN+"/register";
    public static final String LOGIN_LOGIN=LOGIN+"/login";
    public static final String LOGIN_REGISTER_QIYE =LOGIN+"/registerQiye";
    public static final String LOGIN_EMAIL_CHECK =LOGIN+"/emailCheck";
    public static final String LOGIN_MINE_AU =LOGIN+"/mineAu";
    public static final String LOGIN_PHONE_PASSWORD_GET = LOGIN+"/phonePasswordGet";
    public static final String LOGIN_QIYE_REGISTER = LOGIN+"/qiyeRegister";
    public static final String LOGIN_REGISTER_TWO = LOGIN+"/registerTwo";
    public static final String LOGIN_PHONE = "phone";
    public static final String LOGIN_EMAIL_PASSWORD_GET = LOGIN+"/emailPasswordGet";
    public static final String LOGIN_GUIDE = LOGIN+"/guide";
    public static final String LOGIN_SPLASH = LOGIN+"/splash";

    public static final String LOGIN_SERVICE_APPLICATION = LOGIN_SERVICE+"/application";
    public static final String LOGIN_SERVICE_JPUSH = LOGIN_SERVICE+"/jpush";
    public static final String LOGIN_SERVICE_USER = LOGIN_SERVICE+"/user";



    public static final String TONGXUNLU_FRIEND_DETAIL_INFO = TONGXUNLU+"/friendDetailInfo";


    public static final String LIBRARY_SELECT_MAP = LIBRARY+"/selectMap";
    public static final String LIBRARY_SELECT = LIBRARY+"/select";
    public static final String LIBRARY_SELECT_FRIEND = LIBRARY+"/selectFriend";
    public static final String LIBRARY_SELECT_FROM_PHONE = LIBRARY+"/selectFromPhone";
    public static final String LIBRARY_SELECT_QUN_MEMBER = LIBRARY+"/selectQunMember";
    public static final String LIBRARY_SELECT_QUNZU = LIBRARY+"/selectQunzu";
    public static final String LIBRARY_SELECT_RICH_TEXT = LIBRARY+"/selectRichText";
    public static final String LIBRARY_SELECT_START_END_TIME = LIBRARY+"/selectStartEndTime";
    public static final String LIBRARY_SELECT_TONGSHI = LIBRARY+"/selectTongshi";


    public static final String IMRONG_SERVICE_CONVERSITION_LIST = IMRONG_SERVICE+"/conversitionList";
    public static final String IMRONG_CONVERSITION = IMRONG+"/conversition";
    public static final String TONGXUNLU_TEAM_CHEAT_INFO = TONGXUNLU+"/teamCheatInfo";
    public static final String IMRONG_CONVERSITION_LIST = IMRONG+"/conversitionList";
    public static final String IMRONG_SUBCONVERSITION = IMRONG+"/subconversition";
    public static final String IMRONG_SERVICE_RONGYUN_CONNECT = IMRONG_SERVICE+"/rongyunConnect";
    public static final String IMRONG_FRAGMENT_XIAOXI = IMRONG_FRAGMENT + "/xiaoxi";


    public static final String APP_MAIN = APP+"/main";
}
