package com.bm.wjsj.Nearby;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.View.RefreshLayout;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 附近的人页面
 */
public class NearbyFragment extends Fragment implements View.OnClickListener, APICallback.OnResposeListener {

    private View v;
    private ImageView iv_shaixuan;
    private ImageView list_shaixuan;
    private MainActivity ac;
    private List<UserInfo> list_my = new ArrayList<>();
    private ListView lv_near;
    private GridView gv_near;
    private RefreshLayout refre_gv, refre_lv;
    private boolean isGridview;
    int pageNum = 1, pageSize = 10;
    private NearByAdapter adapter;
    private NearByGridAdapter gridAdapter;
    private String time = "3", age = "", sex = "",  provinceId = "", cityId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            if (!WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0")) {
                sex = "0";
            } else {
                sex = "1";
            }
        }
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            WebServiceAPI.getInstance().serNear(time, age, sex,
                    provinceId, cityId, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                    pageNum, pageSize, NearbyFragment.this, getActivity());
        }else {
            WebServiceAPI.getInstance().serNear(time, age, sex,
                    provinceId, cityId, "",
                    pageNum, pageSize, NearbyFragment.this, getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (v != null) {
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeView(v);
            }
            return v;
        }

        if (v == null && Build.VERSION.SDK_INT < 21) {
            v = inflater.inflate(R.layout.fg_nearby, container, false);
        }else if (v == null && Build.VERSION.SDK_INT >= 21){
            v = inflater.inflate(R.layout.fg_nearby_5, container, false);
            Log.e("version:---@@@@--","" + Build.VERSION.SDK_INT);
        }

        initView(v);
        return v;
    }

    private void initView(View v) {

        ac = (MainActivity) getActivity();

        ImageView icon_back = (ImageView) v.findViewById(R.id.icon_back);
        icon_back.setImageResource(R.mipmap.sidebar);
        v.findViewById(R.id.tv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ac.sm.toggle();
            }
        });
        refre_gv = (RefreshLayout) v.findViewById(R.id.refre_gv);
        refre_lv = (RefreshLayout) v.findViewById(R.id.refre_lv);

        lv_near = (ListView) v.findViewById(R.id.lv_near);

        gv_near = (GridView) v.findViewById(R.id.gv_near);

        iv_shaixuan = (ImageView) v.findViewById(R.id.iv_shaixuan);
        iv_shaixuan.setVisibility(View.VISIBLE);
        iv_shaixuan.setOnClickListener(this);


        list_shaixuan = (ImageView) v.findViewById(R.id.list_shaixuan);
        list_shaixuan.setVisibility(View.VISIBLE);
        list_shaixuan.setOnClickListener(this);
        TextView tv = (TextView) v.findViewById(R.id.tv_title);
        tv.setText(getResources().getText(R.string.nearby));
//        list_my.addAll(((MainActivity) getActivity()).getImageList(Constant.imageUrls2, Constant.imageUrls2.length));
        adapter = new NearByAdapter(ac, list_my, false);
        gridAdapter = new NearByGridAdapter(ac, list_my);
        lv_near.setAdapter(adapter);
        gv_near.setAdapter(gridAdapter);
        lv_near.addFooterView(refre_lv.getFootView(), null, false);
        lv_near.setOnScrollListener(refre_lv);
        gv_near.setOnScrollListener(refre_gv);

        if (!TextUtils.isEmpty(WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_B))){//如果不为空就是筛选了
            time = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_TIME);
            age = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_AGE);
            sex = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_SEX);
            provinceId = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_PROVINCEID);
            cityId = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_CITYID);
        }
        refre_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    //Log.e("118sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }else {
                    //Log.e("123sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, "",
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }
            }
        });
        refre_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    //Log.e("135sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }else {
                    //Log.e("140sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, "",
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }
            }
        });
        refre_gv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    //Log.e("152sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }else {
                    //Log.e("157sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, "",
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }
            }
        });
        refre_gv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    //Log.e("169sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }else {
                    //Log.e("174sex------",sex);
                    WebServiceAPI.getInstance().serNear(time, age, sex,
                            provinceId, cityId, "",
                            pageNum, pageSize, NearbyFragment.this, getActivity());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_B))){//如果不为空就是筛选了
            time = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_TIME);
            age = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_AGE);
            sex = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_SEX);
            provinceId = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_PROVINCEID);
            cityId = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_CITYID);
            if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                WebServiceAPI.getInstance().serNear(time, age, sex,
                        provinceId, cityId, WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                        pageNum, pageSize, NearbyFragment.this, getActivity());
            }else {
                WebServiceAPI.getInstance().serNear(time, age, sex,
                        provinceId, cityId, "",
                        pageNum, pageSize, NearbyFragment.this, getActivity());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_shaixuan:
                stopRefre();
                if (!isGridview) {
                    refre_lv.setVisibility(View.GONE);
                    lv_near.setVisibility(View.GONE);
                    refre_gv.setVisibility(View.VISIBLE);
                    gv_near.setVisibility(View.VISIBLE);
                    list_shaixuan.setImageResource(R.mipmap.list_xuan);
                    gridAdapter.notifyDataSetChanged();
                } else {
                    refre_lv.setVisibility(View.VISIBLE);
                    lv_near.setVisibility(View.VISIBLE);
                    refre_gv.setVisibility(View.GONE);
                    gv_near.setVisibility(View.GONE);
                    list_shaixuan.setImageResource(R.mipmap.sudoku);
                    adapter.notifyDataSetChanged();
                }
                isGridview = !isGridview;
                break;
            case R.id.iv_shaixuan://筛选
                Intent intent = new Intent(ac, FilterActivity.class);
                startActivityForResult(intent, 1100);
                break;
        }

    }

    private void stopRefre() {
        refre_gv.setRefreshing(false);
        refre_lv.setRefreshing(false);
        refre_gv.refreshDrawableState();
        refre_lv.refreshDrawableState();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1100:
                    pageNum = 1;
                    time = data.getStringExtra("time");
                    age = data.getStringExtra("age");
                    sex = data.getStringExtra("sex");
                    provinceId = data.getStringExtra("provinceId");
                    cityId = data.getStringExtra("cityId");
//                    WebServiceAPI.getInstance().serNear(gender, ageUp, ageDown, loginTime,
//                            provinceId, cityId, pageNum, pageSize, this, getActivity());

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (isGridview) {
            if (pageNum == 1) {
                list_my.clear();
                refre_gv.setRefreshing(false);
                refre_gv.setLoad_More(true);
            } else {
                refre_gv.setLoading(false);
            }
            list_my.addAll(apiResponse.data.list);
            gridAdapter.notifyDataSetChanged();
            if (apiResponse.data.page.totalPage <= pageNum) {
                refre_gv.setLoad_More(false);
            }

        } else {
            if (pageNum == 1) {
                list_my.clear();
                refre_lv.setRefreshing(false);
                refre_lv.setLoad_More(true);
                list_my.addAll(apiResponse.data.list);
                adapter.notifyDataSetInvalidated();
            } else {
                refre_lv.setLoading(false);
                list_my.addAll(apiResponse.data.list);
                adapter.notifyDataSetChanged();
            }
            for (int i = 0;i < list_my.size();i++){
                //Log.e("list_my.get(i).id",list_my.get(i).id);
            }
            if (apiResponse.data.page.totalPage <= pageNum) {
                refre_lv.setLoad_More(false);
            }
        }


    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
