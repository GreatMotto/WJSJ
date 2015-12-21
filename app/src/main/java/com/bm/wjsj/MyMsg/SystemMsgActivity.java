package com.bm.wjsj.MyMsg;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.MessageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.View.SwipeListView;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 杨凯
 * @description 消息界面
 * @time 2015.3.12
 */
public class SystemMsgActivity extends BaseActivity implements APICallback.OnResposeListener {

    private SwipeListView listview;

    private List<MessageBean> list = new ArrayList<>();
    private SystemMsgAdapter adapter;
    private String title,type;
    private RefreshLayout rfl_lv;
    private int delete,pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra(Constant.TITLE);
        type = getIntent().getStringExtra("type");
        setContentView(R.layout.ac_systemmsg);
        initTitle(title);
        WebServiceAPI.getInstance().msgList(type,String.valueOf(pageNum),"10",SystemMsgActivity.this,SystemMsgActivity.this);
        assignViews();
    }


    private void assignViews() {
        listview = (SwipeListView) findViewById(R.id.listview);
        listview.setRightViewWidth(DisplayUtil.getWidth(this) * 3 / 16);
        rfl_lv = (RefreshLayout) findViewById(R.id.rfl_lv);
        listview.addFooterView(rfl_lv.getFootView());
        listview.setOnScrollListener(rfl_lv);
        rfl_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    WebServiceAPI.getInstance().msgList(type, String.valueOf(pageNum), "10", SystemMsgActivity.this, SystemMsgActivity.this);
                } else {
                    WebServiceAPI.getInstance().msgList(type, String.valueOf(pageNum), "10", SystemMsgActivity.this, SystemMsgActivity.this);
                }
            }
        });
        rfl_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    WebServiceAPI.getInstance().msgList(type, String.valueOf(pageNum), "10", SystemMsgActivity.this, SystemMsgActivity.this);
                } else {
                    WebServiceAPI.getInstance().msgList(type, String.valueOf(pageNum), "10", SystemMsgActivity.this, SystemMsgActivity.this);
                }
            }
        });
        adapter = new SystemMsgAdapter(this, listview, list, rightListener, title, type);
        listview.setAdapter(adapter);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_reply_owner:

                break;
            default:
                break;
        }
    }

    SwipeListView.IOnCustomClickListener rightListener = new SwipeListView.IOnCustomClickListener() {

        // 地址条目左滑删除
        @Override
        public void onDeleteClick(View v, int position) {
            DialogUtils.showProgressDialog("正在删除...", SystemMsgActivity.this);
            delete = position;
            WebServiceAPI.getInstance().mdelete(list.get(position).id, SystemMsgActivity.this, SystemMsgActivity.this);
            list.remove(position);
        }
    };


    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:
                    if (pageNum == 1) {
                        rfl_lv.setRefreshing(false);
                        rfl_lv.setLoad_More(true);
                        list.clear();
                        list.addAll(apiResponse.data.list);
                        adapter.notifyDataSetInvalidated();
                    } else {
                        rfl_lv.setLoading(false);
                        list.addAll(apiResponse.data.list);
                        adapter.notifyDataSetChanged();
                    }
                    if (apiResponse.data.page.totalPage <= pageNum) {
                        rfl_lv.setLoad_More(false);
                    }
                    break;
                case 3:
                    DialogUtils.cancleProgressDialog();
                    WebServiceAPI.getInstance().msgList(type, String.valueOf(pageNum), "10", SystemMsgActivity.this, SystemMsgActivity.this);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
