package com.bm.wjsj.SpiceStore;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.HideSoftInputActivity;
import com.bm.wjsj.Bean.AddressBean;
import com.bm.wjsj.Bean.Helper;
import com.bm.wjsj.Bean.Paytype;
import com.bm.wjsj.Bean.Product;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.Bean.ShopCarBean;
import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Bean.orderinfoBean;
import com.bm.wjsj.Bean.scoreproInfo;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.MainActivity;
import com.bm.wjsj.Personal.MyAddessAcitity;
import com.bm.wjsj.Personal.MyEvaluateActivity;
import com.bm.wjsj.Personal.MyOrdeHisItemAdapter;
import com.bm.wjsj.Personal.MyOrderDetailAdapter;
import com.bm.wjsj.Personal.MyOrderHisActivity;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.Utils.ToastDialogUtils;
import com.bm.wjsj.View.NoScrollListview;
import com.bm.wjsj.WJSJApplication;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 支付界面
 */
public class MyOrderActivity extends HideSoftInputActivity implements APICallback.OnResposeListener{

    private NoScrollListview lvMyOrde;
    ImageView rb_zfb, rb_wx, rb_yl, rb_df;
    private TextView tvPrice;
    private List<Product> list = new ArrayList<Product>();
    private List<orderinfoBean> listOrderInfo=new ArrayList<>();
    private orderBean orderBean;
    private int curNum;//订单数量
    private int scoreNum;//积分订单数量

    private String tag;//判断来源
    private LinearLayout rg_pay;

    private TextView tv_name;//收货人
    private TextView tv_my_phone;//收货人电话
    private TextView tv_my_address;//收货人地址
    private TextView tv_youhui_shuoming;//等级优惠说明
    private EditText et_Remark;//给卖家留言
    private String selectedPaytype="";//选择支付方式
    private String buyNowSpecText="";//由立即支付进入确认订单界面的规格型号
    private String buyNowSpecId="";//由立即支付进入确认订单界面的规格型号id
    private String zfb_by,wx_by,yl_by,df_by,selected_by="0";//微信、支付宝、银联、货到付款包邮价格，选择包邮价格
    private String gradeDiscount = "0";//等级优惠
    private String cityId,isfree,freight;//收货地址城市ID,是否免邮费【0包邮 1不包邮】，邮费
    private float allOrderPrice;//订单总价
    private TextView tv_my_score;//积分
    private TextView tv_my_order_num;
    private TextView tv_my_order_time;
    private TextView tv_my_pay_status;
    private String alls;//购物车总价
    private List<ShopCarBean> selcList = new ArrayList<>();
    private TextView tv_buy;
    private TextView tv_buy_status;

    private String scorepath,flagId,scorename,score;
    private scoreproInfo scoreproinfo;
    private DecimalFormat decimalFormat =new DecimalFormat("0.00");
    private AddressBean defaultAddress;
    private LinearLayout rl_top;
    private TextView tv_cancel;//取消订单
    private TextView tv_yunfei;//运费
    private View ll_fromorder_footer;//底部等级优惠
    private LinearLayout ll_remark;//底部备注列表
    private LinearLayout ll_footDetail;//底部等级优惠和支付列表
    private List<ProvinceBean> listProvince = new ArrayList<ProvinceBean>();
    private RelativeLayout rl_zfb;
    private RelativeLayout rl_wx;
    private RelativeLayout rl_yl;
    private RelativeLayout rl_df;
    private String orderOperationStatus="0";
    private static final int RETURN_REFRESH_CODE=11;
    private LinearLayout ll_wl;//物流信息

