package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wangxl01 on 2015/10/21.
 */
public class orderBean implements Serializable{

//    public String address;
//    public String remark;
//    public String consignee;
//    public String ids;
//    public String paytype;
//    public String realpay;
//    public String userid;
//    public String account;
//    public String mobile;
//    public String ordernum;
//    public String createTime;
//    public String id;
//    public List<orderinfoBean> orderlist;

//    order={
//        address=ä¸­å½å±±ä¸èç¿,
//        remark=,
//                orderlist=[
//        {
//            "pname": "æè¾æ¯21",
//                "path": "\/wj_upload\/temp\/3.jpg",
//                "spec1": "ç½è²,å¤§å·",
//                "count": 1,
//                "productid": "1"
//        }
//        ],
//        consignee=çå¤§é¤,
//                ids=,
//                paytype=æ¯ä»å,
//                realpay=200.0,
//                userid=77,
//                account=S,
//                mobile=15972823048,
//                ordernum=1445685305824,
//                createTime=2015-10-2419: 15: 05,
//                id=412

    public String id;
    public String ordernum;
    public String consignee;
    public String mobile;
    public String address;
    public String cityid;
    public String realpay;
    public String status;
    public String remark;
    public String expcompany;
    public String expnumber;
    public String paytype;
    public String paytime;
    public String createTime;
    public List<orderinfoBean> orderinfo;


    public String ids;
    public String userid;
    public String account;
    public String orderlist;


//    public List<orderinfoBean> orderlist;
//    {
//        "id": "152",
//            "createTime": "2015-10-19 19:41:07",
//            "expnumber": "",
//            "remark": "",
//            "status": "0",
//            "address": "æ¶è´§å°åï¼æ­¦æ±å¸åååå",
//            "consignee": "æ¶è´§äººï¼Lily",
//            "paytype": "",
//            "ordernum": "1445254867305",
//            "realpay": "212.0",
//            "orderinfo":[]
//        "expcompany": "",
//            "paytime": "",
//            "mobile": "1383838438",
//            "cityid": "1"
//    },


}
