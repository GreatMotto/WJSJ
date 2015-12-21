package com.bm.wjsj.SpiceStore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.Personal.MyOrderHisActivity;
import com.bm.wjsj.Personal.ScoreShopActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.WJSJApplication;

/**
 * 支付成功
 */
public class PaySuccessActivity extends BaseActivity {
    private ImageView ivIcon;
    private TextView tvToast;
    private TextView tvLeft;
    private TextView tvRight;
    private boolean isSuccess;
    private boolean isfinish;
    private int tag;

    private void assignViews() {
        initTitle("");
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        tvToast = (TextView) findViewById(R.id.tv_toast);
        tvLeft = (TextView) findViewById(R.id.tv_left);
        tvRight = (TextView) findViewById(R.id.tv_right2);
        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        switch (tag) {
            case 0:
                if (isSuccess) {
                    initTitle("兑换成功");
                    ivIcon.setImageResource(R.mipmap.pay_success);
                    tvToast.setText(getResources().getString(R.string.pay_successjf));
                    tvRight.setText("返回个人中心");
                } else {
                    initTitle("兑换失败");
                    ivIcon.setImageResource(R.mipmap.pay_failure);
                    tvToast.setText(getResources().getString(R.string.pay_failurejf));
                    tvRight.setText("返回订单");
                }
                break;
            case 1:
                if (isSuccess) {
                    initTitle("兑换成功");
                    ivIcon.setImageResource(R.mipmap.pay_success);
                    tvToast.setText(getResources().getString(R.string.pay_successjf));
                    tvRight.setText("返回个人中心");
                } else {
                    initTitle("兑换失败");
                    ivIcon.setImageResource(R.mipmap.pay_failure);
                    tvToast.setText(getResources().getString(R.string.pay_failurejf));
                    tvRight.setText("返回订单");
                }
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_success);
        isSuccess = getIntent().getBooleanExtra("ISUCCESS", false);
//        isfinish = getIntent().getBooleanExtra("isFinish", false);
        tag = getIntent().getIntExtra("tag", 0);
        assignViews();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_left:
                if (isSuccess) {
                    switch (tag) {
                        case 0:
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Intent intent1 = new Intent(this, ScoreShopActivity.class);
                            WJSJApplication.getInstance().clearAc();
                            startActivity(intent1);
                            this.finish();
                            break;
                    }
                }

                break;
            case R.id.tv_right2:
                if (isSuccess) {
                    if (tag != 2) {
                        Intent intent1 = new Intent(this, MainActivity.class);
                        intent1.putExtra("istag", true);
                        intent1.putExtra("noTog", true);
                        startActivity(intent1);
                        PaySuccessActivity.this.finish();
                        break;
                    } else {
                        Intent intent1 = new Intent(this, MainActivity.class);
                        intent1.putExtra("istag", true);
                        intent1.putExtra("noTog", true);
                        startActivity(intent1);
                        PaySuccessActivity.this.finish();
                        break;
                    }
                } else {
//                    if (isfinish) {
//                        this.finish();
//                    } else {
                    Intent intent2 = new Intent(this, MyOrderHisActivity.class);
                    WJSJApplication.getInstance().clearAc();
                    startActivity(intent2);
                    this.finish();
//                    }
                }

                break;
        }
    }
}
