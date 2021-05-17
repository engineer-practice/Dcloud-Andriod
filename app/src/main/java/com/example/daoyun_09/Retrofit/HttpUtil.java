package com.example.daoyun_09.Retrofit;

import com.example.daoyun_09.Entity.defaultInfo;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpUtil{

    public static final String MULTIPART_FORM_DATA = "multipart/form-data";
    private static Retrofit mRetrofit;

    //初始化Rerofit
    private static Retrofit init() {
        if (mRetrofit != null) return mRetrofit;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(20L, TimeUnit.SECONDS)
                .readTimeout(15L, TimeUnit.SECONDS)
                .writeTimeout(15L, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl("http://120.77.220.113:8080/")
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
          return mRetrofit;
    }

    /**
     * 用户模块 <==================================================================================>
     */

    //发送邮件
    public static void sendEmail(String email, defaultInfo callback) {
        Retrofit retrofit = init();
        RetrofitInterface service = retrofit.create(RetrofitInterface.class);
        service.httpSendEmailInterface(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(callback);
    }

}
