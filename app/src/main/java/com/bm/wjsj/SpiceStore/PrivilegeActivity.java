package com.bm.wjsj.SpiceStore;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.R;

public class PrivilegeActivity extends BaseActivity {
    private WebView wvWeb;
    private String name = "";

    private void assignViews() {
        initTitle(name);
        wvWeb = (WebView) findViewById(R.id.wv_web);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privilege);
        name = getIntent().getStringExtra(Constant.WEB_NAME);
        assignViews();

    }

}
