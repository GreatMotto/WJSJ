package com.bm.wjsj.Base;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @description 引导界面
 * @author 杨凯
 * 
 * @time 2014.12.24
 */
public class GuideActivity extends Activity {

    private ViewPager viewpager;
    private ArrayList<View> viewList = new ArrayList<View>();
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_guide);
        init();
    }

    public void init() {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inSampleSize = 2;// 设置缩放比例

//        ImageView imageView1 = new ImageView(this);
//        imageView1.setScaleType(ScaleType.CENTER_CROP);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.guide1, newOpts);
//        imageView1.setImageBitmap(bitmap);
//
//        ImageView imageView2 = new ImageView(this);
//        imageView2.setScaleType(ScaleType.CENTER_CROP);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.guide2, newOpts);
//        imageView2.setImageBitmap(bitmap);
//
//        ImageView imageView3 = new ImageView(this);
//        imageView3.setScaleType(ScaleType.CENTER_CROP);
//        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.guide3, newOpts);
//        imageView3.setImageBitmap(bitmap);
        ImageView imageView1 = new ImageView(this);
        imageView1.setScaleType(ScaleType.CENTER_CROP);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page1, newOpts);
        imageView1.setImageBitmap(bitmap);

        ImageView imageView2 = new ImageView(this);
        imageView2.setScaleType(ScaleType.CENTER_CROP);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page2, newOpts);
        imageView2.setImageBitmap(bitmap);

        ImageView imageView3 = new ImageView(this);
        imageView3.setScaleType(ScaleType.CENTER_CROP);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page3, newOpts);
        imageView3.setImageBitmap(bitmap);

        ImageView imageView4 = new ImageView(this);
        imageView4.setScaleType(ScaleType.CENTER_CROP);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page4, newOpts);
        imageView4.setImageBitmap(bitmap);

        ImageView imageView5 = new ImageView(this);
        imageView5.setScaleType(ScaleType.CENTER_CROP);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.page5, newOpts);
        imageView5.setImageBitmap(bitmap);

        viewList.add(imageView1);
        viewList.add(imageView2);
        viewList.add(imageView3);
        viewList.add(imageView4);
        viewList.add(imageView5);
        // 给viewpager设置适配�?
        viewpager = (ViewPager) findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(viewList);
        viewpager.setAdapter(adapter);

        imageView1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                setGuided();
                setGuided();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);
                finish();
            }
        });
        imageView2.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                setGuided();
                setGuided();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);
                finish();
            }
        });

        imageView3.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                setGuided();
                setGuided();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);
                finish();
            }
        });

        imageView4.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                setGuided();
                setGuided();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);
                finish();
            }
        });

        imageView5.setOnClickListener(new OnClickListener()
        {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                setGuided();
                setGuided();
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.ac_enter, R.anim.ac_exit);
                finish();
            }
        });


    }

    private void setGuided() {
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this, Constant.SP_FILENAME);
        sp.putBooleanValue(Constant.SP_KEY_GUIDE, true);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (bitmap != null && !bitmap.isRecycled()) {
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
    }

    /**
     * @description 帮助界面的�?�配�?
     * @author guoky@bluemobi.sh.cn
     * 
     * @time 2014-9-25
     */
    public class ViewPagerAdapter extends PagerAdapter {

        private List<View> pages;

        public ViewPagerAdapter(List<View> lists) {
            this.pages = lists;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            ((ViewPager) container).removeView(pages.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(pages.get(position));
            return pages.get(position);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

    }
}