package com.bm.wjsj.MyMsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.SplashActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AppShortCutUtil;
import com.bm.wjsj.Utils.BadgeView;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.notification.PushNotificationMessage;

/**
 * Created by liuy02 on 2015/10/28.
 */
public class ConversationListDynamicFragment extends Fragment implements View.OnClickListener,
        RongIMClient.OnReceivePushMessageListener,APICallback.OnResposeListener{

    private FragmentTransaction transaction;
    private View view;
    private ConversationListFragment fragment;
    private TextView tv_sidebar, tv_msg_talk, tv_msg_attention, tv_msg_ac, tv_msg_system;


    public static int msgTemp;

    private JUnReadReceiver receiver = null;

    private  BadgeView attentionBadge,activityBadge,sysBadge;

    /**
     * 目标 Id
     */
    private String mTargetId,path,mTitle;



    private UserInfo user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.rong_activity, container, false);

        init();

        fragment = new ConversationListFragment();
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .build();
        fragment.setUri(uri);

        WJSJApplication.messageUserInfo();

        transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.rong_content, fragment);
        transaction.commit();

        attentionBadge= new BadgeView(getActivity(), tv_msg_attention);
        activityBadge= new BadgeView(getActivity(), tv_msg_ac);
        sysBadge= new BadgeView(getActivity(), tv_msg_system);

        int version = Build.VERSION.SDK_INT;
        if ( version < 21) {
            attentionBadge.setBadgeMargin(40);
            activityBadge.setBadgeMargin(40);
            sysBadge.setBadgeMargin(40);
        }
        else{
            attentionBadge.setBadgeMargin(80);
            activityBadge.setBadgeMargin(80);
            sysBadge.setBadgeMargin(80);
        }
//        tv_msg_attention.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                attentionBadge.toggle(true);
//            }
//        });
        //attentionBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
        //attentionBadge.setBadgeMargin(60);


//        tv_msg_ac.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                activityBadge.toggle(true);
//            }
//        });
        //activityBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);



//        tv_msg_system.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //sysBadge.toggle(true);
//            }
//        });
        //sysBadge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);



         /*动态方式注册广播接收者*/
        receiver = new JUnReadReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.jpush.android.unread");
        getActivity().registerReceiver(receiver, filter);

        WebServiceAPI.getInstance().findUnReadmessageNum(this, getActivity());

        //AppShortCutUtil.addNumShortCut(WJSJApplication.getInstance(), SplashActivity.class, true, "11", false);

        return view;
    }

    public void init(){
        Intent intent = getActivity().getIntent();
        //back键
        tv_sidebar = (TextView) view.findViewById(R.id.tv_sidebar);
        tv_sidebar.setOnClickListener(this);
        //关注消息
        tv_msg_attention = (TextView) view.findViewById(R.id.tv_msg_attention);
        tv_msg_attention.setOnClickListener(this);
        //活动消息
        tv_msg_ac = (TextView) view.findViewById(R.id.tv_msg_ac);
        tv_msg_ac.setOnClickListener(this);
        //系统消息
        tv_msg_system = (TextView) view.findViewById(R.id.tv_msg_system);
        tv_msg_system.setOnClickListener(this);

        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                return false;
            }
        });

        RongIM.getInstance().setOnReceiveUnreadCountChangedListener(
                new RongIM.OnReceiveUnreadCountChangedListener() {
                    @Override
                    public void onMessageIncreased(int i) {
                        try {
                            Intent intent = new Intent();
                            intent.setAction("com.msg.notify");
                            intent.putExtra("msgTemp12", i);
                            WJSJApplication.getInstance().sendBroadcast(intent);
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                }, Conversation.ConversationType.PRIVATE
        );

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //back
            case R.id.tv_sidebar:
                //Log.e("tv_sidebar:","+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                WebServiceAPI.getInstance().findUnReadmessageNum(this, getActivity());
                ((MainActivity) getActivity()).setBadgeText();
                ((MainActivity) getActivity()).sm.toggle();
                break;
            //关注消息
            case R.id.tv_msg_attention:
                gotoSystemMsg(tv_msg_attention.getText().toString(), "2");
                break;
            //活动消息
            case R.id.tv_msg_ac:
                gotoSystemMsg(tv_msg_ac.getText().toString(), "1");
                break;
            //系统消息
            case R.id.tv_msg_system:
                gotoSystemMsg(tv_msg_system.getText().toString(), "0");
                break;
            default:
                break;
        }
    }

    private void gotoSystemMsg(String title, String type) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SystemMsgActivity.class);
        intent.putExtra(Constant.TITLE, title);
        intent.putExtra("type", type);
        ((MainActivity) getActivity()).startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userid) {
                for (int i = 0; i < MainActivity.list.size(); i++) {
                    if (userid.equals(MainActivity.list.get(i).id)) {
                        path = MainActivity.list.get(i).head;
                        mTitle = MainActivity.list.get(i).nickname;

                        if(TextUtils.isEmpty(path)){

                        }

                        user = new UserInfo(
                                userid,
                                mTitle,
                                Uri.parse(Urls.PHOTO + path));
                        RongIM.getInstance().refreshUserInfoCache(user);//刷新消息列表用户信息
                        return user;
                    }else if(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID).equals(userid)){
                        user = new UserInfo(
                                WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                                WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME),
                                Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)));
                        RongIM.getInstance().refreshUserInfoCache(user);//刷新消息列表用户信息
                        return user;
                    }
                }

                return null;
            }
        }, false);

