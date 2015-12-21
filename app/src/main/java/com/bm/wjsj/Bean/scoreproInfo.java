package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangy on 2015/10/21 15:05
 */
public class scoreproInfo implements Serializable{
//    "scoreproinfo": {
//        "id": "1",
//                "price": "399",
//                "imglist": [
//        {
//            "path": "/wj_upload/temp/1443153454107_383335.jpg"
//        }
//        ],
//        "description": "让你们的游戏更有激情",
//                "ordernum": 2,
//                "name": "限量版成人玩具",
//                "score": "600"
//    }
//},
    public String id;

    public String price;

    public List<ImageBean> imglist;

    public String description;

    public int ordernum;

    public String name;

    public String score;

    public String stock;//商品库存数量
}
