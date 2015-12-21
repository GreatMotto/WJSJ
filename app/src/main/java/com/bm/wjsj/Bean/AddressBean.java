package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author 杨凯
 * @description 收货地址对象
 * @time 2014.12.22
 */
public class AddressBean implements Serializable {
    private static final long serialVersionUID = 1L;
//    public String id;
//    public String detail;//地址详情
//    public String appusername;//收货人姓名
//    public String tel;//收货电话
//    public String provinceId;
//    public String cityId;
//    public int select;//是否选中 0：不是 1：是
    public String id;
    public String address;//地址详情
    public String consignee;//收货人姓名
    public String mobile;//收货电话
    public String provinceid;
    public String cityid;
    public String isdefault;//是否选中 0：不是 1：是
}

