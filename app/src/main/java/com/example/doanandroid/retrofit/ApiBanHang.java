package com.example.doanandroid.retrofit;

import com.example.doanandroid.model.LoaispModel;
import com.example.doanandroid.model.SanphamModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaispModel> getloaiSP();


    @GET("getsanphammoinhat.php")
    Observable<SanphamModel> getspMoi();

    @POST("chitiet.php")
    @FormUrlEncoded
    Observable<SanphamModel> getsanpham(
            @Field("page") int page,
            @Field("loai") int loai
    );
}
