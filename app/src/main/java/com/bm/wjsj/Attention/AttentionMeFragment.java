package com.bm.wjsj.Attention;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bm.wjsj.Bean.AttentionListBean;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.View.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwm on 2015/10/21.
 * 关注我的
 */
public class AttentionMeFragment extends Fragment implements APICallback.OnResposeListener {
    private View view;
    private RefreshLayout rfl_lv;
    private ListView listview;
    private AttentionAdapter adapter;
    private List<AttentionListBean> list = new ArrayList<>();
    private String attentionType = "2";//0:相互关注（默认） 1：我的关注 2：关注我的
    private int pageNum = 1, pageSize = 10;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg_attention_me, container, false);
        WebServiceAPI.getInstance().getattentionlist(attentionType, pageNum, pageSize, AttentionMeFragment.this, getActivity());
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
                WebServiceAPI.getInstance().getattentionlist(attentionType, pageNum, pageSize, AttentionMeFragment.this, getActivity());
            }
        });
        rfl_lv.setOnLoadListener(new RefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                pageNum++;
                WebServiceAPI.getInstance().getattentionlist(attentionType, pageNum, pageSize, AttentionMeFragment.this, getActivity());
            }
        });
        adapter = new AttentionAdapter(getActivity(), list, true);
        listview.setAdapter(adapter);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        WebServiceAPI.getInstance().getattentionlist(attentionType, pageNum, pageSize, AttentionMeFragment.this, getActivity());
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
