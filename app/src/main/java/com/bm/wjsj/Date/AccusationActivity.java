package com.bm.wjsj.Date;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bm.wjsj.Base.HideSoftInputActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ToastDialogUtils;

/**
 * 举报页面
 * Created by Administrator on 2015/7/30 0030.
 */
public class AccusationActivity extends HideSoftInputActivity implements APICallback.OnResposeListener {
    private RadioGroup rgAccusation;
    private RadioButton rbLjyy;
    private RadioButton rbYhsq;
    private RadioButton rbMgxx;
    private RadioButton rbSrw;
    private EditText edtYijian;
    private String reporttype = "0", content = "", objectid = "", objecttype = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_accusation);
        objectid = getIntent().getStringExtra(Constant.ID);
        objecttype = getIntent().getStringExtra("objecttype");
        initViews();
    }

    private void initViews() {
        findViewById(R.id.icon_back).setVisibility(View.GONE);
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_right.setOnClickListener(this);
        tv_back.setOnClickListener(this);
        tv_back.setText("取消");
        tv_right.setText("提交");
        rgAccusation = (RadioGroup) findViewById(R.id.rg_accusation);
        rbLjyy = (RadioButton) findViewById(R.id.rb_ljyy);
        rbYhsq = (RadioButton) findViewById(R.id.rb_yhsq);
        rbMgxx = (RadioButton) findViewById(R.id.rb_mgxx);
        rbSrw = (RadioButton) findViewById(R.id.rb_srw);
        edtYijian = (EditText) findViewById(R.id.edt_yijian);
        rgAccusation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_ljyy:
                        reporttype = "0";
                        break;
                    case R.id.rb_yhsq:
                        reporttype = "1";
                        break;
                    case R.id.rb_mgxx:
                        reporttype = "2";
                        break;
                    case R.id.rb_srw:
                        reporttype = "3";
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
//                if (TextUtils.isEmpty(edtYijian.getText().toString().trim())) {
//                    NewToast.show(this, "请填写举报理由", NewToast.LENGTH_LONG);
//                } else {
                    content = edtYijian.getText().toString();
                    WebServiceAPI.getInstance().report(reporttype, content, objectid, objecttype, AccusationActivity.this, AccusationActivity.this);
//                }
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            ToastDialogUtils.getInstance().ShowToast(this, getResources().getString(R.string.toast_title), getResources().getString(R.string.accusation_content), true);
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
