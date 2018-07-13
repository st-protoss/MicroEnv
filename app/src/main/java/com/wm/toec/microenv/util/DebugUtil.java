package com.wm.toec.microenv.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wm.toec.microenv.app.Constants;

/**
 * Created by wm on 2018/6/6.
 * 调试工具
 */

public class DebugUtil {
    public static final String TAG = "wm";

    public static void toast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void debug(String tag, String msg) {
        if (Constants.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void debug(String msg) {
        if (Constants.DEBUG) {
            Log.d(TAG, msg);
        }
    }

    public static void error(String tag, String error) {

        if (Constants.DEBUG) {

            Log.e(tag, error);
        }
    }

    public static void error(String error) {

        if (Constants.DEBUG) {

            Log.e(TAG, error);
        }
    }
}
