package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.myScoreInfoBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */
public class MyGetScoreFragment extends Fragment implements APICallback.OnResposeListener {
    View view;
    ListView lv_score_his;
    private List<myScoreInfoBean> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_exchange, container, false);
        WebServiceAPI.getInstance().scoreMyscoreinfo(1, 10, MyGetScoreFragment.this, getActivity());
        lv_score_his = (ListView) view.findViewById(R.id.lv_score_his);
        return view;
    }


    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            list.addAll(apiResponse.data.list);
            MyGetScoreAdapter adapter = new MyGetScoreAdapter(getActivity(), list);
            lv_score_his.setAdapter(adapter);
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
