package com.bm.wjsj.Base;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;


/**
 * @author 杨凯
 * @description 忘记密码界面
 * @time 2015.3.12
 */
public class ForgetpwdActivity extends BaseActivity implements APICallback.OnResposeListener {

    private EditText etPhone, etCode, etPwd, etAgainPwd;
    private TextView tvCode;
    private RippleView rv_submit;

    private String code = "", phone = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_forgetpwd);
        initTitle(getResources().getString(R.string.findpwd));
        assignViews();
    }

    private void assignViews() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        tvCode = (TextView) findViewById(R.id.tv_code);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etAgainPwd = (EditText) findViewById(R.id.et_again_pwd);
        rv_submit = (RippleView) findViewById(R.id.rv_submit);
        tvCode.setOnClickListener(this);
        etPhone.addTextChangedListener(tw);
        rv_submit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (isParamsOK()) {
                    WebServiceAPI.getInstance().findPwd(etPhone.getText().toString().trim(),etPwd.getText().toString().trim(),etCode.getText().toString().trim(),ForgetpwdActivity.this, ForgetpwdActivity.this);
//                    DialogUtils.showProgressDialog("正在登陆...", LoginActivity.this);

                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_code:
                sentvetify();
                break;
            default:
                break;
        }
    }

    // 监听手机输入框的文本变化
    TextWatcher tw = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void afterTextChanged(Editable arg0) {
            // TODO Auto-generated method stub
            String text = arg0.toString().trim();
            if (!TextUtils.isEmpty(phone) && !text.equals(phone)) {
                etCode.setText("");
                etCode.setEnabled(false);
            } else if (text.equals(phone)) {
                etCode.setEnabled(true);
            }
        }
    };

    // 判断输入参数
    boolean isParamsOK() {
        if (!isPhoneNum(etPhone.getText().toString().trim())) {
            return false;
        }
        String password = etPwd.getText().toString().trim();
        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 12) {
            NewToast.show(this, "请输入6-12位密码", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etPwd);
            return false;
        }
        String password2 = etAgainPwd.getText().toString().trim();
        if (TextUtils.isEmpty(password2)) {
            NewToast.show(this, "请再次输入密码", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etAgainPwd);
            return false;
        } else if (!password.equals(password2)) {
            NewToast.show(this, "两次输入的密码不一致，请重新输入", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etAgainPwd);
            return false;
        }

        String verifCode = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(verifCode)) {
            NewToast.show(this, "请输入验证码", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etCode);
            return false;
        }

//        if (!TextUtils.isEmpty(code)) {
//            if (!code.equals(verifCode)) {
//                NewToast.show(this, "您输入的验证码有误，请重新输入", Toast.LENGTH_SHORT);
//                YoYo.with(Techniques.Shake).duration(1000).playOn(etCode);
//                return false;
//            }
//        }
        return true;
    }

    // 验证码倒计时
    private class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onTick(long millisUntilFinished) {
            // TODO Auto-generated method stub
            tvCode.setClickable(false);
//            tv_code.setBackgroundColor(R.color.tree_tv_select);
            tvCode.setText(millisUntilFinished / 1000 + "秒");

        }

        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
            tvCode.setText("发送验证码");
            tvCode.setClickable(true);
        }

    }

    // 生成验证码
//    private void createCode() {
//        code = "";
//        Random rd = new Random();
//        for (int i = 0; i < 6; i++) {
//            code += rd.nextInt(10);
//        }
//        Log.e("code", code);
//        TimeCount timeCount = new TimeCount(60000, 1000);
//        timeCount.start();
//    }

    // 验证码成功回调
//    private Listener<BaseData> successListenen() {
//        // TODO Auto-generated method stub
//        return new Listener<BaseData>()
//        {
//
//            @Override
//            public void onResponse(BaseData result) {
//                if (result.data != null && result.status.equals("0")) {
//                    NewToast.show(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT);
//                    et_code.setEnabled(true);
//                    phone = et_mobile.getText().toString().trim();
//                    TimeCount timeCount = new TimeCount(60000, 1000);
//                    timeCount.start();
//                }
//            }
//        };
//    }

    // 获取验证码
    public void sentvetify() {
        if (isPhoneNum(etPhone.getText().toString().trim())) {
//            createCode();
            WebServiceAPI.getInstance().sendCode(etPhone.getText().toString().trim(), "1",ForgetpwdActivity.this, ForgetpwdActivity.this);
            NewToast.show(this, "验证码发送成功！", Toast.LENGTH_SHORT);
            TimeCount timeCount = new TimeCount(60000, 1000);
            timeCount.start();
        } else {
            NewToast.show(this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etPhone);
        }
    }

    //验证手机号
    private boolean isPhoneNum(String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            NewToast.show(this, "请输入手机号", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etPhone);
            return false;
        } else if (!phoneNum.matches(Constant.TELREGEX)) {
            NewToast.show(this, "手机号码格式不正确", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etPhone);
            return false;
        }
        return true;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.data != null && apiResponse.status.equals("0") && tag == 2 ) {
            NewToast.show(this, "修改密码成功", Toast.LENGTH_LONG);
                finish();
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
