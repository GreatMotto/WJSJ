package com.bm.wjsj.MyMsg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;


public class MyMsgFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tv_sidebar, tv_msg_talk, tv_msg_attention, tv_msg_ac, tv_msg_system;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fg_mymsg, container, false);
        initView();
        return view;
    }

    private void initView() {
        tv_sidebar = (TextView) view.findViewById(R.id.tv_sidebar);
        tv_sidebar.setOnClickListener(this);
//        tv_msg_talk = (TextView) view.findViewById(R.id.tv_msg_talk);
//        tv_msg_talk.setOnClickListener(this);
        tv_msg_attention = (TextView) view.findViewById(R.id.tv_msg_attention);
        tv_msg_attention.setOnClickListener(this);
        tv_msg_ac = (TextView) view.findViewById(R.id.tv_msg_ac);
        tv_msg_ac.setOnClickListener(this);
        tv_msg_system = (TextView) view.findViewById(R.id.tv_msg_system);
        tv_msg_system.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sidebar:
                ((MainActivity) getActivity()).sm.toggle();
                break;
//            case R.id.tv_msg_talk:
//                ((MainActivity)getActivity()).gotoOtherActivity(ImMsgActivity.class);
//                break;
            case R.id.tv_msg_attention:
                gotoSystemMsg(tv_msg_attention.getText().toString(), "2");
                break;
            case R.id.tv_msg_ac:
                gotoSystemMsg(tv_msg_ac.getText().toString(), "1");
                break;
            case R.id.tv_msg_system:
                gotoSystemMsg(tv_msg_system.getText().toString(), "0");
                break;
            default:
                break;
        }
    }

    private void gotoSystemMsg(String title, String type) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), SystemMsgActivity.class);
        intent.putExtra(Constant.TITLE, title);
        intent.putExtra("type", type);
        ((MainActivity) getActivity()).startActivity(intent);
    }

}
