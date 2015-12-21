package com.bm.wjsj.Date;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/7 0007.
 */
public class MyTzAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageBean> list = new ArrayList<>();

    public MyTzAdapter(Context mContext, List<ImageBean> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return 3;
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
            convertView = View.inflate(mContext, R.layout.my_tz_itme, null);
        }
        SimpleDraweeView sdv_pic = ViewHolder.get(convertView, R.id.sdv_pic);
        sdv_pic.setAspectRatio(1.0f);
        sdv_pic.setImageURI(Uri.parse(list.get(position).path));
        return convertView;
    }
}
