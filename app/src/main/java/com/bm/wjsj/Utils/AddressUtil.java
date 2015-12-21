package com.bm.wjsj.Utils;

import android.content.Context;

import com.bm.wjsj.Bean.CityBean;
import com.bm.wjsj.Bean.ProvinceBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.util.EncodingUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxl01 on 2015/11/13.
 */
public class AddressUtil {
    private static List<ProvinceBean> listProvince = new ArrayList<ProvinceBean>();
    private static Context context;

    private static AddressUtil instance;
    private AddressUtil (){}

    public static AddressUtil getInstance(Context con) {
        if (instance == null) {
            instance = new AddressUtil();
            context=con;
            getJson();
        }
        return instance;
    }

    // 解析JSON，获取城市列表
    private static void getJson() {
        if(listProvince.size()<1) {
            listProvince.clear();
            String jsonData_city = getFromAssets("city.json");
            Gson gson = new Gson();
            listProvince = gson.fromJson(jsonData_city, new TypeToken<List<ProvinceBean>>() {
            }.getType());
        }
    }

    // 解析assets文件
    private static String getFromAssets(String fileName) {
        String result = "";
        InputStream in=null;
        try {
            in = context.getResources().getAssets().open(fileName);
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
        finally {
            if(in!=null){
                try{
                    in.close();
                }catch (Exception ex){}
            }
        }
        return result;
    }

    public static String getCityNameById(String provinceId, String cityId) {
        String names = "";
        if(listProvince.size()<1) {
            getListProvince();
        }
        for (ProvinceBean provinceBean : listProvince) {
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

    public static List<ProvinceBean> getListProvince()
    {
        if(listProvince.size()>0) {
            return listProvince;
        }
        else{
            getJson();
            return listProvince;
        }
    }
}
