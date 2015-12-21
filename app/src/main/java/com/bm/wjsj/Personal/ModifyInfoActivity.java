package com.bm.wjsj.Personal;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Base.RegisterOKActivity;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.SharedPreferencesHelper;
import com.bm.wjsj.View.WheelDialog;
import com.bm.wjsj.WJSJApplication;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * @author 杨凯
 * @description 修改资料界面
 * @time 2015.3.12
 */
public class ModifyInfoActivity extends BaseActivity implements APICallback.OnResposeListener {
    private EditText etNickname;
    private TextView tvSex;
    private TextView tvDate;
    private TextView tvConstellation;
    private TextView tvLocation;
    private EditText etSign;
    private RippleView rvSubmit;
    private String userId, provinceId, cityId;
    private List<ProvinceBean> listProvince;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ac_modifyinfo);
        userId = "";
        initTitle("修改资料");
//        testJson();//初始化地址的集合
//        WebServiceAPI.getInstance().login((WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_USER)),
//                (WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_PWD)), "",
//                ModifyInfoActivity.this, ModifyInfoActivity.this);
        assignViews();
    }

    private void assignViews() {
        final RegisterOKActivity registerActivity = new RegisterOKActivity();
        etNickname = (EditText) findViewById(R.id.et_nickname);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvConstellation = (TextView) findViewById(R.id.tv_constellation);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        etSign = (EditText) findViewById(R.id.et_sign);
        rvSubmit = (RippleView) findViewById(R.id.rv_submit);
        etNickname.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERNAME));
        if (WJSJApplication.getInstance().getSp().getValue(Constant.SP_SEX).equals("0")) {
            tvSex.setText("男");
        }else {
            tvSex.setText("女");
        }
        tvDate.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_BIRTHDAY));
        if (!tvDate.getText().toString().equals("")) {
            showBrithday(tvDate.getText().toString());//星座
        }
        tvLocation.setText(AddressUtil.getInstance(this).getCityNameById(WJSJApplication.getInstance().getSp().getValue(Constant.SP_PROVINCEID),
                WJSJApplication.getInstance().getSp().getValue(Constant.SP_CITYID)));//地址
        etSign.setText(WJSJApplication.getInstance().getSp().getValue(Constant.SP_SIGNATURE));
        rvSubmit.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                //by liuy 2015-10-14 个人信息接口 begin
                String filePath = Environment.getExternalStorageDirectory() + "/" + "touxiang";
                File dirFile = new File(filePath);
                File file = new File(filePath + File.separator + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg");
                if (file.exists()) {
                    file.delete();
                }
                if (!new File(filePath).exists()) {
                    new File(filePath).mkdirs();
                }

                try {
                    file.createNewFile();
                } catch (IOException io) {

                }
                DialogUtils.showProgressDialog("", ModifyInfoActivity.this);
                WebServiceAPI.getInstance().completeInfo(
                        WJSJApplication.getInstance().getSp().getValue(Constant.SP_USERID),
                        WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_PWD),
                        file,
                        etNickname.getText().toString().trim(),
                        tvDate.getText().toString().trim(),
                        provinceId, cityId,
                        etSign.getText().toString().trim(),
                        ModifyInfoActivity.this, ModifyInfoActivity.this);

                //by liuy 2015-10-14 个人信息接口 end