//        Intent intent = new Intent();
//        mTargetId = intent.getData().getQueryParameter("targetId");
//        mTitle = intent.getData().getQueryParameter("title");
//        RongIM.getInstance().refreshUserInfoCache(
//                new UserInfo(
//                        mTargetId,
//                        mTitle,
//                        Uri.parse(Urls.PHOTO + path))
//        );

        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                return false;
            }
        });

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

        WebServiceAPI.getInstance().findUnReadmessageNum(this, getActivity());
    }


    @Override
    public void onDestroyView() {
        super.onDestroy();
        if(receiver!=null)
            getActivity().unregisterReceiver(receiver);
    }

    @Override
    public boolean onReceivePushMessage(PushNotificationMessage pushNotificationMessage) {
        pushNotificationMessage.getContent();
        return false;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    private  int index=1;
    public  class JUnReadReceiver extends BroadcastReceiver {
        private static final String TAG = "JPush";
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                WebServiceAPI.getInstance().findUnReadmessageNum(ConversationListDynamicFragment.this, getActivity());
            }
        }
    }

    public void setBadge(String attnum,String actnum,String sysnum)
    {
        if(attentionBadge!=null &&activityBadge!=null&&sysBadge!=null) {
            if (TextUtils.isEmpty(attnum) || "0".equals(attnum)) {
                if (attentionBadge.isShown()) {
                    attentionBadge.toggle();
                }
            } else {
                attentionBadge.setText("" + attnum);
                if (attentionBadge.isShown()) {
                } else {
                    attentionBadge.show();
                }

            }

            if (TextUtils.isEmpty(actnum) || "0".equals(actnum)) {
                if (activityBadge.isShown()) {
                    activityBadge.toggle();
                }
            } else {
                activityBadge.setText("" + actnum);
                if (activityBadge.isShown()) {
                } else {
                    activityBadge.show();
                }
            }

            if (TextUtils.isEmpty(sysnum) || "0".equals(sysnum)) {
                if (sysBadge.isShown()) {
                    sysBadge.toggle();
                }
            } else {
                sysBadge.setText("" + sysnum);
                if (sysBadge.isShown()) {
                } else {
                    sysBadge.show();
                }
            }
        }
    }


    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 11:
//                    if (TextUtils.isEmpty(apiResponse.data.attnum) || "0".equals(apiResponse.data.attnum)) {
//                        if (attentionBadge.isShown()) {
//                            attentionBadge.toggle();
//                        }
//                    } else {
//                        attentionBadge.setText("" + apiResponse.data.attnum);
//                        if (attentionBadge.isShown()) {
//                        } else {
//                            attentionBadge.show();
//                        }
//
//                    }
//
//                    if (TextUtils.isEmpty(apiResponse.data.actnum) || "0".equals(apiResponse.data.actnum)) {
//                        if (activityBadge.isShown()) {
//                            activityBadge.toggle();
//                        }
//                    } else {
//                        activityBadge.setText("" + apiResponse.data.actnum);
//                        if (activityBadge.isShown()) {
//                        } else {
//                            activityBadge.show();
//                        }
//                    }
//
//                    if (TextUtils.isEmpty(apiResponse.data.sysnum) || "0".equals(apiResponse.data.sysnum)) {
//                        if (sysBadge.isShown()) {
//                            sysBadge.toggle();
//                        }
//                    } else {
//                        sysBadge.setText("" + apiResponse.data.sysnum);
//                        if (sysBadge.isShown()) {
//                        } else {
//                            sysBadge.show();
//                        }
//                    }

                    String attnum= apiResponse.data.attnum;
                    String actnum =apiResponse.data.actnum;
                    String sysnum = apiResponse.data.sysnum;

                    setBadge(attnum,actnum,sysnum);

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
                    SharedPreferencesHelper sp= WJSJApplication.getInstance().getSp();
                    sp.putValue(Constant.UNJREADMESSAGE, "" + allCount);

                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
