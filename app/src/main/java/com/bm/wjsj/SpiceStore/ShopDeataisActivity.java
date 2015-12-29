package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.Product;
import com.bm.wjsj.Bean.ShopCarBean;
import com.bm.wjsj.Bean.SpecBean;
import com.bm.wjsj.Bean.scoreproInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.CommentUtils;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.AutomaticViewPager;
import com.bm.wjsj.View.NoScrollGridView;
import com.bm.wjsj.WJSJApplication;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.socialize.sso.UMSsoHandler;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情
 */
public class ShopDeataisActivity extends BaseActivity implements APICallback.OnResposeListener {
    private AutomaticViewPager viewPager;
    private LinearLayout dotLl;
    private List<ShopCarBean> list = new ArrayList<>();
    private SpecBean spec1;
    private SpecBean spec2;
    private TextView tv_cutnumm;
    private int num = 1;
    private int tag;//0是普通商品详情页面；1是积分商品页面;2是购物车跳转
    private TextView tv_join_shopcat, tv_name, tv_price, tv_yuan_price, tv_salenum, tv_my_score,tv_buy,iv_red_dian,tv_stock,tv_spec;
    private TextView tv_specCount;
    private TextView tv_specPrice;
    private ImageView iv_join_shopcat;
    private ImageView  iv_collect;
    private ImageView iv_youhui;

    private MySizeAdapter adapter, adapter2;

    private Product data;
    private scoreproInfo scoreproinfo;
    private String flagId,price = "0",specCount="0";
    private float onePrice=0;
    private int zt = 0;
    private DecimalFormat decimalFormat =new DecimalFormat("0.00");
    private String selectedSpec1Id,selectedSpec2Id;
    private String goodsScore="0",goodsStock="0",myScore="0";
    private static final int RETURN_REFRESH_CODE=11;
    private String spec;

