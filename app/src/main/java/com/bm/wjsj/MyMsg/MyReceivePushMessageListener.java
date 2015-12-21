package com.bm.wjsj.MyMsg;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;

import io.rong.imlib.RongIMClient;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by yangk on 2015/8/11.
 */
public class MyReceivePushMessageListener implements RongIMClient.OnReceivePushMessageListener {

    /**
     * 收到 push 消息的处理。
     *
     * @param pushNotificationMessage push 消息实体。
     * @return true 自己来弹通知栏提示，false 融云 SDK 来弹通知栏提示。
     */
    @Override
    public boolean onReceivePushMessage(PushNotificationMessage pushNotificationMessage) {
        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            return true;
        } else {
            SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
            String isTalk= sp1.getValue(Constant.ISPUSHTALK);
            if(TextUtils.isEmpty(isTalk)||"1".equals(isTalk)){
                Log.e("isTalk:",isTalk+"-----------2");
                Log.e("MyPush", " Content = " + pushNotificationMessage.getPushContent() + " \n" +
                        pushNotificationMessage.getPushData() + " \n" + pushNotificationMessage.getSenderName()
                        + " \n" + pushNotificationMessage.getTargetUserName());
                return false;



            }else {
                Log.e("isTalk:",isTalk+"-----------1");
                return true;
            }
        }
    }
}
