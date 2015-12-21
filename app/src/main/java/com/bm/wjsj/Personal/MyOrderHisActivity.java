package com.bm.wjsj.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.Goods;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.Product;
import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Bean.orderinfoBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.AliPayActivity;
import com.bm.wjsj.SpiceStore.PayTypeEnum;
import com.bm.wjsj.SpiceStore.WXPayActivity;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.View.SwipeListView;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @description 我的订单
 * @time
 */
public class MyOrderHisActivity extends BaseActivity implements APICallback.OnResposeListener{
    private RadioButton rbOne, rbTwo, rbThree;
    private RelativeLayout rlFirst, rlSecond, rlThird;
    private SwipeListView slvOrerSwipeView;
    private MyOrdeHisAdapter adapter;
    private List<orderinfoBean> orderinfoBeanList=new ArrayList<>();
    private List<ImageBean> list = new ArrayList<>();
    private List<orderBean> orderBeanAllList = null;//所有订单
    private List<orderBean> orderBeanUnpayList=null;//待付款订单
    private List<orderBean> orderBeanUnReceiptList=null;//待收货订单
    private String currentStatus="0";//0待付款 1待发货 2待收货 3待评价 4已完成 5已取消 不传查所有
    private int delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_his);
        initView();
        DialogUtils.showProgressDialog("正在加载...", this);
        WebServiceAPI.getInstance().myOrderList("0",this, this);
    }

    private void initView() {
        initTitle("我的订单");
        rbOne = (RadioButton) findViewById(R.id.rb_one);
        rbTwo = (RadioButton) findViewById(R.id.rb_two);
        rbThree = (RadioButton) findViewById(R.id.rb_three);
        rlFirst = (RelativeLayout) findViewById(R.id.rl_first);
        //ivFirst = (ImageView) findViewById(R.id.iv_first);
        rlSecond = (RelativeLayout) findViewById(R.id.rl_second);
        //ivSecond = (ImageView) findViewById(R.id.iv_second);
        rlThird = (RelativeLayout) findViewById(R.id.rl_third);
        //ivThird = (ImageView) findViewById(R.id.iv_third);
        slvOrerSwipeView = (SwipeListView) findViewById(R.id.orderHisSwipeView);
        //slvOrerSwipeView.setRightViewWidth(DisplayUtil.getWidth(this) * 3 / 16);
        slvOrerSwipeView.setRightViewWidth(0);
        rlFirst.setOnClickListener(this);
        rlSecond.setOnClickListener(this);
        rlThird.setOnClickListener(this);
        list.addAll(getImageList(Constant.imageUrls2, Constant.imageUrls2.length));

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_first:
                if (!rbOne.isChecked()) {
                    rbOne.setChecked(true);
                    currentStatus = "0";
                    DialogUtils.showProgressDialog("正在加载...", this);
                    WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
//                    if (orderBeanUnpayList == null)//如果为null，重新查询
//                    {
//                        DialogUtils.showProgressDialog("正在加载...", this);
//                        WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
//                    } else {
//                        initAdapter(orderBeanUnpayList);
//                    }
                }
                break;
            case R.id.rl_second:
                if (!rbTwo.isChecked()) {
                    rbTwo.setChecked(true);
                    //ivFirst.setImageResource(R.mipmap.yuan);
                    //ivSecond.setImageResource(R.mipmap.yuan2);
                    //ivThird.setImageResource(R.mipmap.yuan);
                    currentStatus = "2";
                    DialogUtils.showProgressDialog("正在加载...", this);
                    WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
//                    if (orderBeanUnReceiptList == null)//如果为null，重新查询
//                    {
//                        DialogUtils.showProgressDialog("正在加载...", this);
//                        WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
//                    } else {
//                        initAdapter(orderBeanUnReceiptList);
//                    }
                }
                break;
            case R.id.rl_third:
                if (!rbThree.isChecked()) {
                    rbThree.setChecked(true);
                    //ivFirst.setImageResource(R.mipmap.yuan);
                    //ivSecond.setImageResource(R.mipmap.yuan);
                    //ivThird.setImageResource(R.mipmap.yuan2);
                    currentStatus = "";
                    DialogUtils.showProgressDialog("正在加载...", this);
                    WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
//                    if (orderBeanAllList == null)//如果为null，重新查询
//                    {
//                        DialogUtils.showProgressDialog("正在加载...", this);
//                        WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
//                    } else {
//                        initAdapter(orderBeanAllList);
//                    }
                }
                break;
            case R.id.icon_back:
                break;
            default:
                break;
        }
    }

    private  void initAdapter(List<orderBean> orderBeanList)
    {
        adapter = new MyOrdeHisAdapter(this,rightListener, slvOrerSwipeView, orderBeanList, currentStatus);
        slvOrerSwipeView.setAdapter(adapter);
        adapter.setAdapterTag(currentStatus);
    }

    private  void resetOrderList()
    {
        orderBeanAllList=null;
        orderBeanUnpayList=null;
        orderBeanUnReceiptList=null;
    }

    SwipeListView.IOnCustomClickListener rightListener = new SwipeListView.IOnCustomClickListener() {
        // 订单列表左滑删除
        @Override
        public void onDeleteClick(View v, int position) {
            DialogUtils.showProgressDialog("正在删除...", MyOrderHisActivity.this);
            delete = position;
            WebServiceAPI.getInstance().deleteOrder(orderBeanAllList.get(position).id, MyOrderHisActivity.this, MyOrderHisActivity.this);
        }
    };

    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:
                    switch (currentStatus) {
                        case "0":
                            orderBeanUnpayList = new ArrayList<>();
                            orderBeanUnpayList.addAll(apiResponse.data.list);
                            initAdapter(orderBeanUnpayList);
                            break;
                        case "2":
                            orderBeanUnReceiptList = new ArrayList<>();
                            orderBeanUnReceiptList.addAll(apiResponse.data.list);
                            initAdapter(orderBeanUnReceiptList);
                            break;
                        case "":
                            orderBeanAllList = new ArrayList<>();
                            orderBeanAllList.addAll(apiResponse.data.list);
                            initAdapter(orderBeanAllList);
                            break;
                    }
                    break;
                case 2:
                    DialogUtils.cancleProgressDialog();
                    orderBeanAllList.remove(delete);
                    initAdapter(orderBeanAllList);
                    //WebServiceAPI.getInstance().addrList(this, this);
                    //adapter.notifyDataSetChanged();
                    break;
                case 11:
                    WebServiceAPI.getInstance().myOrderList(currentStatus, this, this);
                    break;
                case 12://支付
                    orderBean selectedOrderBean = adapter.selectedOrderBean;
                    String ordernum = apiResponse.data.ordernum;
                    if (selectedOrderBean != null) {
                        if (selectedOrderBean.paytype.equals(PayTypeEnum.WinXin.getName())) {
                            WXPayActivity wxPay = new WXPayActivity(this, selectedOrderBean, ordernum);
                            wxPay.sendWXPayReq();
                        }
                    } else {
                        NewToast.show(this, "没有订单信息！", Toast.LENGTH_LONG);
                    }
                    break;
            }
        }
//        if (pageNum == 1) {
//            list.clear();
//            rfl.setRefreshing(false);
//            rfl.setLoad_More(true);
//        } else {
//            rfl.setLoading(false);
//        }
//        list.addAll(apiResponse.data.list);
//        adapter.notifyDataSetChanged();
//        if (apiResponse.data.page.totalPage <= pageNum) {
//            rfl.setLoad_More(false);
//        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            WebServiceAPI.getInstance().myOrderList(currentStatus,this, this);
        }

    }
}
