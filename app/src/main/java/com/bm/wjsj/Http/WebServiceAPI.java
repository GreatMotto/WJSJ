package com.bm.wjsj.Http;

import android.content.Context;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.bm.wjsj.Bean.AddressBean;
import com.bm.wjsj.Bean.AttentionListBean;
import com.bm.wjsj.Bean.BannerBean;
import com.bm.wjsj.Bean.CircleBean;
import com.bm.wjsj.Bean.DynamicInfo;
import com.bm.wjsj.Bean.DynamicListBean;
import com.bm.wjsj.Bean.Helper;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.Message;
import com.bm.wjsj.Bean.MessageBean;
import com.bm.wjsj.Bean.MyDynamicListBean;
import com.bm.wjsj.Bean.Paytype;
import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Bean.PostDetailInfo;
import com.bm.wjsj.Bean.Product;
import com.bm.wjsj.Bean.ReportBean;
import com.bm.wjsj.Bean.Result;
import com.bm.wjsj.Bean.ReviewBean;
import com.bm.wjsj.Bean.ShopCarBean;
import com.bm.wjsj.Bean.Spec;
import com.bm.wjsj.Bean.StoreBean;
import com.bm.wjsj.Bean.StoreListBean;
import com.bm.wjsj.Bean.StoreReview;
import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Bean.myScoreInfoBean;
import com.bm.wjsj.Bean.myScoreProorderBean;
import com.bm.wjsj.Bean.nearPic;
import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Bean.scoreproInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback.OnResposeListener;
import com.bm.wjsj.Utils.Md5Util;
import com.bm.wjsj.WJSJApplication;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit.mime.TypedFile;

/**
 * Created by yangk on 2015/8/24.
 */
public class WebServiceAPI {

    private WebServiceAPI() {

    }


    private static class Datacontroller {

        /**
         * 单例变量
         */
        private static WebServiceAPI instance = new WebServiceAPI();
    }

    public static WebServiceAPI getInstance() {
        return Datacontroller.instance;
    }

