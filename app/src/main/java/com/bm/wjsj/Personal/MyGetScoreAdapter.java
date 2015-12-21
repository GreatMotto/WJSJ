package com.bm.wjsj.Personal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.myScoreInfoBean;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/8/11 0011.
 */
public class MyGetScoreAdapter extends BaseAdapter {
    private Context context;
    private List<myScoreInfoBean> list;

    public MyGetScoreAdapter(Context context, List<myScoreInfoBean> list) {
        this.context = context;
        this.list = list;
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
            convertView = View.inflate(context, R.layout.getscore_itme, null);
        }
        TextView tv_type = ViewHolder.get(convertView,R.id.tv_type);
        TextView tv_time = ViewHolder.get(convertView,R.id.tv_time);
        TextView tv_score = ViewHolder.get(convertView,R.id.tv_score);
        tv_type.setText(list.get(position).description);
        tv_time.setText(list.get(position).createTime);
        tv_score.setText("");
        return convertView;
    }
}
