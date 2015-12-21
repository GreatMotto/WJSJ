package com.bm.wjsj.MyMsg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.SwipeListView;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imkit.widget.provider.LocationInputProvider;
import io.rong.imkit.widget.provider.VoIPInputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;


/**
 * @author 杨凯
 * @description 融云消息界面
 * @time 2015.3.12
 */
public class ImMsgActivity extends BaseActivity implements APICallback.OnResposeListener{

    private SwipeListView listview;

    private List<ImageBean> list = new ArrayList<>();
    private ImMsgAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_immsg);
        initTitle(getResources().getString(R.string.msg_talk));
        initView();
    }

    private void initView() {
        RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {
//                Log.e("setConnectionStatusListener",""+connectionStatus.getMessage());
            }
        });
        WJSJApplication.getInstance().connectRongCloud();
        init();
    }

    private void init() {
        ConversationListFragment fragment =  (ConversationListFragment)getSupportFragmentManager().findFragmentById(R.id.conversationlist);

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")
                .build();

        fragment.setUri(uri);
        //扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new LocationInputProvider(RongContext.getInstance()),//地理位置
                new VoIPInputProvider(RongContext.getInstance()),// 语音通话
        };
        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
//        RongIM.getInstance().resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
//        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//            @Override
//            public UserInfo getUserInfo(String userid) {
//                Log.e("ImMsgActivity", "userid = " + userid);
//                UserInfo user = new UserInfo(userid, "IOS", Uri.parse("http://comment.news.sohu.com/upload/comment4_1/images/face2013/7.gif"));
//                return user;
//            }
//        }, true);
    }

//    private void assignViews() {
//        listview = (SwipeListView) findViewById(R.id.listview);
//        listview.setRightViewWidth(DisplayUtil.getWidth(this) * 3 / 16);
//        list.addAll(getImageList(Constant.imageUrls, Constant.imageUrls2.length));
//        adapter = new ImMsgAdapter(this, listview, list, rightListener);
//        listview.setAdapter(adapter);
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                /**
//                 * 启动单聊
//                 * context - 应用上下文。
//                 * targetUserId - 要与之聊天的用户 Id。
//                 * title - 聊天的标题，如果传入空值，则默认显示与之聊天的用户名称。
//                 */
//                if (RongIM.getInstance() != null) {
//                    RongIM.getInstance().startPrivateChat(ImMsgActivity.this, MainActivity.targetId, "hello");
//                }
//            }
//        });
//    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {

    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
