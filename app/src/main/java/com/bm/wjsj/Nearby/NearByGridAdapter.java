package com.bm.wjsj.Nearby;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 */
public class NearByGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserInfo> list;

    public NearByGridAdapter(Context context, List<UserInfo> list) {
        this.mContext = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.near_item_grid, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        my_image_view.setAspectRatio(1.0f);
        if (!list.get(position).head.equals("")) {
            my_image_view.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).head));
        }else {
            if (list.get(position).sex.equals("0")){
                my_image_view.setImageResource(R.mipmap.touxiangnan);
            }else {
                my_image_view.setImageResource(R.mipmap.touxiangnv);
            }
        }
        TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);
        if (!TextUtils.isEmpty(list.get(position).distance)){
            double distance = Double.parseDouble(list.get(position).distance);
            if (distance >= 1000.0) {
                distance = distance / 1000.0;
                BigDecimal bigDecimal = new BigDecimal(distance);
                int julis = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                tv_distance.setText(julis + "km");
            } else if (distance < 1000.00 && distance > 10.00){
                distance = distance / 1000.0;
                BigDecimal bigDecimal = new BigDecimal(distance);
                double julis = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                tv_distance.setText(julis + "km");
            }else {
                tv_distance.setText(0.01 + "km");
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyDataActivity.class);
                intent.putExtra(Constant.ID, list.get(position).id);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }
}
