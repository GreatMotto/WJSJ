package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Base.FindPwdOkActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Random;


/**
 * @author 杨凯
 * @description 修改密码界面
 * @time 2015.3.12
 */
public class ModifyPwdActivity extends BaseActivity implements APICallback.OnResposeListener {

    private EditText etCode, etPwd, etAgainPwd;
    private TextView tvCode,tvSubmit;
    private RippleView rv_submit;

    private String code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_modifypwd);
        initTitle("修改密码");
        assignViews();
    }

    private void assignViews() {
        etCode = (EditText) findViewById(R.id.et_code);
        tvCode = (TextView) findViewById(R.id.tv_code);
        tvSubmit= (TextView) findViewById(R.id.tv_submit);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etAgainPwd = (EditText) findViewById(R.id.et_again_pwd);
        rv_submit = (RippleView) findViewById(R.id.rv_submit);
        tvCode.setOnClickListener(this);
        rv_submit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (isParamsOK()) {
//                    DialogUtils.showProgressDialog("正在登陆...", LoginActivity.this);
                    SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                    String phone= sp1.getValue(Constant.SP_KEY_USER);
                    WebServiceAPI.getInstance().findPwd(phone, etPwd.getText().toString().trim(), etCode.getText().toString().trim(), ModifyPwdActivity.this, ModifyPwdActivity.this);
                    //gotoOtherActivity(FindPwdOkActivity.class);
                    //finish();
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
            case R.id.tv_submit:
                if (isParamsOK()) {
                    //WebServiceAPI.getInstance().findPwd("",etPwd.getText().toString().trim(),etCode.getText().toString().trim(),this, this);
//                    DialogUtils.showProgressDialog("正在登陆...", LoginActivity.this);

                }
                break;
            default:
                break;
        }
    }

    // 判断输入参数
    boolean isParamsOK() {
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
    private void createCode() {
        code = "";
        Random rd = new Random();
        for (int i = 0; i < 6; i++) {
            code += rd.nextInt(10);
        }
        Log.e("code", code);
        TimeCount timeCount = new TimeCount(60000, 1000);
        timeCount.start();
    }


    // 获取验证码
    public void sentvetify() {
        SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
        String phone= sp1.getValue(Constant.SP_KEY_USER);
        if (isPhoneNum(phone)) {
            createCode();
            WebServiceAPI.getInstance().sendCode(phone.trim(), "1", this, this);
            TimeCount timeCount = new TimeCount(60000, 1000);
            timeCount.start();
        } else {
            NewToast.show(this, "请输入正确的手机号！", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(tvCode);
        }
        //createCode();
    }

    //验证手机号
    private boolean isPhoneNum(String phoneNum) {

        if (TextUtils.isEmpty(phoneNum)) {
            NewToast.show(this, "没有获取到手机号", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(tvCode);
            return false;
        } else if (!phoneNum.matches(Constant.TELREGEX)) {
            NewToast.show(this, "手机号码格式不正确", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(tvCode);
            return false;
        }
        return true;
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dialog_fankui);
        TextView tv_fankui = (TextView) alertDialog.getWindow().findViewById(R.id.tv_fankui);
        TextView tv_tip = (TextView) alertDialog.getWindow().findViewById(R.id.tv_tip);
        tv_tip.setText("修改密码成功！");
        tv_fankui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                finish();
                //onBackPressed();
            }
        });
    }


    //验证手机号
    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:
                    NewToast.show(this, "验证码发送成功！", Toast.LENGTH_SHORT);
                    break;
                case 2:
                    showSure();
                    SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                    sp1.putValue(Constant.SP_KEY_PWD, etPwd.getText().toString().trim());
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
