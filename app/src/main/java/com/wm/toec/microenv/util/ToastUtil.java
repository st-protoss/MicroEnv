package com.wm.toec.microenv.util;

import android.annotation.SuppressLint;
import android.widget.Toast;

import com.wm.toec.microenv.app.App;

/**
 * Created by wm on 2018/6/6.
 * Toast工具
 */

public class ToastUtil {
    private static Toast mToast;

    @SuppressLint("ShowToast")
    public static void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_SHORT);
        }
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setText(text);
        mToast.show();
    }

    @SuppressLint("ShowToast")
    public static void showToastLong(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(App.getAppContext(), text, Toast.LENGTH_LONG);
        }
        mToast.setDuration(Toast.LENGTH_LONG);
        mToast.setText(text);
        mToast.show();
    }
}
