package com.bm.wjsj.MyMsg;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Amap.AmapLocationActivity;
import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.AccusationActivity;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.ViewPagerDialog;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;

public class ConversationActivity extends BaseActivity implements RongIM.LocationProvider {

    private ImageView iv_more;
    private PopupWindow pop;
    private Conversation conversation;
    /**
     * 目标 Id
     */
    private String mTargetId;

    String path;

    private UserInfo user = null;

    private String mTitle;
    /**
     * 刚刚创建完讨论组后获得讨论组的id 为targetIds，需要根据 为targetIds 获取 targetId
     */
    private String mTargetIds;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;

    private boolean isOpen;//消息免打扰是否开启  false未开启，true开启

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //唯一有用的代码，加载一个 layout
        setContentView(R.layout.ac_conversation);
        initTitle("聊天");
        initView();
        Intent intent = getIntent();

        getIntentDate(intent);

        WJSJApplication.getInstance().connectRongCloud();
        init();
    }

    /**
     * 展示如何从 Intent 中得到 融云会话页面传递的 Uri
     */
    private void getIntentDate(Intent intent) {
        mTargetId = intent.getData().getQueryParameter("targetId");
        mTitle = intent.getData().getQueryParameter("title");
        mTargetIds = intent.getData().getQueryParameter("targetIds");
        //intent.getData().getLastPathSegment();//获得当前会话类型
        mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase(Locale.getDefault()));

        enterFragment(mConversationType, mTargetId);
    }

    /**
     * 加载会话页面 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId 目标 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        ConversationFragment fragment = (ConversationFragment) getSupportFragmentManager().findFragmentById(R.id.conversation);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation")
                .appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId)
                .build();

        fragment.setUri(uri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
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
                                mTargetId,
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
//        RongIM.getInstance().refreshUserInfoCache(new UserInfo(
//                        mTargetId,
//                        mTitle,
//                        Uri.parse(Urls.PHOTO + path))
//        );
        RongIM.getInstance().setMessageAttachedUserInfo(true);
//        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
//            @Override
//            public boolean onReceived(Message message, int i) {
//                return false;
//            }
//        });
        //获取是否打开消息免打扰
        if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
            RongIM.getInstance().getRongIMClient().getConversationNotificationStatus(Conversation.ConversationType.PRIVATE,
                    mTargetId, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {

                        @Override
                        public void onSuccess(final Conversation.ConversationNotificationStatus notificationStatus) {
                            //DO_NOT_DISTURB是免打扰  NOTIFY是提醒
                            if (notificationStatus != null) {
                                isOpen = (notificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB ? true : false);
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
        }

        //消息点击事件
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                /* 点击用户头像事件 */
                if (userInfo != null){
//                    Intent intent = new Intent(context, MyDataActivity.class);
//                    intent.putExtra(Constant.ID, userInfo.getUserId());
//                    context.startActivity(intent);
                    //RongIM.getInstance().refreshUserInfoCache(userInfo);//刷新消息列表用户信息
                    //Log.e("userid:","----------------------"+userInfo.getUserId()+"-------------------"+userInfo.getName());
                    if(userInfo.getUserId().equals("userid")&&userInfo.getName().equals(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME)))
                    {
                        Intent intent = new Intent(context, MyDataActivity.class);
                        intent.putExtra(Constant.ID, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
                        context.startActivity(intent);
                    }else {
                        Intent intent = new Intent(context, MyDataActivity.class);
                        intent.putExtra(Constant.ID, mTargetId);
                        context.startActivity(intent);
                    }
                }
                return true;
            }

            @Override
            public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
                return false;
            }

            private int curentImgIndex=0;
            private int allImgIndex=0;
            @Override
            public boolean onMessageClick(Context context, View view, final Message message) {
                if (message.getContent() instanceof LocationMessage) {
                    Intent intent = new Intent(context, AmapLocationActivity.class);
                    intent.putExtra("location", message.getContent());
                    context.startActivity(intent);
                } else if (message.getContent() instanceof RichContentMessage) {
                    RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
                    Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());

                } else if (message.getContent() instanceof ImageMessage) {
                    RongIM.getInstance().getRongIMClient().getHistoryMessages(Conversation.ConversationType.PRIVATE, mTargetId,-1, Integer.MAX_VALUE, new RongIMClient.ResultCallback<List<Message>>() {
                                @Override
                                public void onSuccess(List<Message> messages) {
                                    if(messages!=null) {
                                        //NewToast.show(ConversationActivity.this, "hahaha！" + messages.size(), Toast.LENGTH_LONG);
                                        List<ImageBean> imgList=new ArrayList<ImageBean>();
                                        ImageBean imgBean=null;
                                        curentImgIndex=0;
                                        allImgIndex=0;
                                        for (Message mm : messages){
                                            if (mm.getContent() instanceof  ImageMessage)
                                            {
                                                ImageMessage imageMessage = (ImageMessage) mm.getContent();
                                                imgBean=new ImageBean();
                                                Uri imgUrl=imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri();
                                                if(!imgUrl.getScheme().equals("http") && !imgUrl.getScheme().equals("https")) {
                                                    imgBean.path="file://"+imgUrl.getPath();
                                                    if(mm.getMessageId()==message.getMessageId())
                                                    {
                                                        curentImgIndex=allImgIndex;
                                                    }
                                                    allImgIndex++;
                                                }
                                                else{
                                                    if(!TextUtils.isEmpty(imgUrl.getSchemeSpecificPart()))
                                                    {
                                                        imgBean.path=imgUrl.getScheme()+":"+ imgUrl.getSchemeSpecificPart();
                                                        if(mm.getMessageId()==message.getMessageId())
                                                        {
                                                            curentImgIndex=allImgIndex;
                                                        }
                                                        allImgIndex++;
                                                    }
                                                    else if(!TextUtils.isEmpty(imgUrl.getEncodedSchemeSpecificPart()))
                                                    {
                                                        imgBean.path=imgUrl.getScheme()+":"+ imgUrl.getEncodedSchemeSpecificPart();
                                                        if(mm.getMessageId()==message.getMessageId())
                                                        {
                                                            curentImgIndex=allImgIndex;
                                                        }
                                                        allImgIndex++;
                                                    }
                                                    else{
                                                        imgBean.path="";
                                                    }

                                                }

                                                imgList.add(imgBean);
                                            }
                                        }
                                        //倒序
                                        Collections.reverse(imgList);
                                        curentImgIndex=imgList.size()-curentImgIndex-1;

                                        ViewPagerDialog dlg = new ViewPagerDialog(ConversationActivity.this, imgList, curentImgIndex);
                                        dlg.showViewPagerDialog();

                                    }
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            }
                    );
//                    ImageMessage imageMessage = (ImageMessage) message.getContent();
//                    Intent intent = new Intent(context, PhotoActivity.class);
//
//                    intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
//                    if (imageMessage.getThumUri() != null)
//                        intent.putExtra("thumbnail", imageMessage.getThumUri());
//
//                    context.startActivity(intent);
                }
//                //>>>>>>
//                if (message.getContent() instanceof ImageMessage) {
//                    ImageMessage imageMessage = (ImageMessage) message.getContent();
//                    Intent intent = new Intent(context, PhotoActivity.class);
//                    intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() :
//                            imageMessage.getLocalUri());
//                    if (imageMessage.getThumUri() != null)
//                        intent.putExtra("thumbnail", imageMessage.getThumUri());
//                    context.startActivity(intent);
//                }
                return false;
            }

            @Override
            public boolean onMessageLinkClick(String s) {
                return false;
            }

            @Override
            public boolean onMessageLongClick(Context context, View view, Message message) {
                return false;
            }
        });


        //扩展功能自定义
