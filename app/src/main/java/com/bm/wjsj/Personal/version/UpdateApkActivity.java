package com.bm.wjsj.Personal.version;

import android.app.Activity;
import android.os.Bundle;

import com.bm.wjsj.R;

/**
 * Created by wangxl01 on 2015/11/29.
 */
public class UpdateApkActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        // 版本更新检查
        UpdateManager um = new UpdateManager(UpdateApkActivity.this);
        um.checkUpdate();
    }
}
