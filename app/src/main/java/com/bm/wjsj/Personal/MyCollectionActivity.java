package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.StoreListBean;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.SecondGridAdapter;
import com.bm.wjsj.Utils.AdapterClickInterface;
import com.bm.wjsj.Utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏
 */
public class MyCollectionActivity extends BaseActivity implements APICallback.OnResposeListener {
    private GridView gvSecondClassify;
    private List<StoreListBean> list = new ArrayList<>();
    private SecondGridAdapter secondGridAdapter;
    private boolean isChange;
    private TextView tv_right;
    private int pageSize = 10, pageNum = 1;
    private List<String> productidList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        initTitle("我的收藏");
        assignViews();
        initData();
    }

    private void assignViews() {
        tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        tv_right.setText("编辑");
        gvSecondClassify = (GridView) findViewById(R.id.gv_second_classify);
        secondGridAdapter = new SecondGridAdapter(this, list, false, adapterClickInterface,true);
//        secondGridAdapter.setCollection(true);
        gvSecondClassify.setAdapter(secondGridAdapter);
    }

    private void initData() {
        DialogUtils.showProgressDialog("正在加载...", this);
        WebServiceAPI.getInstance().myCollect(pageNum, pageSize, MyCollectionActivity.this, MyCollectionActivity.this);
    }

    AdapterClickInterface adapterClickInterface = new AdapterClickInterface() {
        @Override
        public void onClick(View v, String s, String id) {
            productidList.add(id);
        }

        @Override
        public void getReport(View v, String id) {

        }

        @Override
        public void onLongClick(View v, String id,String reportid,String message) {

        }
    };

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
                if (checkStatus(this)) {
                    if (!isChange) {
                        secondGridAdapter.setCollection(true);
                        tv_right.setText("完成");
                    } else {
                        secondGridAdapter.setCollection(false);
                        tv_right.setText("编辑");
                        /*
                        if (productidList.size() >= 0) {
                            for (int i = 0; i < productidList.size(); i++) {
                                WebServiceAPI.getInstance().deleteCollect(productidList.get(i), MyCollectionActivity.this, MyCollectionActivity.this);
                            }
                        }
                        productidList.clear();
                        */
                    }
                    isChange = !isChange;
                    secondGridAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        if (apiResponse.status.equals("0") && apiResponse.data != null){
            switch (tag){
                case 1:
                    if (list.size() != 0) {
                        list.clear();
                    }
                    list.addAll(apiResponse.data.list);
                    for (int i = 0; i < list.size();i++) {
                        //Log.e("list.get(i).name", list.get(i).name);
                    }
                    secondGridAdapter.notifyDataSetChanged();
                    break;
                case 2:
//                    NewToast.show(this, "删除成功", NewToast.LENGTH_LONG);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }
}