    //分享图片链接
    private String urlPath;
    // 首先在您的Activity中添加如下成员变量
//    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
//    final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
//            RequestType.SOCIAL);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkDeviceHasNavigationBar(this))
        {
            setContentView(R.layout.activity_shop_deatais);
        }else {
            setContentView(R.layout.activity_shop_deatais_nav);
        }
        tag = getIntent().getIntExtra(Constant.SCORE, 0);
        flagId = getIntent().getStringExtra(Constant.ID);
        spec = getIntent().getStringExtra(Constant.SPEC);
//        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
//            WebServiceAPI.getInstance().shopCarList(this, this);
//        }
        assignViews();
        DialogUtils.showProgressDialog("正在加载...", this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            WebServiceAPI.getInstance().shopCarList(this, this);
        }
    }

    private void assignViews() {
        initTitle(getResources().getString(R.string.shop_deatils));
        ImageView list_shaixuan = (ImageView) findViewById(R.id.list_shaixuan);
        ImageView iv_shaixuan = (ImageView) findViewById(R.id.iv_shaixuan);
        list_shaixuan.setVisibility(View.VISIBLE);
        iv_shaixuan.setVisibility(View.VISIBLE);
        list_shaixuan.setImageResource(R.mipmap.home);
        iv_shaixuan.setImageResource(R.mipmap.icon_more);
        list_shaixuan.setOnClickListener(this);
        iv_shaixuan.setOnClickListener(this);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_collect = (ImageView) findViewById(R.id.iv_collect);
        iv_collect.setOnClickListener(this);
        viewPager = (AutomaticViewPager) findViewById(R.id.view_pager);
        dotLl = (LinearLayout) findViewById(R.id.dot_ll);
//        int width = DisplayUtil.getWidth(this);
//        DisplayUtil.setLayoutParams(viewPager, width, (int) (width / 1.15));
//        viewPager.start(this, 4000, dotLl, R.layout.shop_bottom, R.id.ad_item_v,
//                R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet, getImageList(Constant.imageUrls2, 3), 1.15f);
        iv_youhui = (ImageView) findViewById(R.id.iv_youhui);
//        DisplayUtil.setLayoutParams(iv_youhui, width, width / 4);
        RelativeLayout rl_evaluation = (RelativeLayout) findViewById(R.id.rl_evaluation);
        rl_evaluation.setOnClickListener(this);
        tv_buy = (TextView) findViewById(R.id.tv_buy);
        tv_buy.setOnClickListener(this);
        tv_join_shopcat = (TextView) findViewById(R.id.tv_join_shopcat);
        tv_join_shopcat.setOnClickListener(this);
        tv_yuan_price = (TextView) findViewById(R.id.tv_yuan_price);
        tv_yuan_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_salenum = (TextView) findViewById(R.id.tv_salenum);

        tv_my_score = (TextView) findViewById(R.id.tv_my_score);
        tv_stock = (TextView) findViewById(R.id.tv_stock);
        tv_spec = (TextView) findViewById(R.id.tv_spec);
        LinearLayout ll_myscore = (LinearLayout) findViewById(R.id.ll_myscore);
        RelativeLayout rl_shopcat = (RelativeLayout) findViewById(R.id.rl_shopcat);
        RelativeLayout rl_shop_deatils = (RelativeLayout) findViewById(R.id.rl_shop_deatils);
        rl_shop_deatils.setOnClickListener(this);
        iv_join_shopcat = (ImageView) findViewById(R.id.iv_join_shopcat);
        iv_join_shopcat.setOnClickListener(this);
        iv_red_dian = (TextView) findViewById(R.id.iv_red_dian);
        switch (tag) {
            case 0:
                WebServiceAPI.getInstance().storeDetail(flagId, this, this);
//                tv_yuan_price.setVisibility(View.GONE);
                ll_myscore.setVisibility(View.GONE);
                tv_stock.setVisibility(View.GONE);
                tv_spec.setVisibility(View.GONE);
                tv_join_shopcat.setVisibility(View.VISIBLE);
                list_shaixuan.setVisibility(View.VISIBLE);
                iv_shaixuan.setVisibility(View.VISIBLE);
                rl_evaluation.setVisibility(View.VISIBLE);
                break;
            case 1://积分商品
                WebServiceAPI.getInstance().scoreScoreproinfo(flagId, this, this);
                //商品积分接口调用完毕后再调用我的积分接口
                //WebServiceAPI.getInstance().scoreMyscore(this, this);
                tv_buy.setText("立即兑换");
                tv_stock.setVisibility(View.VISIBLE);
                tv_spec.setVisibility(View.VISIBLE);
                iv_youhui.setVisibility(View.GONE);
                ll_myscore.setVisibility(View.VISIBLE);
                rl_shopcat.setVisibility(View.GONE);
                tv_join_shopcat.setVisibility(View.GONE);
                list_shaixuan.setVisibility(View.GONE);
                iv_shaixuan.setVisibility(View.GONE);
                rl_evaluation.setVisibility(View.GONE);
                break;
            case 2:
                findViewById(R.id.rl_shop_bottom).setVisibility(View.GONE);
                break;
        }
    }

    private  boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.e("showNavigationBarError:","----------------------------------------------------------------");
        }
        return hasNavigationBar;
    }

    private int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_evaluation:
                //商品评价
                Intent intent = new Intent(this, ShopEvaluationActivity.class);
                intent.putExtra(Constant.ID, data.id);
                startActivity(intent);
                break;
            case R.id.tv_buy:
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                } else {
                    switch (tag){
                        case 0:
                        showPhoto();
                            zt = 1;
                            break;
                        case 1:
                            if(checkStatus(this)) {
                                showBuyNum();
                                /*
                                Intent intent1 = new Intent();
                                intent1.setClass(ShopDeataisActivity.this, MyOrderActivity.class);
                                intent1.putExtra("flagId", flagId);
                                intent1.putExtra(Constant.SCORE, "-2");
                                WJSJApplication.getInstance().addAcitivity(ShopDeataisActivity.this);
                                startActivity(intent1);
                                */
                            }
                            break;
//                            if (alertDialog != null && alertDialog.isShowing())
//                                alertDialog.cancel();
//                            break;
                    }
                }
                break;
            case R.id.tv_join_shopcat:
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                } else {
                    if (data.isAddCact.equals("1")) {
                        NewToast.show(this, "此商品已加入购物车！", Toast.LENGTH_LONG);
                    } else {
                        showPhoto();
                        zt = 2;
                    }
                }
                break;
            case R.id.tv_add:
                try {
                    //判断数量是否大于库存
                    int specCountI = Integer.parseInt(specCount);
                    int cutnumI = Integer.parseInt(tv_cutnumm.getText().toString());
                    if (cutnumI >= specCountI) {
                        return;
                    }
                    num += 1;

                    //重新计算价格
                    float allPrice = num*onePrice;
                    tv_specPrice.setText(decimalFormat.format(allPrice));

                    tv_cutnumm.setText(String.valueOf(num));
                }catch (Exception ex){}
                break;
            case R.id.tv_cut:
                if (num == 1) {
                    return;
                }
                try {
                    num -= 1;
                    //重新计算价格
                    float allPrice = num * onePrice;
                    tv_specPrice.setText(decimalFormat.format(allPrice));
                    tv_cutnumm.setText(String.valueOf(num));
                }catch (Exception ex){}

                break;
            case R.id.list_shaixuan:
                Intent intent2 = new Intent(this, MainActivity.class);
                intent2.putExtra("isShop", 1);
                startActivity(intent2);
                break;
            case R.id.iv_shaixuan:
                // 设置分享内容
