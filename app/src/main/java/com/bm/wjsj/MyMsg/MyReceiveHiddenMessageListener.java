package com.bm.wjsj.MyMsg;

import android.text.TextUtils;
import android.util.Log;

import com.bm.wjsj.Base.SplashActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Utils.AppShortCutUtil;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by yangk on 2015/8/11.
 */
public class MyReceiveHiddenMessageListener implements RongIMClient.OnReceiveMessageListener {

    /**
     * 收到 push 消息的处理。
     *
     * @return true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
     */
    @Override
    public boolean onReceived(Message message, int left){
        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            return true;
        } else {
            SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
            String isTalk= sp1.getValue(Constant.ISPUSHTALK);
            if(TextUtils.isEmpty(isTalk)||"1".equals(isTalk)){
                //AppShortCutUtil.addNumShortCut(WJSJApplication.getInstance(),SplashActivity.class,true,"11",false);


                Log.e("isTalk:",isTalk+"-----------2");
                Log.e("MyReceived", " Content = " + message.getContent() + " \n" +
                        message.getSenderUserId() + " \n" + message.getExtra()
                        + " \n" + message.getObjectName());
                return false;



            }else {
                Log.e("isTalk:",isTalk+"-----------1");
                return true;
            }
        }
    }
}
