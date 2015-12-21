package com.bm.wjsj.SpiceStore;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.StoreReview;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品评价
 */
public class ShopEvaluationActivity extends BaseActivity implements APICallback.OnResposeListener {
    private ListView lvEvaluation;
    private RefreshLayout rfl_lv;
    private String id;

    private int pageNum = 1, pageSize = 10;
    private List<StoreReview> list = new ArrayList<>();
    private ShopEvaluationAdapter adapter;

    private void assignViews() {
        initTitle("商品评价");
        rfl_lv = (RefreshLayout) findViewById(R.id.rfl_lv);
        lvEvaluation = (ListView) findViewById(R.id.lv_evaluation);
        lvEvaluation.addFooterView(rfl_lv.getFootView(), null, false);
        lvEvaluation.setOnScrollListener(rfl_lv);
        lvEvaluation.setFooterDividersEnabled(false);
        adapter = new ShopEvaluationAdapter(this, list);
        lvEvaluation.setAdapter(adapter);
        rfl_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                WebServiceAPI.getInstance().storeReview(id, String.valueOf(pageNum),
                        String.valueOf(pageSize), ShopEvaluationActivity.this, ShopEvaluationActivity.this);
            }
        });
        rfl_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                WebServiceAPI.getInstance().storeReview(id, String.valueOf(pageNum),
                        String.valueOf(pageSize), ShopEvaluationActivity.this, ShopEvaluationActivity.this);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_evaluation);
        id = getIntent().getStringExtra(Constant.ID);
        assignViews();
        WebServiceAPI.getInstance().storeReview(id, String.valueOf(pageNum),
                                                String.valueOf(pageSize), this, this);
    }


    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (pageNum == 1) {
            list.clear();
            rfl_lv.setRefreshing(false);
            rfl_lv.setLoad_More(true);
        } else {
            rfl_lv.setLoading(false);
        }
        list.addAll(apiResponse.data.list);
        adapter.notifyDataSetChanged();
        if (pageNum == 1 && list.size() == 0) {
            NewToast.show(ShopEvaluationActivity.this, "该商品暂无评价！", Toast.LENGTH_LONG);
            return;
        }
        if (apiResponse.data.page.totalPage <= pageNum) {
            rfl_lv.setLoad_More(false);
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
