package com.bm.wjsj.Http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by Nathan on 15/5/22.
 */
public class APIClient {

    private static APIClient apiClient;

    private APIService apiService;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.util.Date.class, new DateDeserializerUtils())
            .registerTypeAdapter(java.util.Date.class, new DateSerializerUtils())
            .registerTypeAdapterFactory(new ItemTyoeAdapterFactory())
            .enableComplexMapKeySerialization().setDateFormat(DateFormat.LONG).create();

    public static APIClient getInstance()
    {
        if (apiClient==null)
        {
            apiClient = new APIClient();
        }
        return apiClient;
    }

    public APIClient()
    {

    }


    //http header
    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestFacade request) {
//            request.addHeader("User-Agent", "Retrofit-Sample-App");
//            request.addHeader("Authorization", "Client-ID ccc6ca6a65ecdd8");
        }
    };

    public APIService getAPIService(Class objectClass)
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(Urls.HOST)
                .setConverter(new APIConverter(gson,objectClass))
                .setRequestInterceptor(requestInterceptor)
                .build();

        apiService = restAdapter.create(APIService.class);
        return apiService;
    }
}
