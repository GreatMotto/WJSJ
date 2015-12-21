package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/8/6 0006.
 */
public class Goods implements Serializable {
    public String id;
    public String level1Id;//商品一级分类
    public String position;//
    public String specificationId;
    public boolean isSelect;
    public String picZUrl;//列表主图
    public String plateId;
    public String detail;//商品详情
    public int price;//价格
    public String level2Id;//商品二级分类
    public String picHUrl;
    public String picUrls;
    public int saledCount;//已购数量
    public String specificationIdvalues;
    public String productName;//商品名称
    public String propPrice;//商品原价
    public int isAddCact;//是否加入购物车 0：未加入购物车 1：已加入购物车
    public int isCollect;//是否收藏 0：未收藏 1：已收藏
    public List<ImageBean> picList;
    public List<ImageBean> spcList;
}
