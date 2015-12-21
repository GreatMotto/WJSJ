package com.bm.wjsj.Dynamic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bm.wjsj.Bean.DynamicListBean;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Circle.ImageGridAdapter;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Date.MyDataActivity;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DateUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.NoScrollGridView;
import com.bm.wjsj.View.ViewPagerDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 动态列表适配器
 */
public class DynamicAdapter extends BaseAdapter implements APICallback.OnResposeListener {

    private Activity mContext;
    private List<DynamicListBean> list;
    private ListView listview;
    private List<ImageBean> imglistTest;

    public DynamicAdapter(Activity context, List<DynamicListBean> list, ListView listview) {
        super();
        this.mContext = context;
        this.list = list;
        this.listview = listview;
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
            convertView = View.inflate(mContext, R.layout.lv_item_dynamic, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
        }
        SimpleDraweeView sdv_pic = ViewHolder.get(convertView, R.id.sdv_pic);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_level = ViewHolder.get(convertView, R.id.tv_level);
        TextView tv_sex = ViewHolder.get(convertView, R.id.tv_sex);
        TextView tv_age = ViewHolder.get(convertView, R.id.tv_age);
        TextView tv_dynamic_distance = ViewHolder.get(convertView, R.id.tv_dynamic_distance);
        TextView tv_dynamic_time = ViewHolder.get(convertView, R.id.tv_dynamic_time);
        TextView tv_dynamic_content = ViewHolder.get(convertView, R.id.tv_dynamic_content);
        TextView tv_review = ViewHolder.get(convertView, R.id.tv_review);
        TextView tv_love = ViewHolder.get(convertView, R.id.tv_love);
        TextView tv_loved = ViewHolder.get(convertView, R.id.tv_loved);
        NoScrollGridView gridView = ViewHolder.get(convertView, R.id.gridview);
        ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
        View view_1 = ViewHolder.get(convertView, R.id.view_1);
        View view_2 = ViewHolder.get(convertView, R.id.view_2);
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
                    //intent.putExtra(Constant.ID, list.get(position).id);
                    intent.putExtra(Constant.DYNAMICID, list.get(position).id);
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

        if (!list.get(position).head.equals("")) {
            sdv_pic.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).head));
        } else {
            if (list.get(position).sex.equals("0")) {
                sdv_pic.setImageResource(R.mipmap.touxiangnan);
            } else {
                sdv_pic.setImageResource(R.mipmap.touxiangnv);
            }
        }
        sdv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyDataActivity.class);
                intent.putExtra(Constant.ID, list.get(position).userid);
                mContext.startActivity(intent);
            }
        });
        tv_dynamic_content.setText(list.get(position).content);
        if (list.get(position).praise.equals("0")) {
            tv_love.setVisibility(View.VISIBLE);
            tv_loved.setVisibility(View.GONE);
            tv_love.setText(list.get(position).praisenum);
        } else {
            tv_love.setVisibility(View.GONE);
            tv_loved.setVisibility(View.VISIBLE);
            tv_loved.setText(list.get(position).praisenum);
        }
        tv_love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("list.get(position).id", list.get(position).id);
                WebServiceAPI.getInstance().addPraise(list.get(position).id, "2", DynamicAdapter.this, mContext);
                // 更新界面
                updateItemData(list.get(position));
            }
        });
        tv_loved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WebServiceAPI.getInstance().deletePraise(list.get(position).id, "2", DynamicAdapter.this, mContext);
                // 更新界面
                updateItemData(list.get(position));
            }
        });
        tv_review.setText(list.get(position).comnum);
        tv_name.setText(list.get(position).nickname);
        if (list.get(position).sex.equals("0")) {
            tv_sex.setText("帅哥");
        } else {
            tv_sex.setText("美女");
        }
        tv_age.setText(list.get(position).age + "岁");
        tv_level.setText("V" + list.get(position).level);
        tv_dynamic_time.setText(DateUtil.getDate(list.get(position).createTime));
        if (!TextUtils.isEmpty(list.get(position).juli)) {
            double distance = Double.parseDouble(list.get(position).juli);
            if (distance >= 1000.0) {
                distance = distance / 1000.0;
                BigDecimal bigDecimal = new BigDecimal(distance);
                int juli = bigDecimal.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
                tv_dynamic_distance.setText(juli + "km");
            } else if (distance < 1000.00 && distance > 10.00) {
                distance = distance / 1000.0;
                BigDecimal bigDecimal = new BigDecimal(distance);
                double juli = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                tv_dynamic_distance.setText(juli + "km");
            } else {
                tv_dynamic_distance.setText(0.01 + "km");
            }
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewToast.show(mContext, "点击", NewToast.LENGTH_SHORT);
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DynamicDetailActivity.class);
                intent.putExtra(Constant.DYNAMICID, list.get(position).id);
                //intent.putExtra(Constant.ID, list.get(position).id);
                intent.putExtra(Constant.LIST, (Serializable) list.get(position).imglist);
