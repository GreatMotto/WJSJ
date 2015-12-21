package com.bm.wjsj.MyMsg;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;

/**
 * Created by wangxl01 on 2015/12/6.
 */
public class ConversationListActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
        setContentView(R.layout.title_bar);
        initTitle("");

        //打开我的消息
        Intent intent1 = new Intent(this, MainActivity.class);
        intent1.putExtra("isMsg", true);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.startActivity(intent1);
        finish();
    }

    public void initTitle(String title) {
        tv_back = (TextView) findViewById(R.id.tv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
        tv_back.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



}