//                onBackPressed();
            }
        });
        tvDate.setOnClickListener(this);
        tvLocation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_date:
                showBrithdayDialog();
                break;
            case R.id.tv_location:
                WheelDialog.getInstance().chooseCity(this, tvLocation, new WheelDialog.GetCityIdListener() {
                    @Override
                    public void GetCityId(String provinceId, String cityId) {
                        ModifyInfoActivity.this.provinceId = provinceId;
                        ModifyInfoActivity.this.cityId = cityId;
                    }
                });
                break;
            default:
                break;
        }
    }

    public static String date2Constellation(String month, String day) {
        int monthTemp = Integer.parseInt(month) - 1;
        int dayTemp = Integer.parseInt(day);

        if (monthTemp == 9 && dayTemp == 23) {
            return "天秤座";
        }

        if (monthTemp == 3 && dayTemp == 20) {
            return "金牛座";
        }
        if (monthTemp == 10 && dayTemp == 22) {
            return "天蝎座";
        }
        if (dayTemp < Constant.constellationDay[monthTemp]) {
            monthTemp = monthTemp - 1;
        }
        if (monthTemp >= 0) {
            return Constant.constellationArr[monthTemp];
        }
        return Constant.constellationArr[11];
    }

    private void showBrithdayDialog() {
        // TODO Auto-generated method stub
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        final String defaultDate = (TextUtils.isEmpty(tvDate.getText().toString())) ?
                sDateFormat.format(new Date()) : tvDate.getText().toString();
        showdialog(Gravity.BOTTOM);
        alertDialog.getWindow().setContentView(R.layout.userinfo_data_pivk);
        final DatePicker dp_user_bri = (DatePicker) alertDialog.getWindow().findViewById(
                R.id.dp_user_bri);

        Calendar calendar = Calendar.getInstance();

        dp_user_bri.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        dp_user_bri.setMaxDate(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(sdf.parse(defaultDate));
            dp_user_bri.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        TextView tv_quxiao = (TextView) alertDialog.getWindow().findViewById(R.id.tv_cancel);
        TextView tv_queding = (TextView) alertDialog.getWindow().findViewById(R.id.tv_sure);
        tv_queding.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int month2 = dp_user_bri.getMonth();
                int month3 = month2 + 1;
                tvDate.setText(dp_user_bri.getYear() + "-" + month3 + "-"
                        + dp_user_bri.getDayOfMonth());
                tvConstellation.setText(date2Constellation(String.valueOf(month3), String.valueOf(dp_user_bri.getDayOfMonth())));
                alertDialog.dismiss();
            }
        });
        tv_quxiao.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }
    private void showBrithday(String tvdata) {
        // TODO Auto-generated method stub
        SimpleDateFormat sDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        final String defaultDate = (TextUtils.isEmpty(tvdata)) ?
                sDateFormat.format(new Date()) : tvdata;
        showdialog(Gravity.BOTTOM);
        alertDialog.getWindow().setContentView(R.layout.userinfo_data_pivk);
        final DatePicker dp_user_bri = (DatePicker) alertDialog.getWindow().findViewById(
                R.id.dp_user_bri);

        Calendar calendar = Calendar.getInstance();

        dp_user_bri.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), null);
        dp_user_bri.setMaxDate(System.currentTimeMillis());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(sdf.parse(defaultDate));
            dp_user_bri.updateDate(calendar1.get(Calendar.YEAR), calendar1.get(Calendar.MONTH), calendar1.get(Calendar.DAY_OF_MONTH));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        int month2 = dp_user_bri.getMonth();
        int month3 = month2 + 1;
        tvConstellation.setText(date2Constellation(String.valueOf(month3), String.valueOf(dp_user_bri.getDayOfMonth())));
        alertDialog.dismiss();
    }
    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:
                    DialogUtils.cancleProgressDialog();
                    NewToast.show(ModifyInfoActivity.this, "修改资料成功", Toast.LENGTH_LONG);
                    WebServiceAPI.getInstance().login((WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_USER)),
                            (WJSJApplication.getInstance().getSp().getValue(Constant.SP_KEY_PWD)),
                            ModifyInfoActivity.this, ModifyInfoActivity.this);
                    finish();
                    break;
                case 2:
                    SharedPreferencesHelper sp = WJSJApplication.getInstance().getSp();
                    WJSJApplication.getInstance().clearAc();
                    sp.putValue(Constant.SP_USERNAME, apiResponse.data.appuser.nickname);
                    sp.putValue(Constant.SP_PHOTO, apiResponse.data.appuser.head);
                    sp.putValue(Constant.SP_LEVEL, apiResponse.data.appuser.level);
                    sp.putValue(Constant.SP_INTEGRAL, apiResponse.data.appuser.integral);
                    sp.putValue(Constant.SP_SEX, apiResponse.data.appuser.sex);
                    sp.putValue(Constant.SP_BIRTHDAY, apiResponse.data.appuser.birthday);
                    sp.putValue(Constant.SP_PROVINCEID, apiResponse.data.appuser.provinceId);
                    sp.putValue(Constant.SP_CITYID, apiResponse.data.appuser.cityId);
                    sp.putValue(Constant.SP_SIGNATURE, apiResponse.data.appuser.sign);
                    sp.putValue(Constant.SP_AGE, apiResponse.data.appuser.age);
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }

//    // 解析JSON，获取城市列表
//    private void testJson() {
////        if (listProvince.size() != 0) {
////            listProvince.clear();
////        }
//        String jsonData_city = getFromAssets("city.json");
//        Gson gson = new Gson();
//        listProvince = gson.fromJson(jsonData_city, new TypeToken<List<ProvinceBean>>() {
//        }.getType());
//    }
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
//    //解析省份ID和城市ID
//    public String getCityNameById(String provinceId, String cityId) {
//        String names = "";
//        for (ProvinceBean provinceBean : listProvince) {
//            if (provinceBean.id.equals(provinceId)) {
//                names = provinceBean.name;
//                for (CityBean cityBean : provinceBean.children) {
////                    if (cityBean.id.equals(cityId)) {
////                        if (cityBean.name.equals("")){
////                            return names.equals(cityBean.name) ? names : (names + "省");
////                        }else {
//                    return names.equals(cityBean.name) ? names : (names + "  " + cityBean.name + "市");
////                        }
////                    }
//                }
//            }
//        }
//        return names;
//    }
}
