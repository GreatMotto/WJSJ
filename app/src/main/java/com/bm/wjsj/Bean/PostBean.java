package com.bm.wjsj.Bean;

import java.io.Serializable;

/**
 * 帖子列表bean
 */
public class PostBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public String id;//帖子id
    public String createTime;//帖子发布时间
    public String title;//帖子标题
    public String nickname;
    public String isrecommend;
    public String level;//发帖人等级
    public String firstimg;//帖子首图
    public String praisenum;//点赞数
    public String praise;//是否点赞 0：未点赞 1：已点赞
    public String comnum;//评论数
}
