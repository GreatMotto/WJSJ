package com.bm.wjsj.Bean;

import java.io.Serializable;

/**
 * Created by liuy02 on 2015/10/19.
 */
public class StoreListBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    public String id;

    public String price;

    public int ordernum;

    public String salenum;

    public String name;

    public String path;

    public String score;

    public String productid;

    public String stock;//积分商品库存数

    public String spec;

    public String count;//商品库存--废弃

    public String isEMS;//是否包邮，1 = 否  2 = 是


}
