package com.example.doanandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.doanandroid.R;
import com.example.doanandroid.retrofit.ApiBanHang;
import com.example.doanandroid.retrofit.RetrofitClient;
import com.example.doanandroid.ultil.Server;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.internal.schedulers.SchedulerMultiWorkerSupport;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class DangKyActivity extends AppCompatActivity {
    EditText email, pass, repass, username, mobile;
    AppCompatButton button;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        initView();
        initControll();
    }

    private void initControll() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangky();
            }
        });

    }
    private void dangky(){
        String str_email = email.getText().toString().trim();
        String str_pass = pass.getText().toString().trim();
        String str_repass = repass.getText().toString().trim();
        String str_username = username.getText().toString().trim();
        String str_mobile = mobile.getText().toString().trim();
        if (TextUtils.isEmpty(str_email)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Email", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_pass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Pass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_repass)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Repass", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_mobile)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập SĐT", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(str_username)) {
            Toast.makeText(getApplicationContext(), "Bạn chưa nhập Username", Toast.LENGTH_SHORT).show();
        }else {
            if (str_pass.equals(str_repass)){
                compositeDisposable.add(apiBanHang.dangky(str_email,str_pass,str_repass, str_username, str_mobile).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        userModel -> {
                            if(userModel.isSuccess()){
                                Server.user_current.setEmail(str_email);
                                Server.user_current.setEmail(str_pass);
                                Intent intent = new Intent(getApplicationContext(),DangNhapActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                System.out.println("that bai");
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(),throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));
            }else {
                Toast.makeText(getApplicationContext(),"Pass chưa khớp", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initView() {
        apiBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiBanHang.class);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        repass = findViewById(R.id.repass);
        button = findViewById(R.id.btndangky);
        mobile = findViewById(R.id.mobile);
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}