    private void assignViews() {
        initTitle(getResources().getString(R.string.oder_suer));

        lvMyOrde = (NoScrollListview) findViewById(R.id.lv_my_orde);
//        View odreTop = View.inflate(this, R.layout.address_top, null);
//        View footer = View.inflate(this, R.layout.address_footer, null);
        findViewById(R.id.rl_top).setOnClickListener(this);
        rl_top = (LinearLayout)findViewById(R.id.rl_top);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_my_phone = (TextView) findViewById(R.id.tv_my_phone);
        tv_my_address=(TextView)findViewById(R.id.tv_address);
        et_Remark=(EditText)findViewById(R.id.et_remark);
        et_Remark.addTextChangedListener(textWatcher);
        View ll_formmy = findViewById(R.id.ll_formmy);
        tvPrice = (TextView) findViewById(R.id.tv_price);

        tv_my_order_num=(TextView)findViewById(R.id.tv_order_num);
        tv_my_order_time=(TextView)findViewById(R.id.tv_order_time);
        tv_my_pay_status=(TextView)findViewById(R.id.tv_pay_status);
        //尾部
        rg_pay = (LinearLayout) findViewById(R.id.rg_pay);
        rb_zfb = (ImageView) findViewById(R.id.rb_zfb);
        rb_wx = (ImageView) findViewById(R.id.rb_wx);
        rb_yl = (ImageView) findViewById(R.id.rb_yl);
        rb_df = (ImageView) findViewById(R.id.rb_df);
        tv_buy_status=(TextView) findViewById(R.id.tv_buy_status);
        tv_buy = (TextView) findViewById(R.id.tv_buy);
        tv_buy.setOnClickListener(this);
        ll_fromorder_footer = findViewById(R.id.ll_fromorder_footer);
        ll_remark= (LinearLayout)findViewById(R.id.ll_remark);
        ll_footDetail= (LinearLayout)findViewById(R.id.ll_foot_detail);

         rl_zfb = (RelativeLayout) findViewById(R.id.rl_zfb);
         rl_wx = (RelativeLayout) findViewById(R.id.rl_wx);
         rl_yl = (RelativeLayout) findViewById(R.id.rl_yl);
         rl_df = (RelativeLayout) findViewById(R.id.rl_df);
        rl_zfb.setOnClickListener(this);
        rl_wx.setOnClickListener(this);
        rl_yl.setOnClickListener(this);
        rl_df.setOnClickListener(this);


        findViewById(R.id.view_divider).setVisibility(View.VISIBLE);
        View ll_footer = findViewById(R.id.ll_footer);
//        lvMyOrde.addHeaderView(odreTop, null, false);
//        lvMyOrde.addFooterView(footer, null, false);
//        list.addAll(getImageList(Constant.imageUrls, 2));

        tv_youhui_shuoming = (TextView) findViewById(R.id.tv_youhui_shuoming);
        tv_youhui_shuoming.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_youhui_shuoming.getPaint().setAntiAlias(true);//抗锯齿
        tv_youhui_shuoming.setOnClickListener(this);
        RelativeLayout rl_price = (RelativeLayout) findViewById(R.id.rl_price);
        LinearLayout ll_myscore = (LinearLayout) findViewById(R.id.ll_myscore);
        LinearLayout ll_price = (LinearLayout) findViewById(R.id.ll_price);
        tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
        TextView tv_pay_time = (TextView) findViewById(R.id.tv_pay_time);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_my_score = (TextView) findViewById(R.id.tv_my_score);
        tv_cancel.setOnClickListener(this);

        //物流
        ll_wl = (LinearLayout) findViewById(R.id.ll_wl);
        TextView tv_wl_com = (TextView) findViewById(R.id.tv_wu_com);
        TextView tv_wl_no = (TextView) findViewById(R.id.tv_wu_no);


        initDefaultAddress();
        switch (tag) {
            case "-1"://商城
                MyOrderAdapter adapter = new MyOrderAdapter(this, list,curNum);
                lvMyOrde.setAdapter(adapter);

                ll_wl.setVisibility(View.GONE);
                ll_footer.setVisibility(View.VISIBLE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.GONE);
                tv_yunfei.setVisibility(View.VISIBLE);
                ll_fromorder_footer.setVisibility(View.VISIBLE);
                ll_formmy.setVisibility(View.GONE);
                tv_pay_time.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                rg_pay.setVisibility(View.VISIBLE);
                tv_title.setText("确认订单");
                //wangxl 2015.10.19 update
                tv_buy.setText("确认订单");
                //tv_buy.setText("立即购买");

                break;
            case "-2"://积分
                WebServiceAPI.getInstance().scoreScoreproinfo(flagId, this, this);
                WebServiceAPI.getInstance().scoreMyscore(this, this);

                ll_wl.setVisibility(View.GONE);
                ll_formmy.setVisibility(View.GONE);
                ll_footer.setVisibility(View.GONE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.VISIBLE);
                tv_yunfei.setVisibility(View.GONE);
                ll_price.setVisibility(View.GONE);
                ll_fromorder_footer.setVisibility(View.VISIBLE);
                tv_pay_time.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                rg_pay.setVisibility(View.GONE);
                tv_title.setText("兑换确认");
                tv_buy.setText("立即兑换");
                break;
            case "-3"://购物车
                MyShoppingAdapter adapter_3 = new MyShoppingAdapter(this, selcList);
                lvMyOrde.setAdapter(adapter_3);

                ll_wl.setVisibility(View.GONE);
                ll_footer.setVisibility(View.VISIBLE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.GONE);
                tv_yunfei.setVisibility(View.VISIBLE);
                ll_fromorder_footer.setVisibility(View.VISIBLE);
                ll_formmy.setVisibility(View.GONE);
                tv_pay_time.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                rg_pay.setVisibility(View.VISIBLE);
                tv_title.setText("我的订单");
                //wangxl 2015.10.19 update
                tv_buy.setText("确认订单");
                //tv_buy.setText("立即购买");

                break;
            case "0"://待付款
                MyOrderDetailAdapter adapterOrderInfo = new MyOrderDetailAdapter(this, listOrderInfo);
                lvMyOrde.setAdapter(adapterOrderInfo);
                if(orderBean!=null) {
                    tv_my_order_num.setText(orderBean.ordernum);
                    tv_my_order_time.setText(orderBean.createTime);
                    tv_my_pay_status.setText("待付款");
                    tvPrice.setText(decimalFormat.format(allOrderPrice));
                    et_Remark.setText(orderBean.remark);
                    initPayAddress();
                    //计算邮费
                    int perOrderCount=0;
                    float orderFreight=0,orderGoodsCost=0,perOrderPrice=0;
                    try {
                        for (orderinfoBean bean : listOrderInfo) {
                            perOrderPrice = Float.parseFloat(bean.price);
                            perOrderCount = Integer.parseInt(bean.count);
                            orderGoodsCost = orderGoodsCost + perOrderCount * perOrderPrice;
                        }
                    }
                    catch (Exception ex){}
                    orderFreight=allOrderPrice-orderGoodsCost;
                    if(orderFreight>0) {
                        tv_yunfei.setText(String.format("已含运费:%s元", decimalFormat.format(orderFreight)));
                    }
                    else{
                        tv_yunfei.setText(String.format("已含运费:%s", "免费"));
                    }
                    initOrderPayType(orderBean.paytype);
                }

                ll_wl.setVisibility(View.GONE);
                ll_footer.setVisibility(View.VISIBLE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.GONE);
                tv_yunfei.setVisibility(View.VISIBLE);
                ll_fromorder_footer.setVisibility(View.VISIBLE);
                ll_formmy.setVisibility(View.VISIBLE);
                tv_pay_time.setVisibility(View.GONE);
                rg_pay.setVisibility(View.VISIBLE);
                tv_cancel.setVisibility(View.VISIBLE);
                tv_buy.setText("立即支付");
                tv_title.setText("待付款订单详情");
                rl_top.setEnabled(false);
                et_Remark.setEnabled(false);
                rg_pay.setEnabled(false);
                break;
            case "1"://1待发货
                MyOrderDetailAdapter adapterOrderInfo1 = new MyOrderDetailAdapter(this, listOrderInfo);
                lvMyOrde.setAdapter(adapterOrderInfo1);
                if(orderBean!=null) {
                    tv_my_order_num.setText(orderBean.ordernum);
                    tv_my_order_time.setText(orderBean.createTime);
                    tv_my_pay_status.setText("待发货");
                    tvPrice.setText(decimalFormat.format(allOrderPrice));
                    tv_pay_time.setText("支付时间:"+orderBean.paytime);
                    et_Remark.setText(orderBean.remark);
                    initPayAddress();
                }

                //ll_footer.setVisibility(View.GONE);
                ll_wl.setVisibility(View.VISIBLE);
                ll_remark.setVisibility(View.VISIBLE);
                ll_footDetail.setVisibility(View.GONE);
                tv_pay_time.setVisibility(View.VISIBLE);
                tv_buy.setVisibility(View.GONE);
                tv_buy_status.setVisibility(View.VISIBLE);
                tv_yunfei.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_title.setText("待收货订单详情");
                tv_buy_status.setText("待发货");
                rg_pay.setVisibility(View.GONE);
                rl_top.setEnabled(false);
                et_Remark.setEnabled(false);
                break;
            case "2"://待收货
                MyOrderDetailAdapter adapterOrderInfo2 = new MyOrderDetailAdapter(this, listOrderInfo);
                lvMyOrde.setAdapter(adapterOrderInfo2);
                if(orderBean!=null) {
                    tv_my_order_num.setText(orderBean.ordernum);
                    tv_my_order_time.setText(orderBean.createTime);
                    tv_my_pay_status.setText("待收货");
                    tvPrice.setText(decimalFormat.format(allOrderPrice));
                    tv_pay_time.setText("支付时间:"+orderBean.paytime);
                    et_Remark.setText(orderBean.remark);
                    tv_wl_com.setText(orderBean.expcompany);
                    tv_wl_no.setText(orderBean.expnumber);
                    initPayAddress();
                }

                //ll_footer.setVisibility(View.GONE);
                ll_wl.setVisibility(View.VISIBLE);
                ll_remark.setVisibility(View.VISIBLE);
                ll_footDetail.setVisibility(View.GONE);
                tv_pay_time.setVisibility(View.VISIBLE);
                tv_buy.setText("确认收货");
                tv_yunfei.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_title.setText("待收货订单详情");
                rg_pay.setVisibility(View.GONE);
                rl_top.setEnabled(false);
                et_Remark.setEnabled(false);
                break;
            case "3"://待评价
                MyOrdeHisItemAdapter adapterOrderInfo3 = new MyOrdeHisItemAdapter(this, listOrderInfo,"3",orderBean,"orderDetail");
                lvMyOrde.setAdapter(adapterOrderInfo3);
                if(orderBean!=null) {
                    tv_my_order_num.setText(orderBean.ordernum);
                    tv_my_order_time.setText(orderBean.createTime);
                    tv_my_pay_status.setText("待评价");
                    tvPrice.setText(decimalFormat.format(allOrderPrice));
                    tv_pay_time.setText("支付时间:"+orderBean.paytime);
                    et_Remark.setText(orderBean.remark);
                    tv_wl_com.setText(orderBean.expcompany);
                    tv_wl_no.setText(orderBean.expnumber);
                    initPayAddress();
                }

                //ll_footer.setVisibility(View.GONE);
                ll_wl.setVisibility(View.VISIBLE);
                ll_remark.setVisibility(View.VISIBLE);
                ll_footDetail.setVisibility(View.GONE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.GONE);
                ll_fromorder_footer.setVisibility(View.GONE);
                ll_formmy.setVisibility(View.VISIBLE);
                tv_pay_time.setVisibility(View.VISIBLE);

                tv_buy.setVisibility(View.GONE);
                tv_buy_status.setText("去评价");
                tv_buy_status.setVisibility(View.VISIBLE);

                tv_yunfei.setVisibility(View.GONE);
                //rg_pay.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_title.setText("待评价订单详情");
                rl_top.setEnabled(false);
                et_Remark.setEnabled(false);
                break;
            case "4"://已完成
                MyOrderDetailAdapter adapterOrderInfoFinish = new MyOrderDetailAdapter(this, listOrderInfo);
                lvMyOrde.setAdapter(adapterOrderInfoFinish);
                if(orderBean!=null) {
                    tv_my_order_num.setText(orderBean.ordernum);
                    tv_my_order_time.setText(orderBean.createTime);
                    tv_my_pay_status.setText("已完成");
                    tvPrice.setText(decimalFormat.format(allOrderPrice));
                    tv_pay_time.setText("支付时间:"+orderBean.paytime);
                    et_Remark.setText(orderBean.remark);
                    tv_wl_com.setText(orderBean.expcompany);
                    tv_wl_no.setText(orderBean.expnumber);
                    initPayAddress();
                }

                //ll_footer.setVisibility(View.GONE);
                ll_wl.setVisibility(View.VISIBLE);
                ll_remark.setVisibility(View.VISIBLE);
                ll_footDetail.setVisibility(View.GONE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.GONE);
                tv_yunfei.setVisibility(View.GONE);
                ll_fromorder_footer.setVisibility(View.GONE);
                ll_formmy.setVisibility(View.VISIBLE);
                tv_pay_time.setVisibility(View.VISIBLE);
                rg_pay.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_buy.setVisibility(View.GONE);
                tv_title.setText("已完成订单详情");
                rl_top.setEnabled(false);
                et_Remark.setEnabled(false);
                break;
            case "5"://已取消
                MyOrderDetailAdapter adapterOrderInfoCancle = new MyOrderDetailAdapter(this, listOrderInfo);
                lvMyOrde.setAdapter(adapterOrderInfoCancle);
                if(orderBean!=null) {
                    tv_my_order_num.setText(orderBean.ordernum);
                    tv_my_order_time.setText(orderBean.createTime);
                    tvPrice.setText(decimalFormat.format(allOrderPrice));
                    tv_my_pay_status.setText("已取消");
                    et_Remark.setText(orderBean.remark);
                    tv_wl_com.setText(orderBean.expcompany);
                    tv_wl_no.setText(orderBean.expnumber);
                    initPayAddress();
                }

                ll_wl.setVisibility(View.VISIBLE);
                ll_footer.setVisibility(View.GONE);
                rl_price.setVisibility(View.VISIBLE);
                ll_myscore.setVisibility(View.GONE);
                tv_yunfei.setVisibility(View.GONE);
                ll_fromorder_footer.setVisibility(View.GONE);
                ll_formmy.setVisibility(View.VISIBLE);
                tv_pay_time.setVisibility(View.GONE);
                rg_pay.setVisibility(View.GONE);
                tv_cancel.setVisibility(View.GONE);
                tv_buy.setVisibility(View.GONE);
                tv_title.setText("已取消订单详情");
                rl_top.setEnabled(false);
                et_Remark.setEnabled(false);
                break;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        tag = getIntent().getStringExtra(Constant.SCORE);
        selectedPaytype = PayTypeEnum.ZhiFuBao.getName();//默认支付方式为支付宝
        initPayFont(selectedPaytype);
        switch (tag) {
            case "-1":
                list.addAll((List<Product>) getIntent().getSerializableExtra("selcList"));
                String curNumText = getIntent().getStringExtra("curNum");
                buyNowSpecText = getIntent().getStringExtra("spec1");
                buyNowSpecId = getIntent().getStringExtra("speclist");
                try {
                    if (curNumText == null || curNumText == "")
                        curNum = 0;
                    else
                        curNum = Integer.parseInt(curNumText);
                    String salePriceText = getIntent().getStringExtra("specPrice");
                    float salePrice = 0;
                    for (int i = 0; i < list.size(); i++) {
                        //salePriceText = list.get(i).price;
                        //重新修改list集合中product的价格
                        list.get(i).price = salePriceText;
                        if (salePriceText == null || salePriceText == "")
                            salePrice = 0;
                        else {
                            salePrice = Float.parseFloat(salePriceText);
                        }
                        allOrderPrice = allOrderPrice + curNum * salePrice;
                    }
                }catch (Exception ex){}
                break;
            case "-2":
                flagId = getIntent().getStringExtra("flagId");
                String curScoreNumText = getIntent().getStringExtra("curNum");
                try {
                    if (curScoreNumText == null || curScoreNumText == "")
                         scoreNum= 0;
                    else
                        scoreNum = Integer.parseInt(curScoreNumText);
                }catch (Exception ex){}
                break;
            case "-3":
                selcList.addAll((List<ShopCarBean>) getIntent().getSerializableExtra("selcList"));
                alls = getIntent().getStringExtra("alls");
                float data = Float.parseFloat(alls);
                allOrderPrice = data;
                break;
            default:
                listOrderInfo.addAll((List<orderinfoBean>) getIntent().getSerializableExtra("selcList"));
                orderBean = (orderBean) getIntent().getSerializableExtra("selOrder");
                //Log.e("orderBean1:","-----------------"+orderBean.address);
                String temOrderPrice = orderBean.realpay;
                try {
                    allOrderPrice = Float.parseFloat(temOrderPrice);
                } catch (Exception ex) {
                    allOrderPrice = 0;
                }
                break;
        }

        assignViews();

        //*************先获取收获地址列表，得到默认地址(城市id)；然后根据登陆id得到等级优惠信息(支付包邮信息，默认包邮价格)；再然后初始化邮费（需要城市id,得到邮费）；最后计算费用（需要邮费，选中包邮价格）*******************//
        //初始化邮费,放在初始化城市id之后调用
//        if ("-1".equals(tag)  || "-2" .equals( tag) || "-3" .equals( tag)) {
//            initFreight();
//        }
        //收货地址列表

        if ("-1".equals(tag)  || "-2" .equals( tag) || "-3" .equals( tag)) {
            WebServiceAPI.getInstance().addrList(this, this);
        }

        //根据登陆id判断等级优惠
        //WebServiceAPI.getInstance().findpaytype(this, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_zfb:
                onresrt();
                rb_zfb.setImageResource(R.mipmap.pay_check);
                //1.选择支付方式修改
                selectedPaytype = PayTypeEnum.ZhiFuBao.getName();
                //2.包邮价格修改
                selected_by = zfb_by;
                //3.字体修改
                initPayFont(selectedPaytype);
                //4.费用修改
                setNum();
                //5.订单修改
                updateOrder();
                break;
            case R.id.rl_wx:
                onresrt();
                rb_wx.setImageResource(R.mipmap.pay_check);
                selectedPaytype = PayTypeEnum.WinXin.getName();
                selected_by = wx_by;
                //3.字体修改
                initPayFont(selectedPaytype);
                setNum();
                updateOrder();
                break;
            case R.id.rl_yl:
                onresrt();
                rb_yl.setImageResource(R.mipmap.pay_check);
                selectedPaytype = PayTypeEnum.YinLian.getName();
                selected_by = yl_by;
                //3.字体修改
                initPayFont(selectedPaytype);
                setNum();
                updateOrder();
                break;
            case R.id.rl_df:
                onresrt();
                rb_df.setImageResource(R.mipmap.pay_check);
                selectedPaytype = PayTypeEnum.DaoFu.getName();
                selected_by = df_by;
                //3.字体修改
                initPayFont(selectedPaytype);
                setNum();
                updateOrder();
                break;
            case R.id.tv_youhui_shuoming:
                WebServiceAPI.getInstance().help9("9", MyOrderActivity.this, MyOrderActivity.this);
                //Intent intent = new Intent(this, PrivilegeActivity.class);
                //intent.putExtra(Constant.WEB_NAME, "用户等级优惠说明");
                //startActivity(intent);
                break;
            case R.id.rl_top:
                Intent intent2 = new Intent(this, MyAddessAcitity.class);
                intent2.putExtra("isLook", 1);
                //startActivityForResult(intent2, 1000);
                startActivityForResult(intent2, 999);
                break;
            case R.id.tv_buy:
                switch (tag) {//0待付款 1待发货 2待收货 3待评价 4已完成 5已取消 -1:商城
                    case "-1"://商城
                        //wangxl 2015.10.19 update begin...
                        // String sl = adapter.spec.specList.get(0).id + "," + adapter2.spec.specList.get(0).id;
                        if("".equals(tv_my_address.getText())||"".equals(tv_my_phone.getText())||"".equals(tv_name.getText()))
                        {
                            NewToast.show(this, "请选择收货地址！", Toast.LENGTH_LONG);
                        }else {
                            gotoPay();
                        }
//                        Intent payIntent0 = new Intent(this, PaySuccessActivity.class);
//                        payIntent0.putExtra("ISUCCESS", false);
//                        WJSJApplication.getInstance().addAcitivity(this);
//                        startActivity(payIntent0);
//                        this.finish();
                        //wangxl 2015.10.19 update end...
                        break;
                    case "-2"://积分
//                        String consignee= tv_name.getText().toString();
//                        String mobile=tv_my_phone.getText().toString();
//                        String cityid=cityId;
//                        String address= tv_my_address.getText().toString();
                        if("".equals(tv_my_address.getText())||"".equals(tv_my_phone.getText())||"".equals(tv_name.getText()))
                        {
                            NewToast.show(this, "请选择收货地址！", Toast.LENGTH_LONG);
                        }else {
                            gotoPay();
                        }

                        break;
                    case "-3"://购物车
                        if("".equals(tv_my_address.getText())||"".equals(tv_my_phone.getText())||"".equals(tv_name.getText()))
                        {
                            NewToast.show(this, "请选择收货地址！", Toast.LENGTH_LONG);
                        }else {
                            gotoPay();
                        }
                        break;
                    case "0"://待付款
                        //如果是从订单过来的待付款订单，如果支付方式是微信支付，支付前调用修改编号接口，防止订单编号重复而支付失败
                        if (PayTypeEnum.WinXin.getName().equals(orderBean.paytype)) {
                            WebServiceAPI.getInstance().updateOrderNum(orderBean.id, MyOrderActivity.this, MyOrderActivity.this);
                        } else if (PayTypeEnum.ZhiFuBao.getName().equals(orderBean.paytype)){
                            AliPayActivity aliPay=new AliPayActivity(MyOrderActivity.this, orderBean);
                            aliPay.pay();
                        }
                        /*
                        if(orderBean!=null) {
                            if (PayTypeEnum.WinXin.getName().equals(orderBean.paytype)) {
                                WXPayActivity wxPay = new WXPayActivity(MyOrderActivity.this, orderBean);
                                wxPay.sendWXPayReq();
                            } else {

                                AliPayActivity aliPay=new AliPayActivity(MyOrderActivity.this, orderBean);
                                aliPay.pay();

                            }
                        }
*/
                        break;
                    case "1"://待发货
                        //WebServiceAPI.getInstance().updateOrderStatus(orderBean.id, "2", MyOrderActivity.this, MyOrderActivity.this);
                        //ToastDialogUtils.getInstance().ShowToastHaveCancle(this, "请确认您是否收到商品?", "如果您未收到商品请您不要点击“确认”", false);
                        break;
                    case "2"://待收货
                        showReceipt();
                        //ToastDialogUtils.getInstance().ShowToastHaveCancle(this, "请确认您是否收到商品?", "如果您未收到商品请您不要点击“确认”", false);
                        break;
                    case "3"://待评价
                        Intent pingjiaIntent = new Intent(this, MyEvaluateActivity.class);
                        startActivity(pingjiaIntent);
                        break;
                }

//
                break;
            case R.id.tv_cancel:
                showSure();
                break;

        }
    }

