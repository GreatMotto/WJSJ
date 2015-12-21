package com.bm.wjsj.Date;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.WJSJApplication;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * 搜索附近的人
 */
public class ScanActivity extends Fragment implements APICallback.OnResposeListener {
    private ImageView sousuo;
    private SimpleDraweeView person;
    //    private float X;
//    private float Y;
//    private float Lr;
    private boolean isSearch;
    private boolean fromLogin;
    //    int coun = 0;
    private int[] viewIds = {R.id.pic_1, R.id.pic_2, R.id.pic_3, R.id.pic_4, R.id.pic_5, R.id.pic_6,
            R.id.pic_7, R.id.pic_8, R.id.pic_9, R.id.pic_10, R.id.pic_11};
    //    int[] picArr;
    private List<SimpleDraweeView> imaList = new ArrayList<>();
    //public List<nearPic> nearPicList = new ArrayList<>();
    public static List<UserInfo> listUser = new ArrayList<UserInfo>();
    private Random random;

    private int pageNum = 1, pageSize = 10;
    private String sex="";

    private TextView tv_sidebar;
    private View view;
    private boolean isShowPerson;


    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
//            addImageView();
//            getRand();
            switch (msg.what) {
                case 0:
                    if (!isSearch) {
                        //mHandler.sendEmptyMessageDelayed(0, 100);
                        mHandler.sendEmptyMessageDelayed(0, 500);
                    } else {
                        mHandler.sendEmptyMessageDelayed(1, 500);
                    }
                    break;
                case 1:
                    if (imaList.size() > 0 && listUser.size() > 0) {
                        int indexofView = 0, indexofUri = 0;
                        if (imaList.size() != 1 && listUser.size() != 1) {
                            indexofView = random.nextInt(imaList.size() - 1);
                            indexofUri = random.nextInt(listUser.size() - 1);
                        }
                        SimpleDraweeView ss = imaList.get(indexofView);
                        ss.setVisibility(View.VISIBLE);
                        Uri uri= Uri.parse(Urls.PHOTO + listUser.get(indexofUri).head);
                        ss.setImageURI(uri);
                        //YoYo.with(Techniques.BounceIn).duration(700).playOn(ss);
                        imaList.remove(indexofView);
                        listUser.remove(indexofUri);
                        mHandler.sendEmptyMessageDelayed(1, 500);
                    }
//                    if (imaList.size() > 0 && nearPicList.size() > 0) {
//                        int indexofView = 0, indexofUri = 0;
//                        if (imaList.size() != 1 && nearPicList.size() != 1) {
//                            indexofView = random.nextInt(imaList.size() - 1);
//                            indexofUri = random.nextInt(nearPicList.size() - 1);
//                        }
//                        SimpleDraweeView ss = imaList.get(indexofView);
//                        ss.setVisibility(View.VISIBLE);
//                        ss.setImageURI(Uri.parse(nearPicList.get(indexofUri).url));
//                        YoYo.with(Techniques.BounceIn).duration(700).playOn(ss);
//                        imaList.remove(indexofView);
//                        nearPicList.remove(indexofUri);
//                        mHandler.sendEmptyMessageDelayed(1, 500);
//                    }
                    break;
            }

        }
    };

    private void assignViews() {
//        picArr = getRand();
        random = new Random(new  Date().getTime());
        sousuo = (ImageView) view.findViewById(R.id.sousuo);


//        findViewById(R.id.icon_back).setVisibility(View.INVISIBLE);
//        ImageLoader.getInstance().displayImage(Constant.imageUrls2[1], person, ImageUtils.getOptions());
        //person.setImageURI(Uri.parse(getImagUrl()));
            person = (SimpleDraweeView) view.findViewById(R.id.person);
            if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO).equals("")) {
                    if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0")) {
                        person.setImageResource(R.mipmap.touxiangnan);
                    } else {
                        //person.setImageResource(R.mipmap.touxiangnv);
                    }
                } else {
                    //person.setImageURI(Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)));
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)))
                            .setResizeOptions(new ResizeOptions(80, 80))
                            .build();

                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            //.setUri(Uri.parse(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)))
                            .setImageRequest(request)
                            .setControllerListener(listener)
                            .build();
                    person.setController(controller);

                    //mapHandler.sendEmptyMessage(0);
                    // 开启一个子线程，进行网络操作，等待有返回结果，使用handler通知UI
                    String localValue=WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO_LOCAL);
                    if ("".equals(localValue)) {
//                        Log.e("eeeeeeeeeee",">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>***************");
//                        new Thread(networkTask).start();
                    }
                }
