package com.bm.wjsj;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bm.wjsj.Amap.AmapLocationActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.MyMsg.MyReceiveHiddenMessageListener;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.util.LinkedList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by yangk on 2015/7/20.
 */
public class WJSJApplication extends Application implements RongIM.LocationProvider, AMapLocationListener {
    public static WJSJApplication instance;

    /**
     * 应用实例对象
     */
    public static WJSJApplication app;

    public static int personalCenter;// 标记个人中心

    public static int peopleNearby;// 标记附近的人

    private List<Activity> acys = new LinkedList<Activity>();

    private SharedPreferencesHelper sp;

    private RongIM.LocationProvider.LocationCallback mLastLocationCallback;

    private AMapLocationClient mlocationClient;

    private AMapLocationClientOption mLocationOption;

    //geoLat纬度;geoLng经度
    public String geoLat = "0.0", geoLng = "0.0";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        init();
    }

    private void init() {
        // 初始化定位，
        mlocationClient = new AMapLocationClient(getApplicationContext());
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        // 设置定位模式为低功耗定位
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位回调监听
        mlocationClient.setLocationListener(this);
        // 设置定位参数
        mlocationClient.setLocationOption(mLocationOption);

        if ("com.bm.wjsj".equals(getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(getApplicationContext()))) {

            /* IMKit SDK调用第一步 初始化 */
            Log.e("WJSJApplication", "IMKit SDK调用第一步 初始化");
            RongIM.init(this);
            RongIM.setLocationProvider(this);
            //RongIM.setOnReceivePushMessageListener(new MyReceivePushMessageListener());
            RongIM.getInstance().getRongIMClient().setOnReceiveMessageListener(new MyReceiveHiddenMessageListener());
            connectRongCloud();
            /* 必须在使用 RongIM 的进程注册回调、注册自定义消息等 */
//            if ("com.bm.wjsj".equals(getCurProcessName(getApplicationContext()))) {
//
//            }
        }
        startAmap();
//        Fresco.initialize(this);
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this).setProgressiveJpegConfig(new SimpleProgressiveJpegConfig()).build();
        Fresco.initialize(this, config);
        initImageLoader();
//        // 初始化极光推送
//        JPushInterface.init(this);
//        JPushInterface.setDebugMode(true);
    }

    public void connectRongCloud() {
        //19920407
//        String Token = "n8q1VVsGjpvYrsjvIVyK1YZ/ag8zs/H2dxCB1aOrb1H+ZbVrA6/xcyya+++1iKIDWrR43LygolNbSY9YrxpRoWzZzosm7X7H";//test
        //19920509
//        String Token = "HgbAJpx6FjRDkfEPutKEKHhgb4A0bdGdRbBJ4NsLkeiVkAA3gPEsRCRm6NWqb4Ng90Tw/djAa9MqZ0ggkA12sg==";
        /**
         * IMKit SDK调用第二步
         *
         * 建立与服务器的连接
         *
         */
        String Token = getSp().getValue(Constant.SP_TOKEN);
        Log.e("Token", Token);
        if (!getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN) || TextUtils.isEmpty(Token)) {
            return;
        }
        if (RongIMClient.getInstance() != null &&
                RongIMClient.getInstance().getCurrentConnectionStatus().getValue() != 0) {
            RongIM.connect(Token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {
                    Log.e("MainActivity", "——onTokenIncorrect—");
                }

                @Override
                public void onSuccess(String userId) {
                    Log.e("MainActivity", "——onSuccess—" + userId);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("MainActivity", "——onError—" + errorCode);
                }
            });
        }

        RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(
                Constant.USERID, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME),
                Uri.parse(Urls.PHOTO +
                        WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO))
        ));
    }

    public static void messageUserInfo() {
        RongIM.getInstance().setMessageAttachedUserInfo(true);
    }

    private void initImageLoader() {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPriority(Thread.NORM_PRIORITY)
                .threadPoolSize(3)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new WeakMemoryCache())
                .memoryCacheSize(4 * 1024 * 1024)
                .diskCacheSize(10 * 1024 * 1024)// 10 Mb
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .discCacheExtraOptions(480, 320, null)
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public WJSJApplication() {
        instance = this;
    }

    public static synchronized WJSJApplication getInstance() {
        return instance;
    }

    public void addAcitivity(Activity acy) {
        acys.add(acy);
    }

    public void removeActivity(Activity acy) {
        acy.finish();
        acys.remove(acy);
    }

    public void clearAc() {
        for (Activity ac : acys) {
            if (ac != null)
                ac.finish();
        }
    }

    // 完全退出应用
    public void exit() {
        RongIM.getInstance().disconnect();
        NotWantPop();
        for (Activity ac : acys) {
            ac.finish();
        }
        System.exit(0);

    }

    /* 一个获得当前进程的名字的方法 */
    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    /**
     * 初始化定位
     */
    private void initLocation() {
        // 初始化定位，
        mlocationClient = new AMapLocationClient(getApplicationContext());
        // 初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setOnceLocation(true);
        // 设置定位模式为低功耗定位
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        // 设置定位回调监听
        mlocationClient.setLocationListener(this);
        // 设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
    }


    //开始高德地图定位
    private void startAmap() {
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mlocationClient.startLocation();
    }

    private void stopAmap() {
        // 停止定位
        mlocationClient.stopLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            geoLat = String.valueOf(aMapLocation.getLatitude());//纬度
            geoLng = String.valueOf(aMapLocation.getLongitude());//经度
            Log.e("application", "geoLat纬度 = " + geoLat + "  geoLng经度= " + geoLng);
            stopAmap();
        }
    }

    public SharedPreferencesHelper getSp() {
        if (sp == null)
            sp = new SharedPreferencesHelper(this, Constant.SP_FILENAME);
        return sp;
    }

    private void NotWantPop() {
        getSp();
        sp.putBooleanValue(Constant.ISWANTPOP, false);
    }

    public RongIM.LocationProvider.LocationCallback getLastLocationCallback() {
        return mLastLocationCallback;
    }

    public void setLastLocationCallback(RongIM.LocationProvider.LocationCallback lastLocationCallback) {
        this.mLastLocationCallback = lastLocationCallback;
    }


    @Override
    public void onStartLocation(Context context, LocationCallback locationCallback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        WJSJApplication.getInstance().setLastLocationCallback(locationCallback);
        Intent intent = new Intent(context, AmapLocationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
