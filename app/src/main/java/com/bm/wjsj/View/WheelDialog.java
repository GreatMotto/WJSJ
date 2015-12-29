package com.bm.wjsj.View;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bm.wjsj.Bean.CityBean;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;

import java.util.ArrayList;
import java.util.List;

import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelChangedListener;
import antistatic.spinnerwheel.adapters.AbstractWheelTextAdapter;

/**
 * Created by yangk on 2015/7/22.
 */
public class WheelDialog {

    private Dialog alertDialog;
    protected AbstractWheel wheelview;
    private String province = "", city = "";
    public List<ProvinceBean> list = new ArrayList<ProvinceBean>();
    public static final String ENCODING = "UTF-8";
    public String ages = "", provinceId, cityId;

    AbstractWheel wv_province, wv_city;

    private WheelDialog() {

    }

    private static class Datacontroller {

        /**
         * 单例变量
         */
        private static WheelDialog instance = new WheelDialog();
    }

    public static WheelDialog getInstance() {
        return Datacontroller.instance;
    }

    private void showDialog(Activity context) {
        alertDialog = new Dialog(context, R.style.MyDialogStyle);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.dlganim);
        alertDialog.show();
        WindowManager manager = context.getWindowManager();
        Display display = manager.getDefaultDisplay();
        int width = display.getWidth();
        alertDialog.getWindow().setLayout(width,
                WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.setCanceledOnTouchOutside(true);
    }

