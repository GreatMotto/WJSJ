package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangy on 2015/10/16.
 */
public class Product implements Serializable{

//    {
//        "hpicUrl": "/wj_upload/temp/3.jpg",
//            "id": "2",
//            "isAddCact": "0",
//            "detail": "感觉棒棒的",
//            "price": "222",
//            "salenum": "222",
//            "name": "杜蕾斯2",
//            "isCollect": "0",
//            "picList": [],
//        "oldprice": "215"
//    }
    public String hpicUrl;

    public String id;

    public String isAddCact;

    public String detail;

    public String salenum;

    public String name;

    public String isCollect;

    public String oldprice;

    public String price;

    public List<ImageBean> picList;

    //public String stock;//库存 --废弃

    public String count;//库存

    public String isEMS;//是否包邮，1 = 否  2 = 是
}
