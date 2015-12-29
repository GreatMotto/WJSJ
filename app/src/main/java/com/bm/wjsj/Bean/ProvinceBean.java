package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

public class ProvinceBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public String name;
    public String id;
    public List<CityBean> children;

}
