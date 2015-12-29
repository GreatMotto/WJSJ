package com.bm.wjsj.SpiceStore;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.Result;
import com.bm.wjsj.Bean.StoreListBean;
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
 * 商品列表
 */
public class SecondClassifyActivity extends BaseActivity implements APICallback.OnResposeListener {
    private List<StoreListBean> list = new ArrayList<>();
    private PopupWindow pop;
    private ImageView iv_shaixuan;
    private RefreshLayout rfl_gv, rfl_lv;
    private RadioGroup rg_classify;
    private RadioButton rb_price;

    private boolean isList, isPriceChecked;
    private ListView lv_second_classify;
    private ClassifiyListAdapter lististAdapter;
    private SecondGridAdapter secondGridAdapter;
    private int pageNum = 1, pageSize = 10;
    private int index = 0;
    private String Level2Id, type = "0";
    private List<Result> allTypes = new ArrayList<>();
    private Result curType;
    private String parentTypeName, CurTypeName;
    private String Level1Id = "", plateid, selectName;
    private String parentFlag;
    private TextView tvClassifiy;
    private GridView gvSecondClassify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_classify);
        //type = "2";
        parentFlag = getIntent().getStringExtra("parentFlag");
        Level1Id = getIntent().getStringExtra("Level1Id");
        initTitle("");
        if ("0".equals(parentFlag)) {//从分类中跳转过来
            curType = (Result) getIntent().getSerializableExtra("curType");
            allTypes.addAll((List<Result>) getIntent().getSerializableExtra("allTypes"));
            parentTypeName = getIntent().getStringExtra("parentTypeName");
            assignViews();
        } else if ("1".equals(parentFlag)) {//从首页一级分类过来
//            Level1Id = getIntent().getStringExtra("Level1Id");
            parentTypeName = getIntent().getStringExtra("parentTypeName");
            WebServiceAPI.getInstance().storeType1(Level1Id, SecondClassifyActivity.this, SecondClassifyActivity.this);
        } else if ("2".equals(parentFlag))//从首页板块过来
        {
            plateid = getIntent().getStringExtra("plateid");
            assignViews();
        } else if ("3".equals(parentFlag))//从搜索过来
        {
            selectName = getIntent().getStringExtra("selectName");
            assignViews();
        }
    }

    private void assignViews() {
        rg_classify = (RadioGroup) findViewById(R.id.rg_classify);
        rb_price = (RadioButton) findViewById(R.id.rb_price);
        rfl_gv = (RefreshLayout) findViewById(R.id.rfl_gv);
        rfl_lv = (RefreshLayout) findViewById(R.id.rfl_lv);
        iv_shaixuan = (ImageView) findViewById(R.id.iv_shaixuan);
        iv_shaixuan.setVisibility(View.VISIBLE);
        iv_shaixuan.setImageResource(R.mipmap.liebiao);
        iv_shaixuan.setOnClickListener(this);
        iv_shaixuan.setScaleType(ImageView.ScaleType.FIT_CENTER);
        tvClassifiy = (TextView) findViewById(R.id.tv_classifiy);
        if ("0".equals(parentFlag)) {//从分类中跳转过来
            if (curType != null) {
                Level2Id = curType.id;
                tvClassifiy.setText(curType.name);
            } else {
                tvClassifiy.setText("分类");
            }
            initTitle(parentTypeName);
            tvClassifiy.setOnClickListener(this);
            tvClassifiy.setVisibility(View.VISIBLE);
        } else if ("1".equals(parentFlag)) {
            initTitle(parentTypeName);
            tvClassifiy.setOnClickListener(this);
            tvClassifiy.setVisibility(View.VISIBLE);
            tvClassifiy.setText(CurTypeName);
            if (allTypes.size() < 1) {
                tvClassifiy.setText("");
            }
        } else if ("2".equals(parentFlag))//从首页板块过来
        {
            initTitle("商品列表");
            tvClassifiy.setVisibility(View.GONE);
        } else if ("3".equals(parentFlag))//从搜索过来
        {
            initTitle(selectName);
            tvClassifiy.setVisibility(View.GONE);
        }

        ImageView go_shopcat = (ImageView) findViewById(R.id.go_shopcat);
        go_shopcat.setOnClickListener(this);
        gvSecondClassify = (GridView) findViewById(R.id.gv_second_classify);
        gvSecondClassify.setOnScrollListener(rfl_gv);
        secondGridAdapter = new SecondGridAdapter(this, list);
        gvSecondClassify.setAdapter(secondGridAdapter);
        lv_second_classify = (ListView) findViewById(R.id.lv_second_classify);
        lv_second_classify.addFooterView(rfl_lv.getFootView());
        lv_second_classify.setOnScrollListener(rfl_lv);
        lististAdapter = new ClassifiyListAdapter(this, list);
        lv_second_classify.setAdapter(lististAdapter);

        rg_classify.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_frist_new:
                        type = "0";
                        isPriceChecked = false;
                        selectStoreList();
                        break;
                    case R.id.rb_volume:
                        type = "1";
                        isPriceChecked = false;
                        selectStoreList();
                        break;
                    case R.id.rb_price:
                        break;
                }

            }
        });
        selectStoreList();
        rb_price.setOnClickListener(this);
        rfl_gv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                selectStoreList();
