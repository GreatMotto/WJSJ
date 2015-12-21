package com.bm.wjsj;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Attention.AttentionFragment;
import com.bm.wjsj.Base.LoginActivity;
import com.bm.wjsj.Base.RegisterActivity;
import com.bm.wjsj.Base.SplashActivity;
import com.bm.wjsj.Bean.AttentionListBean;
import com.bm.wjsj.Circle.CircleFragment;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.DateFragment;
import com.bm.wjsj.Date.ScanActivity;
import com.bm.wjsj.Dynamic.DynamicFragment;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MyMsg.ConversationListDynamicFragment;
import com.bm.wjsj.MyMsg.MyMsgFragment;
import com.bm.wjsj.Nearby.NearbyFragment;
import com.bm.wjsj.Personal.PersonalFragment;
import com.bm.wjsj.SpiceStore.SpiceStoreFragment;
import com.bm.wjsj.Utils.AppShortCutUtil;
import com.bm.wjsj.Utils.BadgeView;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.SlidingMenu.SlidingFragmentActivity;
import com.bm.wjsj.View.SlidingMenu.SlidingMenu;
import com.bm.wjsj.View.SlidingMenu.SlidingMenu.CanvasTransformer;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


public class MainActivity extends SlidingFragmentActivity implements APICallback.OnResposeListener {

    private CanvasTransformer mTransformer;
    private RadioGroup radioGroup;
    public SlidingMenu sm;
    private TextView tv_login, tv_register, tv_name, tv_level;
    private LinearLayout ll_no_login, ll_login_ok;
    public static SimpleDraweeView sdv_pic;
    private AttentionFragment attentionFragment;
    private CircleFragment circleFragment;
    private ConversationListDynamicFragment conversationListDynamicFragment;
    private DateFragment dateFragment;
    private DynamicFragment dynamicFragment;
    private MyMsgFragment myMsgFragment;
    private NearbyFragment nearbyFragment;
    private PersonalFragment personalFragment;
    private SpiceStoreFragment spiceStoreFragment;
    private ScanActivity scanActivity;
    private long myExitTime;
    public static RadioButton rb_my_msg,rb_attention, rb_spice_store, rb_my, rb_wantdate, rb_nearby_people;
    private boolean isScan, istag, isend, isMsg;
    public boolean isToggle;
    private boolean canScan;
    private int tag;
    private boolean isJump = false;
    private String TAG = "MainActivity";
    public static ImageView btnFlag;
    public static BadgeView badge;
    public static Boolean isFirst = false;
    private SharedPreferencesHelper sp;

    private JUnReadReceiver receiver = null;

    //public List<ImageBean> imagelist = new ArrayList<>();

    /**
     * 目标 Id
     */
    private String path,mTitle;
    private UserInfo user = null;

    //用户信息
    public static List<AttentionListBean> list = new ArrayList<>();

    public FragmentTransaction getFragmentTransaction() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        return fragmentTransaction;
    }

    private FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "--------onCreate-------");

        //获得banner图
        //WebServiceAPI.getInstance().circleBanner(MainActivity.this, this);

        setContentView(R.layout.activity_main);
        Log.e("Registration ID", JPushInterface.getRegistrationID(this));
//        tag = getIntent().getIntExtra("isShop", 0);
//        Log.e("tag","==========" + tag + "");
//        switch (tag){
//            case 0:
        getFragmentTransaction().replace(R.id.content_frame, getScanFragment()).commit();
//                break;
//            case 1:
//                Log.e("onCreate--------",tag+"");
//                rb_spice_store.setChecked(true);
//                getFragmentTransaction().replace(R.id.content_frame, getSpiceStoreFragment()).commit();
//                break;
//            case 2:
//                Log.e("onCreate--------",tag+"");
//                getFragmentTransaction().replace(R.id.content_frame, getPersonalFragment()).commit();
//                rb_my.setChecked(true);
//                break;
//            case 3:
//                Log.e("onCreate--------",tag+"");
//            getFragmentTransaction().replace(R.id.content_frame, getNearbyFragment()).commit();
//            rb_nearby_people.setChecked(true);
//                break;
//        }
        initSlidingMenu();

        initView();
