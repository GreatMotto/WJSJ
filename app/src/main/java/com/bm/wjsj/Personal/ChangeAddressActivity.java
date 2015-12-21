package com.bm.wjsj.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.AddressBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.AddressUtil;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.NewToast;
import com.bm.wjsj.Utils.ToastUtil;
import com.bm.wjsj.View.WheelDialog;

public class ChangeAddressActivity extends BaseActivity implements APICallback.OnResposeListener {
    private TextView et_area;
    private EditText et_name, et_phone, et_area_deatils;
    private boolean isAdd;
    private AddressBean addressBean;
    private String provinceId, cityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_address);
        isAdd = getIntent().getBooleanExtra("ISADD", false);
        addressBean = (AddressBean) getIntent().getSerializableExtra(Constant.ID);
        initView();
    }

    private void initView() {
        TextView tv_right = (TextView) findViewById(R.id.tv_right);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText("保存");
        tv_right.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_area = (TextView) findViewById(R.id.et_area);
        et_area_deatils = (EditText) findViewById(R.id.et_area_deatils);
        et_name.setFocusable(true);
        et_name.setEnabled(true);
        et_phone.setFocusable(true);
        et_phone.setEnabled(true);
        et_area_deatils.setFocusable(true);
        et_area_deatils.setEnabled(true);
        et_area.setOnClickListener(this);
        if (isAdd) {
            initTitle("新增收货地址");
        } else {
            initTitle("修改收货地址");
            et_name.setText(addressBean.consignee);
            et_area_deatils.setText(addressBean.address);
            et_phone.setText(addressBean.mobile);
            provinceId = addressBean.provinceid;
            cityId = addressBean.cityid;
            String city = AddressUtil.getInstance(ChangeAddressActivity.this).getCityNameById(provinceId, cityId);
            et_area.setText(city);
            //et_area.setText(WheelDialog.getInstance().getCityNameById(this, provinceId, cityId));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.et_area:
                WheelDialog.getInstance().chooseCity(this, et_area, new WheelDialog.GetCityIdListener() {
                    @Override
                    public void GetCityId(String provinceId, String cityId) {
                        ChangeAddressActivity.this.provinceId = provinceId;
                        ChangeAddressActivity.this.cityId = cityId;
                    }
                });
                break;
            case R.id.tv_right:
                if (isAdd) {
                    if (canCommit()) {
                        DialogUtils.showProgressDialog("正在添加...", this);
                        WebServiceAPI.getInstance().addrEdit(et_name.getText().toString().trim(),
                                et_phone.getText().toString().trim(), provinceId, cityId,
                                et_area_deatils.getText().toString().trim(), ChangeAddressActivity.this, ChangeAddressActivity.this);
                    }
                } else {
                    if (canCommit()) {
                        DialogUtils.showProgressDialog("正在修改...", this);
                        WebServiceAPI.getInstance().modifyEdit(addressBean.id, et_name.getText().toString().trim(),
                                et_phone.getText().toString().trim(), provinceId, cityId,
                                et_area_deatils.getText().toString().trim(), ChangeAddressActivity.this, ChangeAddressActivity.this);
                    }
                }

                break;

        }
    }

    private boolean canCommit() {
        String name = et_name.getText().toString();
        String area = et_area.getText().toString();
        String phone = et_phone.getText().toString();
        String area_deatils = et_area_deatils.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(this, "收货人不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(area)) {
            ToastUtil.showToast(this, "地区不能为空", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.showToast(this, "手机号不能为空不能为空", Toast.LENGTH_SHORT);
            return false;
        } else if ((!phone.matches(Constant.TELREGEX))) {
            ToastUtil.showToast(this, "手机格式不正确", Toast.LENGTH_SHORT);
            return false;
        }
        if (TextUtils.isEmpty(area_deatils)) {
            ToastUtil.showToast(this, "详细地址不能为空", Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
        DialogUtils.cancleProgressDialog();
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
        if (apiResponse.status.equals("0")) {
            if (isAdd) {
                NewToast.show(ChangeAddressActivity.this, "添加成功！", Toast.LENGTH_LONG);
                onBackPressed();
                finish();
            } else {
                NewToast.show(ChangeAddressActivity.this, "修改成功！", Toast.LENGTH_LONG);
                addressBean.consignee = et_name.getText().toString().trim();
                addressBean.address = et_area_deatils.getText().toString().trim();
                addressBean.mobile = et_phone.getText().toString().trim();
                addressBean.provinceid = provinceId;
                addressBean.cityid = cityId;
                setResult(RESULT_OK, new Intent().putExtra(Constant.TITLE, addressBean));
                onBackPressed();
                finish();
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
