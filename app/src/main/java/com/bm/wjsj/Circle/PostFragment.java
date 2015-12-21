package com.bm.wjsj.Circle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.R;
import com.bm.wjsj.View.RefreshLayout;

import java.util.ArrayList;
import java.util.List;


public class PostFragment extends Fragment {
    private View view;
    private ListView listview;
    private RefreshLayout rfl_post;

    private PostAdapter adapter;
    private List<PostBean> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_post, container, false);
        initView();
        return view;
    }

    private void initView() {
        listview = (ListView) view.findViewById(R.id.listview);
        rfl_post = (RefreshLayout) view.findViewById(R.id.rfl_post);
        adapter = new PostAdapter(getActivity(), list, false,"",false);
        listview.setAdapter(adapter);
    }


}
