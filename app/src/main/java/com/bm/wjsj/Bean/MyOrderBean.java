package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/8/13 0013.
 */
public class MyOrderBean implements Serializable {
    public List<Goods> goodsList;
    public int state;
}
