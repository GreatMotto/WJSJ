package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yangy on 2015/10/17.
 */
public class SpecBean implements Serializable{
//    {"spec1":{"id":"3","specList":[{"id":"4","name":"白色"}],"name":"颜色"}}
    public String id;
    public List<SpecBean> specList;
    public String name;
    public boolean isSelect;
}
