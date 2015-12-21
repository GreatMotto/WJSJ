package com.bm.wjsj.Personal;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.Personal.version.UpdateManager;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.CommentUtils;
import com.bm.wjsj.Utils.DataCleanManager;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.SwitchView;
import com.bm.wjsj.WJSJApplication;
import com.umeng.socialize.sso.UMSsoHandler;

import io.rong.imkit.RongIM;


/**
 * @author 杨凯
 * @description 设置
 * @time 2015.3.12
 */
public class SetupActivity extends BaseActivity implements APICallback.OnResposeListener {

    private TextView tvModifyP, tvMyaddr, tvModifyPwd, tvFeedback,
            tvAbout, tvHelp, tvTell, tvUpdate, tvClear;
    private RippleView rv_exit;
    private SwitchView svActivity, svTalk, svAdd;
    private String isfollow = "", isactivity = "";
    private Boolean isFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_setup);
        initTitle("设置");
        assignViews();
        WebServiceAPI.getInstance().getSetInfo(SetupActivity.this, SetupActivity.this);

        SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
        String isTalk= sp1.getValue(Constant.ISPUSHTALK);
        Log.e("svTalk:",isTalk+"-------------");
        if(TextUtils.isEmpty(isTalk)||"1".equals(isTalk)){
            svTalk.setOpened(true);
        }else {
            svTalk.setOpened(false);
        }
        //        map.put("lat", WJSJApplication.getInstance().geoLat);
//        map.put("lon", WJSJApplication.getInstance().geoLng);
        Log.e("lat = ", WJSJApplication.getInstance().geoLat + "lon = " + WJSJApplication.getInstance().geoLng);
    }


    private void assignViews() {
        tvModifyP = (TextView) findViewById(R.id.tv_modify_p);
        tvMyaddr = (TextView) findViewById(R.id.tv_myaddr);
        tvModifyPwd = (TextView) findViewById(R.id.tv_modify_pwd);
        svActivity = (SwitchView) findViewById(R.id.sv_activity);
        svTalk = (SwitchView) findViewById(R.id.sv_talk);
        svAdd = (SwitchView) findViewById(R.id.sv_add);
        tvFeedback = (TextView) findViewById(R.id.tv_feedback);
        tvAbout = (TextView) findViewById(R.id.tv_about);
        tvHelp = (TextView) findViewById(R.id.tv_help);
        tvTell = (TextView) findViewById(R.id.tv_tell);
        tvUpdate = (TextView) findViewById(R.id.tv_update);
        tvClear = (TextView) findViewById(R.id.tv_clear);
        rv_exit = (RippleView) findViewById(R.id.rv_exit);
        tvModifyP.setOnClickListener(this);
        tvMyaddr.setOnClickListener(this);
        tvModifyPwd.setOnClickListener(this);
        tvFeedback.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvHelp.setOnClickListener(this);
        tvTell.setOnClickListener(this);
        tvUpdate.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        rv_exit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                showSure();
            }
        });
//        svActivity.setState(SwitchView.STATE_SWITCH_ON);
        svActivity.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                WebServiceAPI.getInstance().setMyPush("", "0", SetupActivity.this, SetupActivity.this);
            }

            @Override
            public void toggleToOff() {
                WebServiceAPI.getInstance().setMyPush("", "1", SetupActivity.this, SetupActivity.this);
            }
        });
