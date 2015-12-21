package com.bm.wjsj.MyMsg;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.wjsj.Base.ViewActivity;
import com.bm.wjsj.Bean.MessageBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.SwipeListView;
import com.bm.wjsj.View.SwipeListView.IOnCustomClickListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * @author Haiheng.Wu@Bluemobi.cn
 * @Description
 * @time 2015/5/13 16:34
 * @return 说明返回值含义
 */
public class SystemMsgAdapter extends BaseAdapter{


    private Context context;
    private List<MessageBean> list;
    private SwipeListView mListView;
    private IOnCustomClickListener mListener = null;
    private String title;
    private String type;


    public SystemMsgAdapter(Context context, SwipeListView mListView,
                            List<MessageBean> list, IOnCustomClickListener mListener, String title, String type) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        this.mListener = mListener;
        this.mListView = mListView;
        this.title = title;
        this.type = type;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_systemmsg,
                    null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        RelativeLayout item_right = ViewHolder.get(convertView, R.id.item_right);
        ImageView iv_icon = ViewHolder.get(convertView, R.id.iv_icon);
        SimpleDraweeView sdv_pic = ViewHolder.get(convertView, R.id.sdv_pic);
        TextView tv_msg_title = ViewHolder.get(convertView, R.id.tv_msg_title);
        TextView tv_msg_content = ViewHolder.get(convertView, R.id.tv_msg_content);
        TextView tv_msg_time = ViewHolder.get(convertView, R.id.tv_msg_time);
        tv_msg_title.setText(title);
        tv_msg_content.setText(list.get(position).title);
        tv_msg_time.setText(list.get(position).createTime.substring(0, 11));
        if (title.equals("关注消息")) {
            sdv_pic.setVisibility(View.VISIBLE);
            iv_icon.setVisibility(View.GONE);
            if (TextUtils.isEmpty(list.get(position).head)) {
                if (list.get(position).sex.equals("0")) {
                    sdv_pic.setImageResource(R.mipmap.touxiangnan);
                } else {
                    sdv_pic.setImageResource(R.mipmap.touxiangnv);
                }
            } else {
                sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).head));
            }
        } else {
            sdv_pic.setVisibility(View.GONE);
            iv_icon.setVisibility(View.VISIBLE);
            iv_icon.setImageResource(R.mipmap.ic_launcher);
        }
        final View cview = convertView;
        LinearLayout.LayoutParams paramRight = new LinearLayout.LayoutParams(mListView.getRightViewWidth(),
                LinearLayout.LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(paramRight);
        item_right.setOnClickListener(new View.OnClickListener() {

            // 左滑删除
            @Override
            public void onClick(View v) {
                mListView.hiddenRight(cview);
                if (mListener != null) {
                    mListener.onDeleteClick(v, position);
                }
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("2")) {
                    Intent intent = new Intent(context, MyDataActivity.class);
                    intent.putExtra(Constant.ID, list.get(position).touserid);
                    context.startActivity(intent);
                }
                if (type.equals("1")) {
                    Intent intent = new Intent(context, ViewActivity.class);
                    intent.putExtra(Constant.ID, list.get(position).id);
                    context.startActivity(intent);
                }
            }
        });
        return convertView;
    }
}
