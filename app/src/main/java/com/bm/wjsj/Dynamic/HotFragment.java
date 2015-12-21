package com.bm.wjsj.Dynamic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bm.wjsj.Bean.DynamicListBean;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.View.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwm on 2015/10/19.
 */
public class HotFragment extends Fragment implements APICallback.OnResposeListener {
    private View view;
    private RefreshLayout rfl_lv;
    private ListView listview;
    private DynamicAdapter adapter;
    private List<DynamicListBean> list = new ArrayList<>();
    private String dynamicType = "1";//0：关注（默认） 1：热门 2：附近
    private int pageNum = 1, pageSize = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_dynamic_hot, container, false);
        WebServiceAPI.getInstance().dynamicList(dynamicType, pageNum, pageSize, HotFragment.this, getActivity());
        initView();
        return view;
    }

    private void initView() {
        rfl_lv = (RefreshLayout) view.findViewById(R.id.rfl_lv);
        listview = (ListView) view.findViewById(R.id.listview);
        listview.addFooterView(rfl_lv.getFootView(), null, false);
        listview.setOnScrollListener(rfl_lv);
        rfl_lv.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                WebServiceAPI.getInstance().dynamicList(dynamicType, pageNum, pageSize, HotFragment.this, getActivity());
            }
        });
        rfl_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                WebServiceAPI.getInstance().dynamicList(dynamicType, pageNum, pageSize, HotFragment.this, getActivity());
            }
        });
//        list.addAll(((MainActivity) getActivity()).getImageList(Constant.imageUrls2, Constant.imageUrls2.length));
        adapter = new DynamicAdapter(getActivity(), list, listview);
        listview.setAdapter(adapter);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        WebServiceAPI.getInstance().dynamicList(dynamicType, pageNum, pageSize, HotFragment.this, getActivity());
//
//    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0") && apiResponse.data != null) {
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
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
