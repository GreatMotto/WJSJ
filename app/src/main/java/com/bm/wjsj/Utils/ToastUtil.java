package com.bm.wjsj.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static Toast mToast;

    public static void showToast(Context context, String toastText, int duration) {
        if (mToast != null) {
            mToast.setText(toastText);
            mToast.setDuration(duration);
            mToast.show();
        }
        else {
            mToast = Toast.makeText(context, toastText, duration);
            mToast.show();
        }
    }
}
