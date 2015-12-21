package com.bm.wjsj.Attention;

import com.bm.wjsj.Bean.AttentionListBean;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/10/26.
 */
public class SortComparator implements Comparator{
    @Override
    public int compare(Object lhs, Object rhs) {
        AttentionListBean a = (AttentionListBean) lhs;
        AttentionListBean b = (AttentionListBean) rhs;
        if (Double.parseDouble(a.juli) < Double.parseDouble(b.juli)){
            return -1;
        }else if (Double.parseDouble(a.juli) == Double.parseDouble(b.juli)){
            return 0;
        }else if (Double.parseDouble(a.juli) > Double.parseDouble(b.juli)){
            return 1;
        }else {
            return 0;
        }
    }
}