//        InputProvider.ExtendProvider[] provider = {
//                new ImageInputProvider(RongContext.getInstance()),//图片
//                new CameraInputProvider(RongContext.getInstance()),//相机
//                new LocationInputProvider(RongContext.getInstance()),//地理位置
//                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
//        };
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
    }

    private void initView() {
        iv_more = (ImageView) findViewById(R.id.iv_more);
        iv_more.setVisibility(View.VISIBLE);
        iv_more.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_more:
                showPopWindow(view);
                break;
            case R.id.tv_flashback:
                showSure();
                pop.dismiss();
                break;
            case R.id.tv_hotreview:
                /*消息免打扰*/
                Conversation.ConversationNotificationStatus status = !isOpen ?
                        Conversation.ConversationNotificationStatus.NOTIFY :
                        Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
                if (isOpen) {
                    status = Conversation.ConversationNotificationStatus.NOTIFY;
                } else {
                    status = Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
                }

                if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {


                    RongIM.getInstance().getRongIMClient().setConversationNotificationStatus(Conversation.ConversationType.PRIVATE, mTargetId, status, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {

                        @Override
                        public void onSuccess(Conversation.ConversationNotificationStatus status) {
                            isOpen = !isOpen;
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {
                        }
                    });

                }
                pop.dismiss();
                break;
            case R.id.tv_report:
                pop.dismiss();
                gotoOtherActivity(AccusationActivity.class);
                break;
            default:
                break;
        }
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_delete);
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_hint);
        tv_hint.setText("是否确认清空聊天记录？");
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*清空消息*/
                conversation = new Conversation();
                conversation.setConversationType(Conversation.ConversationType.PRIVATE);
                conversation.setTargetId(mTargetId);
                if (RongIM.getInstance() != null && RongIM.getInstance().getRongIMClient() != null) {
                    RongIM.getInstance().getRongIMClient().clearMessages(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            //ConversationDatabase.getDraftDao().queryBuilder().where(DraftDao.Properties.Type.eq(conversation.getConversationType().getValue()), DraftDao.Properties.Id.eq(conversation.getTargetId())).buildDelete().executeDeleteWithoutDetachingEntities();
                            NewToast.show(ConversationActivity.this, getString(R.string.rc_setting_clear_msg_success), Toast.LENGTH_LONG);
                            conversation = null;
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            NewToast.show(ConversationActivity.this, getString(R.string.rc_setting_clear_msg_fail), Toast.LENGTH_LONG);
                        }
                    });
                }
                alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    private void showPopWindow(View v) {
        View popv1 = LayoutInflater.from(this).inflate(R.layout.pop_more, null);
        if (pop == null) {
            pop = new PopupWindow(popv1, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.update();
        pop.showAsDropDown(v, -v.getWidth() / 2, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                pop = null;
            }
        });
        TextView tv_flashback = (TextView) popv1.findViewById(R.id.tv_flashback);
        TextView tv_hotreview = (TextView) popv1.findViewById(R.id.tv_hotreview);
        TextView tv_report = (TextView) popv1.findViewById(R.id.tv_report);
        tv_flashback.setText("清空聊天记录");
        tv_hotreview.setText(isOpen ? "关闭免打扰" : "消息免打扰");
        tv_flashback.setOnClickListener(this);
        tv_hotreview.setOnClickListener(this);
        tv_report.setOnClickListener(this);
    }
