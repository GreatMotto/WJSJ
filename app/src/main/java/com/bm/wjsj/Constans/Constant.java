package com.bm.wjsj.Constans;

import com.bm.wjsj.R;

public class Constant {

    public final static String ZZWEB = "wjsj";

    public static final String TAG = "tag";

    public final static String CACHE_JSON_DATA_PATH = "/wjsj/json/";// 缓存json格式数据的路径
    public final static String CACHE_IMAGE_PATH = "/wjsj/cache";// 缓存图片的路径
    public final static String CRASH_ERROR_FILE_PATH = "/wjsj/crash/";// 保存报错信息文件的路径
    public final static String CRASH_PIC_PATH = "/wjsj/pic/";// 保存上传图片的路径

    // 是否首次运行SHAREDPREFERENCES
    public static final String SP_KEY_GUIDE = "guide";
    public final static String UPLOAD_PICTURE_PATH = "/wjsj/upload/";// 保存着上传图片的路径

    //附近的人筛选记录保存
    public static final String SP_FJ_TIME = "SP_FJ_TIME";// 时间
    public static final String SP_FJ_AGE = "SP_FJ_AGE";// 年龄
    public static final String SP_FJ_SEX = "SP_FJ_SEX";// 性别
    public static final String SP_FJ_PROVINCEID = "SP_FJ_PROVINCEID";// 用户省份id
    public static final String SP_FJ_CITYID = "SP_FJ_CITYID";// 用户城市id
    public static final String SP_FJ_B = "SP_FJ_B";//是否筛选

    // 上次登录用户名SHAREDPREFERENCES
    public static final String SP_FILENAME = "sp_file";// sp文件名
    public static final String SP_KEY_USER = "USER";// 帐号
    public static final String SP_KEY_PWD = "pwd";// 密码
//    public static final String SP_CONFIG = "pwd";// 密码
    public static final String SP_KEY_ISLOGIN = "isLogin";// 是否登录
    public static final String SP_USERREMIND = "msgReminder";// 是否消息提醒
    public static final String SP_USERID = "userId";// 用户ID
    public static final String SP_BIRTHDAY = "birthday";// 用户生日
    public static final String SP_AGE = "age";// 用户年龄
    public static final String SP_CONSTELLATION = "constellation";// 用户星座
    public static final String SP_INTEGRAL = "integral";// 用户积分
    public static final String SP_PHOTO = "photo";// 用户头像
    public static final String SP_PHOTO_LOCAL = "SP_PHOTO_LOCAL";// 用户头像
    public static final String SP_SIGNATURE = "signature";// 用户签名
    public static final String SP_SIGNIN = "signin";// 用户签到天数
    public static final String SP_USERNAME = "user_name";// 用户姓名
    public static final String SP_SEX = "sex";// 用户性别
    public static final String SP_LEVEL = "level";// 用户等级
    public static final String SP_PROVINCEID = "provinceId";// 用户省份id
    public static final String SP_CITYID = "cityId";// 用户城市id
    public static final String SP_TOKEN = "token";// 用户融云token
    public static final String SP_MTLATTENTION = "mtlattention";
    public static final String SP_MYATTENTION = "myattention";
    public static final String SP_ATTENTIONME = "attentionme";
    public static final String SP_PAY_PAGE = "SP_PAY_PAGE";//商城调用支付接口后返回的页面
    public static final String SP_PAY_PRODUCTID = "SP_PAY_PRODUCTID";//商城调用支付接口后返回的页面参数
    // 手机号码输入规则
    public static final String TELREGEX = "^(1([34578][0-9]))\\d{8}$";

    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String BOOLEAN = "boolean";
    public static final String ID = "id";
    public static final String POSTID = "postid";
    public static final String USERID = "userid";
    public static final String LIST = "list";

    public static final String DYNAMICID="DYNAMICID";
    public static final String DATAUSERID="DATAUSERID";

    public static final String CONTENT = "content";
    public static final String CREATETIME = "createTime";

    public static final String NOMORE = "没有更多了";

