package com.bm.wjsj.Personal;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.wjsj.Bean.AddressBean;
import com.bm.wjsj.Bean.CityBean;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.ViewHolder;
import com.bm.wjsj.View.SwipeListView;
import com.bm.wjsj.WJSJApplication;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2015/8/5 0005.
 */
public class MyAddressAdapter extends BaseAdapter {
    private Context context;
    private SwipeListView.IOnCustomClickListener mListener = null;
    private SwipeListView mListView;
    private List<AddressBean> list;
    private List<ProvinceBean> listProvince;

    public MyAddressAdapter(Context context, SwipeListView.IOnCustomClickListener mListener, SwipeListView mListView, List<AddressBean> list) {
        this.context = context;
        this.mListener = mListener;
        this.mListView = mListView;
        this.list = list;
        listProvince=AddressUtil.getInstance(context).getListProvince();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.address_itme, null);
            YoYo.with(Techniques.FadeIn).duration(1000).playOn(convertView);
            //testJson();//初始化地址的集合
                listProvince=AddressUtil.getInstance(context).getListProvince();
        }
        RelativeLayout item_right = ViewHolder.get(convertView, R.id.item_right);
        LinearLayout ll_moren = ViewHolder.get(convertView, R.id.ll_moren);
        View view_divider = ViewHolder.get(convertView, R.id.view_divider);
        TextView tv_name = ViewHolder.get(convertView, R.id.tv_name);
        TextView tv_phone = ViewHolder.get(convertView, R.id.tv_phone);
        TextView tv_detail = ViewHolder.get(convertView, R.id.tv_detail);
        TextView tv_cityid = ViewHolder.get(convertView, R.id.tv_cityid);
        tv_cityid.setText(list.get(position).cityid);
        tv_name.setText(list.get(position).consignee);
        tv_phone.setText(list.get(position).mobile);
        //String city = AddressUtil.getInstance(context).getCityNameById(list.get(position).provinceid,list.get(position).cityid);
        //tv_detail.setText(city+list.get(position).address);
        tv_detail.setText(getCityNameById(list.get(position).provinceid,list.get(position).cityid) + list.get(position).address);//地址
//        tv_detail.setText(list.get(position).address);
        if (list.get(position).isdefault.equals("1")) {
            ll_moren.setVisibility(View.VISIBLE);
            view_divider.setVisibility(View.VISIBLE);
        } else {
            ll_moren.setVisibility(View.INVISIBLE);
            view_divider.setVisibility(View.INVISIBLE);
        }
        final View cview = convertView;
        LinearLayout.LayoutParams paramRight = new LinearLayout.LayoutParams(mListView.getRightViewWidth(),
                LinearLayout.LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(paramRight);
        item_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListView.hiddenRight(cview);
                if (mListener != null) {
                    mListener.onDeleteClick(v, position);
                }
            }
        });
        return convertView;
    }

    // 解析JSON，获取城市列表
    private void testJson() {
//        if (listProvince.size() != 0) {
//            listProvince.clear();
//        }
        String jsonData_city = getFromAssets("city.json");
        Gson gson = new Gson();
        listProvince = gson.fromJson(jsonData_city, new TypeToken<List<ProvinceBean>>() {
        }.getType());
    }
    // 解析assets文件
    private String getFromAssets(String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
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
        return result;
    }
    //解析省份ID和城市ID
    private   String getCityNameById(String provinceId, String cityId) {
        String names = "";
        for (ProvinceBean provinceBean : this.listProvince) {
            if (provinceBean.id.equals(provinceId)) {
                names = provinceBean.name;
                for (CityBean cityBean : provinceBean.children) {
                    if (cityBean.id.equals(cityId)) {
                        return names.equals(cityBean.name) ? names.trim()+"市" : (names.trim() + cityBean.name+"市");
                    }
                }
            }
        }
        return names;
    }

}