//                ActivityTransitionLauncher.with((Activity) mContext)
//                        .from(v).launch(intent);
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

    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

    private String getPraisenum(String praise, String oldPrisenum) {
        int pNum = 0;
        try {

            pNum = Integer.parseInt(oldPrisenum);
            if ("0".equals(praise)) {
                pNum++;
            } else {
                pNum--;
            }
        } catch (Exception ex) {

        }
        return "" + pNum;
    }

    @SuppressLint("HandlerLeak")
    private Handler han = new Handler() {
        public void handleMessage(Message msg) {
            updateItem(msg.arg1);
        }

        ;
    };

    /**
     * 刷新指定item
     *
     * @param index item在listview中的位置
     */
    private void updateItem(int index) {
        if (listview == null) {
            return;
        }

        // 获取当前可以看到的item位置
        int visiblePosition = listview.getFirstVisiblePosition();
        // 如添加headerview后 firstview就是hearderview
        // 所有索引+1 取第一个view
        //View view = listview.getChildAt(index - visiblePosition + 1);
        // 获取点击的view
        View view = listview.getChildAt(index - visiblePosition);
        //TextView txt = (TextView) view.findViewById(R.id.tv_love);
        // 获取mDataList.set(ids, item);更新的数据
        DynamicListBean data = (DynamicListBean) getItem(index);
        // 重新设置界面显示数据
        //txt.setText(data.getData());
        //txt.setText(data.praisenum);

        if (data.praise.equals("0")) {
            TextView txtLove = (TextView) view.findViewById(R.id.tv_love);
            TextView txtLoved = (TextView) view.findViewById(R.id.tv_loved);
            txtLoved.setVisibility(View.GONE);
            txtLove.setVisibility(View.VISIBLE);
            txtLove.setText(data.praisenum);

        } else {
            TextView txtLove = (TextView) view.findViewById(R.id.tv_love);
            TextView txtLoved = (TextView) view.findViewById(R.id.tv_loved);
            txtLove.setVisibility(View.GONE);
            txtLoved.setVisibility(View.VISIBLE);
            txtLoved.setText(data.praisenum);

        }
        //Log.e("itemAfter:",data.praisenum+"^^praiseAfter:"+data.praise);
    }


    /**
     * update listview 单条数据
     *
     * @param item 新数据对象
     */
    public void updateItemData(DynamicListBean item) {
        //Log.e("itemBefore:",item.praisenum+"^^praiseBefore:"+item.praise);
        item.praisenum = getPraisenum(item.praise, item.praisenum);
        item.praise = item.praise.equals("0") ? "1" : "0";
        Message msg = Message.obtain();
        int ids = -1;
        // 进行数据对比获取对应数据在list中的位置
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == item.id) {
                ids = i;
            }
        }
        msg.arg1 = ids;
        // 更新mDataList对应位置的数据
        list.set(ids, item);
        // handle刷新界面
        han.sendMessage(msg);

    }
}
