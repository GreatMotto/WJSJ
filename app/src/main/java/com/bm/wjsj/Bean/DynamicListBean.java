package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 动态列表bean
 */
public class DynamicListBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public String id;
    public String userid;
    public String age;
    public String sex;
    public String nickname;
    public String praise;//是否点赞 0：未点赞 1：已点赞
    public String praisenum;//点赞数
    public String head;
    public List<ImageBean> imglist;
    public String createTime;
    public String content;//动态内容
    public String level;
    public String juli;//距离
    public String comnum;//评论数
    public String minutes;
    public String hours;
    public String days;
}
