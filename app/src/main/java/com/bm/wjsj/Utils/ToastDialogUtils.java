package com.bm.wjsj.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.R;

/**
 * Created by Administrator on 2015/7/30 0030.
 */
public class ToastDialogUtils {
    private Dialog alertDialog;

    private ToastDialogUtils() {

    }

    private static class Datacontroller {

        /**
         * 单例变量
         */
        private static ToastDialogUtils instance = new ToastDialogUtils();
    }

    public static ToastDialogUtils getInstance() {
        return Datacontroller.instance;
    }

    private void showDialog(Activity context) {
        alertDialog = new Dialog(context, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setWindowAnimations(R.style.dlganim);
        alertDialog.show();
        WindowManager manager = context.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        alertDialog.getWindow().setLayout(width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void ShowToast(final Activity context, String title, String toast, final boolean isClose) {
        showDialog(context);
        alertDialog.getWindow().setContentView(R.layout.toast_contet);
        TextView tv_toast_title = (TextView) alertDialog.getWindow().findViewById(R.id.tv_toast_title);
        TextView tv_content = (TextView) alertDialog.getWindow().findViewById(R.id.tv_content);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);

        tv_content.setText(toast);
        tv_toast_title.setText(title);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();

            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isClose) {
                    context.finish();
                }
            }
        });

    }

    public void ShowToastHaveCancle(final Activity context, String title, String toast, final boolean isClose) {
        showDialog(context);
        alertDialog.getWindow().setContentView(R.layout.toast_have_cancle);
        TextView tv_toast_title = (TextView) alertDialog.getWindow().findViewById(R.id.tv_toast_title);
        TextView tv_content = (TextView) alertDialog.getWindow().findViewById(R.id.tv_content);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tv_content.setText(toast);
        tv_toast_title.setText(title);
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();

            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (isClose) {
                    context.finish();
                }
            }
        });

    }
}