//                WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                        SecondClassifyActivity.this, SecondClassifyActivity.this);
            }
        });
        rfl_gv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                selectStoreList();
//                WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                        SecondClassifyActivity.this, SecondClassifyActivity.this);
            }
        });
        rfl_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                selectStoreList();
//                WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                        SecondClassifyActivity.this, SecondClassifyActivity.this);
            }
        });
        rfl_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                selectStoreList();
//                WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                        SecondClassifyActivity.this, SecondClassifyActivity.this);
            }
        });
//        selectStoreList();
//        WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                SecondClassifyActivity.this, SecondClassifyActivity.this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rb_price:
                if (isPriceChecked) {
                    if (rb_price.getText().toString().equals("价格↑")) {
                        rb_price.setText("价格↓");
                        type = "3";
                    } else {
                        rb_price.setText("价格↑");
                        type = "2";
                    }

                } else {
                    type = rb_price.getText().toString().equals("价格↑") ? "2" : "3";
                    isPriceChecked = true;
                }
                selectStoreList();
//                WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                        SecondClassifyActivity.this, SecondClassifyActivity.this);
                break;
            case R.id.tv_classifiy:
//                showPopWindow(tvClassifiy);
//                rightPopUtils.showPopWindow(tvClassifiy, list);
//                showPopWindow(tvClassifiy, list);
                if (allTypes.size() > 0) {
                    Result result= new Result();
                    result.name = "全部";
                    result.id = "";
                    result.path = "";
                    if (!allTypes.get(0).name.equals("全部")) {
                        allTypes.add(0, result);
                    }
                    initPop(tvClassifiy);
                    pop.showAsDropDown(tvClassifiy, 0, 0);
                }
                break;
            case R.id.go_shopcat:
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                } else {
                    Intent intentCar = new Intent(this, MyShopCatActivity.class);
                    startActivity(intentCar);
                }
