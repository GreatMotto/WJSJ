package com.bm.wjsj.Bean;

import java.io.Serializable;
import java.util.List;

/**
 * 评论bean
 */
public class ReviewBean implements Serializable {

    private static final long serialVersionUID = 1L;
    public String age;
    public String content;
    public String createTime;
    public String head;
    public String id;
    public String level;
    public String nickname;
    public String praise;
    public String praisenum;
    public String sex;
    public String mainuserid;
    public List<ReportBean> report;
}
