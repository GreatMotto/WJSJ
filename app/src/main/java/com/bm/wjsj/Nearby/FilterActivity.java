package com.bm.wjsj.Nearby;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.WheelDialog;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 筛选
 */
public class FilterActivity extends BaseActivity {

    private RadioGroup rg_gender;
    public static RadioButton rb_nv,rb_nan,rb_all;
    private TextView tvCenter;
    private TextView tvAreaCenter;
    private TextView tvOnlineCenter;
    private List<String> list = new ArrayList<String>();
    public String time = "3", age = "", sex = "", provinceId = "", cityId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_filter);
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);//Activity的启动动画(右向左滑)
        initTitle("筛选");
        intView();
    }

    private void assignViews() {
        rg_gender = (RadioGroup) findViewById(R.id.rg_gender);
        rb_nv = (RadioButton) findViewById(R.id.rb_nv);
        rb_nan = (RadioButton) findViewById(R.id.rb_nan);
        rb_all = (RadioButton) findViewById(R.id.rb_all);
        tvCenter = (TextView) findViewById(R.id.tv_center);
        tvAreaCenter = (TextView) findViewById(R.id.tv_area_center);
        tvOnlineCenter = (TextView) findViewById(R.id.tv_online_center);

        intData();
        tvAreaCenter.setOnClickListener(this);
        tvCenter.setOnClickListener(this);
        findViewById(R.id.rl_online).setOnClickListener(this);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_nv:
                        sex = "1";
                        break;
                    case R.id.rb_nan:
                        sex = "0";
                        break;
                    case R.id.rb_all:
                        sex = "";
                        break;
                }
            }
        });
    }

    public void intData() {

        if (!TextUtils.isEmpty(WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_B))) {//如果不为空就是筛选了
           sex = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_SEX);
            switch (WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_SEX)) {//性别
                case "1":
                    rb_nv.setChecked(true);
                    break;
                case "0":
                    rb_nan.setChecked(true);
                    break;
                case "":
                    rb_all.setChecked(true);
                    break;
            }
            age = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_AGE);
            switch (WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_AGE)) {//年龄
                case "":
                    tvCenter.setText("不限");
                    break;
                case "0":
                    tvCenter.setText("18-22岁");
                    break;
                case "1":
                    tvCenter.setText("22-27岁");
                    break;
                case "2":
                    tvCenter.setText("27-35岁");
                    break;
                case "3":
                    tvCenter.setText("35以上");
                    break;
            }
            time = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_TIME);
            switch (WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_TIME)) {//时间
                case "0":
                    tvOnlineCenter.setText("30分钟");
                    break;
                case "1":
                    tvOnlineCenter.setText("60分钟");
                    break;
                case "2":
                    tvOnlineCenter.setText("1天");
                    break;
                case "3":
                    tvOnlineCenter.setText("七天");
                    break;
            }
            provinceId = WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_PROVINCEID);
            if (!TextUtils.isEmpty(WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_PROVINCEID))) {//地址
                tvAreaCenter.setText(AddressUtil.getInstance(this).getCityNameById(WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_PROVINCEID)
                        , WJSJApplication.getInstance().getSp().getValue(Constant.SP_FJ_CITYID)));
            }else {
                tvAreaCenter.setText("不限");
            }
        }else {
            if (WJSJApplication.getInstance().getSp().getBooleanValue(Constant.SP_KEY_ISLOGIN)) {
                if (!WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0")) {
                    rb_nan.setChecked(true);
                    sex = "0";
                } else {
                    rb_nv.setChecked(true);
                    sex = "1";
                }
            }
        }
    }


    private void intView() {
        findViewById(R.id.icon_back).setVisibility(View.GONE);
        TextView tv_back = (TextView) findViewById(R.id.tv_back);
        tv_back.setText("取消");
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setText("完成");
        tv_right.setOnClickListener(this);
        assignViews();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_area_center:
                WheelDialog.getInstance().chooseCityUnlimited(this, tvAreaCenter, new WheelDialog.GetCityIdListener() {
                    @Override
                    public void GetCityId(String provinceId, String cityId) {
                        FilterActivity.this.provinceId = provinceId;
                        FilterActivity.this.cityId = cityId;
                    }
                });
                break;
            case R.id.tv_center:
                list.clear();
                list.add("不限");
                list.add("18-22岁");
                list.add("22-27岁");
                list.add("27-35岁");
                list.add("35以上");
                WheelDialog.getInstance().ChossAgeScope(this, tvCenter, list, new WheelDialog.GetCityIdListener() {
                    @Override
                    public void GetCityId(String arg0, String arg1) {
                        switch (arg0) {
                            case "不限":
                                age = "";
                                break;
                            case "18-22岁":
                                age = "0";
                                break;
                            case "22-27岁":
                                age = "1";
                                break;
                            case "27-35岁":
                                age = "2";
                                break;
                            case "35以上":
                                age = "3";
                                break;
                        }
                    }
                });
                break;
            case R.id.tv_right:
                SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                sp.putValue(Constant.SP_FJ_TIME, time);
                sp.putValue(Constant.SP_FJ_AGE, age);
                sp.putValue(Constant.SP_FJ_SEX, sex);
                sp.putValue(Constant.SP_FJ_PROVINCEID, provinceId);
                sp.putValue(Constant.SP_FJ_CITYID, cityId);
                sp.putValue(Constant.SP_FJ_B, "shanxuan");//用来判断是否筛选

                Intent data = new Intent();
                data.putExtra("time", time);
                data.putExtra("age", age);
                data.putExtra("sex", sex);
                data.putExtra("cityId", cityId);
                data.putExtra("provinceId", provinceId);
                setResult(RESULT_OK, data);
                this.finish();
                break;
            case R.id.rl_online:
                list.clear();
                list.add("30分钟");
                list.add("60分钟");
                list.add("1天");
                list.add("7天");
                WheelDialog.getInstance().ChossAgeScope(this, tvOnlineCenter, list, new WheelDialog.GetCityIdListener() {
                    @Override
                    public void GetCityId(String arg0, String arg1) {
                        switch (arg0) {
                            case "30分钟":
                                time = "0";
                                break;
                            case "60分钟":
                                time = "1";
                                break;
                            case "1天":
                                time = "2";
                                break;
                            case "七天":
                                time = "3";
                                break;
                        }
                    }
                });
                break;
        }

    }


}
