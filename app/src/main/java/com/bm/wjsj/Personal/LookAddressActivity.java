package com.bm.wjsj.Personal;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.AddressBean;
import com.bm.wjsj.Bean.CityBean;
import com.bm.wjsj.Bean.ProvinceBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.List;

/**
 * 地址详情
 */
public class LookAddressActivity extends BaseActivity implements APICallback.OnResposeListener {
    private TextView tv_dz;
    private EditText et_name, et_phone, et_area_deatils;
    private RippleView rv_default;

    private AddressBean addressBean;
    private List<ProvinceBean> listProvince;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_address);
        addressBean = (AddressBean) getIntent().getSerializableExtra(Constant.ID);
        //testJson();//初始化地址的集合
        initView();

    }

    private void initView() {
        initTitle("查看地址");
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("修改");
        tv_right.setOnClickListener(this);
        RelativeLayout rl_area = (RelativeLayout) findViewById(R.id.rl_area);
        rl_area.setVisibility(View.GONE);
        View view_divider = findViewById(R.id.view_divider);
        rv_default = (RippleView) findViewById(R.id.rv_default);
        view_divider.setVisibility(View.GONE);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        tv_dz = (TextView) findViewById(R.id.tv_dz);
        et_area_deatils = (EditText) findViewById(R.id.et_area_deatils);
        et_name.setFocusable(false);
        et_name.setEnabled(false);
        et_phone.setFocusable(false);
        et_phone.setEnabled(false);
        et_area_deatils.setFocusable(false);
        et_area_deatils.setEnabled(false);
        et_name.setText(addressBean.consignee);
        et_phone.setText(addressBean.mobile);

        String city= AddressUtil.getInstance(LookAddressActivity.this).getCityNameById(addressBean.provinceid,addressBean.cityid);
        et_area_deatils.setText(city + addressBean.address);//地址
        //et_area_deatils.setText(getCityNameById(addressBean.provinceid,addressBean.cityid) + addressBean.address);//地址
        if (addressBean.isdefault.equals("0")) {
            tv_dz.setBackgroundResource(R.mipmap.save_bt);
            rv_default.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                @Override
                public void onComplete(RippleView rippleView) {
                    Log.e("addressBean.id", addressBean.id);
                    WebServiceAPI.getInstance().addrDefault(addressBean.id, LookAddressActivity.this, LookAddressActivity.this);
                }
            });
        }else {
            tv_dz.setBackgroundResource(R.mipmap.btn);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_right:
                Intent intent = new Intent(this, ChangeAddressActivity.class);
                intent.putExtra("ISADD", false);
                intent.putExtra(Constant.ID, addressBean);
                startActivityForResult(intent, 1001);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1001) {
            addressBean = (AddressBean) data.getSerializableExtra(Constant.TITLE);
            et_name.setText(addressBean.consignee);
            et_phone.setText(addressBean.mobile);
            et_area_deatils.setText(addressBean.address);
        }
    }

    private void showSuccess() {
        showdialog(Gravity.CENTER);
        alertDialog.getWindow().setContentView(R.layout.dialog_success);
        TextView tv_sure = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_sure);
        TextView tv_hint = (TextView) alertDialog.getWindow().findViewById(
                R.id.tv_hint);
        tv_hint.setText("默认地址设置成功");
        tv_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
//        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                alertDialog.cancel();
//            }
//        });
    }

    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            showSuccess();
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {
        alertDialog.cancel();
    }

    /*
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
            InputStream in = this.getResources().getAssets().open(fileName);
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
    public String getCityNameById(String provinceId, String cityId) {
        String names = "";
        for (ProvinceBean provinceBean : listProvince) {
            if (provinceBean.id.equals(provinceId)) {
                names = provinceBean.name;
                for (CityBean cityBean : provinceBean.children) {
//                    if (cityBean.id.equals(cityId)) {
//                        if (cityBean.name.equals("")){
//                            return names.equals(cityBean.name) ? names : (names + "省");
//                        }else {
                    return names.equals(cityBean.name) ? names : (names + "  " + cityBean.name + "市");
//                        }
//                    }
                }
            }
        }
        return names;
    }
    */
}
