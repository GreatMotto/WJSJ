package com.bm.wjsj.Circle;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;

import java.util.List;

/**
 * 置顶帖子适配器
 */
public class PostTopAdapter extends BaseAdapter {

    private Context mContext;
    private List<PostBean> list;

    public PostTopAdapter(Context context, List<PostBean> list) {
        super();
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //Log.e("zhidingtiezi", String.valueOf(list.size()));
        return list.size();
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.lv_item_posttop, null);
        }
        TextView tv_ptop_title = ViewHolder.get(convertView, R.id.tv_ptop_title);
        tv_ptop_title.setText(list.get(position).title);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, PostDetailActivity.class);
                intent.putExtra(Constant.POSTID, list.get(position).id);
                intent.putExtra(Constant.BOOLEAN, false);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

}
