package com.bm.wjsj.Circle;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.ViewPagerDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * GridView图片显示适配器
 */
public class ImageGridAdapter extends BaseAdapter {
    private Activity mContext;
    private List<ImageBean> list;
    private int count = 0;

    public ImageGridAdapter(Activity context, List<ImageBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        if (observer != null) {
            super.unregisterDataSetObserver(observer);
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.lv_item_image, null);
        }
        ImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
//        SimpleDraweeView my_image_view = ViewHolder.get(convertView, R.id.my_image_view);
//        my_image_view.setAspectRatio(1.0f);
//        my_image_view.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
//        if ("***".equals(list.get(position).path)) {
//            iv_image.setBackgroundResource(R.color.transparent);
//            iv_image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setClass(mContext, DynamicDetailActivity.class);
//                    intent.putExtra(Constant.ID, list.get(position).id);
////                ActivityTransitionLauncher.with((Activity) mContext)
////                        .from(v).launch(intent);
//                    mContext.startActivity(intent);
//                }
//            });
//            return convertView;
//        }

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.PHOTO + list.get(position).path, iv_image, options);

        iv_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewPagerDialog dlg = new ViewPagerDialog(mContext, list, position);
                dlg.showViewPagerDialog();
            }
        });
        return convertView;
    }
}