//                mController.setShareContent("Test" + "www.baidu.com");
                // 设置分享图片, 参数2为图片的url地址
//                mController.setShareMedia(new UMImage(this,urlPath));
//                mController.openShare(this, false);
               if(checkStatus(this)) {
                   if(tv_name.getText()!=null &&!"".equals(tv_name.getText().toString().trim()))
                   {
                       CommentUtils.share(this, tv_name.getText().toString().trim(), urlPath, null);
                   }else {
                       CommentUtils.share(this, "陌客", urlPath, null);
                   }
               }
                break;
            case R.id.rl_shop_deatils:
                switch (tag){
                    case 0:
                        gotoTextActivity("商品详情", data.detail);
                        break;

                    case 1:
                        gotoTextActivity("商品详情", scoreproinfo.description);
                        break;
                }

//                Intent intent3 = new Intent(this, PrivilegeActivity.class);
//                intent3.putExtra(Constant.WEB_NAME, "商品详情");
//                intent3.putExtra(Constant.URL,data.detail);
//                startActivity(intent3);
                break;
            case R.id.iv_join_shopcat://跳转到购物车
                if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                    gotoLoginAc(this, 333);
                } else {
                    Intent intentCar = new Intent(this, MyShopCatActivity.class);
//                    iv_red_dian.setVisibility(View.GONE);
                    startActivity(intentCar);
                }
                break;
            case R.id.iv_collect:
                try {
                    if (!WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                        gotoLoginAc(this, 333);
                    } else {
                        if (data.isCollect.equals("1")) {
                            iv_collect.setImageResource(R.mipmap.star_select);
                        } else {

                            WebServiceAPI.getInstance().addCollect(data.id, ShopDeataisActivity.this, ShopDeataisActivity.this);
                            //收藏接口
                        }
                    }
                }catch (Exception e){

                }
                break;
        }
    }

    private void resetshowSpec()
    {
        if (alertDialog != null && alertDialog.isShowing())
        {
            String newPrice ="0",newCount="0";
            if(price!=null &&price.length()>0)
            {
                newPrice=price;
            }
            if(specCount!=null&&specCount.length()>0){
                newCount=specCount;
            }

            float priceF=0,countF=0;
            try {
                priceF = Float.parseFloat(newPrice);
                countF=Float.parseFloat(newCount);
            }
            catch (Exception ex){}
            //TextView tv_specPrice = (TextView) alertDialog.getWindow().findViewById(R.id.specPrice);
            //TextView tv_specCount = (TextView) alertDialog.getWindow().findViewById(R.id.specCount);
            if(priceF<=0||countF<=0) {
                if(priceF<=0) {
                    tv_specPrice.setText("0.00");
                }
                else{
                    tv_specPrice.setText(decimalFormat.format(priceF));
                }
                if(countF<=0){
                    tv_specCount.setText("0");
                }else{
                    tv_specCount.setText(""+(int)countF);
                }

                TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
                tv_sure.setText("暂无此规格商品");
                tv_sure.setBackgroundResource(R.mipmap.btn);
                tv_sure.setEnabled(false);
            }
            else{
                tv_specPrice.setText(decimalFormat.format(priceF));
                tv_specCount.setText(""+(int)countF);

                TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
                tv_sure.setText("确定");
                tv_sure.setBackgroundResource(R.mipmap.btn_login_bg);
                tv_sure.setEnabled(true);
            }
        }
    }

    private void showPhoto() {
        if (spec1 == null) {
            NewToast.show(this, "未获取到商品规格！", Toast.LENGTH_LONG);
            return;
        }
        if (num != 1){
            num = 1;
        }
        //重置选中规则id
        selectedSpec1Id="-1";
        selectedSpec2Id="-1";

        showdialog(Gravity.BOTTOM);
        alertDialog.getWindow().setContentView(R.layout.pop_goods);
        NoScrollGridView gv_color = (NoScrollGridView) alertDialog.getWindow().findViewById(
                R.id.gv_color);
        NoScrollGridView gv_size = (NoScrollGridView) alertDialog.getWindow().findViewById(
                R.id.gv_size);
        ImageView iv_cancle = (ImageView) alertDialog.getWindow().findViewById(R.id.iv_cancle);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
        tv_sure.setEnabled(false);
        final TextView tv_color_name = (TextView) alertDialog.getWindow().findViewById(R.id.tv_color_name);
        final TextView tv_size_name = (TextView) alertDialog.getWindow().findViewById(R.id.tv_size_name);
        //final TextView tv_specCount = (TextView) alertDialog.getWindow().findViewById(R.id.specCount);
        LinearLayout ll_second = (LinearLayout) alertDialog.getWindow().findViewById(R.id.ll_second);
        adapter = new MySizeAdapter(this, spec1);

        //SpecBean aa= adapter.spec;
        gv_color.setAdapter(adapter);
        tv_color_name.setText(spec1.name);
        if (spec2 != null) {
            ll_second.setVisibility(View.VISIBLE);
            adapter2 = new MySizeAdapter(this, spec2);
            gv_size.setAdapter(adapter2);
            tv_size_name.setText(spec2.name);
            if(spec2.specList!=null&&spec2.specList.size()>0){//设置规格2默认选中Id
                selectedSpec2Id = spec2.specList.get(0).id;
            }

            adapter2.setOnSizeClickListener(new MySizeAdapter.OnSizeClickListener() {
                @Override
                public void onSizeClicked(int itemPosition, Object dataObject) {
                    //获得选中的商品规格的价格
                    String sp2Name = "";
//                    int temp1 = adapter.selectText;
//                    selectedSpec1Id = spec1.specList.get(temp1).id;
                    if (spec2 != null) {
                        int temp2 = adapter2.selectText;
                        selectedSpec2Id = spec2.specList.get(temp2).id;
                        sp2Name = spec2.specList.get(temp2).name;
                    }
                    Log.e("sizeChanged:", "&&&&&&&&&&&&&&&&&&&&&&spec1Id:" + selectedSpec1Id  + "||sp2Id:" + selectedSpec2Id + "||sp2Name:" + sp2Name);
                    WebServiceAPI.getInstance().storeFindDetail(data.id, selectedSpec1Id, selectedSpec2Id,
                            ShopDeataisActivity.this, ShopDeataisActivity.this);
                }
            });
        }
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (zt) {
                    case 1:
                        Intent intent = new Intent();
                        intent.setClass(ShopDeataisActivity.this, MyOrderActivity.class);
                        List<Product> list = new ArrayList<>();
                        list.add(data);
                        String spec1Text="";
                        String specIdList="";
                        if(spec1!=null)
                        {
                            int temp1 = adapter.selectText;
                            spec1Text=spec1.specList.get(temp1).name;
                            specIdList=spec1.specList.get(temp1).id;
                        }
                        if(spec2!=null)
                        {
                            int temp2 = adapter2.selectText;
                            spec1Text+=","+spec2.specList.get(temp2).name;
                            specIdList+=","+spec2.specList.get(temp2).id;
                        }




                        //前往订单界面前，先保存商品id，支付后，前往指定的商品详情页面
                        SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                        sp.putValue(Constant.SP_PAY_PRODUCTID, data.id);

                        intent.putExtra("selcList", (Serializable) list);
                        intent.putExtra("curNum", tv_cutnumm.getText().toString());
                        intent.putExtra("specPrice",price);
                        intent.putExtra("spec1",spec1Text);
                        intent.putExtra("speclist",specIdList);
                        intent.putExtra(Constant.SCORE, "-1");
                        WJSJApplication.getInstance().addAcitivity(ShopDeataisActivity.this);
                        startActivity(intent);
                        if (alertDialog != null && alertDialog.isShowing())
                            alertDialog.cancel();
                        break;
                    case 2:
                        if (price.equals("")) {
                            price = "0";
                        }
                        if (spec2 == null) {
                            String sl = selectedSpec1Id;
                            WebServiceAPI.getInstance().addShopCar(data.id, tv_cutnumm.getText().toString(), price, sl,
                                    ShopDeataisActivity.this, ShopDeataisActivity.this);
                        } else {
                            String sl = selectedSpec1Id + "," + selectedSpec2Id;
                            WebServiceAPI.getInstance().addShopCar(data.id, tv_cutnumm.getText().toString(), price, sl,
                                    ShopDeataisActivity.this, ShopDeataisActivity.this);
                        }

                        break;
                }

            }
        });
        TextView tv_cut = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cut);
        TextView tv_add = (TextView) alertDialog.getWindow().findViewById(R.id.tv_add);
        tv_cutnumm = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cutnumm);
        tv_specCount= (TextView)  alertDialog.getWindow().findViewById(R.id.specCount);
        tv_specPrice = (TextView) alertDialog.getWindow().findViewById(R.id.specPrice);
        tv_cut.setOnClickListener(this);
        tv_add.setOnClickListener(this);

        if(spec1.specList!=null&&spec1.specList.size()>0){//设置规格1默认选中Id
            selectedSpec1Id = spec1.specList.get(0).id;
        }

        adapter.setOnSizeClickListener(new MySizeAdapter.OnSizeClickListener() {
            @Override
            public void onSizeClicked(int itemPosition, Object dataObject) {
                //获得选中的商品规格的价格
                String sp2Name="";
                int temp1 = adapter.selectText;
                selectedSpec1Id=spec1.specList.get(temp1).id;
//                if (spec2 != null) {
//                    int temp2 = adapter2.selectText;
//                    selectedSpec2Id = spec2.specList.get(temp2).id;
//                    sp2Name= spec2.specList.get(temp2).name;
//                }
                Log.e("sizeChanged:","&&&&&&&&&&&&&&&&&&&&&&spec1Id:" + selectedSpec1Id+"||spec1Name:"+spec1.specList.get(temp1).name +"||sp2Id:" + selectedSpec2Id+"||sp2Name:"+sp2Name);
                WebServiceAPI.getInstance().storeFindDetail(data.id, selectedSpec1Id, selectedSpec2Id,
                        ShopDeataisActivity.this, ShopDeataisActivity.this);
            }
        });

                WebServiceAPI.getInstance().storeFindDetail(data.id, selectedSpec1Id, selectedSpec2Id,
                ShopDeataisActivity.this, ShopDeataisActivity.this);

