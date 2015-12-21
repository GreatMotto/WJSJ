package com.bm.wjsj.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.bm.wjsj.Utils.CommentUtils;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends com.umeng.socialize.weixin.view.WXCallbackActivity
{
    private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        CommentUtils.api.handleIntent(getIntent(), WXEntryActivity.this);
        CommentUtils.api = WXAPIFactory.createWXAPI(this, "wxdbf59d9875952db0", false);
        CommentUtils.api.registerApp("wxdbf59d9875952db0");
    }

//    @Override
//    public void onReq(BaseReq arg0) {
//        NewToast.show(WXEntryActivity.this, "微信请求登录", Toast.LENGTH_LONG);
//    }

//    @Override
//    public void onResp(BaseResp resp) {
//        String result;
//        System.out.println("resp.toString()=========" + resp.toString());
//        NewToast.show(WXEntryActivity.this, "响应微信登录", Toast.LENGTH_LONG);
//        // SendAuth.Resp aresp = (SendAuth.Resp) resp;
////        if (CommentUtils.WXType != 1) {
////            WXEntryActivity.this.finish();
////            return;
////        }
//
//        switch (resp.errCode) {
//        case BaseResp.ErrCode.ERR_OK:
//            String code = ((SendAuth.Resp) resp).code;
//            result = "授权成功";
//            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx11891885791bfa2f&secret=e3f818087e237efaf27acd7f2854a1a8&code="
//                    + code + "&grant_type=authorization_code";
//            NewToast.show(this, result, Toast.LENGTH_SHORT);
//            break;
//        case BaseResp.ErrCode.ERR_USER_CANCEL:
//            result = "授权取消";
//            NewToast.show(this, result, Toast.LENGTH_SHORT);
//            WXEntryActivity.this.finish();
//            break;
//        case BaseResp.ErrCode.ERR_AUTH_DENIED:
//            result = "授权结束";
//            NewToast.show(this, result, Toast.LENGTH_SHORT);
//            WXEntryActivity.this.finish();
//            break;
//        default:
////            result = "授权成功";
////            ToastUtil.showToast(this, result);
////            WXEntryActivity.this.finish();
//            break;
//        }
//        Log.e("weixin","errCode:" + BaseResp.ErrCode.ERR_OK);
//        Log.e("weixin","resp.errStr:" + resp.errStr);
//        NewToast.show(WXEntryActivity.this, resp.errStr, Toast.LENGTH_LONG);
//    }

}
