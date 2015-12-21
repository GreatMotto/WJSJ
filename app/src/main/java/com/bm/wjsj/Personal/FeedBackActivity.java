package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Base.HideSoftInputActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class FeedBackActivity extends HideSoftInputActivity implements APICallback.OnResposeListener {

    private EditText et_content;
    private TextView tv_numbers;
    private RippleView rv_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_feedback);
        initTitle(getResources().getString(R.string.feedback));
        init();
    }

    private void init() {
        et_content = (EditText) findViewById(R.id.et_content);
        tv_numbers = (TextView) findViewById(R.id.tv_numbers);
        rv_submit = (RippleView) findViewById(R.id.rv_submit);
        tv_numbers.setText("0/140");
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString().trim();
                tv_numbers.setText(text.length() + "/140");
            }
        });
        rv_submit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (TextUtils.isEmpty(et_content.getText().toString().trim())) {
                    NewToast.show(FeedBackActivity.this, "请输入您的反馈意见", Toast.LENGTH_LONG);
                    YoYo.with(Techniques.Shake).duration(1000).playOn(et_content);
                    return;
                }
                WebServiceAPI.getInstance().addFeedback(et_content.getText().toString().trim(),
                        FeedBackActivity.this, FeedBackActivity.this);
            }
        });
    }
    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dialog_fankui);
        TextView tv_fankui = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_fankui);
        tv_fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                onBackPressed();
            }
        });
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")){
            showSure();
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}