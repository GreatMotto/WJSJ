package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.scoreproInfo;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by yangy on 2015/10/22 15:40
 */
public class MyOrderScoreAdapter extends BaseAdapter {
    private Context context;
    private scoreproInfo scoreproinfo;

    public MyOrderScoreAdapter(Context context,scoreproInfo scoreproinfo) {
        this.context = context;
        this.scoreproinfo = scoreproinfo;
    }

    public int getCount() {
        return 1;
    }


    public Object getItem(int position) {
        return null;
    }


    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.my_order_item, null);
        }
        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_num = ViewHolder.get(convertView, R.id.tv_num);
        tv_name.setText(scoreproinfo.name);
        tv_price.setText(scoreproinfo.score + "积分");
        tv_num.setText("×" + scoreproinfo.ordernum);
        Uri uriStr= Uri.parse(Urls.PHOTO + scoreproinfo.imglist.get(0).path);
        my_image_view.setImageURI(uriStr);
        return convertView;
    }
}
