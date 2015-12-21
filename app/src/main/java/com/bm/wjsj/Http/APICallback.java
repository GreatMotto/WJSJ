package com.bm.wjsj.Http;

import android.content.Context;
import android.util.Log;

import com.bm.wjsj.Utils.ErrorUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Nathan on 15/5/22.
 */
public class APICallback implements Callback<APIResponse> {

    private final static String TAG = APICallback.class.getSimpleName();

    private Integer tag;

    private OnResposeListener mOnResposeListener;

    private Context context;


    //构造函数
    public APICallback(Context context, OnResposeListener onResposeListener, Integer tag) {
        this.mOnResposeListener = onResposeListener;
        this.tag = tag;
        this.context = context;
    }

    public interface OnResposeListener {

        public void OnFailureData(String error, Integer tag);

        public void OnSuccessData(APIResponse apiResponse, Integer tag);

        public void OnErrorData(String code, Integer tag);
    }

    public void setOnResposeListener(OnResposeListener onResposeListener) {
        mOnResposeListener = onResposeListener;
    }

    @Override
    public void success(APIResponse apiResponse, Response response) {
        String status = apiResponse.status;
        //success
        //Log.e("status", status);
        if (status.equals("0")) {
            mOnResposeListener.OnSuccessData(apiResponse, tag);
            //apiResponse.data 可能为空，下边的打印可能异常
            //Log.e("data", apiResponse.data.toString());
        } else //error
        {
            mOnResposeListener.OnErrorData(apiResponse.msg, tag);
            ErrorUtils.showErrorMsg(context, apiResponse.msg);
            //Log.e("ErrorCode:", apiResponse.msg.split("_")[1]);
            //Log.e("apiResponse:",apiResponse.status);
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Log.e(TAG, "Error : " + error.getMessage());
        mOnResposeListener.OnFailureData(error.getMessage(), tag);
    }

}
