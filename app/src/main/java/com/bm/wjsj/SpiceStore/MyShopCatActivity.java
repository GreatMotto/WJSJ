package com.bm.wjsj.SpiceStore;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.ShopCarBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.Urls;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.WJSJApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车
 */
public class MyShopCatActivity extends BaseActivity implements APICallback.OnResposeListener {
    private RelativeLayout rlTop;
    private ImageView ivCheckAll;
    private ImageView ivCatDelete;
    private RelativeLayout rlBottom;
    private TextView tvHj;
    private TextView tvPrice;
    private TextView tvBuy;
    private ListView lvShopcat;
    private List<ShopCarBean> list = new ArrayList<>();
    private MyShopCatAdapter adapter;
    private int i;
    private boolean isAll;
    private int deleteIndex = 0;
//    private String ids;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shop_cat);
        assignViews();

    }

    @Override
    protected void onResume() {
        super.onResume();
        DialogUtils.showProgressDialog("正在加载...", this);
        if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
            WebServiceAPI.getInstance().shopCarList(this, this);
        }
    }
    private void assignViews() {
        initTitle(getResources().getString(R.string.shop_cat));
        rlTop = (RelativeLayout) findViewById(R.id.rl_top);
        ivCheckAll = (ImageView) findViewById(R.id.iv_check_all);
        RelativeLayout rl_check_all = (RelativeLayout) findViewById(R.id.rl_check_all);
        rl_check_all.setOnClickListener(this);
        ivCatDelete = (ImageView) findViewById(R.id.iv_cat_delete);
        rlBottom = (RelativeLayout) findViewById(R.id.rl_bottom);
        tvHj = (TextView) findViewById(R.id.tv_hj);
        tvPrice = (TextView) findViewById(R.id.tv_price);
        tvBuy = (TextView) findViewById(R.id.tv_buy);
        tvBuy.setOnClickListener(this);
        ivCatDelete.setOnClickListener(this);
        lvShopcat = (ListView) findViewById(R.id.lv_shopcat);
        // 此处模拟订单
        AdapterClic onclic = new AdapterClic();
        adapter = new MyShopCatAdapter(this, list, onclic);
        lvShopcat.setAdapter(adapter);
    }
//    // listview中点击按键弹出对话框
//    public  void showInfo(final int position) {
//                        // 只需要重新设置一下adapter
//                        list.remove(position);
//                        adapter.notifyDataSetChanged();
//
//    }
//    public void data(final int position){
//        String data = "";
//        if (data.equals("")){
//            data = position + "";
//        }else {
//            data += data + "," + position;
//        }
//        ids = data;
//    }
    public class MyShopCatAdapter extends BaseAdapter{
        private Context context;
        private List<ShopCarBean> list;
        private View.OnClickListener clic;

        public MyShopCatAdapter(Context context, List<ShopCarBean> list, View.OnClickListener clic) {
            this.context = context;
            this.list = list;
            this.clic = clic;
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
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.shpcat_item, null);
                YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
            }
            TextView tv_add = ViewHolder.get(convertView, R.id.tv_add);
            TextView tv_cut = ViewHolder.get(convertView, R.id.tv_cut);
            TextView tv_cutnumm = ViewHolder.get(convertView, R.id.tv_cutnumm);
            TextView tv_price = ViewHolder.get(convertView, R.id.tv_price);
            ImageView iv_check = ViewHolder.get(convertView, R.id.iv_check);
            SimpleDraweeView iv_shop = ViewHolder.get(convertView, R.id.iv_shop);
            ImageView iv_delete = ViewHolder.get(convertView, R.id.iv_delete);
            TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
            iv_delete.setOnClickListener(clic);
            tv_cut.setOnClickListener(clic);
            tv_add.setOnClickListener(clic);
            iv_check.setOnClickListener(clic);
            iv_delete.setTag(position);
            tv_add.setTag(position);
            tv_cut.setTag(position);
            iv_check.setTag(position);
            if (list.get(position).isSelect) {
                iv_check.setImageResource(R.mipmap.cat_check);
//                data(position);
            } else {
                iv_check.setImageResource(R.mipmap.cat_uncheck);
            }
            tv_cutnumm.setText(String.valueOf(list.get(position).count));
//            tv_price.setText("¥" + list.get(position).price + ".00");
            if (list.get(position).equals("")){
                tv_price.setText("¥" + list.get(position).price + "0.00");
            }else if (list.get(position).price.indexOf(".") != -1){
                tv_price.setText("¥" + list.get(position).price);
            }else {
                tv_price.setText("¥" + list.get(position).price + ".00");
            }
            tv_name.setText(list.get(position).pname);
            iv_shop.setImageURI(Uri.parse(Urls.PHOTO + list.get(position).path));
