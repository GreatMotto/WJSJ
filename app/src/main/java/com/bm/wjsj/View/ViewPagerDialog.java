package com.bm.wjsj.View;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.ViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.polites.android.GestureImageView;

import java.util.List;

/**
 * @author
 * @Description loading
 * @time 2014-11-12 下午3:53:11
 */
public class ViewPagerDialog implements OnTouchListener {

    List<ImageBean> list;
    public Dialog dlg;
    Activity context;
    ViewPager viewpager;
    int position;
    private int x;

    // public ViewPagerDialog(Context context) {
    // super(context);
    // // TODO Auto-generated constructor stub
    // }

    public ViewPagerDialog(Activity context, List<ImageBean> list, int position) {
        // TODO Auto-generated constructor stub
        this.list = list;
        this.context = context;
        this.position = position;
    }

    public void showViewPagerDialog() {
        dlg = new Dialog(context, R.style.MyDialogStyle);

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dlg_viewpager);
        WindowManager manager = context.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int height = display.getHeight();
        int width = display.getWidth();
        window.setLayout(width, height);
        dlg.show();
        viewpager = (ViewPager) window.findViewById(R.id.viewpager);
        MyViewPagerAdapter VPadapter = new MyViewPagerAdapter(list, context);
        viewpager.setAdapter(VPadapter);
        viewpager.setCurrentItem(position);
        // viewpager.setOnPageChangeListener(new MyPageChangeListener(context,
        // list));
        viewpager.setOnTouchListener(this);
    }

    class MyViewPagerAdapter extends PagerAdapter {

        List<ImageBean> mList = null;
        Context context;

        public MyViewPagerAdapter(List<ImageBean> list, Context context) {
            mList = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View convertView = LayoutInflater.from(context).inflate(R.layout.list_item, null);
//            SimpleDraweeView sdv = ViewHolder.get(convertView, R.id.my_image_view);
//            sdv.setAspectRatio(1.0f);
//            sdv.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
            GestureImageView iv_image = ViewHolder.get(convertView, R.id.iv_image);
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .cacheInMemory(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();
            if(list.get(position).path!=null&&list.get(position).path.contains(Urls.PHOTO)){
                ImageLoader.getInstance().displayImage(list.get(position).path, iv_image, options);
            }
            else if (list.get(position).path!=null&&(list.get(position).path.contains("file://")||list.get(position).path.contains("http")))
            {
                ImageLoader.getInstance().displayImage( list.get(position).path, iv_image, options);
            }
            else {
                ImageLoader.getInstance().displayImage(Urls.PHOTO + list.get(position).path, iv_image, options);
            }

            container.addView(convertView);
            return convertView;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                int distance = (int) event.getX() - x;
                if (distance < -10 && viewpager.getCurrentItem() == (list.size() - 1)) {
//                    NewToast.show(context, "没有照片了哦~~", Toast.LENGTH_LONG);
                }
                if ((distance - x) > 10 && viewpager.getCurrentItem() == 0) {
//                    NewToast.show(context, "这是第一张哦~~", Toast.LENGTH_LONG);
                }
                if (distance >= -10 && distance <= 10) {
                    dlg.cancel();
                }
                x = 0;
                break;

            default:
                break;
        }

        return false;
    }
}