package com.bm.wjsj.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.MyDynamicListBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.WJSJApplication;
import com.bm.wjsj.upload.UploadActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的动态
 */
public class MyDynamicActivity extends BaseActivity implements APICallback.OnResposeListener {

    private ListView listview;
    private ImageView iv_shaixuan;
    private RefreshLayout rfl;
    private MyDynamicAdapter adapter;
    private List<MyDynamicListBean> list = new ArrayList<>();
    private int pageSize = 10, pageNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_my_dynamic);
        initTitle(getResources().getString(R.string.my_dynamic));
        assignViews();
    }


    private void assignViews() {
        listview = (ListView) findViewById(R.id.lv_my_d);
        iv_shaixuan = (ImageView) findViewById(R.id.iv_shaixuan);
        iv_shaixuan.setImageResource(R.mipmap.icon_submit);
        iv_shaixuan.setVisibility(View.VISIBLE);
        iv_shaixuan.setOnClickListener(this);
        rfl = (RefreshLayout) findViewById(R.id.rfl);
        listview.addFooterView(rfl.getFootView(), null, false);
        listview.setOnScrollListener(rfl);
        adapter = new MyDynamicAdapter(this, list,WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID));
        listview.setAdapter(adapter);
        rfl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                WebServiceAPI.getInstance().myDynamicList(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                        pageNum, pageSize, MyDynamicActivity.this, MyDynamicActivity.this);
            }
        });
        rfl.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                WebServiceAPI.getInstance().myDynamicList(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                        pageNum, pageSize, MyDynamicActivity.this, MyDynamicActivity.this);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        WebServiceAPI.getInstance().myDynamicList(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                pageNum, pageSize, MyDynamicActivity.this, MyDynamicActivity.this);
        //Log.e("myDy:","^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.iv_shaixuan:
                if(checkStatus(this)) {
                    Intent intent = new Intent();
                    intent.setClass(this, UploadActivity.class);
                    intent.putExtra(Constant.BOOLEAN, true);
                    startActivity(intent);
                }
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
        if (apiResponse.status.equals("0") && apiResponse.data != null){
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
