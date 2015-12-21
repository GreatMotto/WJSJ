package com.bm.wjsj.MyMsg;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.SwipeListView;
import com.bm.wjsj.View.SwipeListView.IOnCustomClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * @author Haiheng.Wu@Bluemobi.cn
 * @Description
 * @time 2015/5/13 16:34
 * @return 说明返回值含义
 */
public class ImMsgAdapter extends BaseAdapter {


    private Context context;
    private List<ImageBean> list;
    private SwipeListView mListView;
    private IOnCustomClickListener mListener = null;

    public ImMsgAdapter(Context context, SwipeListView mListView,
                        List<ImageBean> list, IOnCustomClickListener mListener) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.list = list;
        this.mListener = mListener;
        this.mListView = mListView;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.lv_item_immsg,
                    null);
        }
        View item_right = ViewHolder.get(convertView, R.id.item_right);
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
        SimpleDraweeView sdv_pic = ViewHolder.get(convertView, R.id.sdv_pic);
        sdv_pic.setImageURI(Uri.parse(list.get(position).path));
        TextView tv_msg_num = ViewHolder.get(convertView, R.id.tv_msg_num);
        TextView tv_msg_name = ViewHolder.get(convertView, R.id.tv_msg_name);
        TextView tv_msg_content = ViewHolder.get(convertView, R.id.tv_msg_content);
        TextView tv_msg_time = ViewHolder.get(convertView, R.id.tv_msg_time);

        return convertView;
    }

}