//    /**
//     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
//     *
//     * @param context 应用当前上下文。
//     * @param message 被点击的消息的实体信息。
//     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
//     */
//    @Override
//    public boolean onMessageClick(Context context, View view, Message message) {
//
//        /**
//         * demo 代码  开发者需替换成自己的代码。
//         */
//        if (message.getContent() instanceof LocationMessage) {
//            Intent intent = new Intent(context, SOSOLocationActivity.class);
//            intent.putExtra("location", message.getContent());
//            context.startActivity(intent);
//        } else if (message.getContent() instanceof RichContentMessage) {
//            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
//            Log.d("Begavior", "extra:" + mRichContentMessage.getExtra());
//
//        } else if (message.getContent() instanceof ImageMessage) {
//            ImageMessage imageMessage = (ImageMessage) message.getContent();
//            Intent intent = new Intent(context, PhotoActivity.class);
//
//            intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
//            if (imageMessage.getThumUri() != null)
//                intent.putExtra("thumbnail", imageMessage.getThumUri());
//
//            context.startActivity(intent);
//        }
//
//        Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId());
//
//        return false;
//    }

    /**
     * 位置信息提供者:LocationProvider 的回调方法，打开第三方地图页面。
     *
     * @param context  上下文
     * @param callback 回调
     */
    @Override
    public void onStartLocation(Context context, RongIM.LocationProvider.LocationCallback callback) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        WJSJApplication.getInstance().setLastLocationCallback(callback);
        context.startActivity(new Intent(context, AmapLocationActivity.class));
    }

}