//                Intent intent = new Intent(this, MyShopCatActivity.class);
//                startActivity(intent);

                break;
            case R.id.iv_shaixuan:
                if (isList) {
                    rfl_gv.setVisibility(View.VISIBLE);
                    rfl_lv.setVisibility(View.GONE);
                    secondGridAdapter.notifyDataSetChanged();
                } else {
                    rfl_gv.setVisibility(View.GONE);
                    rfl_lv.setVisibility(View.VISIBLE);
                    lististAdapter.notifyDataSetChanged();
                }
                isList = !isList;
                break;
        }
    }

    private void initPop(View v) {
        View popv1 = View.inflate(this, R.layout.pop_list, null);
        ListView lv_pop_list = (ListView) popv1.findViewById(R.id.lv_pop_list);
        //PopAdapter popAdapter = new PopAdapter(list, this);
        PopAdapter popAdapter = new PopAdapter(allTypes, this);
        lv_pop_list.setAdapter(popAdapter);

        if (pop == null) {
            pop = new PopupWindow(popv1, v.getMeasuredWidth(),
                    popv1.getLayoutParams().WRAP_CONTENT, true);
        }
        lv_pop_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                tvClassifiy.setText(list.get(index).name);
//                Level2Id = list.get(index).id;
                String typeName = allTypes.get(position).name;
                tvClassifiy.setText(typeName);
                Level2Id = allTypes.get(position).id;
                pageNum = 1;
                selectStoreList();
//                WebServiceAPI.getInstance().storeList("", "", "1", "", type, String.valueOf(pageNum), String.valueOf(pageSize),
//                        SecondClassifyActivity.this, SecondClassifyActivity.this);
                pop.dismiss();
            }
        });
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.update();
    }

    private void selectStoreList() {
        if ("".equals(Level2Id)) {
            WebServiceAPI.getInstance().storeList(Level1Id, Level2Id, plateid, selectName, type, String.valueOf(pageNum), String.valueOf(pageSize),
                    SecondClassifyActivity.this, SecondClassifyActivity.this);
        } else {
            WebServiceAPI.getInstance().storeList(Level1Id, Level2Id, plateid, selectName, type, String.valueOf(pageNum), String.valueOf(pageSize),
                    SecondClassifyActivity.this, SecondClassifyActivity.this);
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }


    @Override
    public void OnErrorData(String code, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        switch (tag) {
            case 1://查询商品
//                list.addAll(apiResponse.data.list);
                if (isList) {
                    if (pageNum == 1) {
                        list.clear();
                        rfl_lv.setRefreshing(false);
                        rfl_lv.setLoad_More(true);
                    } else {
                        rfl_lv.setLoading(false);
                    }
                    list.addAll(apiResponse.data.list);
                    lististAdapter.notifyDataSetChanged();
                    if (apiResponse.data.page.totalPage <= pageNum) {
                        rfl_lv.setLoad_More(false);
                    }

                } else {
                    if (pageNum == 1) {
                        list.clear();
                        rfl_gv.setRefreshing(false);
                        rfl_gv.setLoad_More(true);
                    } else {
                        rfl_gv.setLoading(false);
                    }
                    list.addAll(apiResponse.data.list);
                    secondGridAdapter.notifyDataSetChanged();
                    if (apiResponse.data.page.totalPage <= pageNum) {
                        rfl_gv.setLoad_More(false);
                    }
                }
                break;
            case 2://查询分类
                allTypes = apiResponse.data.result;
                Result result= new Result();
                result.name = "全部";
                result.id = "";
                result.path = "";
                if(allTypes!=null&&allTypes.size()>0) {
                    if (!allTypes.get(0).name.equals("全部")) {
                        allTypes.add(0, result);
                    }
                }
                if (apiResponse.data.result.size() > 0) {
                    Level2Id = allTypes.get(0).id;
                    CurTypeName = allTypes.get(0).name;
                }
                assignViews();
                break;
        }
    }

    /*public void showPopWindow(View v, List<TypeBean> list) {
        v.getWidth();
        View popv1 = View.inflate(this, R.layout.pop_list, null);
        ListView lv_pop_list = (ListView) popv1.findViewById(R.id.lv_pop_list);
        PopAdapter popAdapter = new PopAdapter(list, this);
        lv_pop_list.setAdapter(popAdapter);

        if (pop == null) {
            pop = new PopupWindow(popv1, ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, true);
        }
        pop.setFocusable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOutsideTouchable(true);
        pop.update();

//        pop.showAsDropDown(v, 0, 0);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                pop = null;
            }
        });


    }*/

}
