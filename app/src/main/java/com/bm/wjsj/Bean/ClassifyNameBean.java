package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class ClassifyNameBean implements Serializable {
    public String id;
    public String picUrl;
    public boolean isSelect = false;
    public String moduleName;
    public String parentId;
    public List<ClassifyNameBean> children;

}
