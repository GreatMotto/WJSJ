package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.Result;
import com.bm.wjsj.Bean.StoreListBean;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class PopAdapter extends BaseAdapter {
    private Context context;
    //private List<StoreListBean> list;
    private List<Result> list=new ArrayList<>();

//    public PopAdapter(List<StoreListBean> list, Context context) {
//        this.list = list;
//        this.context = context;
//    }

    public PopAdapter(List<Result> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.pop_item, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        TextView tv_type = ViewHolder.get(convertView, R.id.tv_type);
        tv_type.setText(list.get(position).name);

        return convertView;
    }
}
