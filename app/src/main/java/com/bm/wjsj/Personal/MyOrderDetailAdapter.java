package com.bm.wjsj.Personal;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.orderinfoBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.ShopDeataisActivity;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by wangxl01 on 2015/10/21.
 */
public class MyOrderDetailAdapter extends BaseAdapter {
    private Context context;
    //wangxl 2015.10.19 update begin 用List<Product>代替
    private List<orderinfoBean> list;
    //flag参数没有用，为了和另一个构造函数区分
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public MyOrderDetailAdapter(Context context, List<orderinfoBean> list) {
        this.context = context;
        this.list = list;
    }


    //wangxl 2015.10.19 update end

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
            convertView = View.inflate(context, R.layout.my_order_item, null);
        }
        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_num = ViewHolder.get(convertView, R.id.tv_num);
        tv_name.setText(list.get(position).pname);
        try {
            float realPrice = Float.parseFloat(list.get(position).price);
            tv_price.setText("￥" + decimalFormat.format(realPrice));
        } catch (Exception ex) {
            tv_price.setText("￥" + "0.00");
        }
        tv_num.setText("×" + list.get(position).count);

        Uri uriStr = Uri.parse(Urls.PHOTO + list.get(position).path);
        my_image_view.setImageURI(uriStr);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopDeataisActivity.class);
                    intent.putExtra(Constant.ID, list.get(position).productid);
                    context.startActivity(intent);
                }
            });
        return convertView;
    }
}
