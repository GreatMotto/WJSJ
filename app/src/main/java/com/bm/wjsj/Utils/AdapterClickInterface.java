package com.bm.wjsj.Utils;

import android.view.View;

/**
 * Created by yangk on 2015/7/28.
 */
public interface AdapterClickInterface {
    public void onClick(View v, String s, String id);

    public void getReport(View v, String id);

    public void onLongClick(View v,String userid, String reportid,String message);
}