    /**
     * 注册接口
     *
     * @param gender   性别
     * @param mobile
     * @param password
     * @param code
     */
    public void register(String gender, String mobile, String password, String code, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("sex", gender);
        map.put("mobile", mobile);
        map.put("password", Md5Util.md5(password));
        map.put("code", code);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.REGISTER, map, new APICallback(context, listener, 3));
    }

    /**
     * 上线
     */
    public void online(String pushno, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date = sDateFormat.format(new java.util.Date());
        map.put("loginTime", date);
        map.put("lon", WJSJApplication.getInstance().geoLng);
        map.put("lat", WJSJApplication.getInstance().geoLat);
        map.put("pushno", pushno);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ONLINE, map, new APICallback(context, listener, 22));
    }

    /**
     * 反馈意见
     *
     * @param content  反馈内容
     * @param listener
     * @param context
     */
    public void addFeedback(String content,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("content", content);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDFEEDBACK, map, new APICallback(context, listener, 1));
    }

    /**
     * 获取推送设置信息
     *
     * @param listener
     * @param context
     */
    public void getSetInfo(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(MapData.class).PostAPI(Urls.GETSETINFO, map, new APICallback(context, listener, 1));
    }

    /**
     * 设置我的推送配置
     *
     * @param isfollow   是否接收关注消息 0是 1否
     * @param isactivity 是否接收活动消息 0是 1否
     * @param listener
     * @param context
     */
    public void setMyPush(String isfollow, String isactivity,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("isfollow", isfollow);
        map.put("isactivity", isactivity);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.SETMYPUSH, map, new APICallback(context, listener, 2));
    }


    /**
     * 帮助中心
     *
     * @param type
     */
    public void help(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 2));
    }


    /*yangy 帮助中心区分tag */
    public void help3(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 3));
    }

    public void help4(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 4));
    }

    public void help5(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 5));
    }

    public void help6(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 6));
    }

    public void help8(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 8));
    }

    public void help9(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 9));
    }

    public void help10(String type, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", type);
        APIClient.getInstance().getAPIService(Helper.class).PostAPI(Urls.HELPER, map, new APICallback(context, listener, 10));
    }
    /*yangy 帮助中心区分tag */

    /*修改头像*/
    public void topcompleteInfo(File fromFile, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();

        map.put("userId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        TypedFile file = new TypedFile(getMimeType(fromFile), fromFile);
//        map.put("head", fromFile);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.COMPLETEINFO, file, map, new APICallback(context, listener, 2));

    }

    /**
     * 完善信息接口
     *
     * @param password
     * @param nickname   昵称
     * @param birthday   生日
     * @param provinceId
     * @param cityId
     * @param picture    头像
     * @param listener
     * @param context
     */
    public void completeInfo(String id, String password, File picture,
                             String nickname, String birthday, String provinceId, String cityId, String sign,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("password", Md5Util.md5(password));
        map.put("nickname", nickname);
        map.put("birthday", birthday);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        map.put("sign", sign);
        TypedFile file = new TypedFile(getMimeType(picture), picture);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.COMPLETEINFO, (TypedFile) file, map, new APICallback(context, listener, 1));
    }

    /**
     * 修改资料
     *
     * @param id
     * @param nickname
     * @param birthday
     * @param provinceId
     * @param cityId
     * @param sign
     * @param listener
     * @param context
     */
    public void modifyInfo(String id, String nickname, String birthday, String provinceId, String cityId, String sign, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("nickname", nickname);
        map.put("sign", sign);
        map.put("birthday", birthday);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        Log.e("input param", map.toString());
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.COMPLETEINFO, map, new APICallback(context, listener, 1));
    }

    /**
     * 修改个人信息接口
     *
     * @param nickname   昵称
     * @param birthday   生日
     * @param provinceId
     * @param cityId
     * @param listener
     * @param context
     */
    public void compInfo(String nickname, String sign, String birthday, String provinceId, String cityId, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("nickname", nickname);
        map.put("sign", sign);
        map.put("birthday", birthday);
        map.put("provinceId", provinceId);
        map.put("cityId", cityId);
        File a = null;
        TypedFile file = new TypedFile(getMimeType(a), a);
        System.out.println("prepire to send ");
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.COMPLETEINFO, (TypedFile) file, map, new APICallback(context, listener, 1));
        System.out.println("sended");
    }

    /**
     * 登录
     *
     * @param mobile
     * @param password
     * @param listener
     * @param context
     */
    public void login(String mobile, String password,
                      OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("password", Md5Util.md5(password));
        APIClient.getInstance().getAPIService(UserInfo.class).PostAPI(Urls.LOGIN, map, new APICallback(context, listener, 2));
    }


    /**
     * 所有动态列表
     *
     * @param dynamicType FOCUS：关注（默认） HOT：热门 NEAR：附近
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void dynamicList(String dynamicType, int pageNum, int pageSize,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();

//        Log.e("dynamicList", "dynamicType = " + dynamicType);
//        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
//        map.put("dynamicType", dynamicType);
//        map.put("lat", WJSJApplication.getInstance().geoLat);
//        map.put("lon", WJSJApplication.getInstance().geoLng);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("order", dynamicType);
        APIClient.getInstance().getAPIService(DynamicListBean.class).PostAPI(Urls.DYNAMICLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 动态详情
     *
     * @param id       动态id
     * @param listener
     * @param context
     */
    public void dynamicDetail(String id, String userid, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("userid", userid);
        APIClient.getInstance().getAPIService(DynamicInfo.class).PostAPI(Urls.DYNAMICDETAIL, map, new APICallback(context, listener, 1));
        Log.e("id=======dt======", id);
    }

    /**
     * 发布动态
     *
     * @param content  动态内容
     * @param pics     图片
     * @param listener
     * @param context
     */
    public void dynamicPublish(String content, List<File> pics,
                               OnResposeListener listener, Context context) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
            map.put("content", content);
            map.put("lat", WJSJApplication.getInstance().geoLat);
            map.put("lon", WJSJApplication.getInstance().geoLng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
        TypedFile[] files = new TypedFile[pics.size()];
        TypedFile file;
        for (int i = 0; i < pics.size(); i++) {
            file = new TypedFile(getMimeType(pics.get(i)), pics.get(i));
            files[i] = file;
        }
        */
        // test by Jason
        List<TypedFile> files = new ArrayList<TypedFile>();
        TypedFile file;
        for (int i = 0; i < pics.size(); i++) {
            file = new TypedFile(getMimeType(pics.get(i)), pics.get(i));
            files.add(file);
        }
        map.put("path", files);

        // test by Jason
        //APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DYNAMICPUBLISH, files, map, new APICallback(context, listener, 1));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DYNAMICPUBLISH, 1, map, new APICallback(context, listener, 1));
    }

    /**
     * 查看他人/我的动态列表
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void myDynamicList(String userid, int pageNum, int pageSize,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(MyDynamicListBean.class).PostAPI(Urls.GETMYDYNAMIC, map, new APICallback(context, listener, 2));
    }

    /**
     * 评论动态
     *
     * @param objectid 动态ID
     * @param touserid 被评论用户ID
     * @param content
     * @param listener
     * @param context
     */
    public void commentDynamic(String objectid, String touserid, String content,
                               OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        try {
            map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
            map.put("objectid", objectid);
            map.put("touserid", touserid);
            map.put("content", content);
        } catch (Exception e) {

        }

        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DYNAMICCOMMENT, map, new APICallback(context, listener, 2));
    }

    /**
     * 点赞
     *
     * @param listener
     * @param context
     */
    public void addPraise(String id, String type,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        map.put("type", type);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDPRAISE, map, new APICallback(context, listener, 3));
    }

    /**
     * 取消点赞
     *
     * @param listener
     * @param context
     */
    public void deletePraise(String id, String type,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        map.put("type", type);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETEPRAISE, map, new APICallback(context, listener, 4));
    }

    /**
     * 删除动态
     *
     * @param id       动态id
     * @param listener
     * @param context
     */
    public void deleteDynamic(String id,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
//        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DYNAMICDEL, map, new APICallback(context, listener, 5));
    }

    /**
     * 删除动态评论
     *
     * @param id       动态id
     * @param listener
     * @param context
     */
    public void deleteDynamicReport(String id,
                                    OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DYNAMICCMTDEL, map, new APICallback(context, listener, 6));
    }

    /**
     * 发送验证码
     *
     * @param mobile
     * @param listener
     * @param context
     */
    public void sendCode(String mobile, String flag,
                         OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("type", flag);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.SENDCODE, map, new APICallback(context, listener, 1));
    }

    /**
     * 富文本显示页
     *
     * @param platformType
     * @param listener
     * @param context
     */
    public void webText(String platformType,
                        OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("platformType", platformType);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.WEBTEXT, map, new APICallback(context, listener, 1));
    }

    /**
     * 找回密码/修改密码
     *
     * @param mobile
     * @param password
     * @param code
     * @param listener
     * @param context
     */
    public void findPwd(String mobile, String password, String code,
                        OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("mobile", mobile);
        map.put("password", Md5Util.md5(password));
        map.put("code", code);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.FINDPWD, map, new APICallback(context, listener, 2));
    }

    /**
     * 消息列表
     *
     * @param type     CHATINFO 聊天消息 0：系统消息 1：活动消息 2：关注消息
     * @param listener
     * @param context
     */
    public void msgList(String type, String pageNum, String pageSize,
                        OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("type", type);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        APIClient.getInstance().getAPIService(MessageBean.class).PostAPI(Urls.MSGLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 消息列表
     *
     * @param listener
     * @param context
     */
    public void findUnReadmessageNum(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(MessageBean.class).PostAPI(Urls.UNREADMSGNUM, map, new APICallback(context, listener, 11));
    }

    /**
     * 活动消息详细
     */
    public void mFind(String id, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(Message.class).PostAPI(Urls.FINDACTIVIY, map, new APICallback(context, listener, 2));
        Log.e("iddddddddddd", id);
    }

    /**
     * 删除消息
     */
    public void mdelete(String id, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.MDELETE, map, new APICallback(context, listener, 3));
    }

    /**
     * 举报
     *
     * @param type       1：垃圾营销 2：淫秽色情 3：敏感信息 4：骚扰我
     * @param content    举报内容
     * @param objecttype 被举报对象类型 0用户 1评论 2帖子 3动态
     * @param objectid   被举报对象id
     * @param listener
     * @param context
     */
    public void report(String type, String content, String objectid, String objecttype,
                       OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("type", type);
        map.put("content", content);
        map.put("objectid", objectid);
        map.put("objecttype", objecttype);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.REPORT, map, new APICallback(context, listener, 1));
    }

    /**
     * 圈子首页banner图
     *
     * @param listener
     * @param context
     */
    public void circleBanner(OnResposeListener listener, Context context) {
        APIClient.getInstance().getAPIService(ImageBean.class).PostAPI(Urls.CIRCLEBANNER, new APICallback(context, listener, 5));
    }

    /**
     * banner 广告webview图
     *@param bannerId   bannerId
     * @param listener
     * @param context
     */
    public void getBannerWebview(String bannerId,OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id",bannerId);
        APIClient.getInstance().getAPIService(BannerBean.class).PostAPI(Urls.BANNERWEBVIEW,map, new APICallback(context, listener, 17));
    }

    /*圈子首页-我的圈子 tag 2*/
    public void circleMy(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(CircleBean.class).PostAPI(Urls.CIRCLEMY, map, new APICallback(context, listener, 2));
    }

    /*圈子首页(更多圈子)*/
    public void circleMore(int pageNum, int pageSize,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN))
            map.put("id", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(CircleBean.class).PostAPI(Urls.CIRCLEMORE, map, new APICallback(context, listener, 3));
    }

    //add by yinl 2015-10-21 begin
    /*加入圈子*/
    /*
     * @param userId 用户ID
     * @param circleId 圈子ID
     */
    public void addCircle(String circleId,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN))
            map.put("userId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("circleId", circleId);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDCIRCLE, map, new APICallback(context, listener, 4));
    }

    /*退出圈子*/
    /*
     * @param userId 用户ID
     * @param circleId 圈子ID
     */
    public void exitCircle(String circleId,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN))
            map.put("userId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("circleId", circleId);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.EXITCIRCLE, map, new APICallback(context, listener, 5));
    }
    //add by yinl 2015-10-21 end

    /**
     * 圈子详情-首页帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void circlePost(int type, int pageNum, int pageSize,
                           String circleId, int order, String userid,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", String.valueOf(type));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("circleId", circleId);
        map.put("order", String.valueOf(order));
        map.put("userid", userid);
        APIClient.getInstance().getAPIService(PostBean.class).PostAPI(Urls.CIRCLEPOST, map, new APICallback(context, listener, type));
    }

    /**
     * 置顶帖子列表
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void circleTopPost(int type, int pageNum, int pageSize,
                              String circleId, int order, String userid,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type", String.valueOf(type));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        map.put("circleId", circleId);
        map.put("order", String.valueOf(order));
        map.put("userid", userid);
        APIClient.getInstance().getAPIService(PostBean.class).PostAPI(Urls.CIRCLEPOST, map, new APICallback(context, listener, 1));
    }

    /*圈子详情*/
    public void circleDetail(int pageNum, int pageSize,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(CircleBean.class).PostAPI(Urls.CIRCLEDETAIL, map, new APICallback(context, listener, 1));
    }


    /**
     * 发布帖子
     *
     * @param circleId 圈子ID
     * @param title    标题
     * @param content  帖子内容
     * @param context
     */
    public void postPublish(String circleId, String title, String content, List<File> pics,
                            OnResposeListener listener, Context context) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("circleId", circleId);
        map.put("title", title);
        map.put("content", content);
        map.put("createUser", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        /*
        TypedFile[] files = new TypedFile[pics.size()];
        TypedFile file;
        for (int i = 0; i < pics.size(); i++) {
            file = new TypedFile(getMimeType(pics.get(i)), pics.get(i));
            files[i] = file;
        }
        */
        // test by Jason
        List<TypedFile> files = new ArrayList<TypedFile>();
        TypedFile file;
        for (int i = 0; i < pics.size(); i++) {
            file = new TypedFile(getMimeType(pics.get(i)), pics.get(i));
            files.add(file);
        }
        map.put("path", files);
        // test by Jason
        //APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DYNAMICPUBLISH, files, map, new APICallback(context, listener, 1));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.POSTPUBLISH, 1, map, new APICallback(context, listener, 1));
    }

    /**
     * 帖子列表 (所有)
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void allPostList(String userid, int pageNum, int pageSize,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(PostBean.class).PostAPI(Urls.ALLPOSTLIST, map, new APICallback(context, listener, 3));
    }

    /**
     * 我的帖子列表(包含个人，和他人帖子列表)
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void myPostList(String userid, int pageNum, int pageSize,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", userid);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(PostBean.class).PostAPI(Urls.MYPOSTLIST, map, new APICallback(context, listener, 3));
    }

    /**
     * 帖子详情
     *
     * @param id       帖子详情
     * @param listener
     * @param context
     */
    public void postDetail(String id,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(PostDetailInfo.class).PostAPI(Urls.POSTDETAIL, map, new APICallback(context, listener, 1));
        Log.e("id========tz=====", id);
    }

    /**
     * 帖子评论列表
     *
     * @param id       帖子id
     * @param listener
     * @param context
     */
    public void postReviewList(String id, String order,
                               OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        map.put("order", order);
//        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(ReviewBean.class).PostAPI(Urls.REVIEWPOSTLIST, map, new APICallback(context, listener, 2));
    }

    /**
     * 帖子评论详情
     *
     * @param id       评论id
     * @param listener
     * @param context
     */
    public void reviewDetail(String id, String order,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        map.put("order", order);
        APIClient.getInstance().getAPIService(ReviewBean.class).PostAPI(Urls.REVIEWPOSTLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 帖子评论详情列表
     *
     * @param id       评论id
     * @param listener
     * @param context
     */
    public void reviewDetailList(String id,
                                 OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(ReportBean.class).PostAPI(Urls.REVIEWDETAIL, map, new APICallback(context, listener, 7));
        Log.e("postid==========", id);
    }

    /**
     * 发表帖子评论(主评论/子评论)
     *
     * @param content  评论内容
     * @param listener
     * @param context
     */
    public void reviewPost(String content, String objectid, String type, String touserid,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("content", content);
        map.put("objectid", objectid);
        map.put("type", type);
        map.put("touserid", touserid);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.REVIEWPOST, map, new APICallback(context, listener, 6));
    }

    /**
     * 删除我的帖子
     *
     * @param id       帖子ID
     * @param listener
     * @param context
     */
    public void deleteMyPost(String id,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETEPOST, map, new APICallback(context, listener, 5));
    }

    /**
     * 删除我的帖子下的评论
     *
     * @param id       我的帖子下的评论ID
     * @param listener
     * @param context
     */
    public void deleteMyPostReview(String id,
                                   OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETEPOSTCMT, map, new APICallback(context, listener, 8));
    }

    /**
     * 帖子评论详情页面删除子评论
     *
     * @param id       评论id
     * @param listener
     * @param context
     */
    public void deleteReportDetailReprot(String id,
                                         OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.IDELETEPOSTMYCOM, map, new APICallback(context, listener, 66));
    }

    /**
     * 商城首页
     *
     * @param listener
     * @param context
     */
    public void store(OnResposeListener listener, Context context) {
        APIClient.getInstance().getAPIService(StoreBean.class).PostAPI(Urls.STORE, new APICallback(context, listener, 1));
    }

    /**
     * 商品分类
     *
     * @param shoptype 商城一级分类id
     * @param listener
     * @param context
     */
    public void storeType(String shoptype,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
//        if (!TextUtils.isEmpty(shoptype))
        map.put("parentid", shoptype);
        APIClient.getInstance().getAPIService(Result.class).PostAPI(Urls.STORETYPE, map, new APICallback(context, listener, 1));
    }

    /**
     * 商品分类
     *
     * @param shoptype 商城二级分类id
     * @param listener
     * @param context
     */
    public void storeType1(String shoptype,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
//        if (!TextUtils.isEmpty(shoptype))
        map.put("parentid", shoptype);
        APIClient.getInstance().getAPIService(Result.class).PostAPI(Urls.STORETYPE, map, new APICallback(context, listener, 2));
    }

    /**
     * 商品列表
     *
     * @param type1    商城一级分类
     * @param type2    商城二级分类
     * @param type     查询方式 0：最新上架 （默认） 1：销量（降序） 2：价格（升序） 3：价格（降序）
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void storeList(String type1, String type2,
                          String plateid, String name,
                          String type, String pageNum, String pageSize,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("type1", type1);
        map.put("type2", type2);//类型查询
        map.put("plateid", plateid);//版块查询
        map.put("name", name);//名称查询
        map.put("type", type);//上架商品
        map.put("pageNum", pageNum);//页码
        map.put("pageSize", pageSize);//页数
        APIClient.getInstance().getAPIService(StoreListBean.class).PostAPI(Urls.STORELIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 商品详情
     *
     * @param id       商品id
     * @param listener
     * @param context
     */
    public void storeDetail(String id,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
//        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN))
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(Product.class).PostAPI(Urls.STOREDETAIL, map, new APICallback(context, listener, 1));
    }

    /**
     * 商品规格
     *
     * @param typeId   商品二级分类id
     * @param listener
     * @param context
     */
    public void storeSpec(String typeId,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", typeId);
        APIClient.getInstance().getAPIService(Spec.class).PostAPI(Urls.STORESPEC, map, new APICallback(context, listener, 2));
    }

    /**
     * 商品指定规格的商品价格
     *
     * @param id       商品二级分类id
     * @param type1Id  规格1ID
     * @param type2Id  规格2ID
     * @param listener
     * @param context
     */
    public void storeFindDetail(String id, String type1Id, String type2Id,
                                OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("type1Id", type1Id);
        map.put("type2Id", type2Id);
        APIClient.getInstance().getAPIService(Spec.class).PostAPI(Urls.FINDDETAIL, map, new APICallback(context, listener, 8));
    }

    /**
     * 商品评价
     *
     * @param id       商品id
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void storeReview(String id, String pageNum, String pageSize,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("pageNum", pageNum);
        map.put("pageSize", pageSize);
        APIClient.getInstance().getAPIService(StoreReview.class).PostAPI(Urls.STOREREVIEW, map, new APICallback(context, listener, 1));
    }

    /**
     * 评价商品
     *
     * @param orderid     订单id
     * @param orderinfoid 订单详细id
     * @param productid   商品ID
     * @param content     评价内容
     * @param star        评价等级1-5 ： 分数越高，评价越高
     * @param listener
     * @param context
     */
    public void reviewProduct(String orderid, String orderinfoid, String productid, String content, String star,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("orderid", orderid);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("orderinfoid", orderinfoid);
        map.put("productid", productid);
        map.put("content", content);
        map.put("star", star);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.REVIEWPRODUCT, map, new APICallback(context, listener, 1));
    }

    /**
     * 确认订单
     *
     * @param ids       选择确认生成订单的购物车商品ids，id之间用“，”逗号连接,从立即购买进入确认订单时可为空
     * @param consignee 收货人
     * @param mobile    收货人手机号
     * @param cityid    城市id
     * @param address   收获地址
     * @param paytype   支付方式 WEIXIN：微信支付 YINLIAN：银联支付 ALIPAY：支付宝 DAOFU：货到付款
     * @param realpay   实际付款
     * @param remark    备注
     * @param orderlist
     * @param listener
     * @param context
     */
    public void confirmorder(String ids, String consignee, String mobile, String cityid, String address, String paytype, String realpay, String remark, String orderlist,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("ids", ids);
        map.put("consignee", consignee);
        map.put("mobile", mobile);
        map.put("account", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME));
        map.put("cityid", cityid);
        map.put("address", address);
        map.put("paytype", paytype);
        map.put("realpay", realpay);
        map.put("remark", remark);
        map.put("orderlist", orderlist);
        APIClient.getInstance().getAPIService(orderBean.class).PostAPI(Urls.ORDERCONFIRM, map, new APICallback(context, listener, 10));
    }

    /**
     * 等级优惠
     *
     * @param listener
     * @param context
     */
    public void findpaytype(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(Paytype.class).PostAPI(Urls.FINDPAYTYPE, map, new APICallback(context, listener, 0));
    }

    /**
     * 计算运费(根据城市判断是否需要运费)
     *
     * @param cityid     城市ID
     * @param productids 商品ID逗号隔开
     * @param listener
     * @param context
     */
    public void findfreight(String cityid, String productids, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("cityid", cityid);
        map.put("productids", productids);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.FINDFREIGHT, map, new APICallback(context, listener, 2));
    }

    /**
     * 加入购物车
     *
     * @param productId 商品id
     * @param count     数量，默认1
     * @param list      id 规格id name 规格名称 typeId 二级分类id
     * @param listener
     * @param context
     */
    public void addShopCar(String productId, String count, String price, String list,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("productid", productId);
//        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("count", count);
        map.put("price", price);
        map.put("speclist", list);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDSHOPCAR, map, new APICallback(context, listener, 3));
    }

    /**
     * 关注列表
     *
     * @param type     0:相互关注（默认） 1：我关注的 2：关注我的
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void getattentionlist(String type, int pageNum, int pageSize,
                                 OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("type", type);
        map.put("lat", WJSJApplication.getInstance().geoLat);
        map.put("lon", WJSJApplication.getInstance().geoLng);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(AttentionListBean.class).PostAPI(Urls.GETATTENTIONLIST, map, new APICallback(context, listener, 1));
        Log.e("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
    }

    /**
     * 关注数
     *
     * @param type     关注类型
     * @param listener
     * @param context
     */
    public void attentionNum(String type,
                             OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("type", type);
        map.put("lat", WJSJApplication.getInstance().geoLat);
        map.put("lon", WJSJApplication.getInstance().geoLng);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.GETATTENTIONNUM, map, new APICallback(context, listener, Integer.parseInt(type)));
    }

    /**
     * 关注
     *
     * @param friendType 0:相互关注（默认） 1：我关注的 2：关注我的
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void attentionList(String friendType, int pageNum, int pageSize,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("friendType", friendType);
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("lat", WJSJApplication.getInstance().geoLat);
        map.put("lon", WJSJApplication.getInstance().geoLng);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ATTENTION, map, new APICallback(context, listener, 1));
    }

    /**
     * 关注别人
     *
     * @param touserid 需要关注的好友id
     * @param listener
     * @param context
     */
    public void attentionPerson(String touserid,
                                OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("touserid", touserid);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ATTENTIONPERSON, map, new APICallback(context, listener, 4));
    }

    /**
     * 取消关注
     *
     * @param touserid 需要取消关注的好友id
     * @param listener
     * @param context
     */
    public void deleteAttention(String touserid,
                                OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("touserid", touserid);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETEATTENTION, map, new APICallback(context, listener, 5));
    }


    /**
     * 是否互相关注
     *
     * @param touserid id
     * @param listener
     * @param context
     */
    public void isMutual(String touserid,
                         OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("touserid", touserid);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ISMUTUAL, map, new APICallback(context, listener, 6));
    }

    /**
     * 查看他人信息
     *
     * @param userId   选择用户ID
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void getPersonInfo(String userId, int pageNum, int pageSize,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("lat", WJSJApplication.getInstance().geoLat);
        map.put("lon", WJSJApplication.getInstance().geoLng);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.GETPERSONINFO, map, new APICallback(context, listener, 1));
    }

    /**
     * 查询我的积分
     *
     * @param listener
     * @param context
     */
    public void scoreMyscore(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.MYSCORE, map, new APICallback(context, listener, 5));
    }

    /**
     * 兑换商品列表
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void scoreScoreprolist(int pageNum, int pageSize,
                                  OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(StoreListBean.class).PostAPI(Urls.SCOREPROLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 兑换记录
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void scoreMyscoreproorder(int pageNum, int pageSize,
                                     OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(myScoreProorderBean.class).PostAPI(Urls.MYSCOREPROORDER, map, new APICallback(context, listener, 1));
    }

    /**
     * 积分获取记录
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void scoreMyscoreinfo(int pageNum, int pageSize,
                                 OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(myScoreInfoBean.class).PostAPI(Urls.MYSCOREINFO, map, new APICallback(context, listener, 1));
    }

    /**
     * 奖品详情
     *
     * @param listener
     * @param context
     */
    public void scoreScoreproinfo(String id,
                                  OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", String.valueOf(id));
        APIClient.getInstance().getAPIService(scoreproInfo.class).PostAPI(Urls.SCOREPROINFO, map, new APICallback(context, listener, 4));
    }

    /**
     * 确认兑换
     *
     * @param consignee 收货人姓名
     * @param mobile    收货人手机
     * @param address   收货人地址
     * @param id        商品id
     * @param score     兑换该商品所需积分
     * @param listener
     * @param context
     */
    public void scoreAddscoreproorder(String consignee, String mobile, String address, String id, String score,String pronum,
                                      OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("consignee", consignee);
        map.put("mobile", mobile);
        map.put("address", address);
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", String.valueOf(id));
        map.put("score", String.valueOf(score));
        map.put("pronum", String.valueOf(pronum));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDSCOREPROORDER, map, new APICallback(context, listener, 6));
    }

    /**
     * 收货地址列表
     *
     * @param listener
     * @param context
     */
    public void addrList(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(AddressBean.class).PostAPI(Urls.ADDRLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 选择默认收货地址
     *
     * @param id       收货地址ID
     * @param listener
     * @param context
     */
    public void addrDefault(String id, OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDRDEFAULT, map, new APICallback(context, listener, 3));
    }

    /**
     * 新增收货地址
     *
     * @param consignee 收货人姓名
     * @param mobile    联系方式
     * @param address   详细地址
     * @param listener
     * @param context
     */
    public void addrEdit(String consignee, String mobile, String provinceId, String cityId, String address,
                         OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("consignee", consignee);
        map.put("mobile", mobile);
        map.put("provinceid", provinceId);
        map.put("cityid", cityId);
        map.put("address", address);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDREDIT, map, new APICallback(context, listener, 1));
    }

    /**
     * 修改收货地址
     *
     * @param id        收货地址ID(有地址ID表示编辑，没有则表示新增)
     * @param consignee 收货人姓名
     * @param mobile    联系方式
     * @param address   详细地址
     * @param listener
     * @param context
     */
    public void modifyEdit(String id, String consignee, String mobile, String provinceId, String cityId, String address,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        map.put("consignee", consignee);
        map.put("mobile", mobile);
        map.put("provinceid", provinceId);
        map.put("cityid", cityId);
        map.put("address", address);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ADDRESS, map, new APICallback(context, listener, 1));
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @param listener
     * @param context
     */
    public void deleteAddr(String id, String isdefault,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("id", id);
        map.put("isdefault", isdefault);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETEADDR, map, new APICallback(context, listener, 2));
    }

    /**
     * 我的订单（包含待付款、待收货、所有）
     *
     * @param orderType 订单类型 0：所有  1：待付款  2：待收货  3：待评价  4：完成
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void myOrderList(String orderType, int pageNum, int pageSize,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("orderType", orderType);
        map.put("pageNum", String.valueOf(pageNum));
        map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.MYORDERLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 我的订单（包含待付款、待收货、所有）
     *
     * @param status   订单状态 0待付款 1待发货 2待收货 3待评价 4已完成 5已取消
     * @param listener
     * @param context
     */
    public void myOrderList(String status,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("status", status);
        APIClient.getInstance().getAPIService(orderBean.class).PostAPI(Urls.MYORDERLIST, map, new APICallback(context, listener, 1));
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void orderDetail(String orderId, int pageNum, int pageSize,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("orderId", orderId);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ORDERDETAIL, map, new APICallback(context, listener, 1));
    }

    /**
     * 修改订单
     *
     * @param id        订单id
     * @param consignee 收货人
     * @param mobile    手机号
     * @param cityid    城市id
     * @param address   地址
     * @param paytype   支付方式
     * @param realpay   实际金额
     * @param remark    备注
     * @param listener
     * @param context
     */
    public void updateOrder(String id, String consignee, String mobile, String cityid, String address, String paytype, String realpay, String remark,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("consignee", consignee);
        map.put("mobile", mobile);
        map.put("cityid", cityid);
        map.put("address", address);
        map.put("paytype", paytype);
        map.put("realpay", realpay);
        map.put("remark", remark);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.UPDATEORDER, map, new APICallback(context, listener, 3));
    }

    /**
     * 修改订单状态
     *
     * @param id       订单id
     * @param listener
     * @param context
     */
    public void updateOrderStatus(String id, String status,
                                  OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("status", status);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.UPDATEORDERSTATUS, map, new APICallback(context, listener, 11));
    }


    /**
     * 修改订单编号
     *
     * @param id       订单id
     * @param listener
     * @param context
     */
    public void updateOrderNum(String id,
                               OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.UPDATEORDERNUM, map, new APICallback(context, listener, 12));
    }

    /**
     * 删除订单
     *
     * @param id       订单id
     * @param listener
     * @param context
     */
    public void deleteOrder(String id,
                            OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETEORDER, map, new APICallback(context, listener, 2));
    }

    /**
     * 我的收藏
     *
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void myCollect(int pageNum, int pageSize,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        //map.put("pageNum", String.valueOf(pageNum));
        //map.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(StoreListBean.class).PostAPI(Urls.MYCOLLECT, map, new APICallback(context, listener, 1));
    }

    /**
     * 收藏商品
     *
     * @param productid 商品ID
     * @param listener
     * @param context
     */
    public void addCollect(String productid,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("productid", productid);
        APIClient.getInstance().getAPIService(StoreListBean.class).PostAPI(Urls.ADDCOLLECT, map, new APICallback(context, listener, 6));
    }

    /**
     * 删除收藏
     *
     * @param listener
     * @param context
     */
    public void deleteCollect(String productid,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("productid", productid);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETECOLLECT, map, new APICallback(context, listener, 2));
    }

    /**
     * 确认收货
     *
     * @param orderId
     * @param listener
     * @param context
     */
    public void orderSure(String orderId,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("orderId", orderId);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.ORDERSURE, map, new APICallback(context, listener, 1));
    }

    /**
     * 购物车
     *
     * @param listener
     * @param context
     */
    public void shopCarList(OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(ShopCarBean.class).PostAPI(Urls.SHOPCARLIST, map, new APICallback(context, listener, 7));
    }

    /**
     * 删除购物车（单个或多个删除）
     *
     * @param ids      购物车记录ID(商品id之前用逗号分隔)
     * @param listener
     * @param context
     */
    public void deleteShopCar(String ids,
                              OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("ids", ids);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.DELETESHOPCAR, map, new APICallback(context, listener, 2));
    }

    /**
     * 修改个人资料
     *
     * @param nickname 用户昵称
     * @param birthday 生日 格式：yyyy-MM-dd
     * @param sign     个人签名
     * @param listener
     * @param context
     */
    public void userUpdate(String nickname, String birthday, String sign,
                           OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("nickname", nickname);
        map.put("birthday", birthday);
        map.put("sign", sign);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.USERINFOUPDATE, map, new APICallback(context, listener, 1));
    }

    /**
     * 反馈
     *
     * @param content
     * @param listener
     * @param context
     */
    public void feedBack(String content,
                         OnResposeListener listener, Context context) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("currentUserId", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        map.put("content", content);
        APIClient.getInstance().getAPIService(String.class).PostAPI(Urls.FEEDBACK, map, new APICallback(context, listener, 1));
    }

    /**
     * 附近的人
     *
     * @param time       在线时间 0--30分钟之内 1--60分钟之内 2--1天之内 3--七天之内
     * @param age        0--18~22 1--23~26 2--27~35 3--35以上  全部传空字符串
     * @param sex        0：不限 1：男 2：女
     * @param provinceId 省份ID
     * @param cityId     城市ID
     * @param pageNum
     * @param pageSize
     * @param listener
     * @param context
     */
    public void serNear(String time, String age, String sex,
                        String provinceId, String cityId, String userid,
                        int pageNum, int pageSize, OnResposeListener listener, Context context) {
        HashMap<String, String> param = new HashMap<>();
        param.put("lon", WJSJApplication.getInstance().geoLng);
        param.put("lat", WJSJApplication.getInstance().geoLat);
        param.put("time", time);
        param.put("age", age);
        param.put("sex", sex);
        param.put("provinceId", provinceId);
        param.put("cityId", cityId);
        param.put("userid", userid);
        param.put("pageNum", String.valueOf(pageNum));
        param.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(UserInfo.class).PostAPI(Urls.SEARCH_NEARBY, param, new APICallback(context, listener, 1));

    }

    /**
     * 附近的人详细信息
     *
     * @param id       查看对象的ID
     * @param listener
     * @param context
     */
    public void srarchInfo(String id, OnResposeListener listener, Context context) {
        HashMap<String, String> params = new HashMap<>();
        params.put("lon", WJSJApplication.getInstance().geoLng);
        params.put("lat", WJSJApplication.getInstance().geoLat);
        params.put("userid", id);
        params.put("touserid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        APIClient.getInstance().getAPIService(UserInfo.class).PostAPI(Urls.SEARCH_INFO, params, new APICallback(context, listener, 1));
    }

    /**
     * 附近的人头像扫描
     *
     * @param listener
     * @param context
     */
    public void srarchPic(OnResposeListener listener, Context context) {
        HashMap<String, String> params = new HashMap<>();
//        params.put("lat", WJSJApplication.getInstance().geoLat + "");
//        params.put("lon", WJSJApplication.getInstance().geoLng + "");
        APIClient.getInstance().getAPIService(nearPic.class).PostAPI(Urls.NEAR_PIC, params, new APICallback(context, listener, 1));
    }

    /**
     * 想约
     *
     * @param sex      登录人性别 0男 1女
     * @param pageNum
     * @param pageSize 城市ID
     * @param listener
     * @param context
     */
    public void findtryst(String sex, int pageNum, int pageSize,
                          OnResposeListener listener, Context context) {
        HashMap<String, String> param = new HashMap<>();
        param.put("lon", WJSJApplication.getInstance().geoLng);
        param.put("lat", WJSJApplication.getInstance().geoLat);
        param.put("userid", WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        if (!"".equals(sex)) {
            param.put("sex", sex);
        }
        param.put("pageNum", String.valueOf(pageNum));
        param.put("pageSize", String.valueOf(pageSize));
        APIClient.getInstance().getAPIService(UserInfo.class).PostAPI(Urls.SEARCH_FINDSRYST, param, new APICallback(context, listener, 11));

    }

    private String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    private String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }
}
