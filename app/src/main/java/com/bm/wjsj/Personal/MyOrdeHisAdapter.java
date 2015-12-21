package com.bm.wjsj.Personal;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.SpiceStore.AliPayActivity;
import com.bm.wjsj.SpiceStore.PayTypeEnum;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.NoScrollListview;
import com.bm.wjsj.View.SwipeListView;
import com.bm.wjsj.WJSJApplication;

import java.util.List;

/**
 * 我的订单Adapter
 * Created by Administrator on 2015/8/11 0011.
 */
public class MyOrdeHisAdapter extends BaseAdapter {
    private Context context;
    private List<orderBean> list;
    private String tag;
    private SwipeListView.IOnCustomClickListener mListener = null;
    private SwipeListView mListView;
    private String selectId;
    public orderBean selectedOrderBean;

    public MyOrdeHisAdapter(Context context, SwipeListView.IOnCustomClickListener mListener, SwipeListView mListView, List<orderBean> list, String tag) {
        this.context = context;
        this.list = list;
        this.tag = tag;
        this.mListener = mListener;
        this.mListView = mListView;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.my_order_his_itme, null);
            }
            NoScrollListview lv_myshop_cat = ViewHolder.get(convertView, R.id.lv_myshop_cat);
            MyOrdeHisItemAdapter adapter = new MyOrdeHisItemAdapter(context, list.get(position).orderinfo, list.get(position).status, list.get(position),"orderHis");
            lv_myshop_cat.setAdapter(adapter);


