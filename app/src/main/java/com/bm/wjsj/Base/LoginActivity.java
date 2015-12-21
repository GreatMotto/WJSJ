package com.bm.wjsj.Base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.InputTools;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import cn.jpush.android.api.JPushInterface;


/**
 * @author 杨凯
 * @description 登录界面
 * @time 2015.3.12
 */
public class LoginActivity extends BaseActivity implements APICallback.OnResposeListener {

    private EditText et_name, et_pass;
    private TextView tv_forgetpass;
    private RippleView rv_login, rv_register;
    private ImageView iv_clear_name, iv_clear_psw, iv_logo;
    private LinearLayout iv_back;
    private LinearLayout ll_layout;

    public final int mRequestCode = 1001;
    private SharedPreferencesHelper sp;
    private boolean isGoDate;
    public static String mNickName, mUserId;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isGoDate = getIntent().getBooleanExtra(Constant.BOOLEAN, false);
        setContentView(R.layout.ac_login);
        sp = new SharedPreferencesHelper(this, Constant.SP_FILENAME);
        init();
        // 获取登录账号
//        sp = ZZWebApplication.getInstance().getSp();
//        // 取消默认登录
//        sp.putBooleanValue(Constant.SP_KEY_ISLOGIN, false);
//        // 记住上次登录的账号
//        et_name.setText(sp.getValue(Constant.SP_KEY_USER));
//        //check if user is a supervison
//        ZZWebApplication.getInstance().isSupervison = sp.getBooleanValue(Constant.SP_ISSUPERVISION);
//        if (ZZWebApplication.getInstance().isSupervison) {
//            iv_supervison.setImageResource(R.mipmap.check_yes);
//            et_name.setHint(getResources().getString(R.string.logname_edit_));
//            et_name.setKeyListener(listener);
//            et_name.setMaxLines(20);
//        }
//        ZZWebApplication.getInstance().rememberpsw = sp.getBooleanValue(Constant.SP_REMPSW);
//        if (ZZWebApplication.getInstance().rememberpsw) {
//            et_pass.setText(sp.getValue(Constant.SP_KEY_PWD));
//        }
    }

    /* 初始化控件 */
    public void init() {
        mainActivity = new MainActivity();
        et_name = (EditText) findViewById(R.id.et_loginname);
        et_pass = (EditText) findViewById(R.id.et_loginpass);
        ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
        tv_forgetpass = (TextView) findViewById(R.id.tv_forgetpass);
        rv_login = (RippleView) findViewById(R.id.rv_login);
        rv_register = (RippleView) findViewById(R.id.rv_register);
        iv_clear_name = (ImageView) findViewById(R.id.iv_clear_name);
        iv_clear_psw = (ImageView) findViewById(R.id.iv_clear_psw);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        iv_back = (LinearLayout) findViewById(R.id.iv_back);
        ViewTreeObserver vto = iv_logo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_logo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ll_layout.getLayoutParams();
                layoutParams.setMargins(0, -iv_logo.getHeight() / 2 - DisplayUtil.dip2px(LoginActivity.this, 2), 0, 0);//4个参数按顺序分别是左上右下
                ll_layout.setLayoutParams(layoutParams);
            }
        });
        tv_forgetpass.setOnClickListener(this);
        iv_clear_name.setOnClickListener(this);
        iv_clear_psw.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    iv_clear_name.setVisibility(View.VISIBLE);
                } else {
                    // 此处为失去焦点时的处理内容
                    iv_clear_name.setVisibility(View.GONE);
                }
            }
        });
        et_pass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    iv_clear_psw.setVisibility(View.VISIBLE);
                } else {
                    // 此处为失去焦点时的处理内容
                    iv_clear_psw.setVisibility(View.GONE);
                }
            }
        });
        rv_login.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                if (isParamsOK(et_name.getText().toString().trim(), et_pass.getText().toString().trim())) {
                    InputTools.HideKeyboard(rippleView);
                    DialogUtils.showProgressDialog("正在登录，请稍等...", LoginActivity.this);
//                    WebServiceAPI.getInstance().store(LoginActivity.this, LoginActivity.this);
                    WebServiceAPI.getInstance().login(et_name.getText().toString().trim(),
                            et_pass.getText().toString().trim(),
                            LoginActivity.this, LoginActivity.this);
//                    if (!isGoDate) {
//                        Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
//                        intent.putExtra("fromLogin", true);
//                        startActivityForResult(intent, 100);
//                    } else {
//                        setResult(RESULT_OK);
//                    }
//                    finish();
                }
            }
        });
        rv_register.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                InputTools.HideKeyboard(rippleView);
                WJSJApplication.getInstance().addAcitivity(LoginActivity.this);
                gotoOtherActivity(RegisterActivity.class);
            }
        });
    }

    // 回调注册账号 密码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case mRequestCode:

                    et_name.setText(data.getStringExtra("phone"));
                    et_pass.setText(data.getStringExtra("psw"));

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_forgetpass:// 忘记密码
                gotoOtherActivity(ForgetpwdActivity.class);
                break;
            case R.id.iv_clear_name:// 记住密码勾选框
                et_name.setText("");
                break;
            case R.id.iv_clear_psw://if user is a supervison
                et_pass.setText("");
                break;
            case R.id.iv_back://返回键
                onBackPressed();
                break;
            default:
                break;
        }
    }

    public boolean isParamsOK(String phoneNum, String pwd) {
        if (TextUtils.isEmpty(phoneNum)) {
            NewToast.show(this, "请输入您的手机号", Toast.LENGTH_LONG);
            YoYo.with(Techniques.Shake).duration(1000).playOn(et_name);
            return false;
        } else if (!phoneNum.matches(Constant.TELREGEX)) {
            NewToast.show(this, "手机号格式不正确", Toast.LENGTH_LONG);
            YoYo.with(Techniques.Shake).duration(1000).playOn(et_name);
            return false;
        } else if (TextUtils.isEmpty(pwd) || pwd.length() < 6 || pwd.length() > 16) {
            NewToast.show(this, "请输入6-12位密码", Toast.LENGTH_LONG);
            YoYo.with(Techniques.Shake).duration(1000).playOn(et_pass);
            return false;
        }
        return true;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
//        if(apiResponse.status.equals("0")){
//            NewToast.show(this, "登录成功11111", Toast.LENGTH_LONG);
//        }else if (apiResponse.data != null){
//            NewToast.show(this, "登录成功2222222", Toast.LENGTH_LONG);
//        }else if (apiResponse.data == null && apiResponse.status.equals("1")){
//            NewToast.show(this, "登录失败", Toast.LENGTH_LONG);
//        }
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
            switch (tag) {
                case 2:
                    NewToast.show(this, "登录成功", Toast.LENGTH_LONG);
                    DialogUtils.cancleProgressDialog();
                    SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                    sp.putBooleanValue(Constant.SP_KEY_ISLOGIN, true);
                    sp.putValue(Constant.SP_KEY_USER, et_name.getText().toString().trim());
                    sp.putValue(Constant.SP_KEY_PWD, et_pass.getText().toString().trim());
                    sp.putValue(Constant.SP_USERNAME, apiResponse.data.appuser.nickname);
                    mNickName = apiResponse.data.appuser.nickname;
                    sp.putValue(Constant.SP_USERID, apiResponse.data.appuser.id.toString());
                    mUserId = apiResponse.data.appuser.id.toString();
                    sp.putValue(Constant.SP_PHOTO, apiResponse.data.appuser.head);
                    sp.putValue(Constant.SP_LEVEL, apiResponse.data.appuser.level);
                    sp.putValue(Constant.SP_INTEGRAL, apiResponse.data.appuser.integral);
                    sp.putValue(Constant.SP_SEX, apiResponse.data.appuser.sex);
                    sp.putValue(Constant.SP_BIRTHDAY, apiResponse.data.appuser.birthday);
                    sp.putValue(Constant.SP_PROVINCEID, apiResponse.data.appuser.provinceId);
                    sp.putValue(Constant.SP_CITYID, apiResponse.data.appuser.cityId);
                    sp.putValue(Constant.SP_SIGNATURE, apiResponse.data.appuser.sign);
                    sp.putValue(Constant.SP_CONSTELLATION, apiResponse.data.appuser.constellation);
                    sp.putValue(Constant.SP_AGE, apiResponse.data.appuser.age);
                    sp.putValue(Constant.SP_TOKEN, apiResponse.data.appuser.token);
                    //上线
                    Log.e("lng:", ">>>>>>>*********>>>>>>>" + WJSJApplication.getInstance().geoLng);
                    WebServiceAPI.getInstance().online(JPushInterface.getRegistrationID(this), LoginActivity.this, LoginActivity.this);

                    Log.e("Loginac", "token = " + apiResponse.data.appuser.token);
                    WJSJApplication.getInstance().connectRongCloud();
                    if(mainActivity!=null)
                    {
                        Intent intent=new Intent();
                        intent.setAction("cn.jpush.android.unread");
                        this.sendBroadcast(intent);
                    }
                    if (!isGoDate) {
//                    Intent intent = new Intent(LoginActivity.this, ScanActivity.class);
//                    intent.putExtra("fromLogin", true);
//                    startActivityForResult(intent, 100);
                        try {
//                        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
//                        intent.putExtra("fromLogin", true);
//                        startActivityForResult(intent, 100);
                            mainActivity.sm.toggle();
                            mainActivity.getFragmentTransaction()
                                    .replace(R.id.content_frame, mainActivity.getScanFragment())
                                    .commit();
                        } catch (Exception e) {

                        }
                    } else {
                        setResult(RESULT_OK);
                    }
                    finish();
                    break;
                case 22:
                    SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                    //sp1.putValue(Constant.STATUS, "1");
                    sp1.putValue(Constant.STATUS, apiResponse.data.status);
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
