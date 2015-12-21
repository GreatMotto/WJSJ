package com.bm.wjsj.Http;

import java.io.Serializable;

/**
 * Created by Nathan on 15/5/22.
 */

public class APIResponse<T> implements Serializable
{
    /**
     * 父类数据结构 map里面无限添加对象
     */
    /* 标志位 success成功 fail失败 */
    public String status;

    /* 错误信息 */
    public String msg;

    /* 总数据结构 */
    public MapData<T> data;

}
