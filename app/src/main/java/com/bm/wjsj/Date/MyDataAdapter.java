package com.bm.wjsj.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.MyDynamicListBean;
import com.bm.wjsj.Circle.ImageGridAdapter;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Dynamic.DynamicDetailActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.NoScrollGridView;

import java.util.List;

/**
 * TA的动态Adapter
 */
public class MyDataAdapter extends BaseAdapter {
    private Activity mContext;
    private List<MyDynamicListBean> list;
    private MyDataActivity ac;
    private String id;
//    private String id;
//    public boolean isDingtai;

    public MyDataAdapter(Activity mContext,List<MyDynamicListBean> list,String id) {
        this.mContext = mContext;
        ac = (MyDataActivity) mContext;
        this.list = list;
        this.id = id;
//        list.addAll(ac.getImageList(Constant.imageUrls, 9));
//        this.id = id;
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

//    public void changTab(boolean isDingtai) {
//        this.isDingtai = isDingtai;
//        notifyDataSetChanged();
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.lv_item_mydynamic, null);
        }
        NoScrollGridView gridView = ViewHolder.get(convertView, R.id.gv_tupian);
        TextView tv_april = ViewHolder.get(convertView, R.id.tv_april);//月
        TextView tv_day = ViewHolder.get(convertView, R.id.tv_day);//日
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);
        View view_1 = ViewHolder.get(convertView, R.id.view_1);
        View view_2 = ViewHolder.get(convertView, R.id.view_2);
        TextView tv_zan_num = ViewHolder.get(convertView, R.id.tv_zan_num);
        TextView tv_review = ViewHolder.get(convertView, R.id.tv_review);
        TextView tv_jubao = ViewHolder.get(convertView, R.id.tv_jubao);
        try {
            String april = list.get(position).createTime.substring(5,7);
            String day = list.get(position).createTime.substring(8,10);
            tv_day.setText(day);
            switch (april){
                case "1":
                    tv_april.setText("一月");
                    break;
                case "2":
                    tv_april.setText("二月");
                    break;
                case "3":
                    tv_april.setText("三月");
                    break;
                case "4":
                    tv_april.setText("四月");
                    break;
                case "5":
                    tv_april.setText("五月");
                    break;
                case "6":
                    tv_april.setText("六月");
                    break;
                case "7":
                    tv_april.setText("七月");
                    break;
                case "8":
                    tv_april.setText("八月");
                    break;
                case "9":
                    tv_april.setText("九月");
                    break;
                case "10":
                    tv_april.setText("十月");
                    break;
                case "11":
                    tv_april.setText("十一月");
                    break;
                case "12":
                    tv_april.setText("十二月");
                    break;
                default:
                    tv_april.setText("");
                    break;
            }
            if (list.get(position).imglist.size() == 1 ||
                    list.get(position).imglist.size() == 4 ||
                    list.get(position).imglist.size() == 7) {
                view_2.setVisibility(View.VISIBLE);
                view_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, DynamicDetailActivity.class);
                        intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                        //intent.putExtra(Constant.ID, list.get(position).id);
                        mContext.startActivity(intent);
                    }
                });
            } else if (list.get(position).imglist.size() == 2 ||
                    list.get(position).imglist.size() == 5 ||
                    list.get(position).imglist.size() == 8) {
                view_1.setVisibility(View.VISIBLE);
                view_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, DynamicDetailActivity.class);
                        intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                        //intent.putExtra(Constant.ID, list.get(position).id);
                        mContext.startActivity(intent);
                    }
                });
            }
            ImageGridAdapter adapter = new ImageGridAdapter(mContext, list.get(position).imglist);
            gridView.setAdapter(adapter);
            tv_content.setText(list.get(position).content);
            tv_zan_num.setText(list.get(position).praisenum);
            tv_review.setText(list.get(position).comnum);
        }catch (Exception e){

        }
        tv_jubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AccusationActivity.class);
                mContext.startActivity(intent);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                //intent.putExtra(Constant.ID,list.get(position).id);
                mContext.startActivity(intent);

            }
        });
        return convertView;
    }
}
