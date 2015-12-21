package com.bm.wjsj.Utils;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bm.wjsj.R;


/**
 * 
 * @Description loading
 * @author
 * @time 2014-11-12
 */
public class DialogUtils {

    public static Dialog mProgressDialog;
    public static View view;

    /**
     * 
     * @deprecated
     * @param message
     * @param context
     */
    public static void showProgressDialog(String message, Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.comm_popw_view, null);
        TextView title_view = (TextView) view.findViewById(R.id.title);
        title_view.setText(message);

        mProgressDialog = new Dialog(context, R.style.MyDialog);
        mProgressDialog.setContentView(view);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }

    /**
     * 
     * @deprecated
     */
    public static void cancleProgressDialog() {
        if (mProgressDialog != null)
            mProgressDialog.cancel();
    }
}