package com.bm.wjsj.Utils;

import android.content.Context;
import android.widget.Toast;

public class ErrorUtils {

    public static void showErrorMsg(Context context, String error) {
        String errorMsg = "";
        String[] errorMsgSplits = error.split("_");
        String errorMsgSplit = errorMsgSplits[1];
        int errorCodeInt = Integer.parseInt(errorMsgSplit);
        switch (errorCodeInt) {
            case 1:
                errorMsg = "参数不正确";
                break;
            case 2:
                errorMsg = "服务器异常";
                break;
            case 3:
                errorMsg = "类型不存在";
                break;
            case 4:
                errorMsg = "用户不存在";
                break;
            case 5:
                errorMsg = "用户名或密码错误";
                break;
            case 6:
                errorMsg = "用户被禁用";
                break;
            case 7:
                errorMsg = "手机号已使用";
                break;
            case 8:
                errorMsg = "昵称已使用";
                break;
            case 9:
                errorMsg = "验证码错误";
                break;
            case 10:
                errorMsg = "该用户已经绑定过业主信息";
                break;
            case 11:
                errorMsg = "绑定时输入的业主姓名错误";
                break;
            case 12:
                errorMsg = "原密码错误";
                break;
            case 13:
                errorMsg = "手机号未注册用户";
                break;
            case 14:
                errorMsg = "城市ID错误";
                break;
            case 15:
                errorMsg = "事件不存在";
                break;
            case 16:
                errorMsg = "已经点赞过";
                break;
            case 17:
                errorMsg = "该用户不能评分";
                break;
            case 18:
                errorMsg = "修改密码新密码为空";
                break;
            case 19:
                errorMsg = "该时间已经评分过一次";
                break;
            case 20:
                errorMsg = "单元编号不匹配";
                break;
            case 21:
                errorMsg = "业主单元编号匹配失败";
                break;
            case 22:
                errorMsg = "昵称包含敏感词";
                break;
            case 23:
                errorMsg="账户积分不足";
                break;
            case 24:
                errorMsg="报修ID错误";
                break;
            case 25:
                errorMsg="该用户没有删除权限";
                break;
            case 26:
                errorMsg="工单不存在";
                break;
            case 27:
                errorMsg = "评论不存在";
                break;
            case 28:
                errorMsg="帖子不存在";
                break;
            case 29:
                errorMsg="无法删除帖子";
                break;
            case 30:
                errorMsg="动态不存在";
                break;
            case 31:
                errorMsg="无法删除动态";
                break;
            case 32:
                errorMsg = "无法删除购物车";
                break;
            case 33:
                errorMsg="无法删除地址";
                break;
            case 34:
                errorMsg="空数据";
                break;
            case 35:
                errorMsg="该用户没有删除权限";
                break;
            case 36:
                errorMsg="已加入购物车";
                break;
            case 37:
                errorMsg = "你已关注了该用户";
                break;
            case 38:
                errorMsg="你已点赞";
                break;
            case 39:
                errorMsg="你尚未点赞";
                break;
            case 40:
                errorMsg="积分不足";
                break;
        }
        if (DialogUtils.mProgressDialog != null) {
            DialogUtils.mProgressDialog.cancel();
        }
        NewToast.show(context, errorMsg, Toast.LENGTH_SHORT);
    }
}
