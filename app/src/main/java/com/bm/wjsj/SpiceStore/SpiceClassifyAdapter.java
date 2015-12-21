//package com.bm.wjsj.SpiceStore;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.provider.SyncStateContract;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bm.wjsj.Bean.ClassfiyBean;
//import com.bm.wjsj.Constans.Constant;
//import com.bm.wjsj.R;
//import com.bm.wjsj.Utils.ViewHolder;
//import com.daimajia.androidanimations.library.Techniques;
//import com.daimajia.androidanimations.library.YoYo;
//
//import java.util.List;
//
///**
// * Created by Administrator on 2015/7/30 0030.
// */
//public class SpiceClassifyAdapter extends BaseAdapter {
//    private Context mContext;
//    private List<ClassfiyBean> list;
//
//    public SpiceClassifyAdapter(Context mContext, List<ClassfiyBean> list) {
//        this.mContext = mContext;
//        this.list = list;
//    }
//
//    @Override
//    public int getCount() {
//        return list.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
//            convertView = View.inflate(mContext, R.layout.classfiy_itme, null);
//            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
//        }
//        ImageView iv_classfiy = ViewHolder.get(convertView, R.id.iv_classfiy);
//        TextView tv_classfiy = ViewHolder.get(convertView, R.id.tv_classfiy);
//        iv_classfiy.setImageResource(list.get(position).resource);
////        Drawable drawable = mContext.getResources().getDrawable(list.get(position).resource);
//
///// 这一步必须要做,否则不会显示.
////        convertView.setBackgroundResource(Constant.classfiybg[position]);
////        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//        tv_classfiy.setTextColor(mContext.getResources().getColor(Constant.classfiytextfont[position]));
////        tv_classfiy.setCompoundDrawables(null, null, drawable, null);
//        tv_classfiy.setText(list.get(position).name);
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent inent = new Intent(mContext, SecondClassifyActivity.class);
//                mContext.startActivity(inent);
//            }
//        });
//
//        return convertView;
//    }
//}