//        connectRongCloud();
//        HashMap<String, String> map = new HashMap<String, String>();
//        map.put("userId", "43");
//        map.put("type", "0");
//        map.put("pageNum", "1");
//        map.put("pageSize", "20");
//        APIClient.getInstance().getAPIService(SizeBean.class).GetAPI("YYZJ/api/news/list", map, new APICallback(this,this, 1));
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            //上线
            WebServiceAPI.getInstance().online(JPushInterface.getRegistrationID(this), MainActivity.this, MainActivity.this);
            WebServiceAPI.getInstance().getattentionlist("0", 1, 10, MainActivity.this, this);
        }

        sp= WJSJApplication.getInstance().getSp();

        badge= new BadgeView(this, btnFlag);
        badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);

        /*动态方式注册广播接收者*/
        receiver = new JUnReadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.jpush.android.unread");
        this.registerReceiver(receiver, filter);
    }

    private void initSlidingMenu() {
        sm = getSlidingMenu();
        setSlidingActionBarEnabled(true);
        sm.setBehindScrollScale(0.0f);
        sm.setBehindCanvasTransformer(getmTransformer());
        /*菜单*/
        setBehindContentView(R.layout.menu_frame);
        sm.setShadowWidthRes(R.dimen.margin);//阴影宽度
        sm.setShadowDrawable(R.drawable.shadow);//阴影样式
        sm.setBackgroundColor(getResources().getColor(R.color.bg_menu));
        sm.setBehindOffset(DisplayUtil.getWidth(this) / 2);//左侧菜单展开后距右边距的距离，即展开后的内容宽度
        sm.setFadeDegree(0.5f);
//        sm.setSlidsds
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    private void initView() {
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_level = (TextView) findViewById(R.id.tv_level);
        tv_register = (TextView) findViewById(R.id.tv_register);
        rb_my_msg = (RadioButton) findViewById(R.id.rb_my_msg);
        btnFlag = (ImageView) findViewById(R.id.btnFlag);
        btnFlag.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        rb_attention = (RadioButton) findViewById(R.id.rb_attention);
        rb_spice_store = (RadioButton) findViewById(R.id.rb_spice_store);
        rb_nearby_people = (RadioButton) findViewById(R.id.rb_nearby_people);
        rb_my = (RadioButton) findViewById(R.id.rb_my);
        rb_wantdate = (RadioButton) findViewById(R.id.rb_wantdate);
        ll_no_login = (LinearLayout) findViewById(R.id.ll_no_login);
        ll_login_ok = (LinearLayout) findViewById(R.id.ll_login_ok);
        sdv_pic = (SimpleDraweeView) findViewById(R.id.sdv_pic);
        sdv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.putExtra(Constant.BOOLEAN, true);
                    startActivityForResult(intent, 0);
                } else {
                    sm.toggle();
                    rb_my.setChecked(true);
                    sm.toggle();
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 点击事件获取的选择对象
                switch (checkedId) {
                    case R.id.rb_attention:
                        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.putExtra(Constant.BOOLEAN, true);
                            startActivityForResult(intent, 5);
                        } else {
                            sm.toggle();
                            getFragmentTransaction().replace(R.id.content_frame, new AttentionFragment()).commit();
                        }
                        break;
                    case R.id.rb_circle:
                        sm.toggle();
                        getFragmentTransaction().replace(R.id.content_frame, getCircleFragment()).commit();
                        break;
                    case R.id.rb_dynamic:
                        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.putExtra(Constant.BOOLEAN, true);
                            startActivityForResult(intent, 6);
                        } else {
                            sm.toggle();
                            getFragmentTransaction().replace(R.id.content_frame, new DynamicFragment()).commit();
                        }
                        break;
                    case R.id.rb_my:
                        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.putExtra(Constant.BOOLEAN, true);
                            startActivityForResult(intent, 3);
                        } else {
                            sm.toggle();
                            getFragmentTransaction().replace(R.id.content_frame, getPersonalFragment()).commit();
                        }
                        break;
                    case R.id.rb_my_msg:
                        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.putExtra(Constant.BOOLEAN, true);
                            startActivityForResult(intent, 4);
                        } else {
                            sm.toggle();
                            WebServiceAPI.getInstance().getattentionlist("0", 1, 10, MainActivity.this, MainActivity.this);
//                            Intent intent = new Intent(MainActivity.this, ImMsgActivity.class);
//                            intent.putExtra(Constant.BOOLEAN, true);
//                            startActivityForResult(intent, 4);
                            getFragmentTransaction().replace(R.id.content_frame, getConversationListDynamicFragment()).commit();
                        }
                        break;
                    case R.id.rb_nearby_people:
                        sm.toggle();
                        getFragmentTransaction().replace(R.id.content_frame, getNearbyFragment()).commit();
                        break;
                    case R.id.rb_spice_store:
                        sm.toggle();
