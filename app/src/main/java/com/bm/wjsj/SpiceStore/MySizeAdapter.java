package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bm.wjsj.Bean.SizeBean;
import com.bm.wjsj.Bean.SpecBean;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2015/8/10 0010.
 */
public class MySizeAdapter extends BaseAdapter {
    private Context context;
    //    private List<SizeBean> list;
//    public SizeBean sizeBean;
    public SpecBean spec;
    public int selectText;

    private OnSizeClickListener mOnSizeClickListener;

    public interface OnSizeClickListener {
        public void onSizeClicked(int itemPosition, Object dataObject);
    }

    public void setOnSizeClickListener(OnSizeClickListener onSizeClickListener) {
        this.mOnSizeClickListener = onSizeClickListener;
    }
//    private List<SpecBean> spec;

    public MySizeAdapter(Context context, SpecBean spec) {
        this.context = context;
//        this.list = list;
        this.spec = spec;

    }


    @Override
    public int getCount() {
        return spec.specList.size();
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
            convertView = View.inflate(context, R.layout.size_itme, null);
            for (int i = 0; i < spec.specList.size(); i++) {
                if (i == 0) {
                    spec.specList.get(i).isSelect = true;
                    selectText=0;

                } else {
                    spec.specList.get(i).isSelect = false;
                }
            }
        }
        final TextView tv_size = ViewHolder.get(convertView, R.id.tv_size);
        if (spec.specList.get(position).isSelect) {
            tv_size.setBackgroundResource(R.mipmap.size_chek);
            tv_size.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            tv_size.setBackgroundResource(R.mipmap.size_uncheck);
            tv_size.setTextColor(context.getResources().getColor(R.color.deafaut_text));
        }
        tv_size.setText(spec.specList.get(position).name);
        tv_size.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < spec.specList.size(); i++) {
                    if (i == position) {
                        spec.specList.get(i).isSelect = true;
                        selectText = i;
                        if (mOnSizeClickListener != null)
                            mOnSizeClickListener.onSizeClicked(i,
                                    i);
                    } else {
                        spec.specList.get(i).isSelect = false;
                    }
                }
                MySizeAdapter.this.notifyDataSetChanged();
            }
        });
//        if (position == 0) {
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                tv_size.callOnClick();
//            } else {
//                tv_size.performClick();
//            }
//            MySizeAdapter.this.notifyDataSetChanged();
//        }

        return convertView;
    }
}