    public final static String[] constellationArr = {"水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座",
            "处女座", "天秤座", "天蝎座", "射手座", "魔羯座"};
    public final static int[] constellationDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};
    //正方形图片
    public static final String[] imageUrls2 = {"http://f.hiphotos.baidu.com/image/h%3D360/sign=8d04dca1013b5bb5a1d726f806d3d523/a6efce1b9d16fdfa0a611be4b68f8c5494ee7baf.jpg",
            "http://d.hiphotos.baidu.com/image/h%3D360/sign=69856497a60f4bfb93d09852334e788f/10dfa9ec8a1363278957c93b938fa0ec08fac704.jpg",
            "http://img2.imgtn.bdimg.com/it/u=864293005,1366582771&fm=23&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1296437029,2192518954&fm=23&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1352344554,1553026899&fm=23&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=2512883029,489796336&fm=23&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1204916681,2600537094&fm=23&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2718537821,1027049906&fm=23&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1641248542,3076031739&fm=23&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3752371578,1960096672&fm=23&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3109072621,3566748263&fm=23&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3704778188,3261777697&fm=23&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3889451826,3273809739&fm=23&gp=0.jpg"};

    //长方形图片
    public static final String[] imageUrls = {"http://b.hiphotos.baidu.com/image/h%3D300/sign=be5a59d2bd99a90124355d362d940a58/2934349b033b5bb56880a53833d3d539b700bca5.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D360/sign=e8adaa6e4310b912a0c1f0f8f3fffcb5/42a98226cffc1e17af3e61e44e90f603728de966.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D360/sign=aaf15ff103082838770dda128898a964/d62a6059252dd42aa5baf545073b5bb5c8eab8a9.jpg",
            "http://e.hiphotos.baidu.com/image/h%3D360/sign=4f37731fc880653864eaa215a7dda115/8cb1cb13495409230170952f9058d109b3de4942.jpg",
            "http://g.hiphotos.baidu.com/image/h%3D360/sign=a2656ed5f9edab646b724bc6c737af81/8b13632762d0f70397e585c20afa513d2697c55c.jpg",
            "http://a.hiphotos.baidu.com/image/h%3D360/sign=85712f2a71f08202329297397bfbfb8a/63d9f2d3572c11df81d6742a612762d0f703c2b0.jpg",
            "http://b.hiphotos.baidu.com/image/h%3D360/sign=537dd9164f90f6031bb09a410913b370/472309f790529822abf16ce4d2ca7bcb0b46d4d3.jpg",
            "http://b.hiphotos.baidu.com/image/h%3D360/sign=b213983066d9f2d33f1122e999ee8a53/3bf33a87e950352a0fbb82d95743fbf2b3118b0d.jpg",
            "http://b.hiphotos.baidu.com/image/h%3D360/sign=6b46d78568224f4a4899751539f69044/b3b7d0a20cf431ad1252e29a4e36acaf2fdd9895.jpg",
            "http://b.hiphotos.baidu.com/image/h%3D360/sign=19e72ad5d02a28345ca6300d6bb7c92e/e61190ef76c6a7efb7a592eef9faaf51f2de6672.jpg",
            "http://e.hiphotos.baidu.com/image/h%3D360/sign=c960816522a4462361caa364a8227246/0df431adcbef7609ef7462802cdda3cc7cd99eb6.jpg",
            "http://h.hiphotos.baidu.com/image/h%3D360/sign=b23e2cf453da81cb51e685cb6266d0a4/4bed2e738bd4b31c985150bb85d6277f9e2ff845.jpg"};

    //连体裤图片
    public static final String[] imageUrlsKu = {"http://img2.imgtn.bdimg.com/it/u=2161029966,3189345582&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=1126939196,2323285719&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=874245245,2937071797&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=915590380,3778321535&fm=21&gp=0.jpg",
            "http://img5.imgtn.bdimg.com/it/u=3685119134,3205004359&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3206880931,2778469889&fm=21&gp=0.jpg",
            "http://img2.imgtn.bdimg.com/it/u=3663649208,3566356402&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3019300444,795785576&fm=21&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=1601223735,2364650106&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=188118784,3284283170&fm=21&gp=0.jpg",
            "http://img1.imgtn.bdimg.com/it/u=2189475024,2510867921&fm=21&gp=0.jpg"};

    public static final String[] classfiyNames = {"套套世界", "情趣内衣", "情趣用品", "男性玩具", "女性玩具", "调情助兴"};
    public static final int[] classfiyIcon = {R.mipmap.taotao, R.mipmap.neiyi, R.mipmap.qingqu, R.mipmap.nanxing, R.mipmap.nvxing, R.mipmap.tiaoqing};
    public static final int[] classfiybg = {R.mipmap.taotao_box, R.mipmap.neiyi_box, R.mipmap.qingqu_box, R.mipmap.nanxing_box, R.mipmap.nvxing_box, R.mipmap.tiaoqing_box};
    public static final int[] classfiytextfont = {R.color.taotao_font, R.color.neiyi_font, R.color.qingqu_font, R.color.nanxing_font, R.color.nvxing_font, R.color.tiaoqing_font};
    public static final String ISWANTPOP = "ISWANTPOP";
    public final static String WEB_NAME = "web_name";
    public final static String SCORE = "ISSCORE";
    public final static String SPEC = "SPEC";
    public final static String STATUS = "STATUS";//是否禁用
    public final static String UNREADMESSAGE="UNREADMESSAGE";//融云未读消息
    public final static String UNJREADMESSAGE="UNJREADMESSAGE";//极光未读消息
    public final static String ISPUSHTALK="ISPUSHTALK";//聊天消息推送


    //appid
    //请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
    //public static final String APP_ID = "wxf2f565574a968187";
    //商户号
    //public static final String MCH_ID = "1233848001";
    //  API密钥，在商户平台设置
    //public static final  String API_KEY="412fde4e9c2e2bb619514ecea142e449";


    public static final String APP_ID = "wxdbf59d9875952db0";
    public static final String APP_SECRET="c27427de23177d9305d02cb743678271";
    public static final String MCH_ID="1278232901";
    public static final String API_KEY="deuthptxgtdeuthptxgtdeuthptxgt88";
    //public static final String SP_URL="http://139.196.9.230:8080/wj/service/alipay/wechatCallBack";

}
