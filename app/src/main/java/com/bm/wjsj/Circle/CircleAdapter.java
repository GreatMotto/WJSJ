package com.bm.wjsj.Circle;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.CircleBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * 圈子列表适配器
 */
public class CircleAdapter extends BaseAdapter implements APICallback.OnResposeListener{

    private Context mContext;
    private List<CircleBean> list;
    private boolean isMyCircle;
    JoinClickInterface joinClickInterface;

    public CircleAdapter(Context context, List<CircleBean> list, boolean isMyCircle, JoinClickInterface joinClickInterface) {
        super();
        this.mContext = context;
        this.list = list;
        this.isMyCircle = isMyCircle;
        this.joinClickInterface = joinClickInterface;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
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
            convertView = View.inflate(mContext, R.layout.lv_item_circle, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        final SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
        my_image_view.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).image));
        TextView tv_circle_title = ViewHolder.get(convertView, R.id.tv_circle_title);
        TextView tv_circle_join = ViewHolder.get(convertView, R.id.tv_circle_join);
        TextView tv_circle_discrip = ViewHolder.get(convertView, R.id.tv_circle_discrip);
        if (isMyCircle) {
            tv_circle_join.setVisibility(View.GONE);
        }
        tv_circle_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                WebServiceAPI.getInstance().addCircle(list.get(position).id, CircleAdapter.this, mContext);
                joinClickInterface.joinClick(v, position);
            }
        });
        tv_circle_discrip.setText(list.get(position).description);
        tv_circle_title.setText(list.get(position).name);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() == 0){
                    WebServiceAPI.getInstance().circleMy(CircleAdapter.this,mContext);
                }
                Intent intent = new Intent();
                intent.setClass(mContext, CircleDetailActivity.class);
                intent.putExtra(Constant.BOOLEAN, isMyCircle);
                intent.putExtra("isEssence", true);
                intent.putExtra(Constant.URL, list.get(position));
                intent.putExtra("circleId", list.get(position).id);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")){
            if (list.size() == 0){
                list.addAll(apiResponse.data.list);
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

    public interface JoinClickInterface{
        public void joinClick(View v, int position);
    }
}
