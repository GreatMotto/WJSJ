package com.bm.wjsj.Personal;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.Helper;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;

public class HelpCenterActivity extends BaseActivity implements APICallback.OnResposeListener{
    private TextView tvCp;
    private TextView tvShc;
    private TextView tvWuliu;
    private TextView tvOrder;
    private TextView tvLevel;
    private TextView tv_qita;

    Helper helper;
//    public String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_helpcenter);
        initTitle(getResources().getString(R.string.help));
        assignViews();
    }

    private void assignViews() {
        tvCp = (TextView) findViewById(R.id.tv_cp);
        tvShc = (TextView) findViewById(R.id.tv_shc);
        tvWuliu = (TextView) findViewById(R.id.tv_wuliu);
        tvOrder = (TextView) findViewById(R.id.tv_order);
        tvLevel = (TextView) findViewById(R.id.tv_level);
        tv_qita = (TextView) findViewById(R.id.tv_qita);
        tvCp.setOnClickListener(this);
        tvShc.setOnClickListener(this);
        tvWuliu.setOnClickListener(this);
        tvOrder.setOnClickListener(this);
        tvLevel.setOnClickListener(this);
        tv_qita.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_cp:

//                data = apiResponse.data.product;
//                tv_name.setText(data.productName);
                WebServiceAPI.getInstance().help3("3",
                        HelpCenterActivity.this, HelpCenterActivity.this);

                break;
            case R.id.tv_shc:
                WebServiceAPI.getInstance().help4("4",
                        HelpCenterActivity.this, HelpCenterActivity.this);

                break;
            case R.id.tv_wuliu:
                WebServiceAPI.getInstance().help5("5",
                        HelpCenterActivity.this, HelpCenterActivity.this);

                break;
            case R.id.tv_order:
                WebServiceAPI.getInstance().help6("6",
                        HelpCenterActivity.this, HelpCenterActivity.this);

                break;
            case R.id.tv_level:
                WebServiceAPI.getInstance().help("2",
                        HelpCenterActivity.this, HelpCenterActivity.this);

                break;
            case R.id.tv_qita:
                WebServiceAPI.getInstance().help10("10",
                        HelpCenterActivity.this, HelpCenterActivity.this);

                break;
            default:
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.data != null && apiResponse.status.equals("0")  ) {
            helper = apiResponse.data.helper;
            switch (tag){
                case 2:
                    gotoTextActivity(tvLevel.getText().toString(), helper.content);
                    break;
                case 3:
                    gotoTextActivity(tvCp.getText().toString(), helper.content);
                    break;
                case 4:
                    gotoTextActivity(tvShc.getText().toString(), helper.content);
                    break;
                case 5:
                    gotoTextActivity(tvWuliu.getText().toString(), helper.content);
                    break;
                case 6:
                    gotoTextActivity(tvOrder.getText().toString(), helper.content);
                    break;
                case 10:
                    gotoTextActivity(tv_qita.getText().toString(), helper.content);
                    break;

            }

        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}