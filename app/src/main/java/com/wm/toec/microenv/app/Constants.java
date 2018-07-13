package com.wm.toec.microenv.app;

import java.io.File;

/**
 * Created by wm on 2018/6/6.
 * 常量类
 */

public class Constants {
    public static boolean DEBUG = true;
    //服务器的路径
    public final static String TOEC_SERVER_URL = "192.168.1.1";
    //网络数据缓存路径
    public final static String NET_CACHE_URL = App.getAppContext().getCacheDir().getAbsolutePath() + File.separator + "ToecNetCache";
    //当前是否登陆过
    public static boolean isLogin = false;
    //sp中存储的登录状态
    public final static String IS_LOGIN = "is_login";
    //sp中存储的用户Id
    public static String USER_ID = "";
    //避免重复读写sp 记录在每次运行时的Id
    public static String userID = "";
    //版本号
    public final static String VERSION = "VERSION";
    //是否开启推送
    public final static String PUSH_SWITCH = "PUSH_SWITCH";
}
