package com.bm.wjsj.SpiceStore;

/**
 * Created by wangxl01 on 2015/10/20.
 */
public enum PayTypeEnum
{
    ZhiFuBao(1, "支付宝"), WinXin(2, "微信支付"), YinLian(3, "银联"),DaoFu(4,"货到付款");
    private int _value;
    private String _name;
    private PayTypeEnum(int value, String name)
    {
        _value = value;
        _name = name;
    }
    public int value()
    {
        return _value;
    }
    public String getName()
    {
        return _name;
    }
}