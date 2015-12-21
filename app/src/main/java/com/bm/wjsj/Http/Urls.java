package com.bm.wjsj.Http;

/**
 * Created by yangk on 2015/8/24.
 */
public class Urls {

    /* 修改个人资料地址 */
    public final static String HOSTS = "http://10.58.168.91:8099/wjsj-server-web/app/";

    /* 服务器地址 */
//    内网IP
//   public final static String HOST = "http://10.58.174.150:8080/wj/service";
    //外网IP
//   public final static String HOST = "http://139.196.9.230/wj/service";
    //外网测试IP
    public final static String HOST = "http://139.196.9.230:8080/wj/service";

    /* 内网图片地址 */
//    public final static String PHOTO = "http://10.58.174.150:8080";
    /* 外网图片地址 */
//    public final static String PHOTO = "http://139.196.9.230";
    /* 外网测试图片地址 */
    public final static String PHOTO = "http://139.196.9.230:8080";

    /*附近的人*/
    public final static String SEARCH_NEARBY = "user/Ifindnearby";

    /*想约*/
    public final static String SEARCH_FINDSRYST = "user/Ifindtryst";

    /*附近的人详细信息*/
    public final static String SEARCH_INFO = "user/findnearbyinfo";

    /*上线*/
    public final static String ONLINE = "user/online";

    /*反馈意见*/
    public final static String ADDFEEDBACK = "feedback/addfeedback";

    /*注册*/
    public final static String REGISTER = "user/register";

    /*帮助中心*/
    public final static String HELPER = "helper/findhelper";

    /*完善信息*/
    public final static String COMPLETEINFO = "user/completeInfo";

    /*登录*/
    public final static String LOGIN = "user/login";

    /*搜索附近的人*/
    public final static String NEAR_PIC = "community/nearPic";

    /*发送验证码*/
    public final static String SENDCODE = "comment/sendCode";

    /*忘记密码*/
    public final static String FINDPWD = "user/findPassword";

    /*富文本*/
    public final static String WEBTEXT = "globle/help";

    /*消息列表*/
    public final static String MSGLIST = "message/findmessage";

    /*未读消息*/
    public final static String UNREADMSGNUM = "message/findmessagenum";

    /*活动消息详细*/
    public final static String FINDACTIVIY = "message/findactivity";

    /*删除消息*/
    public final static String MDELETE = "message/delete";

    /*举报*/
    public final static String REPORT = "report/addreport";

    /*圈子banner图*/
    public final static String CIRCLEBANNER = "advertisement/getadvlist";

    /*banner Webview*/
    public final static String BANNERWEBVIEW = "advertisement/getadvinfo";

    /*圈子首页(我的圈子)*/
    public final static String CIRCLEMY = "circle/mycircle";

    /*圈子首页(更多圈子)*/
    public final static String CIRCLEMORE = "circle/morecircle";

    /*圈子详情-首页帖子列表*/
    public final static String CIRCLEPOST = "post/getpostlist";

    /*圈子详情*/
    public final static String CIRCLEDETAIL = "community/detail";

    /*加入圈子*/
    public final static String ADDCIRCLE = "circle/addcircle";

    /*退出圈子*/
    public final static String EXITCIRCLE = "circle/exitcircle";

    /*所有动态列表*/
    public final static String DYNAMICLIST = "dynamic/getalldynamic";

    /*发布动态*/
    public final static String DYNAMICPUBLISH = "dynamic/adddynamic";

    /*动态详情*/
    public final static String DYNAMICDETAIL = "dynamic/dynamicinfo";

    /*我的动态列表*/
    public final static String GETMYDYNAMIC = "dynamic/getmydynamic";

    /*评论动态*/
    public final static String DYNAMICCOMMENT = "dynamic/adddynamiccomment";

    /*点赞*/
    public final static String ADDPRAISE = "praise/addpraise";

    /*取消点赞*/
    public final static String DELETEPRAISE = "praise/deletepraise";

    /*删除动态*/
    public final static String DYNAMICDEL = "dynamic/deletedynamic";

    /*删除动态评论*/
    public final static String DYNAMICCMTDEL = "dynamic/deletedynamiccomment";

    /*发布帖子*/
    public final static String POSTPUBLISH = "post/addpost";

    /*我的帖子列表*/
    public final static String MYPOSTLIST = "post/mypostlist";

    /*帖子列表 (所有)*/
    public final static String ALLPOSTLIST = "post/getpostlist";

    /*帖子详情*/
    public final static String POSTDETAIL = "post/postlistinfo";

    /*帖子评论列表*/
    public final static String REVIEWPOSTLIST = "criticism/getcommentlist";

    /*帖子评论详情*/
    public final static String REVIEWDETAIL = "criticism/getcommentinfo";

