package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.wjsj.Bean.StoreReview;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

/**
 * Created by Administrator on 2015/8/4 0004.
 */
public class ShopEvaluationAdapter extends BaseAdapter {
    private Context context;
    private List<StoreReview> list;

    public ShopEvaluationAdapter(Context context, List<StoreReview> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.evaluation_item, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        TextView tv_user = ViewHolder.get(convertView, R.id.tv_user);
        RatingBar rb_level = ViewHolder.get(convertView, R.id.rb_level);
        TextView tv_content = ViewHolder.get(convertView, R.id.tv_content);
        TextView tv_time = ViewHolder.get(convertView, R.id.tv_time);
        RelativeLayout rl_reply = ViewHolder.get(convertView, R.id.rl_reply);
        TextView tv_reply_content = ViewHolder.get(convertView, R.id.tv_reply_content);
        TextView tv_reply_time = ViewHolder.get(convertView, R.id.tv_reply_time);
        tv_user.setText(getName(list.get(position).nickname));
        rb_level.setRating(Float.parseFloat(list.get(position).star));
        tv_content.setText(list.get(position).content);
        tv_time.setText(list.get(position).createTime);
        if (TextUtils.isEmpty(list.get(position).content)) {
            rl_reply.setVisibility(View.GONE);
        } else {
            rl_reply.setVisibility(View.VISIBLE);
            tv_reply_content.setText(list.get(position).content);
            tv_reply_time.setText(list.get(position).replyTime);
        }
        return convertView;
    }

    private String getName(String name) {
        if (name.length() <= 1) {
            return "*";
        } else {
            String newName = name.substring(0, 1);
            for (int i = 0; i < name.length() - 1; i++) {
                newName += "*";
            }
            return newName;
        }

    }
}
