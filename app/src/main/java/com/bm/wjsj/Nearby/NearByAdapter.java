package com.bm.wjsj.Nearby;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
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
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Administrator on 2015/7/27 0027.
 *
 */
public class NearByAdapter extends BaseAdapter {
    private Context mContext;
    private List<UserInfo> list;
    private boolean isAttention;

    public static String userId,userName,userHead;

    public NearByAdapter(Context context, List<UserInfo> list, boolean isAttention) {
        super();
        this.mContext = context;
        this.list = list;
        this.isAttention = isAttention;
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
        int version = Build.VERSION.SDK_INT;
        if (convertView == null && version < 21) {
            convertView = View.inflate(mContext, R.layout.nearby_item, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }else if (convertView == null && version >= 21){
            convertView = View.inflate(mContext, R.layout.nearby_item_5, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
            Log.e("version:-----","" + version);
        }
        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        TextView tv_age = ViewHolder.get(convertView, R.id.tv_age);
        TextView person_name = ViewHolder.get(convertView, R.id.person_name);
//        my_image_view.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).head));
        if (!list.get(position).head.equals("")) {
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(7f).setRoundingMethod(RoundingParams.RoundingMethod.OVERLAY_COLOR).setOverlayColor(Color.WHITE);
            my_image_view.getHierarchy().setRoundingParams(roundingParams);
            my_image_view.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).head));
        }else {
            if (list.get(position).sex.equals("0")){
                my_image_view.setImageResource(R.mipmap.touxiangnan);
            }else {
                my_image_view.setImageResource(R.mipmap.touxiangnv);
            }
        }
//        TextView tv_love = ViewHolder.get(convertView, R.id.tv_love);
        TextView tv_level = ViewHolder.get(convertView, R.id.tv_level);
        TextView tv_sex = ViewHolder.get(convertView, R.id.tv_sex);
        TextView tv_discrip = ViewHolder.get(convertView, R.id.tv_discrip);
        TextView tv_distance = ViewHolder.get(convertView, R.id.tv_distance);

//        tv_love.setVisibility(isAttention ? View.GONE : View.VISIBLE);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inent = new Intent(mContext, MyDataActivity.class);
                inent.putExtra(Constant.ID, list.get(position).id);

                userId = list.get(position).id;
                userHead = list.get(position).head;
                userName = list.get(position).nickname;

                mContext.startActivity(inent);
            }
        });
//        tv_age.setText(list.get(position).age + "岁");
        tv_age.setText(TextUtils.isEmpty(list.get(position).age) ?
                "0" + "岁" : list.get(position).age + "岁");
        person_name.setText(list.get(position).nickname);
        tv_level.setText("V" + list.get(position).level);
        if (list.get(position).sex.equals("0")) {
            tv_sex.setText("帅哥");
        }else {
            tv_sex.setText("美女");
        }
        tv_discrip.setText(TextUtils.isEmpty(list.get(position).sign) ?
                "暂无签名！" : list.get(position).sign);
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
//        tv_distance.setText(list.get(position).distance + "km");
//        tv_love.setText(list.get(position).distance);

        return convertView;
    }
}