//            MyOrderAdapter adapter = new MyOrderAdapter(context, list.get(position).orderinfo);
//            lv_myshop_cat.setAdapter(adapter);
            RelativeLayout item_order_right = ViewHolder.get(convertView, R.id.item_order_right);

            TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
            TextView tv_pay_status = ViewHolder.get(convertView, R.id.tv_pay_status);
            TextView tv_order_status = ViewHolder.get(convertView, R.id.tv_order_status);
            TextView tv_order_num = ViewHolder.get(convertView, R.id.tv_order_num);
            TextView tv_order_time = ViewHolder.get(convertView, R.id.tv_order_time);
            TextView tv_buy = ViewHolder.get(convertView, R.id.tv_buy);
            TextView tv_cancle = ViewHolder.get(convertView, R.id.tv_yunfei);

            tv_order_num.setText(list.get(position).ordernum);
            tv_order_time.setText(list.get(position).createTime);
            tv_price.setText(list.get(position).realpay);

            tv_order_status.setVisibility(View.GONE);
            switch (list.get(position).status) {//订单状态 0待付款 1待发货 2待收货 3待评价 4已完成 5已取消 不传查所有
                case "0":
                    tv_cancle.setVisibility(View.VISIBLE);
                    tv_pay_status.setVisibility(View.VISIBLE);
                    tv_buy.setVisibility(View.VISIBLE);
                    tv_order_status.setVisibility(View.GONE);
                    tv_pay_status.setText("待付款");
                    tv_buy.setText("立即支付");
                    item_order_right.setVisibility(View.GONE);
                    mListView.setRightViewWidth(0);
                    break;
                case "1":
                    if(list.get(position).paytype.equals(PayTypeEnum.DaoFu.getName()))//到付可以取消订单
                    {
                        tv_cancle.setVisibility(View.GONE);
                        tv_pay_status.setVisibility(View.VISIBLE);
                        tv_buy.setVisibility(View.GONE);
                        tv_order_status.setVisibility(View.VISIBLE);
                        tv_pay_status.setText("待发货");
                        tv_order_status.setText("取消订单");
                        item_order_right.setVisibility(View.GONE);
                        mListView.setRightViewWidth(0);
                    }
                    else {
                        tv_cancle.setVisibility(View.GONE);
                        tv_pay_status.setVisibility(View.VISIBLE);
                        tv_buy.setVisibility(View.GONE);
                        tv_order_status.setVisibility(View.VISIBLE);
                        tv_pay_status.setText("待发货");
                        tv_order_status.setText("待发货");
                        item_order_right.setVisibility(View.GONE);
                        mListView.setRightViewWidth(0);
                    }
                    break;
                case "2":
                    tv_cancle.setVisibility(View.GONE);
                    tv_pay_status.setVisibility(View.VISIBLE);
                    tv_buy.setVisibility(View.VISIBLE);
                    tv_order_status.setVisibility(View.GONE);
                    tv_pay_status.setText("待收货");
                    tv_buy.setText("确认收货");
                    item_order_right.setVisibility(View.GONE);
                    mListView.setRightViewWidth(0);
                    break;
                case "3":
                    tv_cancle.setVisibility(View.GONE);
                    tv_pay_status.setVisibility(View.VISIBLE);
                    tv_buy.setVisibility(View.GONE);
                    tv_order_status.setVisibility(View.GONE);
                    //tv_buy.setText("去评价");
                    tv_pay_status.setText("待评价");
                    item_order_right.setVisibility(View.GONE);
                    mListView.setRightViewWidth(0);
                    break;
                case "4":
                    tv_cancle.setVisibility(View.GONE);
                    tv_buy.setVisibility(View.GONE);
                    tv_pay_status.setVisibility(View.GONE);
                    tv_order_status.setVisibility(View.VISIBLE);
                    tv_order_status.setText("已完成");
                    tv_pay_status.setText("");
                    item_order_right.setVisibility(View.VISIBLE);

                    final View cviewFinish = convertView;
                    mListView.setRightViewWidth(DisplayUtil.getWidth(context) * 3 / 16);
                    LinearLayout.LayoutParams paramRightFinish = new LinearLayout.LayoutParams(mListView.getRightViewWidth(),
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    item_order_right.setLayoutParams(paramRightFinish);
                    item_order_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListView.hiddenRight(cviewFinish);
                            if (mListener != null) {
                                mListener.onDeleteClick(v, position);
                            }
                        }
                    });
                    break;
                case "5":
                    tv_cancle.setVisibility(View.GONE);
                    tv_buy.setVisibility(View.GONE);
                    tv_pay_status.setVisibility(View.GONE);
                    tv_order_status.setVisibility(View.VISIBLE);
                    tv_order_status.setText("已取消");
                    tv_pay_status.setText("");
                    item_order_right.setVisibility(View.VISIBLE);

                    final View cviewCancle = convertView;
                    mListView.setRightViewWidth(DisplayUtil.getWidth(context) * 3 / 16);
                    LinearLayout.LayoutParams paramRightCancle = new LinearLayout.LayoutParams(mListView.getRightViewWidth(),
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    item_order_right.setLayoutParams(paramRightCancle);
                    item_order_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListView.hiddenRight(cviewCancle);
                            if (mListener != null) {
                                mListener.onDeleteClick(v, position);
                            }
                        }
                    });
                    break;
            }
            tv_order_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if("1".equals(list.get(position).status)) {
                        if(list.get(position).paytype.equals(PayTypeEnum.DaoFu.getName()))//到付可以取消订单
                        {
                            selectId = list.get(position).id;
                            showSure();
                        }
                    }
                }
            });
            tv_cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectId = list.get(position).id;
                    showSure();
                }
            });
            tv_buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (list.get(position).status) {
                        case "0":
                            selectedOrderBean=  list.get(position);
                            if(selectedOrderBean!=null)
                            {
                                //如果是从订单过来的，调用支付接口后，保存跳转页面信息
                                SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                                sp.putValue(Constant.SP_PAY_PAGE, "ORDER");
                                //如果是从订单过来的待付款订单，支付前调用修改编号接口，防止订单编号重复而支付失败
                                if (PayTypeEnum.WinXin.getName().equals(selectedOrderBean.paytype)) {
                                    WebServiceAPI.getInstance().updateOrderNum(selectedOrderBean.id, (MyOrderHisActivity)context, (MyOrderHisActivity)context);
                                } else if (PayTypeEnum.ZhiFuBao.getName().equals(selectedOrderBean.paytype)){
                                    AliPayActivity aliPay=new AliPayActivity((MyOrderHisActivity)context, selectedOrderBean);
                                    aliPay.pay();
                                }

                                /*
                                if (orderBean.paytype.equals(PayTypeEnum.ZhiFuBao.getName())) {
                                    AliPayActivity aliPay = new AliPayActivity(context, orderBean);
                                    aliPay.pay();
                                } else if (orderBean.paytype.equals(PayTypeEnum.WinXin.getName())) {
                                    WXPayActivity wxPay = new WXPayActivity(context, orderBean);
                                    wxPay.sendWXPayReq();
                                } else if (orderBean.paytype.equals(PayTypeEnum.YinLian.getName())) {

                                } else if (orderBean.paytype.equals(PayTypeEnum.DaoFu.getName())) {
                                }
                                */
                            }
                            else{
                                NewToast.show(context, "没有订单信息！", Toast.LENGTH_LONG);
                            }
//                        Intent intent = new Intent(context, MyOrderActivity.class);
//                        intent.putExtra(Constant.SCORE, list.get(position).status);
//                        intent.putExtra("selcList", (Serializable) list.get(position).orderinfo);
//                        intent.putExtra("selOrder", (Serializable) list.get(position));
//                        context.startActivity(intent);
                            break;
                        case "2":
                            selectId = list.get(position).id;
                            showReceipt();
                            break;
                    }
                }
            });
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, MyOrderActivity.class);
//                    intent.putExtra(Constant.SCORE, list.get(position).status);
//                    intent.putExtra("selcList", (Serializable) list.get(position).orderinfo);
//                    intent.putExtra("selOrder", (Serializable) list.get(position));
//
//                    context.startActivity(intent);
//                }
//            });


        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
        return convertView;
    }


    private void showSure() {
        final MyOrderHisActivity hisActivity=(MyOrderHisActivity)context;
        hisActivity.showdialog(Gravity.CENTER);
        hisActivity.alertDialog.getWindow().setContentView(R.layout.dlg_delete);
        TextView tv_hint = (TextView) hisActivity.alertDialog.getWindow().findViewById(
                R.id.tv_hint);
        tv_hint.setText("确定取消订单吗？");
        TextView tv_yes = (TextView) hisActivity.alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) hisActivity.alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().updateOrderStatus(selectId, "5",hisActivity, hisActivity);
                hisActivity.alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hisActivity.alertDialog.cancel();
            }
        });

    }

    private void showReceipt() {
        final MyOrderHisActivity hisActivity=(MyOrderHisActivity)context;
        hisActivity.showdialog(Gravity.CENTER);
        hisActivity.alertDialog.getWindow().setContentView(R.layout.dlg_receipt);
//        TextView tv_hint = (TextView) hisActivity.alertDialog.getWindow().findViewById(
//                R.id.tv_hint);
//        tv_hint.setText("确定取消订单吗？");
        TextView tv_yes = (TextView) hisActivity.alertDialog.getWindow().findViewById(
                R.id.tv_yes);
        TextView tv_no = (TextView) hisActivity.alertDialog.getWindow().findViewById(
                R.id.tv_no);
        tv_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebServiceAPI.getInstance().updateOrderStatus(selectId, "3",hisActivity, hisActivity);
                hisActivity.alertDialog.cancel();
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hisActivity.alertDialog.cancel();
            }
        });

    }

    public void setAdapterTag(String tag) {
        this.tag = tag;
        notifyDataSetChanged();
    }
}
