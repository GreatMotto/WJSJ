package com.bm.wjsj.Bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/3 0003.
 */
public class TypeBean implements Serializable {
    private static final long serialVersionUID = 1L;
    public int id;
    public String name;

    public TypeBean(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