//            iv_delete.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    WebServiceAPI.getInstance().deleteShopCar(list.get(position).id, MyShopCatActivity.this, context);
////                    showInfo(position);
//                    list.remove(position);
//                    adapter.notifyDataSetChanged();
//                }
//
//
//            });
            convertView.setOnClickListener(new View.OnClickListener() {//购物车跳转到商品详情
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ShopDeataisActivity.class);
//                    intent.putExtra(Constant.SCORE,2);//购物车跳转到商品详情
                    intent.putExtra(Constant.SCORE,0);
                    intent.putExtra(Constant.ID, list.get(position).productid);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }

    }



    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.rl_check_all:
                if (list.size() != 0) {
                    if (!isAll) {
                        ivCheckAll.setImageResource(R.mipmap.cat_check);
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).isSelect = true;
                        }
                    } else {
                        ivCheckAll.setImageResource(R.mipmap.cat_uncheck);
                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).isSelect = false;
                        }
                    }
                    isAll = !isAll;
                    adapter.notifyDataSetChanged();
                    setNum();
                }
                break;
            case R.id.tv_buy://结算
                List<ShopCarBean> selcList = new ArrayList<>();
                for (ShopCarBean bean : list) {
                    if (bean.isSelect) {
                        selcList.add(bean);
                    }
                }
                Intent intent = new Intent(this, MyOrderActivity.class);
                intent.putExtra("selcList", (Serializable) selcList);
                String tvtext = tvPrice.getText().toString();
                String alls = tvtext.substring(1,tvtext.length());
                intent.putExtra("alls", alls);
                Log.e("tvPrice", tvPrice.getText().toString());
                intent.putExtra(Constant.SCORE, "-3");
                startActivity(intent);
                break;
            case R.id.iv_cat_delete:
                boolean isdata = true;//避免重复调用删除购物车接口
                String ids = "";
                String data = "";
                if (list.size() == 0){
                    Toast.makeText(this,"请选择要删除的商品",Toast.LENGTH_SHORT).show();
                }else {
                    if (isAll) {
                        for (int i = 0; i < list.size(); i++) {
                            ids += (i == 0 ? list.get(i).id : "," + list.get(i).id);
                        }
                        if (!ids.equals("")) {
                            Log.e("ids--------------", ids);
                            WebServiceAPI.getInstance().deleteShopCar(ids, MyShopCatActivity.this, MyShopCatActivity.this);
                            isdata = false;
                        }

                    }
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect) {
                            data += (i == 0 ? list.get(i).id : "," + list.get(i).id);
                        }
                    }
                    if (!data.equals("")) {
                        if (isdata) {
                            Log.e("data-------------", data);
                            WebServiceAPI.getInstance().deleteShopCar(data, MyShopCatActivity.this, MyShopCatActivity.this);

                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isSelect) {
                                    list.remove(i);
                                }
                            }
                        }
                    }
                    if (ids.equals("") && data.equals("")){
                        Toast.makeText(this,"请选择要删除的商品",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        switch (tag) {
            case 7:
                if (apiResponse.data.list != null || apiResponse.data.list.size() > 0) {
                    list.clear();
                    list.addAll(apiResponse.data.list);
                    adapter.notifyDataSetChanged();
                }
                break;
            case 2:
                if (isAll) {
                    list.clear();
                }
                Toast.makeText(this,"删除成功",Toast.LENGTH_SHORT).show();
                ivCheckAll.setImageResource(R.mipmap.cat_uncheck);
                adapter.notifyDataSetChanged();
                setNum();
                break;
        }

    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

    private class AdapterClic implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int postion = (int) v.getTag();
            ShopCarBean good = list.get(postion);
            switch (v.getId()) {
                case R.id.tv_cut:
                    if (good.count.equals("1")) {
//                        Toast.makeText(MyShopCatActivity.this, "至少为1件", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    i = Integer.parseInt(good.count);
                    i--;
                    good.count = i + "";
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.tv_add:
                    i = Integer.parseInt(good.count);
                    i++;
                    good.count = i + "";
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.iv_delete:
                    deleteIndex = postion;
                    DialogUtils.showProgressDialog("正在删除...", MyShopCatActivity.this);
                    WebServiceAPI.getInstance().deleteShopCar(list.get(postion).id, MyShopCatActivity.this, MyShopCatActivity.this);
                    list.remove(postion);
                    break;
                case R.id.iv_check:
                    if (good.isSelect) {
                        good.isSelect = false;
                    } else {
                        good.isSelect = true;
                    }
                    adapter.notifyDataSetChanged();
                    break;

            }
            setNum();

        }
    }

    /*设置购物车总价格*/
    private void setNum() {
        float count = 0;
        float couns;
        int coun;
        int price;
        for (ShopCarBean bean : list) {
            if (list.size() != 0) {
                if (bean.isSelect) {
                    if (bean.price.contains(".")) {
                            coun = Integer.parseInt(bean.count);
                            couns = Float.parseFloat(bean.price);
                            count = count + coun * couns;
//                        }
                    }else {
                        coun = Integer.parseInt(bean.count);
                        price = Integer.parseInt(bean.price);
                        count = count + coun * price;
                    }
                }
            }
        }
        if (count == 0){
            tvPrice.setText("¥0.00");
        }else {
            String dataprice = count + "";
            if (dataprice.contains(".")){
                tvPrice.setText("¥" + dataprice);
            }else {
                tvPrice.setText("¥" + dataprice + ".00");
            }
        }
    }
}