//                        WebServiceAPI.getInstance().store(MainActivity.this, MainActivity.this);
                        getFragmentTransaction().replace(R.id.content_frame, getSpiceStoreFragment()).commit();
                        break;
                    case R.id.rb_wantdate:
                        if (!canScan) {
//                            goToScan();
                            sm.toggle();
                            getFragmentTransaction().replace(R.id.content_frame, getScanFragment()).commit();
                        } else {
                            canScan = false;
                        }

                        break;
                }
            }

        });
    }


    public void goToFragment() {
        sm.toggle();
        rb_attention.setChecked(true);
        sm.toggle();
//        getFragmentTransaction().replace(R.id.content_frame,fm).commit();
        sm.showContent();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(receiver!=null)
            this.unregisterReceiver(receiver);
    }

    public void goToScan() {
//        if (isScan) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "--------onResume-------");
        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
                new RongIM.OnReceiveUnreadCountChangedListener() {
                    @Override
                    public void onMessageIncreased(int i) {
                        try {

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        } finally {
                            Intent intent = new Intent();
                            intent.setAction("com.msg.notify");
                            intent.putExtra("msgTemp12", i);
                            WJSJApplication.getInstance().sendBroadcast(intent);
                        }
                    }
                }, Conversation.ConversationType.PRIVATE
        );


        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userid) {
                for (int i = 0; i < MainActivity.list.size(); i++) {
                    if (userid.equals(MainActivity.list.get(i).id)) {
                        path = MainActivity.list.get(i).head;
                        mTitle = MainActivity.list.get(i).nickname;

                        user = new UserInfo(
                                userid,
                                mTitle,
                                Uri.parse(Urls.PHOTO + path));
                        RongIM.getInstance().refreshUserInfoCache(user);//刷新消息列表用户信息
                        return user;
                    }
                }

                return null;
            }
        }, false);
        RongIM.getInstance().setMessageAttachedUserInfo(false);


        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            ll_login_ok.setVisibility(View.VISIBLE);
            ll_no_login.setVisibility(View.GONE);
            Log.e("photo===========", WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO));
            sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)));
            if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO).equals("")) {
                if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0")) {
                    sdv_pic.setImageResource(R.mipmap.touxiangnan);
                } else {
                    sdv_pic.setImageResource(R.mipmap.touxiangnv);
                }
            } else {
                sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)));
            }
            tv_name.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME));
            tv_level.setText("V" + WJSJApplication.getInstance().getSp().getValue(Constant.SP_LEVEL));
        } else {
            sdv_pic.setImageResource(R.mipmap.default_face);
            ll_login_ok.setVisibility(View.GONE);
            ll_no_login.setVisibility(View.VISIBLE);
        }
        sm.showContent();

    }

    public static class MessageChangeReceiver extends BroadcastReceiver {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void onReceive(final Context context, Intent intent) {
            // TODO Auto-generated method stub
            if (intent.getAction().equals("com.msg.notify")) {
                ConversationListDynamicFragment.msgTemp = intent.getIntExtra("msgTemp12", 77);

                try{
                    //NewToast.show(context,ConversationListDynamicFragment.msgTemp + "",Toast.LENGTH_LONG);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

                int Te = Math.abs(ConversationListDynamicFragment.msgTemp);
                SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                sp1.putValue(Constant.UNREADMESSAGE, ""+Te);

                //Log.e("rongUnRead:","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+Te);
                setBadgeText();

//                if (Te == 0 ){
//                    if (badge.isShown()) {
//                        badge.toggle();
//                    } else {
//                    }
//                }else{
//                    badge.setText("" + Te);
//                    if (badge.isShown()) {
//                    } else {
//                        badge.show();
//                    }
//                    isFirst = true;
//                }


            }
        }
    }


    public static   void setBadgeText()
    {
        int allUnRead=0,rongUnReadI =0,jUnReadI=0;
        SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
        String rongUnRead = sp1.getValue(Constant.UNREADMESSAGE);
        String jUnRead = sp1.getValue(Constant.UNJREADMESSAGE);
        //Log.e("rongUnRead:", "$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$setBadgeText(1)");
        try {
            if(!TextUtils.isEmpty(rongUnRead)) {
                rongUnReadI=  Integer.parseInt(rongUnRead);
            }
            if(!TextUtils.isEmpty(jUnRead)) {
                 jUnReadI = Integer.parseInt(jUnRead);
            }
            allUnRead=rongUnReadI+jUnReadI;
            //Log.e("rongUnRead:","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$setBadgeText(2)");
        }catch (Exception ex){}
        badge.setText("" + allUnRead);
        if (allUnRead == 0 ){
            if (badge.isShown()) {
                //Log.e("rongUnRead:","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$setBadgeText(0show)");
                badge.toggle();
            } else {
                //Log.e("rongUnRead:","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$setBadgeText(0toggle)");
            }
        }else {
            if (badge.isShown()) {
                //Log.e("rongUnRead:","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$setBadgeText(1show)");
            } else {
                //Log.e("rongUnRead:","$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$setBadgeText(1toggle)");
                badge.show();
            }
        }

    }



    public  class JUnReadReceiver extends BroadcastReceiver {
        private static final String TAG = "JPush";
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            //Log.e("unRead:", "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

            WebServiceAPI.getInstance().findUnReadmessageNum(MainActivity.this, MainActivity.this);

            //AppShortCutUtil.addNumShortCut(WJSJApplication.getInstance(), SplashActivity.class, true, "11", false);

        }


    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isScan = intent.getBooleanExtra("isScan", false);
        canScan = intent.getBooleanExtra("fromLogin", false);
        istag = intent.getBooleanExtra("istag", false);
        isend = intent.getBooleanExtra("isend", false);
        isMsg = intent.getBooleanExtra("isMsg", false);
        boolean noTog = intent.getBooleanExtra("noTog", false);
        if (istag) {//个人中心
            if (noTog) {
                sm.showMenu(false);
                rb_my.setChecked(true);
                sm.showContent();
            } else {
                boolean isFlag = sm.isSlidingEnabled();
                sm.toggle();//改变radioButton事件之前必须加
                rb_my.setChecked(true);
                sm.toggle();//改变radioButton事件之后必须加，隐藏侧边栏
            }

        }
        if (isend) {//附近的人
            sm.toggle();//改变radioButton事件之前必须加
//            getFragmentTransaction().replace(R.id.content_frame, getNearbyFragment()).commit();
            rb_nearby_people.setChecked(true);
            sm.toggle();//改变radioButton事件之后必须加，隐藏侧边栏
//            sm.showContent();
        }
        if (isScan) {//想约
            getFragmentTransaction().replace(R.id.content_frame, getDateFragment()).commit();
            rb_wantdate.setChecked(true);
        }
        if (isMsg){//消息
            sm.showMenu(false);
            rb_my_msg.setChecked(true);
            sm.showContent();
        }


    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_login:
                gotoOtherActivity(LoginActivity.class);
//                sm.toggle();
                break;
            case R.id.tv_register:
//                sm.toggle();
                gotoOtherActivity(RegisterActivity.class);
                break;
            default:
                break;
        }
    }

    private CanvasTransformer getmTransformer() {
        if (mTransformer == null) {
            mTransformer = new CanvasTransformer() {
                @Override
                public void transformCanvas(Canvas canvas, float percentOpen) {
                    float scale = (float) (percentOpen * 0.25 + 0.75);
                    canvas.scale(scale, scale, canvas.getWidth() / 2, canvas.getHeight() / 2);
                }
            };
        }
        return mTransformer;
    }

    public AttentionFragment getAttentionFragment() {
        if (attentionFragment == null)
            attentionFragment = new AttentionFragment();
        return attentionFragment;
    }

    public CircleFragment getCircleFragment() {
        if (circleFragment == null)
            circleFragment = new CircleFragment();
        return circleFragment;
    }

    public ConversationListDynamicFragment getConversationListDynamicFragment() {
        if (conversationListDynamicFragment == null) {
            conversationListDynamicFragment = new ConversationListDynamicFragment();
        }
        return conversationListDynamicFragment;
    }

    public DateFragment getDateFragment() {
        if (dateFragment == null)
            dateFragment = new DateFragment();
        return dateFragment;
    }

    public DynamicFragment getDynamicFragment() {
        if (dynamicFragment == null)
            dynamicFragment = new DynamicFragment();
        return dynamicFragment;
    }

    public MyMsgFragment getMyMsgFragment() {
        if (myMsgFragment == null)
            myMsgFragment = new MyMsgFragment();
        return myMsgFragment;
    }

    public NearbyFragment getNearbyFragment() {
        if (nearbyFragment == null)
            nearbyFragment = new NearbyFragment();
        return nearbyFragment;
    }

    public PersonalFragment getPersonalFragment() {
        if (personalFragment == null)
            personalFragment = new PersonalFragment();
        return personalFragment;
    }

    public SpiceStoreFragment getSpiceStoreFragment() {
        if (spiceStoreFragment == null)
            spiceStoreFragment = new SpiceStoreFragment();
        return spiceStoreFragment;
    }

    public ScanActivity getScanFragment() {
        if (scanActivity == null)
            scanActivity = new ScanActivity();
        return scanActivity;
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - myExitTime > 2000) {
            NewToast.show(MainActivity.this, "再按一次退出！", Toast.LENGTH_SHORT);
            myExitTime = System.currentTimeMillis();
        } else {
            MainActivity.this.finish();
            System.exit(0);
//            WJSJApplication.getInstance().exit();
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
//            NewToast.show(MainActivity.this, "商场数据初始化成功！", Toast.LENGTH_SHORT);
//            Log.e("Loginac", apiResponse.data.storeBean.bannaerList.size()+""+"++++++++++++++++++++");

            switch (tag) {
                case 1:
                    list.clear();
                    list.addAll(apiResponse.data.list);
                    for (int i = 0; i < MainActivity.list.size(); i++) {
                            path = MainActivity.list.get(i).head;
                            mTitle = MainActivity.list.get(i).nickname;
//                        Log.e("|id=", MainActivity.list.get(i).id);
//                        Log.e("|path=",path);
                        Log.e("|Test=",Urls.PHOTO + path + "|");
                    }
                    break;
                case 5:
//                    if (imagelist.size() != 0) {
//                        imagelist.clear();
//                    }
//                    imagelist.addAll(apiResponse.data.list);
//                    Log.e("imagelist","!!!!!!!" + imagelist.size());
                    break;
                case 22:
                    SharedPreferencesHelper sp1 = WJSJApplication.getInstance().getSp();
                    sp1.putValue(Constant.STATUS, apiResponse.data.status);
                    break;
                case 11:
                    String attnum= apiResponse.data.attnum;
                    String actnum =apiResponse.data.actnum;
                    String sysnum = apiResponse.data.sysnum;
                    int allCount=0,attCount=0,actCount=0,sysCount=0;
                    try{
                        if(!TextUtils.isEmpty(attnum))
                        {
                             attCount = Integer.parseInt(attnum);
                        }
                        if(!TextUtils.isEmpty(actnum)){
                            actCount=Integer.parseInt(actnum);
                        }
                        if(!TextUtils.isEmpty(sysnum)){
                            sysCount=Integer.parseInt(sysnum);
                        }
                        allCount=attCount+actCount+sysCount;
                    }catch (Exception ex){}
                    sp.putValue(Constant.UNJREADMESSAGE, "" + allCount);
                    setBadgeText();

                    ConversationListDynamicFragment df= getConversationListDynamicFragment();
                    df.setBadge(attnum,actnum,sysnum);
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        Log.e("tag", code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    getFragmentTransaction().replace(R.id.content_frame, getPersonalFragment()).commit();
                    break;
                case 4:
                    getFragmentTransaction().replace(R.id.content_frame, getConversationListDynamicFragment()).commit();
                    break;
                case 5:
                    getFragmentTransaction().replace(R.id.content_frame, getAttentionFragment()).commit();
                    break;
                case 6:
                    getFragmentTransaction().replace(R.id.content_frame, getDynamicFragment()).commit();
                    break;
            }
        }

    }


}
