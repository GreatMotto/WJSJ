package com.bm.wjsj.Circle;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.wjsj.Bean.PostBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DateUtil;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子列表adapter
 */
public class PostAdapter extends BaseAdapter {

    private Context mContext;
    private List<PostBean> list = new ArrayList<>();
    boolean isRecommend;//是否推荐
    boolean isMine;//是否是我的帖子
    private String id;
    boolean isname;//是否是TA的帖子

    public PostAdapter(Context context, List<PostBean> list, boolean isMine, String id, boolean isname) {
        super();
        this.mContext = context;
        this.list = list;
        this.isMine = isMine;
        this.id = id;
        this.isname = isname;
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
    public void unregisterDataSetObserver(DataSetObserver observer) {
        try {
            if (observer != null)
                super.unregisterDataSetObserver(observer);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.lv_item_post, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        SimpleDraweeView sdv_pic = ViewHolder.get(convertView, R.id.sdv_pic);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_level = ViewHolder.get(convertView, R.id.tv_level);
        ImageView iv_recommemd = ViewHolder.get(convertView, R.id.iv_recommemd);
        TextView tv_post_title = ViewHolder.get(convertView, R.id.tv_post_title);
        TextView tv_post_time = ViewHolder.get(convertView, R.id.tv_post_time);
        TextView tv_review = ViewHolder.get(convertView, R.id.tv_review);
        TextView tv_love = ViewHolder.get(convertView, R.id.tv_love);

        if (isMine) {
            tv_name.setVisibility(View.GONE);
            tv_level.setVisibility(View.GONE);
        }else {
            PostBean pb = list.get(position);
            if(pb.isrecommend!=null) {
                if (list.get(position).isrecommend.equals("1")) {
                    isRecommend = true;
                } else {
                    isRecommend = false;
                }
            }
        }
        if (isname) {
            tv_name.setVisibility(View.GONE);
        }
        if (isRecommend) {
            iv_recommemd.setVisibility(View.VISIBLE);
        }else {
            iv_recommemd.setVisibility(View.GONE);
        }
        if (!list.get(position).firstimg.equals("")) {
            sdv_pic.setVisibility(View.VISIBLE);
            sdv_pic.setAspectRatio(150 / 100f);
            sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).firstimg));
        } else {
            sdv_pic.setVisibility(View.GONE);
        }
        tv_post_time.setText(list.get(position).createTime);
        tv_review.setText(list.get(position).comnum);
        tv_post_title.setText(list.get(position).title, TextView.BufferType.EDITABLE);
        tv_name.setText(list.get(position).nickname);
        tv_post_title.setText(list.get(position).title, TextView.BufferType.EDITABLE);
        tv_post_time.setText(DateUtil.getDate(list.get(position).createTime));
        tv_review.setText(list.get(position).comnum);
        tv_love.setText(list.get(position).praisenum);
        tv_level.setText("V" + list.get(position).level);
        tv_name.setText(list.get(position).nickname);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, PostDetailActivity.class);
                intent.putExtra(Constant.POSTID, list.get(position).id);
                intent.putExtra(Constant.ID, id);
                intent.putExtra(Constant.BOOLEAN, isMine);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

}
