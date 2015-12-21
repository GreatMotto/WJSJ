package com.bm.wjsj.Personal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.wjsj.Bean.MyDynamicListBean;
import com.bm.wjsj.Circle.ImageGridAdapter;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Dynamic.DynamicDetailActivity;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.CommentUtils;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.NoScrollGridView;
import com.bm.wjsj.View.ViewPagerDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;

/**
 * 我的动态适配器
 */
public class MyDynamicAdapter extends BaseAdapter {

    private Activity mContext;
    private List<MyDynamicListBean> list;
    private String dataUserId;

    public MyDynamicAdapter(Activity context, List<MyDynamicListBean> list,String dataUserId) {
        super();
        this.mContext = context;
        this.list = list;
        this.dataUserId=dataUserId;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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
            convertView = View.inflate(mContext, R.layout.lv_item_mydynamic, null);
        }
        TextView tv_day = ViewHolder.get(convertView, R.id.tv_day);
        TextView tv_month = ViewHolder.get(convertView, R.id.tv_month);
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);
        TextView tv_love = ViewHolder.get(convertView, R.id.tv_love);
        TextView tv_review = ViewHolder.get(convertView, R.id.tv_review);
        View view_1 = ViewHolder.get(convertView, R.id.view_1);
        View view_2 = ViewHolder.get(convertView, R.id.view_2);
        NoScrollGridView gridView = ViewHolder.get(convertView, R.id.gridview);
        ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);

        String[] date = CommentUtils.getDate(list.get(position).createTime);
        tv_day.setText(date[2]);
        tv_month.setText(date[1]);
        tv_content.setText(list.get(position).content);
        tv_love.setText(list.get(position).praisenum);
        tv_review.setText(list.get(position).comnum);

        if (list.get(position).imglist.size() == 1) {
            gridView.setVisibility(View.GONE);
            iv_image.setVisibility(View.VISIBLE);
            view_1.setVisibility(View.GONE);
            view_2.setVisibility(View.GONE);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            ImageLoader.getInstance().displayImage(Urls.PHOTO + list.get(position).imglist.get(0).path, iv_image, options);
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewPagerDialog dlg = new ViewPagerDialog(mContext, list.get(position).imglist, position);
                    dlg.showViewPagerDialog();
                }
            });

        } else if (list.get(position).imglist.size() == 4 ||
                list.get(position).imglist.size() == 7) {
            iv_image.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            view_1.setVisibility(View.GONE);
            view_2.setVisibility(View.VISIBLE);
            ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, list.get(position).imglist);
            gridView.setAdapter(gridAdapter);
            view_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, DynamicDetailActivity.class);
                    intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                    intent.putExtra(Constant.DATAUSERID, dataUserId);
                    //intent.putExtra(Constant.ID, list.get(position).id);
                    intent.putExtra(Constant.LIST, (Serializable) list.get(position).imglist);
                    mContext.startActivity(intent);
                }
            });
        } else if (list.get(position).imglist.size() == 2 ||
                list.get(position).imglist.size() == 5 ||
                list.get(position).imglist.size() == 8) {
            iv_image.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            view_1.setVisibility(View.VISIBLE);
            view_2.setVisibility(View.GONE);
            ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, list.get(position).imglist);
            gridView.setAdapter(gridAdapter);
            view_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, DynamicDetailActivity.class);
                    intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                    intent.putExtra(Constant.DATAUSERID, dataUserId);
                    //intent.putExtra(Constant.ID, list.get(position).id);
                    intent.putExtra(Constant.LIST, (Serializable) list.get(position).imglist);
                    mContext.startActivity(intent);
                }
            });
        }else {
            iv_image.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
            view_1.setVisibility(View.GONE);
            view_2.setVisibility(View.GONE);
            ImageGridAdapter gridAdapter = new ImageGridAdapter(mContext, list.get(position).imglist);
            gridView.setAdapter(gridAdapter);
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DynamicDetailActivity.class);
                //intent.putExtra(Constant.ID, list.get(position).id);
                intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                intent.putExtra(Constant.DATAUSERID, dataUserId);
                intent.putExtra(Constant.LIST, (Serializable) list.get(position).imglist);
                intent.putExtra(Constant.BOOLEAN, true);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }

}
