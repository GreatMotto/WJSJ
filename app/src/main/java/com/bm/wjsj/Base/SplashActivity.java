package com.bm.wjsj.Base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.bm.wjsj.Bean.AttentionListBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class SplashActivity extends Activity implements APICallback.OnResposeListener {

    private Bitmap bitmap;
    private ImageView imageview;
    private SharedPreferencesHelper sp;
    private MainActivity mainActivity;
    private List<AttentionListBean> list = new ArrayList<>();
    /**
     * 目标 Id
     */
    private String path,mTitle;
    private UserInfo user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_splash);
        // 初始化极光推送
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);

        init();
//        int width = DisplayUtil.getWidth(this);
//        int height = DisplayUtil.getHeight(this);
//        Log.e("SplashActivity","width = "+width+"  height = "+height+"  17sp = "+DisplayUtil.sp2px(this,17)+"  15sp = "+DisplayUtil.sp2px(this,15));
        //WebServiceAPI.getInstance().srarchPic(this, this);
    }

    private void init() {
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            //WebServiceAPI.getInstance().getattentionlist("0", 1, 10, SplashActivity.this, this);
        }
//        final BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        newOpts.inSampleSize = 2;// 设置缩放比例
        sp = new SharedPreferencesHelper(this, Constant.SP_FILENAME);
        imageview = (ImageView) findViewById(R.id.splash_iv);
//        YoYo.with(Techniques.FadeIn).duration(1500).playOn(imageview);
//        imageview.setScaleType(ScaleType.CENTER_CROP);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.splash_bg, newOpts);
//        imageview.setImageBitmap(bitmap);

        Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        if (sp.getBooleanValue(Constant.SP_KEY_GUIDE)) {
//                            ZZWebApplication.getInstance().rememberpsw = sp
//                                    .getBooleanValue(Constant.SP_REMPSW);
                            gotoOtherActivity(MainActivity.class);
//                            finish();
                            try{
                                mainActivity = new MainActivity();
                                mainActivity.sm.toggle();
                                mainActivity.getFragmentTransaction()
                                        .replace(R.id.content_frame, mainActivity.getScanFragment())
                                        .commit();
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }finally {
                                SplashActivity.this.finish();
                            }
                        } else {
                            gotoOtherActivity(GuideActivity.class);
                            SplashActivity.this.finish();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        // 闪屏页播放3秒
        handler.sendEmptyMessageDelayed(0, 2500);


        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userid) {
//                Uri uri = getIntent().getData();
//                String path = uri.getPathSegments().get(0);
                if (list != null && list.size() > 0) {
                    //增强for把所有的用户信息 return 到融云服务端
                    for (AttentionListBean friend : list) {
                        //判断返回的userId
                        if (friend.id.equals(userid)) {
                            path = friend.head;
                            mTitle = friend.nickname;
                            user = new UserInfo(
                                    userid,
                                    mTitle,
                                    Uri.parse(Urls.PHOTO + path));
                            RongIM.getInstance().refreshUserInfoCache(user);//刷新消息列表用户信息
                            return user;
//                            return new UserInfo(friend.id, friend.nickname,
//                                    Uri.parse(Urls.PHOTO + friend.head));
                        }
                    }
                }
                return null;
            }
        }, false);
    }

    // 跳转到其他Activity
    public void gotoOtherActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra("isScan", true);
        startActivity(intent);
        overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        Log.e("SplashFailureData:", "**************************************************error:" + error + ",tag:" + tag);
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
       // WJSJApplication.getInstance().nearPicList.addAll(apiResponse.data.list);
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
                list.clear();
                list.addAll(apiResponse.data.list);

        }

    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        Log.e("SplashErrorData:", "**************************************************error:" + code + ",tag:" + tag);
    }
}
