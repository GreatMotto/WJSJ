package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 杨凯
 * @description 购物车对象
 * @time 2014.12.22
 */
public class ShopCarBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public String spec;
    public String id;//购物车记录ID
    public String price;
    public String count;
    public String path;
    public String productid;//商品ID
    public String speclist;
    public String pname;
    public boolean isSelect;
    public String stock;//库存
}

