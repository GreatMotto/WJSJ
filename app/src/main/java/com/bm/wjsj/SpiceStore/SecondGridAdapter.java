package com.bm.wjsj.SpiceStore;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.wjsj.Bean.StoreListBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.Personal.MyCollectionActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AdapterClickInterface;
import com.bm.wjsj.Utils.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 商品列表 gridview adapter
 * Created by Administrator on 2015/7/31 0031.
 */
public class SecondGridAdapter extends BaseAdapter {
    private Context context;
    private List<StoreListBean> list;
    private boolean isScore;//积分商品兑换确认界面
    private boolean isCollection;
    private boolean jumpCollection;
    AdapterClickInterface adapterClickInterface;
    private static final int RETURN_REFRESH_CODE=11;

    public SecondGridAdapter(Context context, List<StoreListBean> list, boolean isScore) {
        this.context = context;
        this.list = list;
        this.isScore = isScore;
    }

    public SecondGridAdapter(Context context, List<StoreListBean> list) {
        this.context = context;
        this.list = list;
    }

    public SecondGridAdapter(Context context, List<StoreListBean> list, boolean isScore, AdapterClickInterface adapterClickInterface,boolean jumpCollection) {
        super();
        this.context = context;
        this.list = list;
        this.isScore = isScore;
        this.adapterClickInterface = adapterClickInterface;
        this.jumpCollection=jumpCollection;
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
            convertView = View.inflate(context, R.layout.second_grid_item, null);
        }
        SimpleDraweeView iv_shop_image = ViewHolder.get(convertView, R.id.iv_shop_image);
        TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
        TextView tv_buynum = ViewHolder.get(convertView, R.id.tv_buynum);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_stock = ViewHolder.get(convertView, R.id.tv_stock);
        TextView tv_spec = ViewHolder.get(convertView, R.id.tv_spec);
        ImageView iv_delete = ViewHolder.get(convertView, R.id.iv_delete);

        iv_shop_image.setAspectRatio(1.0f);
        iv_shop_image.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
        tv_name.setText(list.get(position).name);
        if (isScore) {
            tv_stock.setVisibility(View.VISIBLE);
            tv_spec.setVisibility(View.VISIBLE);
            tv_stock.setText(list.get(position).stock + "库存");
            tv_spec.setText(list.get(position).spec);
//            tv_stock.setText(list.get(position).count + "库存");
            tv_price.setText(list.get(position).score + "积分");
            tv_buynum.setText(String.valueOf(list.get(position).ordernum) + "人购买");
        } else {
            tv_stock.setVisibility(View.GONE);
            tv_spec.setVisibility(View.GONE);
            if("2".equals(list.get(position).isEMS))
            {
                tv_price.setText("¥" + list.get(position).price+"(包邮)");
            }else {
                tv_price.setText("¥" + list.get(position).price);
            }
            tv_buynum.setText(String.valueOf(list.get(position).salenum) + "人购买");
        }
        if (isCollection) {

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(jumpCollection)
                {
                    Intent inent = new Intent(context, ShopDeataisActivity.class);
                    Log.e("233333","113");
                    inent.putExtra(Constant.ID, list.get(position).productid);
                    inent.putExtra(Constant.SCORE, 0);
                    context.startActivity(inent);
                }
                else {
                    Intent inent = new Intent(context, ShopDeataisActivity.class);
                    Log.e("233333","120");
                    inent.putExtra(Constant.ID, list.get(position).id);
                    if (isScore) {
                        Log.e("233333","123");
                        inent.putExtra(Constant.SCORE, 1);
                        inent.putExtra(Constant.SPEC,list.get(position).spec);
                        ((Activity)context).startActivityForResult(inent, RETURN_REFRESH_CODE);
                    } else {
                        Log.e("233333","127");
                        inent.putExtra(Constant.SCORE, 0);
                        context.startActivity(inent);
                    }

                }

            }
        });
        //删除我的收藏
        if (isCollection) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.GONE);
        }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adapterClickInterface.onClick(v, null, list.get(position).productid);
                WebServiceAPI.getInstance().deleteCollect(list.get(position).productid,(MyCollectionActivity)context, context);
                list.remove(position);
                SecondGridAdapter.this.notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public void setCollection(boolean isCollection) {
        this.isCollection = isCollection;
    }

}
