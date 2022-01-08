package com.example.doanandroid.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.doanandroid.R;
import com.example.doanandroid.adapter.DienThoaiAdapter;
import com.example.doanandroid.adapter.SanphamAdapter;
import com.example.doanandroid.model.Sanpham;
import com.example.doanandroid.retrofit.ApiBanHang;
import com.example.doanandroid.retrofit.RetrofitClient;
import com.example.doanandroid.ultil.Server;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DienThoaiActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    ApiBanHang apiBanHang;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    int page=1;
    int loai ;
    DienThoaiAdapter adapterDT;
    List<Sanpham> sanphammoilist;
    LinearLayoutManager linearLayoutManager;
    Handler handler = new Handler();
    boolean isLoading = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dien_thoai);
        apiBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiBanHang.class);
        loai = getIntent().getIntExtra("loai",1);
        Anhxa();
        ActionBar();
        getData(page);
        addEventLoad();
    }

    private void addEventLoad() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(isLoading == false ){
                    if(linearLayoutManager.findLastCompletelyVisibleItemPosition() == sanphammoilist.size()-1){
                        isLoading = true;
                        loadMore();
                    }
                }
            }
        });
    }

    private void loadMore() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                sanphammoilist.add(null);
                adapterDT.notifyItemInserted(sanphammoilist.size()-1);
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //remover null
                sanphammoilist.remove(sanphammoilist.size()-1);
                adapterDT.notifyItemRemoved(sanphammoilist.size());
                page = page+1;
                getData(page);
                adapterDT.notifyDataSetChanged();
                isLoading = false;
            }
        },2000);
    }

    private void getData(int page) {
        compositeDisposable.add(apiBanHang.getsanpham(page,loai)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanphamModel -> {
                            if(sanphamModel.isSuccess()){
                                if (adapterDT == null){
                                    sanphammoilist = sanphamModel.getResult();
                                    adapterDT = new DienThoaiAdapter(getApplicationContext(),sanphammoilist);
                                    recyclerView.setAdapter(adapterDT);
                                }
                                else {
                                    int vitri = sanphammoilist.size()-1;
                                    int soluongadd = sanphamModel.getResult().size();
                                    for(int i=0;i<soluongadd;i++){
                                        sanphammoilist.add(sanphamModel.getResult().get(i));
                                    }
                                    adapterDT.notifyItemRangeInserted(vitri,soluongadd);
                                }


                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Đã hết", Toast.LENGTH_SHORT).show();
                                isLoading=true;
                            }
                        },
                        Throwable ->{
                            Toast.makeText(getApplicationContext(), "Không kết nối được với server"+Throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                ));

    }

    private void ActionBar() {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
        }

        private void Anhxa() {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            recyclerView = findViewById(R.id.recyclerview_dt);
            linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setHasFixedSize(true);
            sanphammoilist = new ArrayList<>();

        }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}