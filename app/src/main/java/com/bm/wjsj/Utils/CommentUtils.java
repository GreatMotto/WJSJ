package com.bm.wjsj.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.Toast;

import com.bm.wjsj.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.bean.RequestType;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.SmsHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;


public class CommentUtils {

    public static UMSocialService mController =UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);;

    public static String DOWNLOAD = "";

    public static IWXAPI api;

    public static Activity mActivity;

    /**
     * 分享
     */
    @SuppressWarnings("deprecation")
    public static void share(Context context, String content, String imgUrl, Bitmap bm) {
        // TDApplication.WXType = 2;
        String appname = context.getResources().getString(R.string.app_name);
        mActivity=(Activity) context;
        if (mController == null) {
            mController = UMServiceFactory.getUMSocialService("com.umeng.share",
                    RequestType.SOCIAL);
        }
        if (TextUtils.isEmpty(content)) {
            mController.setShareContent("测试");
        } else {
            mController.setShareContent(content + DOWNLOAD);
        }
        if (TextUtils.isEmpty(imgUrl)) {
            // mController.setShareContent("测试");
        } else {
            mController.setShareMedia(new UMImage(context, imgUrl));
        }

        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID

        String appID = "wxdbf59d9875952db0";
        String appSecret = "c27427de23177d9305d02cb743678271";
        if (api == null)
            api = WXAPIFactory.createWXAPI(context, null);
        api.registerApp(appID);
        // 添加微信平台

        UMWXHandler wxHandler = new UMWXHandler((Activity) context, appID, appSecret);
//        wxHandler.setTitle("陌客");
//        mController.setShareContent(content + DOWNLOAD);
        wxHandler.setTargetUrl(imgUrl);
        wxHandler.addToSocialSDK();
//        wxHandler.setTargetUrl(imgUrl);
        wxHandler.showCompressToast(false);
        //mController.setShareContent(content + DOWNLOAD);
         //支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler((Activity) context, appID, appSecret);
//        wxHandler.setTitle("陌客");
//        mController.setShareContent(content + DOWNLOAD);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
        wxCircleHandler.showCompressToast(false);
        //mController.setShareContent(content + DOWNLOAD);

        UMImage urlImage = new UMImage(context, imgUrl);
        // 设置微信好友分享内容

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(content);
        weixinContent.setTitle("陌客");
        weixinContent.setShareImage(urlImage);
        weixinContent.setTargetUrl(imgUrl);
        mController.setShareMedia(weixinContent);

        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareImage(urlImage);
        circleMedia.setShareContent(content);
        circleMedia.setTitle("陌客");
        circleMedia.setShareContent(content);
        circleMedia.setTargetUrl(imgUrl);
        mController.setShareMedia(circleMedia);

        //UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, "1104725667", "GFVKaNiWw4XUx7v3");
        //qqSsoHandler.addToSocialSDK();

        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, "1104725667",
                "GFVKaNiWw4XUx7v3");
        qZoneSsoHandler.addToSocialSDK();

        //QQShareContent qq = new QQShareContent();
        //qq.setShareImage(urlImage);
        //qq.setShareContent(content);
        //qq.setTargetUrl(imgUrl);
        //mController.setShareMedia(qq);


        // 添加sina平台
        SinaSsoHandler sinaSsoHandler = new SinaSsoHandler();
        //设置新浪SSO handler
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
        SinaShareContent sina = new SinaShareContent();
        sina.setShareContent(content);
        sina.setShareImage(urlImage);
        sina.setTitle(appname);
        sina.setTargetUrl(imgUrl);
        sinaSsoHandler.addToSocialSDK();
        mController.setShareMedia(sina);
        //mController.setShareMedia(new UMImage(context, imgUrl));

        QZoneShareContent qqZone = new QZoneShareContent();
        qqZone.setShareContent(content);
        qqZone.setShareImage(urlImage);
        qqZone.setTitle(appname);
        qqZone.setTargetUrl(imgUrl);
//        qqZone.setShareMedia(urlImage);
        mController.setShareMedia(qqZone);
        // 添加短信
        //SmsHandler smsHandler = new SmsHandler();
        //smsHandler.addToSocialSDK();
        mController.getConfig().setPlatformOrder(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA);
        mController.getConfig().removePlatform(SHARE_MEDIA.QQ);
        //mController.getConfig().removePlatform(SHARE_MEDIA.SMS);
        mController.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
//        mController.getConfig().removePlatform(SHARE_MEDIA.QZONE);
       // mController.openShare((Activity) context, false);

        SocializeListeners.SnsPostListener mSnsPostListener  = new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {

            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int stCode,
                                   SocializeEntity entity) {
                if (platform == SHARE_MEDIA.WEIXIN_CIRCLE) {
                    if (stCode == 200) {
                        Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT)
                                .show();
                    }
                    else if(stCode==40000)//取消
                    {

                    }
                    else {
                        Toast.makeText(mActivity,
                                "分享失败 : error code : " + stCode, Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }
        };
        mController.registerListener(mSnsPostListener);
        mController.openShare((Activity) context, false);
    }

    /**
     * 如需使用sso需要在onActivity中调用此方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void authSSO(int requestCode, int resultCode, Intent data){
        /** 使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }


    private static final String[] MONTH = {"一月", "二月", "三月", "四月", "五月", "六月",
                                            "七月", "八月", "九月", "十月", "十一月", "十二月"};

    public static String[] getDate(String date) {
        String[] strs = date.split("-");
        strs[1] = MONTH[Integer.parseInt(strs[1]) - 1];
        strs[2] = strs[2].substring(0,2);
        return strs;
    }

}
