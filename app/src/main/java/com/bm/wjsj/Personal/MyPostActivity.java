package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Circle.PostAdapter;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的帖子列表
 */
public class MyPostActivity extends BaseActivity implements APICallback.OnResposeListener {

    private ListView listview;
    private RefreshLayout rfl;
    private PostAdapter adapter;
    private List<PostBean> list = new ArrayList<>();
    private int pageSize = 10, pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_mypost);
        initTitle("我的话题");
        assignViews();
    }


    private void assignViews() {
        listview = (ListView) findViewById(R.id.listview);
        rfl = (RefreshLayout) findViewById(R.id.rfl);
        listview.addFooterView(rfl.getFootView(), null, false);
        listview.setOnScrollListener(rfl);
        adapter = new PostAdapter(this, list, true,"",false);
        listview.setAdapter(adapter);
        rfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                WebServiceAPI.getInstance().myPostList(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),pageNum, pageSize, MyPostActivity.this, MyPostActivity.this);
            }
        });
        rfl.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                WebServiceAPI.getInstance().myPostList(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),pageNum, pageSize, MyPostActivity.this, MyPostActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebServiceAPI.getInstance().myPostList(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),pageNum, pageSize, MyPostActivity.this, MyPostActivity.this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_shaixuan:

                break;
            default:
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
            if (pageNum == 1) {
                list.clear();
                rfl.setRefreshing(false);
                rfl.setLoad_More(true);
            } else {
                rfl.setLoading(false);
            }
            list.addAll(apiResponse.data.list);
            adapter.notifyDataSetChanged();
            if (apiResponse.data.page.totalPage <= pageNum) {
                rfl.setLoad_More(false);
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
