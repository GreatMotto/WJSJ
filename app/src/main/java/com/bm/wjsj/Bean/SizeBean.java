package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 规格
 * Created by Administrator on 2015/8/10 0010.
 */
public class SizeBean implements Serializable {
    public String name;
    public String id;
    public String parentId;
    public String level;
    public String sTypeId;
    public String typeId;
    public List<SizeBean> list;
    public boolean isSelect;
}