    //重新计算邮费
    private void initFreight()
    {
        if (!tag.equals("-2")) {
            String productids = "";
            if ("-1".equals(tag)) {//商城
                for (int i = 0; i < list.size(); i++) {
                    productids += list.get(i).id + ",";
                }
            } else if ("-3".equals(tag)) {//购物车
                for (int i = 0; i < selcList.size(); i++) {
                    productids += selcList.get(i).productid + ",";
                }
            } else {//订单
                for (int i = 0; i < listOrderInfo.size(); i++) {
                    productids += listOrderInfo.get(i).productid + ",";
                }
            }
            if (productids.length() > 1) {
                productids = productids.substring(0, productids.length() - 1);
            }
            WebServiceAPI.getInstance().findfreight(cityId, productids,
                    MyOrderActivity.this, MyOrderActivity.this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if(requestCode==1000){
                Intent intentResult = new Intent();
                setResult(RESULT_OK, intentResult);
                this.finish();
            }else {
                defaultAddress = (AddressBean) data.getSerializableExtra(Constant.ID);
                //1.修改界面显示地址信息
                initDefaultAddress();
                if (!tag.equals("-2")) {
                    //2.重新计算邮费
                    initFreight();
                    //3.修改订单信息
                    updateOrder();
                }
            }
        }

        if(requestCode == RETURN_REFRESH_CODE) {
            if (resultCode == RESULT_CANCELED) {
                WebServiceAPI.getInstance().scoreScoreproinfo(flagId, this, this);
            }
        }
    }

    /*
    // 解析JSON，获取城市列表
    private void getJson() {
        listProvince.clear();
        String jsonData_city = getFromAssets("city.json");
        Gson gson = new Gson();
        listProvince = gson.fromJson(jsonData_city, new TypeToken<List<ProvinceBean>>() {
        }.getType());
    }

    // 解析assets文件
    private String getFromAssets(String fileName) {
        String result = "";
        InputStream in=null;
        try {
            in = this.getResources().getAssets().open(fileName);
            //InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);

            result = EncodingUtils.getString(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(in!=null){
                try{
                    in.close();
                }catch (Exception ex){}
            }
        }
        return result;
    }

    private String getCityNameById(String provinceId, String cityId) {
        String names = "";
        for (ProvinceBean provinceBean : listProvince) {
            if (provinceBean.id.equals(provinceId)) {
                names = provinceBean.name;
                for (CityBean cityBean : provinceBean.children) {
                    if (cityBean.id.equals(cityId)) {
                        return names.equals(cityBean.name) ? names+"市" : (names + " " + cityBean.name+"市");
                    }
                }
            }
        }
        return names;
    }
*/

    private void onresrt() {
        rb_zfb.setImageResource(R.mipmap.pay_uncheck);
        rb_wx.setImageResource(R.mipmap.pay_uncheck);
        rb_yl.setImageResource(R.mipmap.pay_uncheck);
        rb_df.setImageResource(R.mipmap.pay_uncheck);

    }


    private void initDefaultAddress() {
        tv_my_address.setText("");
        tv_my_phone.setText("");
        tv_name.setText("");

        //tv_buy.setEnabled(false);
        //tv_buy.setBackgroundResource(R.mipmap.btn_unuse);
        if (defaultAddress != null) {
            //String city=getCityNameById(defaultAddress.provinceid,defaultAddress.cityid);
            String city= AddressUtil.getInstance(MyOrderActivity.this).getCityNameById(defaultAddress.provinceid,defaultAddress.cityid);
            tv_my_address.setText(city+defaultAddress.address);
            tv_my_phone.setText(defaultAddress.mobile);
            tv_name.setText(defaultAddress.consignee);
            cityId=defaultAddress.cityid;
            tv_buy.setEnabled(true);
            tv_buy.setBackgroundResource(R.mipmap.buy);
        }
    }

    private void initPayAddress() {
        tv_my_address.setText("");
        tv_my_phone.setText("");
        tv_name.setText("");

        //tv_buy.setEnabled(false);
        //tv_buy.setBackgroundResource(R.mipmap.btn_unuse);
        if (orderBean != null) {
            //String city=getCityNameById(defaultAddress.provinceid,defaultAddress.cityid);

            //String city= AddressUtil.getInstance(MyOrderActivity.this).getCityNameById(defaultAddress.provinceid,defaultAddress.cityid);
            Log.e("address:","**************"+orderBean.address);
            Log.e("mobile:","**************"+orderBean.mobile);
            Log.e("consignee:","**************"+orderBean.consignee);
            Log.e("cityid:","**************"+orderBean.cityid);
            tv_my_address.setText(orderBean.address);
            tv_my_phone.setText(orderBean.mobile);
            tv_name.setText(orderBean.consignee);
            cityId=orderBean.cityid;
            tv_buy.setEnabled(true);
            tv_buy.setBackgroundResource(R.mipmap.buy);
        }
    }

    private void initPayFont(String orderPayType) {
        TextView tv_wx = (TextView) findViewById(R.id.tv_wx);
        TextView tv_wx_by = (TextView) findViewById(R.id.tv_wx_by);
        TextView tv_zfb = (TextView) findViewById(R.id.tv_zfb);
        TextView tv_zfb_by = (TextView) findViewById(R.id.tv_zfb_by);
        TextView tv_df = (TextView) findViewById(R.id.tv_df);
        TextView tv_df_by = (TextView) findViewById(R.id.tv_df_by);

        tv_wx.setTextColor(getResources().getColor(R.color.oder_pay));
        tv_wx_by.setTextColor(getResources().getColor(R.color.oder_pay));
        tv_zfb.setTextColor(getResources().getColor(R.color.oder_pay));
        tv_zfb_by.setTextColor(getResources().getColor(R.color.oder_pay));
        tv_df.setTextColor(getResources().getColor(R.color.oder_pay));
        tv_df_by.setTextColor(getResources().getColor(R.color.oder_pay));

        if (orderPayType.equals(PayTypeEnum.WinXin.getName())) {
            tv_wx.setTextColor(getResources().getColor(R.color.deafaut_text));
            tv_wx_by.setTextColor(getResources().getColor(R.color.deafaut_text));
        } else if (orderPayType.equals(PayTypeEnum.ZhiFuBao.getName())) {
            tv_zfb.setTextColor(getResources().getColor(R.color.deafaut_text));
            tv_zfb_by.setTextColor(getResources().getColor(R.color.deafaut_text));
        } else {
            tv_df.setTextColor(getResources().getColor(R.color.deafaut_text));
            tv_df_by.setTextColor(getResources().getColor(R.color.deafaut_text));
        }
    }

    private void initOrderPayType(String orderPayType) {
        onresrt();
        rl_zfb.setEnabled(false);
        rl_wx.setEnabled(false);
        rl_df.setEnabled(false);
        if (orderPayType.equals(PayTypeEnum.WinXin.getName())) {
            rb_wx.setImageResource(R.mipmap.pay_check);
        } else if (orderPayType.equals(PayTypeEnum.ZhiFuBao.getName())) {
            rb_zfb.setImageResource(R.mipmap.pay_check);
        } else {
            rb_df.setImageResource(R.mipmap.pay_check);
        }
        initPayFont(orderPayType);
    }

    //去支付
    private void gotoPay() {
        String ids = "";
        String consignee = tv_name.getText().toString();
        String mobile = tv_my_phone.getText().toString();
        String cityid = cityId;
        String address = tv_my_address.getText().toString();

        String realpay = tvPrice.getText().toString();
        String remark = et_Remark.getText().toString();

        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        boolean hasStock=true;
        switch (tag) {
            case "-1":
                for (int i = 0; i < list.size(); i++) {
                    map = new HashMap<>();
                    map.put("productid", list.get(i).id);
                    map.put("pname", list.get(i).name);
                    map.put("spec1", buyNowSpecText);
                    map.put("speclist", buyNowSpecId);
                    map.put("count", curNum);
                    String stockS= TextUtils.isEmpty(list.get(i).count)?"0":list.get(i).count;
                    Float stockF=Float.parseFloat(stockS);
                    if(curNum>stockF){
                        NewToast.show(this, "库存不足！", Toast.LENGTH_LONG);
                        return;
                    }
                    if (list.get(i).picList.size() > 0) {
                        map.put("path", list.get(i).picList.get(0).path);
                    } else {
                        map.put("path", list.get(i).hpicUrl);
                    }
                    listMap.add(map);
                }
                break;
            case "-2":
                try {
                    if (consignee.equals("") || mobile.equals("") || address.equals("")) {
                        NewToast.show(this, "请选择收货地址！", Toast.LENGTH_LONG);
                    } else {
                        Float goodsScoreF = Float.parseFloat(scoreproinfo.score);
                        Float myScoreF = Float.parseFloat(score);
                        int allScore = (int) (scoreNum * goodsScoreF);
                        String stockS= TextUtils.isEmpty(scoreproinfo.stock)?"0":scoreproinfo.stock;
                        Float stockF=Float.parseFloat(stockS);
                        if(scoreNum>stockF)
                        {
                            NewToast.show(this, "库存不足！", Toast.LENGTH_LONG);
                            return;
                        }else {
                            if (scoreNum * goodsScoreF > myScoreF) {
                                NewToast.show(this, "积分不足！", Toast.LENGTH_LONG);
                            } else {
                                WebServiceAPI.getInstance().scoreAddscoreproorder(consignee, mobile,
                                        address, flagId, allScore + "", ""+scoreNum,MyOrderActivity.this, MyOrderActivity.this);
                            }
                        }
                    }
                } catch (Exception ex) {
                    NewToast.show(this, "兑换失败！", Toast.LENGTH_LONG);
                }
                break;
            case "-3":
                for (int i = 0; i < selcList.size(); i++) {
                    map = new HashMap<>();
                    map.put("productid", selcList.get(i).productid);
                    map.put("pname", selcList.get(i).pname);
                    map.put("spec1", selcList.get(i).spec);
                    map.put("speclist", selcList.get(i).speclist);
                    map.put("count", selcList.get(i).count);
                    map.put("path", selcList.get(i).path);
                    String stockS= TextUtils.isEmpty(selcList.get(i).stock)?"0":selcList.get(i).stock;
                    Float stockF=Float.parseFloat(stockS);
                    Float countF=Float.parseFloat(selcList.get(i).count);
                    if(countF>stockF){
                        NewToast.show(this, "库存不足！", Toast.LENGTH_LONG);
                        return;
                    }
                    if (ids.equals("")) {
                        ids = selcList.get(i).id;
                    } else {
                        ids = ids + "," + selcList.get(i).id;
                    }
                    listMap.add(map);
                }
                break;
            default:
                for (int i = 0; i < listOrderInfo.size(); i++) {
                    map = new HashMap<>();
                    map.put("productid", listOrderInfo.get(i).productid);
                    map.put("pname", listOrderInfo.get(i).pname);
                    map.put("spec1", listOrderInfo.get(i).spec1);
                    map.put("count", listOrderInfo.get(i).count);
                    map.put("path", listOrderInfo.get(i).path);
                    listMap.add(map);
                }
                break;
        }
        if (!tag.equals("-2")) {
            Gson gsonArray = new Gson();
            String orderlist = gsonArray.toJson(listMap);
            if (selectedPaytype.equals(PayTypeEnum.DaoFu.getName())) {
                WebServiceAPI.getInstance().confirmorder(ids, consignee, mobile, cityid, address, selectedPaytype, realpay, remark, orderlist,
                        MyOrderActivity.this, MyOrderActivity.this);
                //showSureBuy(ids, consignee, mobile, cityid, address, realpay, remark, orderlist);
            } else {
                WebServiceAPI.getInstance().confirmorder(ids, consignee, mobile, cityid, address, selectedPaytype, realpay, remark, orderlist,
                        MyOrderActivity.this, MyOrderActivity.this);
            }
        }
    }

    //修改订单
    private void updateOrder() {
        if ("0".equals(tag)) {//待付款
            if (orderBean == null) {
                NewToast.show(this, "请选择订单！", Toast.LENGTH_LONG);
            } else {
                String id = orderBean.id;
                String consignee = tv_name.getText().toString();
                String mobile = tv_my_phone.getText().toString();
                String address = tv_my_address.getText().toString();
                String realpay = tvPrice.getText().toString();
                String remark = et_Remark.getText().toString();
                WebServiceAPI.getInstance().updateOrder(id, consignee, mobile, cityId, address, selectedPaytype, realpay, remark,
                        MyOrderActivity.this, MyOrderActivity.this);
            }
        }
    }


    //计算费用，优惠价
    //通过城市ID返回不包邮，但是已经满68了，就包邮，运费为0
    //通过城市ID返回包邮，仍然显示满68包邮，但运费为0
    //freight:邮费  gradeDiscount;等级优惠  isfree:是否免邮费【0包邮 1不包邮】
    private void setNum() {
        float freightPrice=0,selected_byPrice=0,discountPrice=0;
        try {
        float cutPercent=Float.parseFloat(gradeDiscount)/100;
        if(cutPercent>100)
            cutPercent=100;
        if(cutPercent<0)
            cutPercent=0;
//        float freightPrice = Float.parseFloat(freight);
//        float selected_byPrice = Float.parseFloat(selected_by);
//        //优惠价
//        float discountPrice=allOrderPrice*(1-cutPercent);
//        BigDecimal bigDecimal=new BigDecimal(discountPrice);
//        float discountPriceRound= bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP).floatValue();


            freightPrice = Float.parseFloat(freight);
            selected_byPrice = Float.parseFloat(selected_by);
            //优惠价
            discountPrice = allOrderPrice * (1 - cutPercent);
        }catch (Exception ex){}
        //BigDecimal bigDecimal=new BigDecimal(discountPrice);
        //float discountPriceRound= bigDecimal.setScale(0,BigDecimal.ROUND_HALF_UP).floatValue();
        TextView tv_youhuiPrice = (TextView) findViewById(R.id.tv_youhuiPrice);
        DecimalFormat youhuiFormat =new DecimalFormat("0");
        tv_youhuiPrice.setText(String.format("%s元", youhuiFormat.format(discountPrice)));

        float realPrice=allOrderPrice-discountPrice;

        //判断是否可以包邮
        if(isfree!=null && isfree.equals("0"))//包邮
        {
            //BigDecimal bigRealPrice=new BigDecimal(realPrice);
            //float bigRealPriceRound= bigRealPrice.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

            tvPrice.setText("" + decimalFormat.format(realPrice));
            TextView tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
            tv_yunfei.setText(String.format("已含运费:%s", "免费"));
        }
        else {
            if (realPrice < selected_byPrice) {
                realPrice = realPrice + freightPrice;
                tvPrice.setText("" + decimalFormat.format(realPrice));
                TextView tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
                if(freightPrice>0) {
                    tv_yunfei.setText(String.format("已含运费:%s元", decimalFormat.format(freightPrice)));
                }
                else{
                    tv_yunfei.setText(String.format("已含运费:%s", "免费"));
                }
            }
            else{
                tvPrice.setText("" + decimalFormat.format(realPrice));
                TextView tv_yunfei = (TextView) findViewById(R.id.tv_yunfei);
                tv_yunfei.setText(String.format("已含运费:%s", "免费"));
            }

        }
    }

    //初始化用户等级优惠信息
    private  void initUserDiscount(List<Paytype> paytypeList,String discount)
    {
        TextView tv_zfb_by,tv_wx_by,tv_yl_by,tv_df_by;
        for(Paytype paytype :paytypeList)
        {
            switch (paytype.type)//支付方式 0支付宝 1微信支付 2银联支付 3货到付款
            {
                case "0":
                    tv_zfb_by= (TextView) findViewById(R.id.tv_zfb_by);
                    tv_zfb_by.setText(String.format("满%s包邮",paytype.content));
                    zfb_by=paytype.content;
                    break;
                case "1":
                    tv_wx_by = (TextView) findViewById(R.id.tv_wx_by);
                    tv_wx_by.setText(String.format("满%s包邮",paytype.content));
                    wx_by=paytype.content;
                    break;
                case "2":
                    tv_yl_by = (TextView) findViewById(R.id.tv_yl_by);
                    tv_yl_by.setText(String.format("满%s包邮",paytype.content));
                    yl_by=paytype.content;
                    break;
                case "3":
                    tv_df_by = (TextView) findViewById(R.id.tv_df_by);
                    tv_df_by.setText(String.format("满%s包邮",paytype.content));
                    df_by=paytype.content;
                    break;
            }
        }
        selected_by=zfb_by;//默认选择支付宝支付包邮
        //等级优惠
        //freight="0";//初始还没有选择地址时，默认值0
        if(discount!=null ||discount.length()>1) {
            gradeDiscount = discount.substring(0, discount.length() - 1);
        }
        else
        {
            gradeDiscount="0";
        }
        //计算费用
        //setNum();
        //setNum(20f, 10);
    }

    private void showSure() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_delete);
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_hint);
        tv_hint.setText("确定取消订单吗？");
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().updateOrderStatus(orderBean.id, "5",MyOrderActivity.this, MyOrderActivity.this);
                orderOperationStatus="5";
                alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    private void showSureBuy(final String ids, final String consignee, final String mobile, final String cityid,final String address,final String realpay,final String remark, final String orderlist) {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_receipt);
        TextView tv_title = (TextView) alertDialog.getWindow().findViewById(R.id.tv_title);
        tv_title.setText("确认购买");
        tv_title.setTextColor(getResources().getColor(R.color.tv_color_login));
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(R.id.tv_hint);
        tv_hint.setText("生成订单，等待商家发货？");
        tv_hint.setTextColor(getResources().getColor(R.color.red));
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().confirmorder(ids, consignee, mobile, cityid, address, selectedPaytype, realpay, remark, orderlist,
                        MyOrderActivity.this, MyOrderActivity.this);
                alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
    }

    private void showSureTip() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_receipt);
        TextView tv_title = (TextView) alertDialog.getWindow().findViewById(R.id.tv_title);
        tv_title.setText("提示");
        tv_title.setTextColor(getResources().getColor(R.color.tv_color_login));
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(R.id.tv_hint);
        tv_hint.setText("已生成订单，等待商家发货,\n请前往我的订单查看");
        tv_hint.setTextColor(getResources().getColor(R.color.red));
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPayActivity();
                alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPayActivity();
                alertDialog.cancel();
            }
        });
    }

    private void gotoPayActivity()
    {
        if(this.tag.equals("-1")) {//跳转到商品详情
            SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
            String flagid = sp.getValue(Constant.SP_PAY_PRODUCTID);
            Intent intent1 = new Intent(this, ShopDeataisActivity.class);
            intent1.putExtra(Constant.ID, flagid);
            intent1.putExtra(Constant.SCORE, 0);
            startActivity(intent1);
            finish();
        }
        else if(this.tag.equals("-3"))//跳转到购物车
        {
            Intent intent1 = new Intent(this, MyShopCatActivity.class);
            startActivity(intent1);
            finish();
        }else{//跳转到我的订单
            Intent intent1 = new Intent(MyOrderActivity.this, MainActivity.class);
            intent1.putExtra("istag", true);
            intent1.putExtra("noTog", true);
            startActivity(intent1);
            finish();
        }
    }

    private void showReceipt() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dlg_receipt);
