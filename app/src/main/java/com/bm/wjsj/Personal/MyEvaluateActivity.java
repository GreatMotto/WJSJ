package com.bm.wjsj.Personal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.orderinfoBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.ToastDialogUtils;
import com.bm.wjsj.View.RatingBarView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//import butterknife.OnTextChanged;

public class MyEvaluateActivity extends BaseActivity implements APICallback.OnResposeListener{
    private RatingBarView starView;
    private EditText edt_eval;
    private TextView tv_eval_num;
    int MAX_LENGTH = 140;                   //最大输入字符数500
    int Rest_Length = MAX_LENGTH;
    private orderinfoBean orderInfo=new orderinfoBean();
    private String orderId="";
    private TextView tv_eval_pro_name;
    private ImageView iv_pro_pic;
    private int star=0;

    private void assignViews() {
        initTitle("评价");
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setOnClickListener(this);
        tv_right.setText("提交");
        edt_eval = (EditText) findViewById(R.id.edt_eval);
        tv_eval_num = (TextView) findViewById(R.id.tv_eval_num);
        starView = (RatingBarView) findViewById(R.id.starView);
        starView.setmClickable(true);

        tv_eval_pro_name = (TextView) findViewById(R.id.tv_shop_name);
        iv_pro_pic = (ImageView) findViewById(R.id.iv_shop_image);
        tv_eval_pro_name.setText(orderInfo.pname);
        Log.e("orderInfo.path--->", orderInfo.path);
        Uri picUrl = Uri.parse(Urls.PHOTO + orderInfo.path);
        Log.e("picUrl--->","" + picUrl.toString());
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
        ImageLoader.getInstance().displayImage(Urls.PHOTO + orderInfo.path, iv_pro_pic, options);
        //iv_pro_pic.setImageURI(picUrl);
        //you can set up view here or in XML

        edt_eval.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tv_eval_num.setText("(" + s + "/140)");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Rest_Length > 0) {
                    Rest_Length = edt_eval.getText().length();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                tv_eval_num.setText("(" + Rest_Length + "/140)");
            }
        });

        Drawable sss = getResources().getDrawable(R.drawable.start_kong);
        starView.setStarEmptyDrawable(sss);
        starView.setStarFillDrawable(getResources().getDrawable(R.drawable.start_shi));
        starView.setStarImageSize(DisplayUtil.dip2px(this, 50));
        starView.setStarCount(5);
        starView.setStar(0, false);
        //bind some data
        starView.setBindObject(0);
        starView.setOnRatingListener(new RatingBarView.OnRatingListener() {
            @Override
            public void onRating(Object bindObject, int RatingScore) {
                star = RatingScore;
                Log.e("哈哈", RatingScore + "");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_evaluate);
        orderInfo=(orderinfoBean) getIntent().getSerializableExtra("orderInfo");
        orderId=getIntent().getStringExtra("orderid");
        assignViews();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
                if (TextUtils.isEmpty(edt_eval.getText().toString())) {
                    ToastDialogUtils.getInstance().ShowToast(this, "提示", "内容不能为空", false);
                } else {
                    String content=edt_eval.getText().toString();
                    WebServiceAPI.getInstance().reviewProduct(orderId, orderInfo.id, orderInfo.productid,content,""+star , MyEvaluateActivity.this, MyEvaluateActivity.this);
                }
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            Intent intentResult = new Intent();
            switch (tag) {
                case 1:
                    intentResult.putExtra("orderId", orderId);
                    setResult(RESULT_OK, intentResult);
                    ToastDialogUtils.getInstance().ShowToast(this, "提示", "评价成功！", true);
                //WebServiceAPI.getInstance().updateOrderStatus(orderId, "4", MyEvaluateActivity.this, MyEvaluateActivity.this);
                    break;
                case 11:
                    intentResult.putExtra("orderId", orderId);
                    setResult(RESULT_OK, intentResult);
                    MyEvaluateActivity.this.finish();
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        ToastDialogUtils.getInstance().ShowToast(this, "提示", "评价失败！", true);
    }

}
