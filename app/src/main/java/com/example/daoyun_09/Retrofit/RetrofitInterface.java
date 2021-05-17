package com.example.daoyun_09.Retrofit;



import android.database.Observable;

import com.example.daoyun_09.Entity.defaultInfo;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitInterface {

    /**
     * email code
     */
    @Multipart
    @POST("/sendcode")
    Observable<defaultInfo> httpSendEmailInterface(@Part("email") String email);

}
