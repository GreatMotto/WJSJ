package com.bm.wjsj.Personal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.AddressBean;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.Utils.DisplayUtil;
import com.bm.wjsj.View.SwipeListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 收货地址列表
 */
public class MyAddessAcitity extends BaseActivity implements APICallback.OnResposeListener {
    private SwipeListView lvAddress;
    private List<AddressBean> list = new ArrayList<>();
    private MyAddressAdapter adapter;
    private int isLook, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_addess_acitity);
        isLook = getIntent().getIntExtra("isLook", 0);
        assignViews();
    }

    private void assignViews() {
        switch (isLook) {
            case 0:
                initTitle("地址管理");
                break;
            case 1:
                initTitle("选择地址");
                break;
        }
        ImageView iv_shaixuan = (ImageView) findViewById(R.id.iv_shaixuan);
        iv_shaixuan.setVisibility(View.VISIBLE);
        iv_shaixuan.setImageResource(R.mipmap.plus);
        iv_shaixuan.setOnClickListener(this);
        lvAddress = (SwipeListView) findViewById(R.id.lv__address);
        lvAddress.setRightViewWidth(DisplayUtil.getWidth(this) * 3 / 16);
        adapter = new MyAddressAdapter(this, rightListener, lvAddress, list);
        lvAddress.setAdapter(adapter);
        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (isLook) {
                    case 0:
                        Intent intent = new Intent(MyAddessAcitity.this, LookAddressActivity.class);
                        intent.putExtra(Constant.ID, list.get(position));
                        startActivity(intent);
                        break;
                    case 1:
                        //MyAddessAcitity.this.setResult(2000);
                        //pwxl 2015.10.20 add
                        Intent intentResult=new Intent();
                        intentResult.putExtra(Constant.ID, list.get(position));
                        setResult(RESULT_OK, intentResult);
                        MyAddessAcitity.this.finish();
                        break;
                }


            }
        });
    }

    SwipeListView.IOnCustomClickListener rightListener = new SwipeListView.IOnCustomClickListener() {

        // 地址条目左滑删除
        @Override
        public void onDeleteClick(View v, int position) {
//            NewToast.show(getActivity(), "点击了删除" + position, Toast.LENGTH_LONG);
            DialogUtils.showProgressDialog("正在删除...", MyAddessAcitity.this);
            delete = position;
                WebServiceAPI.getInstance().deleteAddr(list.get(position).id,
                        list.get(position).isdefault, MyAddessAcitity.this, MyAddessAcitity.this);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        WebServiceAPI.getInstance().addrList(this, this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_shaixuan:
                Intent intent = new Intent(this, ChangeAddressActivity.class);
                intent.putExtra("ISADD", true);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void OnFailureData(String error, Integer tag) {
    }

    @Override
    public void OnSuccessData(APIResponse apiResponse, Integer tag) {
        if (apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:
                    list.clear();
                    if (apiResponse.data.list != null && apiResponse.data.list.size() > 0) {
                        list.addAll(apiResponse.data.list);
                        for (int i = 0; i < list.size(); i ++){
                            if (list.get(i).isdefault.equals("1")){
                                break;
                            }else {
                                if (i == list.size()-1){
                                    list.get(0).isdefault = "1";
                                    WebServiceAPI.getInstance().addrDefault(list.get(0).id, MyAddessAcitity.this, MyAddessAcitity.this);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                    break;
                case 2:
                    DialogUtils.cancleProgressDialog();
                    list.remove(delete);
                    WebServiceAPI.getInstance().addrList(this, this);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