//        svAdd.setState(SwitchView.STATE_SWITCH_ON);
        svAdd.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                WebServiceAPI.getInstance().setMyPush("0", "", SetupActivity.this, SetupActivity.this);
            }

            @Override
            public void toggleToOff() {
                WebServiceAPI.getInstance().setMyPush("1", "", SetupActivity.this, SetupActivity.this);
            }
        });
        //svTalk.setOpened(true);
        svTalk.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                sp1.putValue(Constant.ISPUSHTALK, "1");

                isFlag = true;
            }

            @Override
            public void toggleToOff() {

                SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                sp1.putValue(Constant.ISPUSHTALK, "0");
                isFlag = false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_modify_p:
                if (checkStatus(this)) {
                    gotoOtherActivity(ModifyInfoActivity.class);
                }
                break;
            case R.id.tv_myaddr:
                if (checkStatus(this)) {
                    gotoOtherActivity(MyAddessAcitity.class);
                }
                break;
            case R.id.tv_modify_pwd:
                if (checkStatus(this)) {
                    gotoOtherActivity(ModifyPwdActivity.class);
                }
                break;
            case R.id.tv_feedback:
                if (checkStatus(this)) {
                    gotoOtherActivity(FeedBackActivity.class);
                }
                break;
            case R.id.tv_about:
                WebServiceAPI.getInstance().help3("1",
                        SetupActivity.this, SetupActivity.this);
                break;
            case R.id.tv_help:
                gotoOtherActivity(HelpCenterActivity.class);
                break;
            case R.id.tv_tell:
                if (checkStatus(this)) {
                    String urlPath= Urls.PHOTO+"/wj_web/loadPage/index.html";
                    CommentUtils.share(this, "推荐情趣交友应用\"陌客\"，知情更知趣，最懂陌生人的社交APP。", urlPath, null);
//                    CommentUtils.share(this, "测试一下，随便玩玩！", "http://h.hiphotos.baidu.com/image/pic/item/77c6a7efce1b9d1632701663f5deb48f8c546479.jpg",
//                            BitmapFactory.decodeResource(getResources(), R.mipmap.splash_bg));
                }
                break;
            case R.id.tv_update:
                UpdateManager um = new UpdateManager(SetupActivity.this);
                um.checkUpdate();
                break;
            case R.id.tv_clear:
                try {
                    DialogUtils.showProgressDialog("正在清理...", this);
                    DataCleanManager.cleanInternalCache(this);
                    DataCleanManager.cleanExternalCache(this);
                    Thread.sleep(1000);
                }catch (Exception ex) {
                    DialogUtils.cancleProgressDialog();
                }
                DialogUtils.cancleProgressDialog();
                NewToast.show(this, "清理成功！", Toast.LENGTH_LONG);
                break;
            default:
                break;
        }
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_delete);
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_hint);
        tv_hint.setText("确定要退出陌客吗？");
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
                RongIM.getInstance().disconnect();
                WJSJApplication.getInstance().getSp().putBooleanValue(Constant.SP_KEY_ISLOGIN, false);
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_TOKEN, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_USERID, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_KEY_USER, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_KEY_PWD, "");

                WJSJApplication.getInstance().getSp().putValue(Constant.SP_FJ_AGE, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_FJ_SEX, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_FJ_PROVINCEID, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_FJ_CITYID, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_FJ_B, "");
                WJSJApplication.getInstance().getSp().putValue(Constant.SP_FJ_TIME, "");

                WJSJApplication.getInstance().getSp().putValue(Constant.UNREADMESSAGE, "0");
                WJSJApplication.getInstance().getSp().putValue(Constant.UNJREADMESSAGE, "0");
//                SetupActivity.this.onBackPressed();//退出之后返回到上一个界面(个人中心)
                Intent intent = new Intent(SetupActivity.this, MainActivity.class);
//                intent.putExtra("isShop", 3);
                intent.putExtra("isend", true);
                startActivity(intent);
                overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);//动画效果
                SetupActivity.this.finish();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            switch (tag) {
                case 1://获取推送设置信息
                    isfollow = apiResponse.data.isfollow;
                    isactivity = apiResponse.data.isactivity;
                    Log.e("isfollow", isfollow);
                    Log.e("isactivity", isactivity);
                    if (isactivity.equals("0")) {
                        svActivity.setOpened(true);
                    } else {
                        svActivity.setOpened(false);
                    }
                    if (isfollow.equals("0")) {
                        svAdd.setOpened(true);
                    } else {
                        svAdd.setOpened(false);
                    }
                    break;
                case 2://设置我的推送配置
//                    NewToast.show(this, "ok", NewToast.LENGTH_SHORT);
//                    WebServiceAPI.getInstance().getSetInfo(SetupActivity.this, SetupActivity.this);
                    break;
                case 3:
                    gotoTextActivity(getResources().getString(R.string.aboutus), apiResponse.data.helper.content);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = CommentUtils.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

}