//            person.setImageURI(Uri.parse(Urls.PHOTO+WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO)));
            } else {
                person.setImageResource(R.mipmap.icon_logo);
            }
            Log.e("showPerson:",">>>>"+Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO));

        tv_sidebar = (TextView) view.findViewById(R.id.tv_sidebar);
        tv_sidebar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).sm.toggle();
            }
        });

    }




    ControllerListener listener = new BaseControllerListener(){
        @Override
        public void onFinalImageSet(String id, Object imageInfo, Animatable animatable) {
            super.onFinalImageSet(id, imageInfo, animatable);
        }

        @Override
        public void onFailure(String id, Throwable throwable) {
            super.onFailure(id, throwable);
            //person.setImageResource(R.mipmap.touxiangnan);
            //saveBitmapToSharedPreferences();
            //getBitmapFromSharedPreferences();
            Log.e("personFail:","************************************"+throwable.toString());
        }

        @Override
        public void onIntermediateImageFailed(String id, Throwable throwable) {
            super.onIntermediateImageFailed(id, throwable);
            Log.e("onIntermediateFailed:", "************************************");
        }
    };



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null) {
//                parent.removeView(view);
//            }
//            resetFindPic();
//            findPicView();
//            resetFindPic();
//            random =new Random(new  Date().getTime());
//
//            if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
//                getBitmapFromSharedPreferences();
//            }
//
//            WebServiceAPI.getInstance().findtryst(sex,pageNum,pageSize,ScanActivity.this,getActivity());
//            return view;
//        }

        view = inflater.inflate(R.layout.activity_scan, container, false);
        pageNum=1;

        //WebServiceAPI.getInstance().srarchPic(this, this);
        WebServiceAPI.getInstance().findtryst(sex,pageNum,pageSize,ScanActivity.this,getActivity());
//        coun = 0;
        fromLogin = getActivity().getIntent().getBooleanExtra("fromLogin", false);
//        initTitle("约会");
//        findViewById(R.id.title).setBackground(null);
        findPicView();
//        tv_title.setTextColor(getResources().getColor(R.color.deafaut_text));
        assignViews();
//        X = DisplayUtil.getWidth(ScanActivity.this) / 2;
//        Y = DisplayUtil.getHeight(ScanActivity.this) / 2;
//        int th = re_top.getHeight();
//        Lr = X - DisplayUtil.dip2px(ScanActivity.this, 50);
//        Log.e("中心点坐标", "X:" + X + "Y:" + Y);

//        RotateAnimation ra=new RotateAnimation(0,)
//        setPic();
        return view;
    }


    private void resetFindPic() {
        for (SimpleDraweeView sdv : imaList) {
            sdv.setVisibility(View.GONE);
        }
    }

    public void findPicView() {
        imaList.clear();
        for(int i : viewIds){
            imaList.add((SimpleDraweeView) view.findViewById(i));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //imaList.clear();
        //findPicView();
        //assignViews();


        Animation operatingAnim = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
        operatingAnim.setDuration(4000);
        sousuo.startAnimation(operatingAnim);
        operatingAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //mHandler.sendEmptyMessageDelayed(0, 1);
                mHandler.sendEmptyMessageDelayed(0, 500);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent intent = new Intent(getActivity(), MainActivity.class);

                intent.putExtra("fromLogin", fromLogin);

                intent.putExtra("isScan", true);

                startActivity(intent);
//                ((MainActivity) getActivity()).sm.toggle();
//                getActivity().finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /*public void setPic() {
        for (int i = 0; i < picArr.length; i++) {
            SimpleDraweeView ss = imaList.get(picArr[i]);
            ss.setImageURI(Uri.parse(nearPicList.get(i).url));
            YoYo.with(Techniques.BounceIn).duration(700).playOn(ss);

        }
    }*/
/*
    public void addImageView() {


        ImageView iv = new ImageView(ScanActivity.this);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(70, 70);
        iv.setLayoutParams(lp);
        ImageLoader.getInstance().displayImage(WJSJApplication.getInstance().nearPicList.get(coun).url, iv, ImageUtils.getOptionsCircle());
        float x = getlX();
        float y = getlY();
        if (x < (X - DisplayUtil.dip2px(this, 80)) || x > (X + DisplayUtil.dip2px(this, 80))
                && (y < (Y - DisplayUtil.dip2px(this, 80)) && y > (Y + DisplayUtil.dip2px(this, 80)))) {
            iv.setX(x);
            iv.setY(y);
            coun++;
            reSousuo.addView(iv);
//            YoYo.with(Techniques.BounceIn).duration(700).playOn(iv);
            reSousuo.invalidate();

        }

    }*/

    /*private int[] getRand() {

//        int i = re.nextInt(8) ;
//        Log.e("i","随机的:"+i);
//        pic_1.setImageURI(Uri.parse(WJSJApplication.getInstance().nearPicList.get(coun).url));
//        coun++;
        int[] intRet = new int[4];
        int intRd = 0; //存放随机数
        int count = 0; //记录生成的随机数个数
        int flag = 0; //是否已经生成过标志
        while (count < 4) {
            Random rdm = new Random(System.currentTimeMillis());
            intRd = Math.abs(rdm.nextInt()) % 8;
            for (int i = 0; i < count; i++) {
                if (intRet[i] == intRd) {
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
            if (flag == 0) {
                intRet[count] = intRd;
                count++;
            }
        }
        for (int t = 0; t < 4; t++) {
            System.out.println(t + "->" + intRet[t]);
        }

        return intRet;


    }*/

    /*private float getlX() {
        Random re = new Random();
        float max = (X + Lr) - (X - Lr);
        float i = re.nextInt((int) (2 * Lr)) + X - Lr;
        Log.e("位置", "随机出来的X" + i);
        return i;

    }*/

   /* private float getlY() {
        Random re = new Random();
//        Log.e("位置", "Y" + (Y ));
//        Log.e("位置", "Lr" + ( Lr));
        float max = 2 * Lr;
        float i = re.nextInt((int) max) + (Y - Lr);
        Log.e("位置", "随机出来的Y" + i);
        return i;

    }*/


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        switch (tag) {
            case 11:
                listUser.clear();
                listUser.addAll(apiResponse.data.list);
                if (apiResponse.data.page.totalNum < 10) {
                    pageNum = 1;
                } else {
                    pageNum = pageNum + 1;
                }
                //nearPicList.addAll(apiResponse.data.list);
                isSearch = true;
                break;
        }
//        setPic();
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }


    private Handler mapHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }

    };

    /**
     * 网络操作相关的子线程
     */
    Runnable networkTask = new Runnable() {
        @Override
        public void run() {
            Bitmap bm=   GetLocalOrNetBitmap(Urls.PHOTO + WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO));
            if(bm!=null) {
                saveBitmapToSharedPreferences(bm);
            }
            Message msg = new Message();
            mapHandler.sendMessage(msg);
        }
    };

    public  Bitmap GetLocalOrNetBitmap(String url)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try
        {
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
//            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//            out = new BufferedOutputStream(dataStream,1024);
//            copy(in, out);
//            out.flush();
//            byte[] data = dataStream.toByteArray();
            BitmapFactory.Options outOptions = new BitmapFactory.Options();
            // 设置该属性为true，不加载图片到内存，只返回图片的宽高到options中。
            outOptions.inJustDecodeBounds = true;

            // 加载获取图片的宽高
            BitmapFactory.decodeStream(in,null , outOptions);

            int height = outOptions.outHeight;

            if (outOptions.outWidth > 80)
            {
                // 根据宽设置缩放比例
                outOptions.inSampleSize = outOptions.outWidth / 80 + 1;
                outOptions.outWidth = 80;

                // 计算缩放后的高度
                height = outOptions.outHeight / outOptions.inSampleSize;
                outOptions.outHeight = height;
            }

            // 重新设置该属性为false，加载图片返回
            outOptions.inJustDecodeBounds = false;
            outOptions.inPurgeable = true;
            outOptions.inInputShareable = true;
            in = new BufferedInputStream(new URL(url).openStream(), 1024);
            bitmap = BitmapFactory.decodeStream(in, null, outOptions);

            //bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //data = null;
            return bitmap;
        }
        catch (Exception e)
        {
            Log.e("bitmapException:",">>>>>>>>>"+e.toString());
            e.printStackTrace();
            return null;
        }
    }


    private  void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private void saveBitmapToSharedPreferences(Bitmap bitmap){

        //Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.mipmap.touxiangnv);
        //第一步:将Bitmap压缩至字节数组输出流ByteArrayOutputStream
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            //第二步:利用Base64将字节数组输出流中的数据转换成字符串String
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String imageString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
            //第三步:将String保持至SharedPreferences
            SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
            sp.putValue(Constant.SP_PHOTO_LOCAL, imageString);
        }
        catch (Exception ex){}
    }

    private void getBitmapFromSharedPreferences(){
        try {
            SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
            //第一步:取出字符串形式的Bitmap
            String imageString = sp.getValue(Constant.SP_PHOTO_LOCAL);
            //第二步:利用Base64将字符串转换为ByteArrayInputStream
            if(!"".equals(imageString)) {
                byte[] byteArray = Base64.decode(imageString, Base64.DEFAULT);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                //第三步:利用ByteArrayInputStream生成Bitmap
                Bitmap bitmap = BitmapFactory.decodeStream(byteArrayInputStream);
                //mImageView.setImageBitmap(bitmap);
                //person=null;
                person = (SimpleDraweeView) view.findViewById(R.id.person);
                person.setImageBitmap(bitmap);
            }
            //person.setImageResource(R.mipmap.emoji_1f353);
        }
        catch (Exception ex){}
    }

}
