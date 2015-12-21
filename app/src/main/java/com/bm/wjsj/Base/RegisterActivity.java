package com.bm.wjsj.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Bean.Helper;
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

import cn.jpush.android.api.JPushInterface;


/**
 * @author 杨凯
 * @description 注册界面
 * @time 2015.3.12
 */
public class RegisterActivity extends BaseActivity implements APICallback.OnResposeListener {

    private EditText etPhone, etCode, etPwd, etAgainPwd;
    private TextView tvReason, tvCode, tvAgreement;
    private EditText etSex;//性别
    private RippleView rv_submit;
    private InputMethodManager mSoftManager;
    private SharedPreferencesHelper sp;
    private String code = "", phone = "";
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_register);
        sp = WJSJApplication.getInstance().getSp();
        initTitle(getResources().getString(R.string.register));
        assignViews();
        mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//打开软键盘
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                mSoftManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if (mSoftManager == null) {
            mSoftManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        }
        if (getCurrentFocus() != null) {
            mSoftManager.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);// 隐藏软键盘
        }
    }

    private void assignViews() {
        etSex = (EditText) findViewById(R.id.et_sex);
        tvReason = (TextView) findViewById(R.id.tv_reason);
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        tvCode = (TextView) findViewById(R.id.tv_code);
        etPwd = (EditText) findViewById(R.id.et_pwd);
        etAgainPwd = (EditText) findViewById(R.id.et_again_pwd);
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        rv_submit = (RippleView) findViewById(R.id.rv_submit);
        etSex.setOnClickListener(this);
        tvReason.setOnClickListener(this);
        tvCode.setOnClickListener(this);
        tvAgreement.setOnClickListener(this);
        etPhone.addTextChangedListener(tw);
        rv_submit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (isParamsOK()) {
                    showSure();
//                    DialogUtils.showProgressDialog("正在登陆...", LoginActivity.this);

//                    Intent intent = new Intent();
//                    intent.setClass(RegisterActivity.this, RegisterOKActivity.class);
//                    intent.putExtra("mobile", etPhone.getText().toString().trim());
//                    intent.putExtra("pwd", etPwd.getText().toString().trim());
//                    startActivity(intent);
//                    finish();
                }
            }
        });
        etSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.et_sex) {
                    showPhoto(true);
                }
            }
        });

    }

    private String getSex() {
        return etSex.getText().toString().trim().equals("男") ? "0" : "1";
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dialog_sure);
        TextView tv_sex = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_sex);
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_sex.setText(etSex.getText().toString());
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                WebServiceAPI.getInstance().register(getSex(), etPhone.getText().toString().trim(),
                        etPwd.getText().toString().trim(), etCode.getText().toString().trim(),
                        RegisterActivity.this, RegisterActivity.this);
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    /**
     * 性别和选择头像弹框
     *
     * @param isSex 是否是性别弹框
     */
    private void showPhoto(final boolean isSex) {
        showdialog(Gravity.BOTTOM);
        alertDialog.getWindow().setContentView(R.layout.dialog_photo);
        TextView tv_camera = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_camera);
        TextView tv_album = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_album);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_cancel);
        if (isSex) {
            tv_camera.setText("男");
            tv_album.setText("女");
        }
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSex) {
                    etSex.setText("男");
                }
                alertDialog.cancel();
            }
        });
        tv_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSex) {
                    etSex.setText("女");
                }
                alertDialog.cancel();
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_reason:
                WebServiceAPI.getInstance().help("7",
                        RegisterActivity.this, RegisterActivity.this);
//                gotoTextActivity("注册声明", "");
                break;
            case R.id.tv_code:
                NewToast.show(RegisterActivity.this, "验证码已发送", Toast.LENGTH_SHORT);
                sentvetify();
                break;
            case R.id.tv_agreement:
                WebServiceAPI.getInstance().help8("8",
                        RegisterActivity.this, RegisterActivity.this);
                //gotoTextActivity(getResources().getString(R.string.agreement), "");
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
        } else if (TextUtils.isEmpty(etSex.getText().toString().trim())) {
            NewToast.show(this, "请输入性别", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etSex);
            return false;
        }

        String verifCode = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(verifCode)) {
            NewToast.show(this, "请输入验证码", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etCode);
            return false;
        }

        if (verifCode.length() != 6) {
            NewToast.show(this, "您输入的验证码有误，请重新输入", Toast.LENGTH_SHORT);
            YoYo.with(Techniques.Shake).duration(1000).playOn(etCode);
            return false;
        }

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

//    // 生成验证码
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
//                    etCode.setEnabled(true);
//                    phone = etPhone.getText().toString().trim();
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
            WebServiceAPI.getInstance().sendCode(etPhone.getText().toString().trim(), "0", RegisterActivity.this, RegisterActivity.this);
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
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
            switch (tag) {
                case 3:
//                sp.putValue(Constant.SP_SEX, apiResponse.data.appuser.sex.toString());
                    sp.putValue(Constant.SP_USERID, apiResponse.data.appuser.id.toString());
                    NewToast.show(this, "注册成功", Toast.LENGTH_LONG);
                    Intent intent = new Intent();
                    intent.setClass(RegisterActivity.this, RegisterOKActivity.class);
                    intent.putExtra("mobile", etPhone.getText().toString().trim());
                    intent.putExtra("pwd", etPwd.getText().toString().trim());
                    startActivity(intent);
                    RegisterActivity.this.finish();
                    //上线
                    WebServiceAPI.getInstance().online(JPushInterface.getRegistrationID(this), RegisterActivity.this, RegisterActivity.this);
                    Log.e("lon", WJSJApplication.getInstance().geoLng + "");
                    Log.e("lat", WJSJApplication.getInstance().geoLat + "");
                    break;
                case 2:
                    helper = apiResponse.data.helper;
                    gotoTextActivity("注册声明", helper.content);

                    break;
                case 22:
                    SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                    sp1.putValue(Constant.STATUS, apiResponse.data.status);
                    break;
                case 8:
                    helper = apiResponse.data.helper;
                    gotoTextActivity("服务协议及隐私政策", helper.content);
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
