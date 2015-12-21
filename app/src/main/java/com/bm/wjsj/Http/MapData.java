package com.bm.wjsj.Http;

import com.bm.wjsj.Bean.BannerBean;
import com.bm.wjsj.Bean.DynamicInfo;
import com.bm.wjsj.Bean.Helper;
import com.bm.wjsj.Bean.ImageBean;
import com.bm.wjsj.Bean.Message;
import com.bm.wjsj.Bean.Page;
import com.bm.wjsj.Bean.Paytype;
import com.bm.wjsj.Bean.PostDetailInfo;
import com.bm.wjsj.Bean.Product;
import com.bm.wjsj.Bean.SpecBean;
import com.bm.wjsj.Bean.UserInfo;
import com.bm.wjsj.Bean.orderBean;
import com.bm.wjsj.Bean.scoreproInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Nathan on 15/5/22.
 */
public class MapData<T> implements Serializable {
    /**
     * MapData封装所有数据类型
     */
    /* 数据List */
    public List<T> list;

    public List<T> result;

    public UserInfo appuser;

    public Page page;

    public Helper helper;

    public List<ImageBean> bannaerList;

    public List<ImageBean> moduleList;

    public List<ImageBean> productList;

    public Product product;

    public SpecBean spec1;

    public SpecBean spec2;

    public DynamicInfo dynamicinfo;

    public PostDetailInfo postinfo;

    public List<Paytype> paytype;//支付方式列表

    public String discount;//折扣

    public String isfree;//0包邮 1不包邮

    public String freight;//邮费

    public String num;// 关注数/是否已关注（0：否，1：是）

    public String score;//积分

    public String price;//商品规格的价钱
    public String count;//商品规格的数量

    public scoreproInfo scoreproinfo;

    public Message message;

    public orderBean order;//订单

    public String status;//上线状态

    public String ismutual;//是否被关注（0：否，1：是）

    public String ordernum;//订单编号

    /*推送*/
    public String id;//该设置信息id

    public String isfollow;//是否接受关注消息，0是；1否

    public String isactivity;//是否接受活动消息， 0是；1否

    public String sysnum;//系统未读消息
    public String actnum;//活动未读消息
    public String attnum;//关注未读消息

    public BannerBean advinfo;//广告webview
}

