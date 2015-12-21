package com.bm.wjsj.SpiceStore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.bm.wjsj.Base.BaseActivity;
import com.bm.wjsj.Bean.Result;
import com.bm.wjsj.Constans.Constant;
import com.bm.wjsj.Http.APICallback;
import com.bm.wjsj.Http.APIResponse;
import com.bm.wjsj.Http.WebServiceAPI;
import com.bm.wjsj.R;
import com.bm.wjsj.Utils.DialogUtils;
import com.bm.wjsj.WJSJApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * 分类界面
 */
public class ClassifiyAcitivity extends BaseActivity implements APICallback.OnResposeListener {
    private ClassifiyGridAdapter classifiyGridAdapter;
    private ClassifyNameAdapter adapter;
    private List<Result> result;
    private List<Result> result1;
    private String currentTypeName="",Level1Id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifiy_acitivity);
        assignViews();

    }

    private ListView lvClassName;
    private GridView gvClasscontetn;

    private void assignViews() {
        WebServiceAPI.getInstance().storeType("-1", ClassifiyAcitivity.this, ClassifiyAcitivity.this);
        initTitle(getResources().getString(R.string.fenlei));
        lvClassName = (ListView) findViewById(R.id.lv_class_name);
        gvClasscontetn = (GridView) findViewById(R.id.gv_classcontetn);
//        adapter = new ClassifyNameAdapter(this, list);
//        lvClassName.setAdapter(adapter);
//        lvClassName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                for (int i = 0; i < list.size(); i++) {
//                    if (i == position) {
//                        list.get(i).isSelect = true;
//                        classifiyGridAdapter.setName(.get(i));
//
//                    } else {
//                        list.get(i).isSelect = false;
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
        DialogUtils.showProgressDialog("正在请求...", this);

    }


    @Override
    public void OnFailureData(String error, Integer tag) {

    }

    @Override
    public void OnSuccessData(final APIResponse apiResponse, Integer tag) {
        DialogUtils.cancleProgressDialog();
//        boolean isSelect = false;
//        for (int i = 0; i < apiResponse.data.result.size(); i++) {
//            if (i == 0) {
//                booleans.get(i).isSelect = true;
//            }else{
//                booleans.get(i).isSelect = false;
//            }
        if (apiResponse.data != null && apiResponse.status.equals("0")) {
            switch (tag) {
                case 1:

//            Result r = new Result();

                    result = apiResponse.data.result;
                    result.get(0).isSelect = true;
                    currentTypeName=result.get(0).name;
                    WebServiceAPI.getInstance().storeType1(result.get(0).id + "", ClassifiyAcitivity.this, ClassifiyAcitivity.this);
                    adapter = new ClassifyNameAdapter(ClassifiyAcitivity.this, result);
                    lvClassName.setAdapter(adapter);

                    lvClassName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            for (int i = 0; i < apiResponse.data.result.size(); i++) {

                                if (i == position) {
//                            booleans.get(i).isSelect = true;
                                    result.get(i).isSelect = true;
                                    currentTypeName=result.get(i).name;
                                    WebServiceAPI.getInstance().storeType1(result.get(i).id + "", ClassifiyAcitivity.this, ClassifiyAcitivity.this);
                                    Level1Id = result.get(i).id;

                                } else {
                                    result.get(i).isSelect = false;
//                            data = 0;
//                            booleans.get(i).isSelect = false;
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case 2:
                    result1 = apiResponse.data.result;
                    Log.e("touxiang", WJSJApplication.getInstance().getSp().getValue(Constant.SP_PHOTO));
////                    for (int i = 0;i < apiResponse.data.result.size();i++){
////                        result1.get(i).id = result.get(i).id;
////                        result1.get(i).name = result.get(i).name;
////                        result1.get(i).path = result.get(i).path;
////                    }
//                    Log.e("classifiyGridAdapter","diaoyongchenggong");
                    classifiyGridAdapter = new ClassifiyGridAdapter(ClassifiyAcitivity.this, result1,currentTypeName,Level1Id);
                    gvClasscontetn.setAdapter(classifiyGridAdapter);
//                    Log.e("classifiyGridAdapter","diaoyongchenggong"+result.get(0).name.toString());
                    classifiyGridAdapter.setName(result1);
                    break;

            }
        }

//            if (apiResponse.data.list == null || apiResponse.data.list.size() == 0) {
//                NewToast.show(this, "暂无分类数据！", Toast.LENGTH_LONG);
//            } else {
//                list.addAll(apiResponse.data.list);
//                list.get(0).isSelect = true;
//                adapter.notifyDataSetChanged();
//                classifiyGridAdapter = new ClassifiyGridAdapter(this, list.get(0));
//                gvClasscontetn.setAdapter(classifiyGridAdapter);
//            }

    }

    @Override
    public void OnErrorData(String code, Integer tag) {

    }
}
