package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.Result;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class ClassifyNameAdapter extends BaseAdapter {
    private Context mContext;
    private List<Result> result;

    public ClassifyNameAdapter(Context mContext,List<Result> result) {
        this.mContext = mContext;
        this.result =result;
    }




    @Override
    public int getCount() {
        return result.size();
//        return list.size();
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
            convertView = View.inflate(mContext, R.layout.classifyname_itme, null);
        }
        View view_shu = ViewHolder.get(convertView, R.id.view_shu);
        TextView tv_classfiy_name = ViewHolder.get(convertView, R.id.tv_classfiy_name);
        tv_classfiy_name.setText(result.get(position).name);
        if (result.get(position).isSelect) {
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.bg_color));
            view_shu.setVisibility(View.VISIBLE);
            tv_classfiy_name.setTextColor(mContext.getResources().getColor(R.color.theme_color));
        } else {
            tv_classfiy_name.setTextColor(mContext.getResources().getColor(R.color.classifyname_color));
            convertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            view_shu.setVisibility(View.GONE);
        }


        return convertView;
    }
}

