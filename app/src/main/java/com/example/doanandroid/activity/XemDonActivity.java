package com.example.doanandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.doanandroid.R;
import com.example.doanandroid.retrofit.ApiBanHang;
import com.example.doanandroid.retrofit.RetrofitClient;
import com.example.doanandroid.ultil.Server;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class XemDonActivity extends AppCompatActivity {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xem_don);
        apiBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiBanHang.class);
        compositeDisposable.add(apiBanHang.xemDonHang(Server.user_current.getId())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
            donHangModel -> {
                System.out.println(donHangModel.getResult().get(0).getItem().get(0).getTensanpham());
                Toast.makeText(getApplicationContext(), donHangModel.getResult().get(0).getItem().get(0).getTensanpham(), Toast.LENGTH_SHORT).show();
            },throwable -> {
                
                }
        ));
    }
}