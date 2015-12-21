package com.bm.wjsj.Personal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.myScoreProorderBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.ShopDeataisActivity;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2015/8/10 0010.
 */
public class MyExchangeAdapter extends BaseAdapter {
    private Context context;
    private List<myScoreProorderBean> list;

    public MyExchangeAdapter(Context context, List<myScoreProorderBean> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.exchange_itme, null);
        }
        SimpleDraweeView iv_shop_image = ViewHolder.get(convertView, R.id.iv_shop_image);
        TextView tv_name = ViewHolder.get(convertView,R.id.tv_name);
        TextView tv_time = ViewHolder.get(convertView,R.id.tv_time);
        TextView tv_score = ViewHolder.get(convertView,R.id.tv_score);
        iv_shop_image.setAspectRatio(1.0f);
        iv_shop_image.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
        tv_name.setText(list.get(position).name);
        tv_time.setText(list.get(position).createTime);
        tv_score.setText(list.get(position).score+"积分");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent = new Intent(context, ShopDeataisActivity.class);
                inent.putExtra(Constant.SCORE, 1);
                inent.putExtra(Constant.ID, list.get(position).scoreproid);
                context.startActivity(inent);
            }
        });
        return convertView;
    }
}
