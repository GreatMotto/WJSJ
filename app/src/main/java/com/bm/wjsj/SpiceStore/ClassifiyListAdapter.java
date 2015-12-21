package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.StoreListBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 商品列表listview adapter
 * Created by Administrator on 2015/8/10 0010.
 */
public class ClassifiyListAdapter extends BaseAdapter {
    private Context context;
    private List<StoreListBean> list;

    public ClassifiyListAdapter(Context context, List<StoreListBean> list) {
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
            convertView = View.inflate(context, R.layout.second_list_itme, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        SimpleDraweeView iv_shop_image = ViewHolder.get(convertView, R.id.iv_shop_image);
        iv_shop_image.setAspectRatio(1.0f);
        iv_shop_image.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_buynum = ViewHolder.get(convertView, R.id.tv_buynum);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        tv_name.setText(list.get(position).name);
        tv_price.setText("¥" + list.get(position).price);
        tv_buynum.setText(String.valueOf(list.get(position).salenum) + "人购买");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent = new Intent(context, ShopDeataisActivity.class);
                inent.putExtra(Constant.ID,list.get(position).id);
                context.startActivity(inent);
            }
        });
        return convertView;
    }
}
