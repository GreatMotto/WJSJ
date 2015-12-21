package com.bm.wjsj.Bean;


import java.io.Serializable;

/**
 * Created by Administrator on 2015/7/31 0031.
 */
public class classifyContentBean implements Serializable {
    public String name;
    public int id;
    public ImageBean iamge;

    public classifyContentBean(int id, ImageBean iamge, String name) {
        this.id = id;
        this.iamge = iamge;
        this.name = name;
    }
}
