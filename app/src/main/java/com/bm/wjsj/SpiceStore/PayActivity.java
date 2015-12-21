package com.bm.wjsj.SpiceStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.Personal.ScoreShopActivity;
import com.bm.wjsj.R;


/**
 * Created by wangxl01 on 2015/10/27.
 */
public class PayActivity extends BaseActivity {
    private ImageView ivIcon;
    private TextView tvToast;
    private TextView tvLeft;
    private TextView tvRight;
    private boolean isSuccess;
    private boolean isfinish;
    private int tag;

    private void assignViews() {
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvToast = (TextView) findViewById(R.id.tv_toast);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right2);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        switch (tag){
            case 0:
                if (isSuccess) {
                    //initTitle(getResources().getString(R.string.pay_s));
                    //ivIcon.setImageResource(R.mipmap.pay_success);
                    //tvToast.setText(getResources().getString(R.string.pay_success));
                    tvRight.setText("返回想约");
                } else {
                    //initTitle(getResources().getString(R.string.pay_f));
                    //ivIcon.setImageResource(R.mipmap.pay_failure);
                    //tvToast.setText(getResources().getString(R.string.pay_failure));
                    tvRight.setText("返回想约");
                }
                break;
            case 1:
                if (isSuccess) {
                    //initTitle(getResources().getString(R.string.pay_s));
                    //ivIcon.setImageResource(R.mipmap.pay_success);
                    //tvToast.setText(getResources().getString(R.string.pay_successjf));
                    tvRight.setText("返回想约");
                } else {
                    //initTitle(getResources().getString(R.string.pay_f));
                    //ivIcon.setImageResource(R.mipmap.pay_failure);
                    //tvToast.setText(getResources().getString(R.string.pay_failure));
                    tvRight.setText("返回想约");
                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay);
        isSuccess = getIntent().getBooleanExtra("ISUCCESS", false);
        tag = getIntent().getIntExtra("tag", 0);
        assignViews();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left:
                if (isSuccess) {
                    switch (tag){
                        case 0:
                            Intent intent = new Intent(this, MainActivity.class);
                            intent.putExtra("isShop", 1);
                            startActivity(intent);
                            break;
                        case 1:
                            this.gotoOtherActivity(ScoreShopActivity.class);
//                            Intent intent1 = new Intent(this, ScoreShopActivity.class);
//                            startActivity(intent1);
                            break;
                    }
                } else {

                }

                break;
            case R.id.tv_right2:
                if (isSuccess) {
                    Intent intent2 = new Intent(this, MainActivity.class);
                    intent2.putExtra("isShop", 2);
                    startActivity(intent2);
                } else {
                    if (isfinish) {
                        this.finish();
                    } else {
                        Intent intent2 = new Intent(this, MainActivity.class);
                        //WJSJApplication.getInstance().clearAc();
                        startActivity(intent2);
                        this.finish();
                    }
                }

                break;
        }
    }
}
