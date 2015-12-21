package com.bm.wjsj.Personal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Bean.orderinfoBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.MyOrderActivity;
import com.bm.wjsj.SpiceStore.ShopDeataisActivity;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.List;

/**
 * 订单中商品adapter
 * Created by wangxl01 on 2015/10/21.
 */
public class MyOrdeHisItemAdapter  extends BaseAdapter {
    private Context context;
    private List<orderinfoBean> list;
    private String status;
    private orderBean orderBean;
    private DecimalFormat decimalFormat =new DecimalFormat("0.00");
    private String orderFlag;
    public MyOrdeHisItemAdapter(Context context, List<orderinfoBean> list,String status,orderBean orderBean,String orderFlag) {
        this.context = context;
        this.list = list;
        this.status=status;
        this.orderBean=orderBean;
        this.orderFlag=orderFlag;
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
            convertView = View.inflate(context, R.layout.my_order_his_item_pj, null);
        }
        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_num = ViewHolder.get(convertView, R.id.tv_num);
        TextView tv_pingjia = ViewHolder.get(convertView, R.id.tv_pingjia);
        LinearLayout ll_root=ViewHolder.get(convertView, R.id.ll_order_root);
        tv_name.setText(list.get(position).pname);
        try {
            float realPrice = Float.parseFloat(list.get(position).price);
            tv_price.setText("￥" + decimalFormat.format(realPrice));
        } catch (Exception ex) {
            tv_price.setText("￥" + "0.00");
        }
        //tv_price.setText("￥" + list.get(position).price);
        tv_num.setText("×" + list.get(position).count);

        Uri uriStr = Uri.parse(Urls.PHOTO + list.get(position).path);
        my_image_view.setImageURI(uriStr);
        if (status.equals("3")) {
            if (list.get(position).isassess.equals("0")) {
                tv_pingjia.setText("去评价");
                tv_pingjia.setVisibility(View.VISIBLE);
            } else {
                tv_pingjia.setText("已评价");
                tv_pingjia.setVisibility(View.VISIBLE);
            }
        } else {
            tv_pingjia.setVisibility(View.GONE);
        }

        if("1".equals(list.get(position).iscut))
        {
            ll_root.setBackgroundColor(Color.GRAY);
        }
        else{
            ll_root.setBackgroundColor(Color.WHITE);
        }
        tv_pingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(WJSJApplication.getInstance().getSp().getValue(Constant.STATUS))) {
                    NewToast.show(context, "用户被禁用！", Toast.LENGTH_LONG);
                } else {
                    Intent intent = new Intent(context, MyEvaluateActivity.class);
                    intent.putExtra("orderInfo", (Serializable) list.get(position));
                    intent.putExtra("orderid", orderBean.id);
                    //context.startActivity(intent);
                    if("orderHis".equals(orderFlag)) {
                        ((MyOrderHisActivity) context).startActivityForResult(intent, 1000);
                    }
                    else{
                        ((MyOrderActivity) context).startActivityForResult(intent, 1000);
                    }
                }
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if("orderHis".equals(orderFlag)) {
                    Intent intent = new Intent(context, MyOrderActivity.class);
                    intent.putExtra(Constant.SCORE, status);
                    intent.putExtra("selcList", (Serializable) list);
                    intent.putExtra("selOrder", (Serializable) orderBean);
                    ((MyOrderHisActivity) context).startActivityForResult(intent, 1000);
                }
                else{
                    Intent intent = new Intent(context, ShopDeataisActivity.class);
                    intent.putExtra(Constant.ID, list.get(position).productid);
                    ((MyOrderActivity) context).startActivityForResult(intent, 1000);
                }
            }
        });

        return convertView;
    }
}
