package com.bm.wjsj.Base;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.TextView;

import com.bm.wjsj.Bean.Message;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;

/**
 * Created by yangy on 2015/10/21 18:23
 */
public class ViewActivity extends BaseActivity implements APICallback.OnResposeListener {

    private WebView wv_desc;
    private TextView tv_content, tv_createtime;
    private Message message;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_view);
        init();
        id = getIntent().getStringExtra(Constant.ID);
        if (!TextUtils.isEmpty(id)) {
            DialogUtils.showProgressDialog("正在加载...", this);
            WebServiceAPI.getInstance().mFind(id, ViewActivity.this, ViewActivity.this);
        }
    }

    private void init() {
        wv_desc = (WebView) findViewById(R.id.wv_desc);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_createtime = (TextView) findViewById(R.id.tv_createtime);
    }

    private String getWidthPx() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        int dipWidth=(int)( mScreenWidth/dm.density);
        int realWidth = (int)(dipWidth-5*dm.density);
        return "" + realWidth;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {

        if (apiResponse.status.equals("0")) {
            message = apiResponse.data.message;
            tv_content.setText(message.title);
            tv_createtime.setText(message.createTime);
            initTitle("活动详情");
            wv_desc.getSettings().setDefaultTextEncodingName("UTF-8");
            wv_desc.loadDataWithBaseURL(null,
                    message.content, "text/html",
                    "utf-8", null);

            wv_desc.getSettings().setJavaScriptEnabled(true);
            String widthPx= getWidthPx();
            Log.e("widthPx", "-------------------" + widthPx + "Build.VERSION.SDK_INT:" + Build.VERSION.SDK_INT);
            //widthPx="320";
            //WebSettings settings = wv_desc.getSettings();
            //settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            //settings.setTextSize(WebSettings.TextSize.LARGER);
            //settings.setUseWideViewPort(true);
            //settings.setLoadWithOverviewMode(true);
            if (Build.VERSION.SDK_INT >=19) {
                Log.e("widthPx","--------111-----------"+widthPx+"---------------------------");
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
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }
}