    /*发表帖子评论(主评论/子评论)*/
    public final static String REVIEWPOST = "criticism/addcomment";

    /*删除我的帖子*/
    public final static String DELETEPOST = "post/deletepost";

    /*删除我的帖子下面的评论*/
    public final static String DELETEPOSTCMT = "criticism/deletemycomment";

    /*帖子评论详情页面删除子评论*/
    public final static String IDELETEPOSTMYCOM = "post/Ideletepostmycom";

    /*商城首页*/
    public final static String STORE = "mall/index";

    /*商城分类*/
    public final static String STORETYPE = "producttype/findtype";

    /*商品列表*/
    public final static String STORELIST = "product/findproduct";

    /*商品详情*/
    public final static String STOREDETAIL = "product/findproductinfo";

    /*商品规格*/
    public final static String STORESPEC = "producttype/findspec";

    /*不同规格的价钱*/
    public final static String FINDDETAIL = "productdetail/findDetail";

    /*邮费*/
    public final static String FINDFREIGHT = "product/findfreight";

    /*商品评价列表（此数据只有用户评价及后台平台回复）*/
    public final static String STOREREVIEW = "product/findprocomment";

    /*评价商品*/
    public final static String REVIEWPRODUCT = "product/addprocomment";

    /*确认订单*/
    public final static String ORDERCONFIRM = "order/confirmorder";

    /*修改订单*/
    public final static String UPDATEORDER = "order/update";

    /*修改订单状态*/
    public final static String UPDATEORDERSTATUS = "order/updateorder";

    /*修改订单编号*/
    public final static String UPDATEORDERNUM = "order/findordernum";

    /*删除订单*/
    public final static String DELETEORDER = "order/delete";

    /*等级优惠*/
    public final static String FINDPAYTYPE = "paytype/findpaytype";

    /*加入购物车*/
    public final static String ADDSHOPCAR = "shopcar/addshopcar";

    /*关注列表*/
    public final static String GETATTENTIONLIST = "attention/getattentionlist";

    /*关注数*/
    public final static String GETATTENTIONNUM = "attention/getattentioninfo";

    /*关注的人*/
    public final static String ATTENTION = "user/friend";

    /*关注别人*/
    public final static String ATTENTIONPERSON = "attention/addattention";

    /*取消关注*/
    public final static String DELETEATTENTION = "attention/deleteattention";

    /*是否互相关注*/
    public final static String ISMUTUAL = "attention/ismutual";

    /*查看他人信息*/
    public final static String GETPERSONINFO = "user/person/detail";

    /*查询我的积分*/
    public final static String MYSCORE = "score/myscore";

    /*兑换商品列表*/
    public final static String SCOREPROLIST = "score/scoreprolist";

    /*兑换记录*/
    public final static String MYSCOREPROORDER = "score/myscoreproorder";

    /*积分获取记录*/
    public final static String MYSCOREINFO = "score/myscoreinfo";

    /*奖品详情*/
    public final static String SCOREPROINFO = "score/scoreproinfo";

    /*确定兑换*/
    public final static String ADDSCOREPROORDER = "score/addscoreproorder";

    /*收货地址列表*/
    public final static String ADDRLIST = "address/myaddress";

    /*选择默认收货地址*/
    public final static String ADDRDEFAULT = "address/setaddress";

    /*新增收货地址*/
    public final static String ADDREDIT = "address/addaddress";

    /*修改收货地址*/
    public final static String ADDRESS = "address/updateaddress";

    /*删除 收货地址*/
    public final static String DELETEADDR = "address/deleteaddress";

    /*我的订单（包含待付款、待收货、所有）*/
    public final static String MYORDERLIST = "order/findbyuserid";

    /*订单详情*/
    public final static String ORDERDETAIL = "user/order/detail";

    /*我的收藏*/
    public final static String MYCOLLECT = "collect/findbyuserid";

    /*收藏商品*/
    public final static String ADDCOLLECT = "collect/addcollect";

    /*删除收藏*/
    public final static String DELETECOLLECT = "collect/delete";

    /*确认收货*/
    public final static String ORDERSURE = "user/order/sure";

    /*购物车*/
    public final static String SHOPCARLIST = "shopcar/findbyuserid";

    /*删除购物车*/
    public final static String DELETESHOPCAR = "shopcar/delete";

    /*修改个人资料*/
    public final static String USERINFOUPDATE = "user/updateInfo";

    /*反馈意见*/
    public final static String FEEDBACK = "user/feedback";

    /*获取推送设置信息*/
    public final static String GETSETINFO = "push/getsetinfo";

    /*设置我的推送配置*/
    public final static String SETMYPUSH = "push/setmypush";

}