//        String spec1Id="-1",spec2Id="-1",spec1Name="",spec2Name="";
//        if(spec1!=null&&spec1.specList.size()>0){
//            spec1Id=spec1.specList.get(0).id;
//            spec1Name=spec1.specList.get(0).name;
//        }
//        if(spec2!=null&&spec2.specList.size()>0){
//            spec2Id=spec2.specList.get(0).id;
//            spec2Name=spec2.specList.get(0).name;
//        }

        //Log.e("sizeChanged:", "&&&&&&&&&&&&&&&&&&&&&&spec1Id:" + spec1Id + "||spec1Name:" + spec1Name + "||sp2Id:" + spec2Id + "||sp2Name:" + spec2Name);
    }

    private void showBuyNum() {
//        showdialog(Gravity.BOTTOM);
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.score_buynum);
        final EditText et_num = (EditText) alertDialog.getWindow().findViewById(
                R.id.et_goodsNum);
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_num.getText().toString().trim();
                try {
                    int buyNum = Integer.parseInt(num);
                    Float goodsScoreF = Float.parseFloat(goodsScore);
                    Float myScoreF = Float.parseFloat(myScore);
                    if (buyNum * goodsScoreF > myScoreF) {
                        NewToast.show(ShopDeataisActivity.this, "积分不足", Toast.LENGTH_LONG);
                        alertDialog.cancel();
                    } else {
                        if (Integer.parseInt(num) > Integer.parseInt(scoreproinfo.stock)){
                            NewToast.show(ShopDeataisActivity.this, "库存不足", Toast.LENGTH_LONG);
                            alertDialog.cancel();
                        }else {
                            Intent intent1 = new Intent();
                            intent1.setClass(ShopDeataisActivity.this, MyOrderActivity.class);
                            intent1.putExtra("flagId", flagId);
                            intent1.putExtra(Constant.SCORE, "-2");
                            intent1.putExtra("curNum", buyNum + "");
                            WJSJApplication.getInstance().addAcitivity(ShopDeataisActivity.this);
                            //startActivity(intent1);
                            startActivityForResult(intent1, RETURN_REFRESH_CODE);
                            alertDialog.cancel();
                        }
                        //finish();
                    }
                } catch (Exception ex) {
                }
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
        Log.e("shopDetailFailureData:", "**************************************************error:" + error + ",tag:" + tag);
        NewToast.show(this, "网络错误！", Toast.LENGTH_LONG);
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        if (apiResponse.status.equals("0") ) {
            switch (tag) {
                case 1://商品详情
                    data = apiResponse.data.product;
                    //Log.e("data.id", data.id);
                    if (data.oldprice.equals("")){
                        tv_yuan_price.setText("¥" + data.oldprice + "0.00");
                    }else if (data.oldprice.indexOf(".") != -1){
                        tv_yuan_price.setText("¥" + data.oldprice);
                    }else {
                        tv_yuan_price.setText("¥" + data.oldprice + ".00");
                    }
                    tv_name.setText(data.name);
                    if (data.price.equals("")){
                        tv_price.setText("¥" + data.price + "0.00");
                    }else if (data.price.indexOf(".") != -1){
                        if("2".equals(data.isEMS)) {
                            tv_price.setText("¥" + data.price+"(包邮)");
                        }
                        else{
                            tv_price.setText("¥" + data.price);
                        }
                    }else {
                        if("2".equals(data.isEMS)) {
                            tv_price.setText("¥" + data.price+ ".00"+"(包邮)");
                        }
                        else {
                            tv_price.setText("¥" + data.price + ".00");
                        }
                    }
                    tv_salenum.setText(data.salenum + "人购买");
                    if (data.isCollect.equals("1")) {
                        iv_collect.setImageResource(R.mipmap.star_select);
                    }
//                    String stockS= TextUtils.isEmpty(data.count)?"0":data.count;
//                    tv_stock.setText(stockS + "库存");
//                    if (!TextUtils.isEmpty(spec)){
//                        tv_spec.setText(spec);
//                    }
//                    iv_youhui.setImageURI(Uri.parse(Urls.PHOTO + data.hpicUrl));
                    DisplayImageOptions options = new DisplayImageOptions.Builder()
                            .cacheInMemory(true)
                            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                            .bitmapConfig(Bitmap.Config.RGB_565)
                            .build();

                    ImageLoader.getInstance().displayImage(Urls.PHOTO + data.hpicUrl, iv_youhui, options);
                    if(data.picList!=null&&data.picList.size()>0)
                    {
                        urlPath = Urls.PHOTO + data.picList.get(0).path;
                    }else {
                        urlPath = Urls.PHOTO + data.hpicUrl;
                    }
//                    iv_youhui.setAspectRatio(3.80124224f);

//                        viewPager.start(this, 4000, dotLl, R.layout.shop_bottom, R.id.ad_item_v,
//                                R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet, getImageBean(data.picList, data.picList.size()), 1.15f);
                    viewPager.setClickFlag("0");
                    viewPager.start(this, 4000, dotLl, R.layout.bannaer_bottom, R.id.iv_bannaer,
                            R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet, getImageBean(data.picList, data.picList.size()), 1.15f);
                    WebServiceAPI.getInstance().storeSpec(data.id, ShopDeataisActivity.this, ShopDeataisActivity.this);

                    break;
                case 2://商品规格

                    spec1 = apiResponse.data.spec1;
                    spec2 = apiResponse.data.spec2;
//                    String sp2 ="";
//                    if(spec2 != null){
//                        sp2 = spec2.specList.get(0).id;
//                    }
//                    WebServiceAPI.getInstance().storeFindDetail(data.id, spec1.specList.get(0).id,sp2,
//                            ShopDeataisActivity.this, ShopDeataisActivity.this);
                    break;
                case 3://购物车数量

                    data.isAddCact.equals("1");
//                    iv_collect.setImageResource(R.mipmap.star_select);
                    if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                        WebServiceAPI.getInstance().shopCarList(this, this);
                    }
                    NewToast.show(this, "成功加入购物车！", Toast.LENGTH_LONG);
                    if (alertDialog != null && alertDialog.isShowing())
                        alertDialog.cancel();
                    break;
                case 4://积分商品详细
                    scoreproinfo = apiResponse.data.scoreproinfo;
                    if (scoreproinfo.price.equals("")){
                        tv_yuan_price.setText("¥" + scoreproinfo.price + "0.00");
                    }else if (scoreproinfo.price.indexOf(".") != -1){
                        tv_yuan_price.setText("¥" + scoreproinfo.price);
                    }else {
                        tv_yuan_price.setText("¥" + scoreproinfo.price + ".00");
                    }
                    tv_yuan_price.setText("¥" + scoreproinfo.price  + ".00");
                    tv_name.setText(scoreproinfo.name);
                    tv_price.setText(scoreproinfo.score + "积分");
                    tv_salenum.setText(scoreproinfo.ordernum + "人购买");
                    tv_stock.setText(scoreproinfo.stock + "库存");

                    int stockI=0;
                    try{
                        stockI=Integer.parseInt(scoreproinfo.stock);

                    }catch (Exception ex){}
                    if(stockI<1){
                        tv_buy.setEnabled(false);
                        tv_buy.setBackgroundResource(R.mipmap.btn_unuse);
                    }
                    else{
                        tv_buy.setEnabled(true);
                        tv_buy.setBackgroundResource(R.mipmap.buy);
                    }

                    if (!TextUtils.isEmpty(spec)){
                        tv_spec.setText(spec);
                    }
                    goodsScore=scoreproinfo.score;
                    goodsStock=scoreproinfo.stock;
                    if (!scoreproinfo.imglist.equals("")) {
                        viewPager.setClickFlag("0");
                        viewPager.start(this, 4000, dotLl, R.layout.bannaer_bottom, R.id.iv_bannaer,
                                R.mipmap.shop_dian_select, R.mipmap.shop_dian_unselcet, getImageBean(scoreproinfo.imglist, scoreproinfo.imglist.size()), 1.15f);
                    }
                    WebServiceAPI.getInstance().scoreMyscore(this, this);
                    break;
                case 5://我的积分
                tv_my_score.setText(apiResponse.data.score);
                    myScore=apiResponse.data.score;
                    try{
                       Float goodsScoreNum=Float.parseFloat(goodsScore);
                        Float myScoreF=Float.parseFloat(apiResponse.data.score);
                        if(tv_buy.isEnabled()) {//如果按钮可见
                            if (myScoreF < goodsScoreNum) {
                                tv_buy.setEnabled(false);
                                tv_buy.setBackgroundResource(R.mipmap.btn_unuse);
                            } else {
                                tv_buy.setEnabled(true);
                                tv_buy.setBackgroundResource(R.mipmap.buy);
                            }
                        }
                    }catch (Exception ex){}
                    break;
                case 6://收藏
                    data.isCollect = "1";
                    iv_collect.setImageResource(R.mipmap.star_select);
                    NewToast.show(this, "收藏成功", Toast.LENGTH_LONG);
                    break;
                case 7:
                    if (list.size() != 0){
                        list.clear();
                    }
                    list.addAll(apiResponse.data.list);
                    if (list.size() != 0){
                        iv_red_dian.setText(list.size() + "");
                        iv_red_dian.setVisibility(View.VISIBLE);
                    }else{
                        iv_red_dian.setVisibility(View.GONE);
                    }
                    break;
                case 8:
                    price = apiResponse.data.price;
                    specCount=apiResponse.data.count;
                    if(price!=null&&price.length()>0){
                       try{
                           onePrice=Float.parseFloat(price);
                       }catch (Exception ex){}
                    }else{
                        price="0";
                    }
                    if(specCount!=null&&specCount.length()>0)
                    {}else{
                        specCount="0";
                    }
                    resetshowSpec();
                    /*
                    if (alertDialog != null && alertDialog.isShowing())
                        alertDialog.cancel();
                    price = apiResponse.data.price;
                    Log.e("zt-----",zt + "");
                    switch (zt) {
                        case 1:
                            Intent intent = new Intent();
                            intent.setClass(ShopDeataisActivity.this, MyOrderActivity.class);
                            List<Product> list = new ArrayList<>();
                            list.add(data);
                            String spec1Text="";
                            if(spec1!=null)
                            {
                                spec1Text=adapter.spec.specList.get(0).name;
                            }
                            if(spec2!=null)
                            {
                                spec1Text+=","+adapter2.spec.specList.get(0).name;
                            }

                            intent.putExtra("selcList", (Serializable) list);
                            intent.putExtra("curNum", tv_cutnumm.getText().toString());
                            intent.putExtra("spec1",spec1Text);
                            intent.putExtra(Constant.SCORE, "-1");
                            WJSJApplication.getInstance().addAcitivity(ShopDeataisActivity.this);
                            startActivity(intent);
                            if (alertDialog != null && alertDialog.isShowing())
                                alertDialog.cancel();
                            break;
                        case 2:
                            if (price.equals("")) {
                                price = "0";
                            }
                                if (spec2 == null) {
                                    String sl = adapter.spec.specList.get(0).id;
                                    WebServiceAPI.getInstance().addShopCar(data.id, tv_cutnumm.getText().toString(), price, sl,
                                            ShopDeataisActivity.this, ShopDeataisActivity.this);
                                } else {
                                    String sl = adapter.spec.specList.get(0).id + "," + adapter2.spec.specList.get(0).id;
                                    WebServiceAPI.getInstance().addShopCar(data.id, tv_cutnumm.getText().toString(), price, sl,
                                            ShopDeataisActivity.this, ShopDeataisActivity.this);
                                }

                            break;
                    }
                    */
                    break;
            }
        }

    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        Log.e("shopDetailErrorData:","**************************************************error:"+code+",tag:"+tag);
        if (alertDialog != null && alertDialog.isShowing())
            alertDialog.cancel();
    }

