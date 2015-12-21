package com.bm.wjsj.Http;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit.mime.TypedFile;

/**
 * Created by Nathan on 15/5/22.
 */
public interface APIService {

    @GET("/{action}") //GET不带参数
    public void GetAPI(@Path(value = "action", encode = false) String action, Callback<APIResponse> callback);

    @GET("/{action}") //GET带参数
    public void GetAPI(@Path(value = "action", encode = false) String action, @QueryMap Map<String, String> params, Callback<APIResponse> callback);

    @POST("/{action}")//POST不带参数
    public void PostAPI(@Path(value = "action", encode = false) String action, Callback<APIResponse> callback);

    @POST("/{action}")//POST带参数
    @FormUrlEncoded
    public void PostAPI(@Path(value = "action", encode = false) String action, @FieldMap Map<String, String> params, Callback<APIResponse> callback);

    @POST("/{action}")//POST 上传key==file的文件
    @Multipart
    public void PostAPI(@Path(value = "action", encode = false) String action, @Part("head") TypedFile photo, @PartMap Map<String, String> params, Callback<APIResponse> callback);

    @POST("/{action}")//POST 多个文件
    public void PostAPI(@Path(value = "action", encode = false) String action, @Query("a") int a, @Body Map<String, Object> params, Callback<APIResponse> callback);

}