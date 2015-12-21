package com.bm.wjsj.Circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.View.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的圈子
 */
public class MyCircleFragment extends Fragment {
    private View view, layout_header;
    private ListView lv_circles;
    private ListView lv_my_circle;
    private RefreshLayout rfl_circle;

    private List<ImageBean> list_my = new ArrayList<>();
    private List<ImageBean> list = new ArrayList<>();
    private CircleAdapter myadapter, adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_mycircle, container, false);
        initView();
        return view;
    }

    private void initView() {
        lv_circles = (ListView) view.findViewById(R.id.lv_circles);
        rfl_circle = (RefreshLayout) view.findViewById(R.id.rfl_circle);
        layout_header = LayoutInflater.from(getActivity()).inflate(R.layout.header_mycircle, null);
        lv_circles.addHeaderView(layout_header, null, false);
        lv_my_circle = (ListView) layout_header.findViewById(R.id.lv_my_circle);
        list_my.addAll(((MainActivity) getActivity()).getImageList(Constant.imageUrls2, Constant.imageUrls2.length));
//        myadapter = new CircleAdapter(getActivity(), list_my, true);
        lv_my_circle.setAdapter(myadapter);
        list.addAll(((MainActivity) getActivity()).getImageList(Constant.imageUrls2, Constant.imageUrls2.length));
//        adapter = new CircleAdapter(getActivity(), list, false);
        lv_circles.setAdapter(adapter);
        int height = 98 * (list_my.size());
        DisplayUtil.setLayoutParams(lv_my_circle, DisplayUtil.getWidth(getActivity()) - DisplayUtil.dip2px(getActivity(), 16), DisplayUtil.dip2px(getActivity(), height));

    }

}