//    class Guige implements Serializable {
//        private static final long serialVersionUID = 1L;
//
//        public Guige(String id, String sid) {
//            this.id = id;
//            this.sid = sid;
//        }
//
//        public String id;
//        public String sid;
//    }

//    // 解析JSON，获取城市列表
//    private void testJson() {
//        listProvince.clear();
//        String jsonData_city = getFromAssets("json.txt");
//        Gson gson = new Gson();
//        listProvince = gson.fromJson(jsonData_city, new TypeToken<List<ProvinceBean>>() {
//        }.getType());
//    }
//
//    // 解析assets文件
//    private String getFromAssets(String fileName) {
//        String result = "";
//        try {
//            InputStream in = this.getResources().getAssets().open(fileName);
//            //InputStream in = context.getResources().getAssets().open(fileName);
//            // 获取文件的字节数
//            int lenght = in.available();
//            // 创建byte数组
//            byte[] buffer = new byte[lenght];
//            // 将文件中的数据读到byte数组中
//            in.read(buffer);
//
//            result = EncodingUtils.getString(buffer, "UTF-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
//
//    public String getCityNameById(String provinceId, String cityId) {
//        String names = "";
//        for (ProvinceBean provinceBean : listProvince) {
//            if (provinceBean.id.equals(provinceId)) {
//                names = provinceBean.name;
//                for (CityBean cityBean : provinceBean.cityList) {
//                    if (cityBean.id.equals(cityId)) {
//                        return names.equals(cityBean.name) ? names : (names + "省" + cityBean.name+"市");
//                    }
//                }
//            }
//        }
//        return names;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RETURN_REFRESH_CODE) {
            if (resultCode == RESULT_CANCELED) {
                WebServiceAPI.getInstance().scoreScoreproinfo(flagId, this, this);
            }
        }
            /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = CommentUtils.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}


