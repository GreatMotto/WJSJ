package com.bm.wjsj.Base;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;

public class TextActivity extends BaseActivity {

    private WebView wv_desc;

    private String title, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_text);
        title = getIntent().getStringExtra(Constant.TITLE);
        url = getIntent().getStringExtra(Constant.URL);
        initTitle(title);
        DialogUtils.showProgressDialog("正在加载...", this);
        init();
    }


    private String getWidthPx() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        int dipWidth=(int)( mScreenWidth/dm.density);
        int realWidth = (int)(dipWidth-5*dm.density);
        return "" + realWidth;
    }

//    @SuppressLint("WrongViewCast")
//    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void init() {
        wv_desc = (WebView) findViewById(R.id.wv_desc);
        wv_desc.getSettings().setJavaScriptEnabled(true);
        wv_desc.getSettings().setDefaultTextEncodingName("UTF-8");
        wv_desc.loadDataWithBaseURL(null,
                url, "text/html",
                "utf-8", null);
        //WebSettings settings = wv_desc.getSettings();
        //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //settings.setTextSize(WebSettings.TextSize.LARGER);
        //settings.setUseWideViewPort(true);
        //settings.setLoadWithOverviewMode(true);
        String widthPx= getWidthPx();
        Log.e("widthPx","-------------------"+widthPx+"Build.VERSION.SDK_INT:"+Build.VERSION.SDK_INT);
        //widthPx="320";
        if (Build.VERSION.SDK_INT >=19) {
            Log.e("widthPx", "--------111-----------" + widthPx + "---------------------------");
            wv_desc.evaluateJavascript("javascript:(function(){" +
                    "var script = document.createElement('script');"
                    + "script.type = 'text/javascript';"
                    + "script.text = \"function ResizeImages() { "
                    + "var myimg;"
                    + "for(i=0;i <document.images.length;i++)" + "{"
                    + "myimg = document.images[i];" + "myimg.setAttribute('style','max-width:"+widthPx+"px;height:auto');"
                    + "}" + "}\";"
                    + "document.getElementsByTagName('head')[0].appendChild(script);" +
                    "})()", new ValueCallback<String>() {

                @Override
                public void onReceiveValue(String value) {
                    wv_desc.loadUrl("javascript:ResizeImages()");//必需在回调中加这句话，放在其它地方无效
                    try {
                        Thread.sleep(1000);//防止ResizeImages()没执行
                        wv_desc.loadUrl("javascript:ResizeImages()");
                    }catch (Exception ex){
                        Log.e("widthPxExcepton","-------------------"+ex.toString());
                    }

                }
            });
        } else {
            // 自适应图片大小
            Log.e("widthPx","--------222-----------"+widthPx+"---------------------------");
            wv_desc.loadUrl("javascript:(function(){" +
                    "var script = document.createElement('script');"
                    + "script.type = 'text/javascript';"
                    + "script.text = \"function ResizeImages() { "
                    + "var myimg;"
                    + "for(i=0;i <document.images.length;i++)" + "{"
                    + "myimg = document.images[i];" + "myimg.setAttribute('style','max-width:" + widthPx + "px;height:auto');"
                    + "}" + "}\";"
                    + "document.getElementsByTagName('head')[0].appendChild(script);" +
                    "})()");

            wv_desc.loadUrl("javascript:ResizeImages()");
            try {
                Thread.sleep(1000);//防止ResizeImages()没执行
            }catch (Exception ex){}
            wv_desc.loadUrl("javascript:ResizeImages()");
        }
        DialogUtils.cancleProgressDialog();
        Log.e("widthPx", "--------333-----------" + widthPx + "---------------------------");


        /*
        final LinearLayout ly_map = (LinearLayout) findViewById(R.id.ly_map);
        ViewTreeObserver vto2 = ly_map.getViewTreeObserver();
        vto2.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ly_map.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                //获取要显示图片的布局宽高
                int h = ly_map.getHeight();
                int w = ly_map.getWidth();
                //imageUrl = "file://mnt/sdcard/img/" + fileName;


                //String data = "<HTML><IMG src=\"" + imageUrl + "\"" + "width=" + w + "height=" + h + "/>";
                wv_desc.loadDataWithBaseURL(null, url, "text/html", "utf-8", null);

                //webview_evacuation.loadUrl(imageUrl);//直接显示网上图片
                wv_desc.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
                wv_desc.getSettings().setSupportZoom(true); //可以缩放
                wv_desc.setSaveEnabled(true);
            }
        });
        */
    }




}