//        TextView tv_hint = (TextView) hisActivity.alertDialog.getWindow().findViewById(
//                R.id.tv_hint);
//        tv_hint.setText("确定取消订单吗？");
        TextView tv_yes = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().updateOrderStatus(orderBean.id, "3", MyOrderActivity.this, MyOrderActivity.this);
                orderOperationStatus="3";
                alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void afterTextChanged(Editable s) {
            updateOrder();
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }
    };



    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
        NewToast.show(this, "网络错误！", Toast.LENGTH_LONG);
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 0: //等级优惠
                    List<Paytype> paytypeList = apiResponse.data.paytype;
                    String discount = apiResponse.data.discount;
                    //初始化包邮信息，选择默认包邮方式及价格
                    initUserDiscount(paytypeList, discount);
                    //初始化等级优惠及包邮方式后，初始化邮费
                    if("".equals(tv_my_address.getText())||"".equals(tv_my_phone.getText())||"".equals(tv_name.getText())) {
                         setNum();
                    }
                    else {
                        if ("-1".equals(this.tag) || "-3".equals(this.tag)) {
                            initFreight();
                        }
                    }
                    break;
                case 1://默认地址
                    List<AddressBean> addresslist = new ArrayList<>();
                    addresslist.addAll(apiResponse.data.list);
                    //Log.e("##########","&&&&&&&&&&&&&&&&&&&&&&&"+apiResponse.data.list.size());
                    if(apiResponse.data.list!=null&&apiResponse.data.list.size()>0) {
                        for (int i = 0; i < addresslist.size(); i++) {
                            if (addresslist.get(i).isdefault.equals("1")) {
                                defaultAddress = addresslist.get(i);
                                break;
                            }
                        }
                        //Log.e("##########","&&&&&&&&&&&&&&&&&&&&&&&default:"+defaultAddress.address);
                        initDefaultAddress();
                    }
                    if("".equals(tv_my_address.getText())||"".equals(tv_my_phone.getText())||"".equals(tv_name.getText()))
                    {
                        NewToast.show(this, "请选择收货地址！", Toast.LENGTH_LONG);
                    }
                        //初始化城市id后，根据登陆id判断等级优惠
                        if (!"-2".equals(this.tag)) {
                            //Log.e("##########","&&&&&&&&&&&&&&&&&&&&&&&-2:"+this.tag);
                            WebServiceAPI.getInstance().findpaytype(this, this);
                        }

                    break;
                case 2://根据地址判断运费
                    isfree = apiResponse.data.isfree;
                    freight = apiResponse.data.freight;
                    Log.e("freight:", "^^^^^^^^^^^^^^^^^^^^^^isfree:" + isfree + ",freight:" + freight + "^^^^^^^^^^^^^^^^^^^^^^^^^^^");
                    //初始化城市Id,包邮方式，邮费后，计算费用
                    setNum();//重新计算费用
                    break;
                case 3://修改订单
                    //NewToast.show(this, "修改订单成功！", Toast.LENGTH_LONG);
                    break;
                case 4:
                    scoreproinfo = apiResponse.data.scoreproinfo;
                    scoreproinfo.ordernum = scoreNum;
                    MyOrderScoreAdapter scoreAdapter = new MyOrderScoreAdapter(this, scoreproinfo);
                    Log.e("scoreproinfo", scoreproinfo.imglist.get(0).path);
                    lvMyOrde.setAdapter(scoreAdapter);
                    break;
                case 5:
                    score = apiResponse.data.score;
                    tv_my_score.setText(score);
                    break;
                case 6:
                    Intent payIntent1 = new Intent(this, PaySuccessActivity.class);
                    payIntent1.putExtra("ISUCCESS", true);
                    payIntent1.putExtra("tag", 1);
                    //WJSJApplication.getInstance().addAcitivity(this);

                    //startActivity(payIntent1);
                    startActivityForResult(payIntent1,RETURN_REFRESH_CODE);
                    this.finish();

                    break;
                case 9: //等级优惠规则
                    Helper helper = apiResponse.data.helper;
                    gotoTextActivity(tv_youhui_shuoming.getText().toString(), helper.content);
                    break;
                case 10: //确认订单,调用后前往支付界面
                    orderBean = apiResponse.data.order;
                    if (orderBean != null) {
                        SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                        if(this.tag.equals("-1")) {//如果是从商城过来的，调用支付接口后，保存跳转页面信息
                            sp.putValue(Constant.SP_PAY_PAGE, "DETAIL");
                        }
                        else if(this.tag.equals("-3"))
                        {
                            sp.putValue(Constant.SP_PAY_PAGE, "SHOPCAT");
                        }else{
                            sp.putValue(Constant.SP_PAY_PAGE, "ORDER");
                        }
                        if (selectedPaytype.equals(PayTypeEnum.ZhiFuBao.getName())) {
                            AliPayActivity aliPay = new AliPayActivity(MyOrderActivity.this, orderBean);
                            aliPay.pay();
                        } else if (selectedPaytype.equals(PayTypeEnum.WinXin.getName())) {
                            WXPayActivity wxPay = new WXPayActivity(MyOrderActivity.this, orderBean, "");
                            wxPay.sendWXPayReq();
                            WJSJApplication.getInstance().addAcitivity(this);
                        } else if (selectedPaytype.equals(PayTypeEnum.YinLian.getName())) {

                        } else if (selectedPaytype.equals(PayTypeEnum.DaoFu.getName())) {
                            showSureTip();
                        }
                    } else {
                        NewToast.show(this, "没有订单信息！", Toast.LENGTH_LONG);
                    }
                    break;
                case 11:
                    if("5".equals(orderOperationStatus)) {
                        NewToast.show(this, "取消订单成功！", Toast.LENGTH_LONG);
                        try {
                            tv_yunfei.setVisibility(View.GONE);
                            ll_fromorder_footer.setVisibility(View.GONE);
                            //ll_formmy.setVisibility(View.VISIBLE);
                            //tv_pay_time.setVisibility(View.GONE);
                            rg_pay.setVisibility(View.GONE);
                            tv_cancel.setVisibility(View.GONE);
                            tv_buy.setVisibility(View.GONE);
                            tv_title.setText("已取消订单详情");
                            tv_my_pay_status.setText("已取消");
                            rl_top.setEnabled(false);
                            et_Remark.setEnabled(false);
                        } catch (Exception ex) {

                        }
                    }
                    else if("3".equals(orderOperationStatus)){
                        NewToast.show(this, "已收货，欢迎评价", Toast.LENGTH_LONG);
                        tv_buy.setVisibility(View.GONE);
                        tv_buy_status.setVisibility(View.VISIBLE);
                        tv_title.setText("待评价订单详情");
                        tv_my_pay_status.setText("去评价");
                    }
                    break;
                case 12:
                    String ordernum = apiResponse.data.ordernum;
                    SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                    sp.putValue(Constant.SP_PAY_PAGE, "ORDER");
                    if (orderBean != null) {
                        WXPayActivity wxPay = new WXPayActivity(MyOrderActivity.this, orderBean, ordernum);
                        wxPay.sendWXPayReq();
                    }
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        switch (tag) {
            case 1://确认订单
//                Intent payIntent0 = new Intent(this, PaySuccessActivity.class);
//                payIntent0.putExtra("ISUCCESS", true);
//                WJSJApplication.getInstance().addAcitivity(this);
//                startActivity(payIntent0);
                this.finish();
                break;
            case 3://修改订单
                //NewToast.show(this, "修改订单失败！", Toast.LENGTH_LONG);
                break;
            default:
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.cancel();
                break;
        }
    }
}


