package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.Goods;
import com.bm.wjsj.Bean.Product;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class MyOrderAdapter extends BaseAdapter {
    private Context context;
    //wangxl 2015.10.19 update begin 用List<Product>代替
    private List<Product> list;
    private List<Goods> listGoods;
    private int orderNum;
    private DecimalFormat decimalFormat =new DecimalFormat("0.00");
    //flag参数没有用，为了和另一个构造函数区分
    public MyOrderAdapter(Context context, List<Product> list,int orderNum) {
        this.context = context;
        this.list = list;
        this.orderNum=orderNum;
    }

    public MyOrderAdapter(Context context, List<Goods> list) {
        this.context = context;
        this.listGoods = list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.my_order_item, null);
        }
        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_num = ViewHolder.get(convertView, R.id.tv_num);
        //wangxl 2015.10.19 update begin
        tv_name.setText(list.get(position).name);
        try {
            float realPrice = Float.parseFloat(list.get(position).price);
            tv_price.setText("￥" + decimalFormat.format(realPrice));
        }
        catch (Exception ex){
            tv_price.setText("￥" + "0.00");
        }
        tv_num.setText("×" + orderNum);
        if(list.get(position).picList.size()>0) {
            Uri uriStr= Uri.parse(Urls.PHOTO + list.get(position).picList.get(0).path);
            my_image_view.setImageURI(uriStr);
            //iv_shop.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
            //my_image_view.setImageURI(Uri.parse(list.get(position).picList.get(0).path));
        }
        else{
            my_image_view.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).hpicUrl));
            //my_image_view.setImageURI(Uri.parse(list.get(position).hpicUrl));
        }
//        tv_name.setText(list.get(position).productName);
//        tv_price.setText("￥" + list.get(position).price);
//        tv_num.setText("×" + list.get(position).saledCount);
//        my_image_view.setImageURI(Uri.parse(list.get(position).picZUrl));
        //wangxl 2015.10.19 update end
        return convertView;
    }
}
