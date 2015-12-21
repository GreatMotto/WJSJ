package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 动态bean
 */
public class DynamicBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public String id;
    public String createTime;
    public String content;//动态内容
    public String praiseCount;//点赞数
    public String isPraise;//是否点赞 0：未点赞 1：已点赞
    public List<ImageBean> picList;
    public String hot;
    public String commentCount;//评论数
    public UserInfo user;
    public String distance;//距离
}