    public void chooseCity(final Activity context, final TextView v,
                           final GetCityIdListener listener) {
        //list.clear();
        //testJson(context);
        list= AddressUtil.getInstance(context).getListProvince();
        showDialog(context);
        alertDialog.getWindow().setContentView(R.layout.wheel_area);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_sure);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_cancel);
        province = list.get(0).name;
        city = list.get(0).children.get(0).name;
        provinceId = list.get(0).id;
        cityId = list.get(0).children.get(0).id;
        wv_province = (AbstractWheel) alertDialog.findViewById(R.id.wv_province);
        wv_city = (AbstractWheel) alertDialog.findViewById(R.id.wv_city);
        wv_province.setVisibleItems(3);
        wv_province.setViewAdapter(new IdentifyAdapter(context, null, true));
        wv_province.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                province = list.get(newValue).name;
                city = list.get(newValue).children.get(0).name;
                provinceId = list.get(newValue).id;
                cityId = list.get(newValue).children.get(0).id;
                wv_city.setViewAdapter(new IdentifyAdapter(context, list.get(newValue).children, false));
                wv_city.setCurrentItem(0);
                // v.setText(identify);
            }
        });
        wv_city.setVisibleItems(3);
        wv_city.setViewAdapter(new IdentifyAdapter(context, list.get(0).children, false));
        wv_city.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                city = list.get(wv_province.getCurrentItem()).children.get(wv_city.getCurrentItem()).name;
                cityId = list.get(wv_province.getCurrentItem()).children.get(wv_city.getCurrentItem()).id;
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                alertDialog.cancel();

            }
        });
        tv_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (province.equals(city)) {
                    v.setText(city);
                } else {
                    v.setText(province + "" + city);
                }
                listener.GetCityId(provinceId, cityId);
                alertDialog.cancel();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("setOnCancelListener", "setOnCancelListener");
                list.clear();
            }
        });

    }

    public void chooseCityUnlimited(final Activity context, final TextView v,
                           final GetCityIdListener listener) {
        //list.clear();
        //testJson(context);
        list= AddressUtil.getInstance(context).getListProvince();
        ProvinceBean unlimitedProvince=new ProvinceBean();
        CityBean unlimitedCitye=new CityBean();
        unlimitedCitye.name="不限";
        unlimitedCitye.id="";
        unlimitedProvince.name="不限";
        unlimitedProvince.id="";
        List<CityBean> unlimitedCitys=new ArrayList<>();
        unlimitedCitys.add(unlimitedCitye);
        unlimitedProvince.children=unlimitedCitys;
        list.add(0,unlimitedProvince);
        showDialog(context);
        alertDialog.getWindow().setContentView(R.layout.wheel_area);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_sure);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_cancel);
        province = list.get(0).name;
        city = list.get(0).children.get(0).name;
        provinceId = list.get(0).id;
        cityId = list.get(0).children.get(0).id;
        wv_province = (AbstractWheel) alertDialog.findViewById(R.id.wv_province);
        wv_city = (AbstractWheel) alertDialog.findViewById(R.id.wv_city);
        wv_province.setVisibleItems(3);
        wv_province.setViewAdapter(new IdentifyAdapter(context, null, true));
        wv_province.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                province = list.get(newValue).name;
                city = list.get(newValue).children.get(0).name;
                provinceId = list.get(newValue).id;
                cityId = list.get(newValue).children.get(0).id;
                wv_city.setViewAdapter(new IdentifyAdapter(context, list.get(newValue).children, false));
                wv_city.setCurrentItem(0);
                // v.setText(identify);
            }
        });
        wv_city.setVisibleItems(3);
        wv_city.setViewAdapter(new IdentifyAdapter(context, list.get(0).children, false));
        wv_city.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                city = list.get(wv_province.getCurrentItem()).children.get(wv_city.getCurrentItem()).name;
                cityId = list.get(wv_province.getCurrentItem()).children.get(wv_city.getCurrentItem()).id;
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                alertDialog.cancel();

            }
        });
        tv_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                if (province.equals(city)) {
                    v.setText(city);
                } else {
                    v.setText(province + "" + city);
                }
                listener.GetCityId(provinceId, cityId);
                alertDialog.cancel();
            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.e("setOnCancelListener", "setOnCancelListener");
                list.clear();
            }
        });

    }

    private List<String> lists = new ArrayList<String>();


    public void ChossAgeScope(final Activity context, final TextView textView, List<String> list,
                              final GetCityIdListener listener) {
        lists.clear();
        showDialog(context);
        alertDialog.getWindow().setContentView(R.layout.agechoose_scope);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_sure);
        TextView tv_cancel = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_cancel);
        lists.addAll(list);

        ages = list.get(0);
        final AbstractWheel wv_age = (AbstractWheel) alertDialog.findViewById(R.id.wv_age);
        wv_age.setVisibleItems(3);
        AgeAdapetr ageAdapetr = new AgeAdapetr(context, lists);
        wv_age.setViewAdapter(ageAdapetr);
        wv_age.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(AbstractWheel wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                ages = lists.get(newValue);
                // v.setText(identify);
            }
        });
        tv_cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {

                alertDialog.cancel();

            }
        });
        tv_sure.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                textView.setText(ages);
                listener.GetCityId(ages, "");
                alertDialog.cancel();
            }
        });

    }

    /**
     * 类型选择弹出框的适配器
     */
    public class IdentifyAdapter extends AbstractWheelTextAdapter {

        List<CityBean> listcity;
        boolean isProvince;

        /**
         * Constructor
         */
        protected IdentifyAdapter(Context context, List<CityBean> listcity, boolean isProvince) {
            super(context, R.layout.wheel_item, DEFAULT_TEXT_COLOR);
            this.listcity = listcity;
            this.isProvince = isProvince;
            setItemTextResource(R.id.tv_identify_item);
        }


        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return isProvince ? list.size() : listcity.size();
        }

        @Override
        protected CharSequence getItemText(int index) {

            return isProvince ? list.get(index).name : listcity.get(index).name;
        }

        @Override
        public int getItemResource() {

            return super.getItemResource();
        }

    }

    private class AgeAdapetr extends AbstractWheelTextAdapter {
        List<String> lists;

        protected AgeAdapetr(Context context, List<String> lists) {
//            super(context);
            super(context, R.layout.wheel_item, DEFAULT_TEXT_COLOR);
            this.lists = lists;
            setItemTextResource(R.id.tv_identify_item);
        }

        @Override
        protected CharSequence getItemText(int index) {
            return lists.get(index);
        }

        @Override
        public int getItemsCount() {
            return lists.size();
        }

        @Override
        public int getItemResource() {

            return super.getItemResource();
        }
    }

    public interface GetCityIdListener {

        public void GetCityId(String provinceId, String cityId);
    }

}
