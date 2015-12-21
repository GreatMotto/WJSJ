package com.bm.wjsj.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.StoreListBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.PrivilegeActivity;
import com.bm.wjsj.SpiceStore.SecondGridAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 积分商品列表
 */
public class ScoreShopActivity extends BaseActivity implements APICallback.OnResposeListener{
    private TextView tvScoreLeft;
    private TextView tvScoreNum;
    private TextView tvMyscoreHis;
    private GridView gvSecondClassify;
    private List<StoreListBean> list_my = new ArrayList<>();
    private static final int RETURN_REFRESH_CODE=11;

    private void assignViews() {
        initTitle("积分兑换");
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("查看规则");
        tv_right.setOnClickListener(this);
        tvScoreLeft = (TextView) findViewById(R.id.tv_score_left);
        tvScoreNum = (TextView) findViewById(R.id.tv_score_num);
        tvMyscoreHis = (TextView) findViewById(R.id.tv_myscore_his);
        gvSecondClassify = (GridView) findViewById(R.id.gv_second_classify);
        tvMyscoreHis.setOnClickListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_shop);
        WebServiceAPI.getInstance().scoreScoreprolist(1, 10, ScoreShopActivity.this, ScoreShopActivity.this);
        WebServiceAPI.getInstance().scoreMyscore(ScoreShopActivity.this, ScoreShopActivity.this);
        assignViews();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
                WebServiceAPI.getInstance().help("0", ScoreShopActivity.this, ScoreShopActivity.this);

//                Intent intent = new Intent(this, PrivilegeActivity.class);
//                intent.putExtra(Constant.WEB_NAME, "积分规则");
//                startActivity(intent);
                break;
            case R.id.tv_myscore_his:
                Intent intent2 = new Intent(this, MyScoreHisActivity.class);
                startActivity(intent2);
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")){
            switch (tag){
                case 5:
                    tvScoreNum.setText(apiResponse.data.score);
                    break;
                case 1:
                    if (list_my != null){
                        list_my.clear();
                    }
                    list_my.addAll(apiResponse.data.list);
                    SecondGridAdapter secondGridAdapter = new SecondGridAdapter(this, list_my, true);
                    gvSecondClassify.setAdapter(secondGridAdapter);
                    break;
                case 2:
                    gotoTextActivity("积分规则",apiResponse.data.helper.content);
                    break;
            }

        }

    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RETURN_REFRESH_CODE) {
            if (resultCode == RESULT_CANCELED) {
                WebServiceAPI.getInstance().scoreMyscore(ScoreShopActivity.this, ScoreShopActivity.this);
            }
        }
    }
}
