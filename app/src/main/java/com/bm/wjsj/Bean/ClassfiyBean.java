package com.bm.wjsj.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/30 0030.
 */
public class ClassfiyBean implements Serializable {
    public String name;
    public int resource;
    public int id;

    public ClassfiyBean(String name, int resource, int id) {
        this.name = name;
        this.resource = resource;
        this.id = id;
    }
}
