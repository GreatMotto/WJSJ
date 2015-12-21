package com.bm.wjsj.Circle;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.CircleBean;
import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.NoScrollListview;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.WJSJApplication;
import com.bm.wjsj.upload.UploadActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 圈子详情界面
 */
public class CircleDetailActivity extends BaseActivity implements APICallback.OnResposeListener {

    private ListView listview;
    private View layout_header;
    private NoScrollListview lv_top;
    private RadioGroup radioGroup;
    private SimpleDraweeView my_image_view;
    private RefreshLayout rfl_lv;
    private TextView tv_title_right, tv_circle_join, tv_circle_title, tv_circle_discrip;
    private boolean isMine, isEssence;
    private PostAdapter adapter;
    private List<PostBean> list = new ArrayList<>();
    private CircleBean circleBean;
    private PostTopAdapter postTopAdapter;
    private List<PostBean> listtop = new ArrayList<>();
    public static String circleIdFlag;
    private int pageNum = 1, pageSize = 10;
    private int type = 2;// 0：热门帖子，1：置顶帖子，2：全部帖子，3：精华帖子

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_circledetail);
        initTitle(getResources().getString(R.string.circledetail));
        isMine = getIntent().getBooleanExtra(Constant.BOOLEAN, false);
        isEssence = getIntent().getBooleanExtra("isEssence", false);
        circleBean = (CircleBean) getIntent().getSerializableExtra(Constant.URL);
        circleIdFlag = getIntent().getStringExtra("circleId");
        assignViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 默认显示置顶贴
        WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
        // 默认显示全部贴
        WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
    }

    private void assignViews() {
        rfl_lv = (RefreshLayout) findViewById(R.id.rfl_lv);
        tv_title_right = (TextView) findViewById(R.id.tv_title_right);
        listview = (ListView) findViewById(R.id.listview);
        listview.addFooterView(rfl_lv.getFootView(), null, false);
        layout_header = LayoutInflater.from(this).inflate(R.layout.header_circledetail, null);
        listview.addHeaderView(layout_header, null, false);

        lv_top = (NoScrollListview) layout_header.findViewById(R.id.lv_top);
        my_image_view = (SimpleDraweeView) layout_header.findViewById(R.id.my_image_view);
        tv_circle_title = (TextView) layout_header.findViewById(R.id.tv_circle_title);
        tv_circle_join = (TextView) layout_header.findViewById(R.id.tv_circle_join);
        tv_circle_discrip = (TextView) layout_header.findViewById(R.id.tv_circle_discrip);
        radioGroup = (RadioGroup) layout_header.findViewById(R.id.radiogroup);

        radioGroup.check(R.id.rb_one);
        my_image_view.setImageURI(Uri.parse(Urls.PHOTO + circleBean.image));
        tv_circle_title.setText(circleBean.name);
        tv_circle_discrip.setText(circleBean.description);
        tv_circle_discrip.setMaxLines(10);
        tv_title_right.setText("发帖");
        tv_title_right.setVisibility(View.VISIBLE);
        listview.setOnScrollListener(rfl_lv);
        tv_title_right.setOnClickListener(this);

        if (isMine) {
            tv_circle_join.setText("退出");
        } else {
            tv_circle_join.setText("加入");
        }
        tv_circle_join.setOnClickListener(this);

        postTopAdapter = new PostTopAdapter(this, listtop);
        lv_top.setAdapter(postTopAdapter);
        adapter = new PostAdapter(this, list, false,"",false);
        listview.setAdapter(adapter);

        rfl_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isEssence) {
                    pageNum = 1;
                    WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                    WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                } else {
                    pageNum = 1;
                    WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                    WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                }
            }
        });

        rfl_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (isEssence) {
                    pageNum++;
                    WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                    WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                } else {
                    pageNum++;
                    WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                    WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                            1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // 点击事件获取的选择对象
                switch (checkedId) {
                    //全部贴
                    case R.id.rb_one:
                        type = 2;
                        WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                                1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                        WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                                1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                        break;
                    //精华帖
                    case R.id.rb_two:
                        type = 3;
                        WebServiceAPI.getInstance().circleTopPost(1, pageNum, pageSize, circleIdFlag,
                                1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                        WebServiceAPI.getInstance().circlePost(type, pageNum, pageSize, circleIdFlag,
                                1, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID), CircleDetailActivity.this, CircleDetailActivity.this);
                        break;
                }
            }
        });
    }

    private boolean checkUserAuthority() {
        if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            gotoLoginAc(this, 333);
            return false;
        } else {
            if (checkStatus(this)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
            super.onClick(view);
            switch (view.getId()) {
                //发帖
                case R.id.tv_title_right:
                    if(checkUserAuthority()) {
                        Intent intent = new Intent(this, UploadActivity.class);
                        intent.putExtra(Constant.BOOLEAN, false);
                        startActivity(intent);
                    }
                    break;
                //加入|退出
                case R.id.tv_circle_join:
                    if(checkUserAuthority()) {
                        if (!isMine) {
                            isMine = true;
                            tv_circle_join.setText("退出");
                            WebServiceAPI.getInstance().addCircle(circleIdFlag,
                                    CircleDetailActivity.this, CircleDetailActivity.this);
                        } else {
                            isMine = false;
                            tv_circle_join.setText("加入");
                            WebServiceAPI.getInstance().exitCircle(circleIdFlag,
                                    CircleDetailActivity.this, CircleDetailActivity.this);
//                    onBackPressed();
                        }
                    }
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
            switch (tag) {
                case 1:// 置顶帖子
                    if (pageNum == 1) {
                        listtop.clear();
                        rfl_lv.setRefreshing(false);
                        rfl_lv.setLoad_More(true);
                        listtop.addAll(apiResponse.data.list);
                        postTopAdapter.notifyDataSetInvalidated();
                        Log.e("置顶帖子", String.valueOf(listtop.size()));
                    } else {
                        rfl_lv.setLoading(false);
                        listtop.addAll(apiResponse.data.list);
                        postTopAdapter.notifyDataSetChanged();
                    }
                    if (apiResponse.data.page.totalPage <= pageNum) {
                        rfl_lv.setLoad_More(false);
                    }
                    break;
                case 2:// 全部帖子
                    if (pageNum == 1) {
                        list.clear();
                        rfl_lv.setRefreshing(false);
                        rfl_lv.setLoad_More(true);
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
                case 3:// 精华帖子
                    if (pageNum == 1) {
                        list.clear();
                        rfl_lv.setRefreshing(false);
                        rfl_lv.setLoad_More(true);
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
                case 4://加入圈子
                    NewToast.show(this, "加入成功", NewToast.LENGTH_LONG);
                    break;
                case 5://退出圈子
                    Toast.makeText(this,"退出成功",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }

        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

}
