package com.bm.wjsj.Base;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.SharedPreferencesHelper;

public class FindPwdOkActivity extends BaseActivity {

    private RippleView rv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_successpwd);
        initTitle(getResources().getString(R.string.findpwd));
        init();
    }

    private void init() {
        rv_submit = (RippleView) findViewById(R.id.rv_submit);
        rv_submit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                onBackPressed();
            }
        });
    }
}