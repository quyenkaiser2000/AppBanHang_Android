package com.example.doanandroid.activity;


import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.doanandroid.R;
import com.example.doanandroid.adapter.LoaispAdapter;
import com.example.doanandroid.adapter.SanphamAdapter;
import com.example.doanandroid.model.Loaisp;
import com.example.doanandroid.model.Sanpham;
import com.example.doanandroid.model.SanphamModel;
import com.example.doanandroid.retrofit.ApiBanHang;
import com.example.doanandroid.retrofit.RetrofitClient;
import com.example.doanandroid.ultil.Server;
import com.google.android.material.navigation.NavigationView;
import com.nex3z.notificationbadge.NotificationBadge;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;
    Toolbar toolbar;
    RecyclerView recyclerViewmanghinhchinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaispAdapter loaispAdapter;
    List<Loaisp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;

    List<Sanpham> mangspmoi;
    SanphamAdapter sanphamAdapter;

    NotificationBadge badge;
    FrameLayout frameLayout;
    ImageView imgsearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Server.BASE_URL).create(ApiBanHang.class);
        Anhxa();
        ActionBar();


        if(isConnected(this)){
            System.out.println("ok");
            ActionViewFlipper();
            GetDuLieuLoaisp();
            GetDuLieuSPMoiNhat();
            GetEventClick();
        }
        else {
            Toast.makeText(getApplicationContext(), "khong co internet", Toast.LENGTH_SHORT).show();
            System.out.println("no");
        }



    }

    private void GetEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent dienthoai = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        dienthoai.putExtra("loai",1);
                        startActivity(dienthoai);
                        break;
                    case 2:
                        Intent laptop = new Intent(getApplicationContext(),DienThoaiActivity.class);
                        laptop.putExtra("loai",2);
                        startActivity(laptop);
                        break;
                    case 3:
                        Intent lienhe = new Intent(getApplicationContext(),LienHeActivity.class);
                        startActivity(lienhe);
                        break;
                    case 4:
                        Intent mapmini = new Intent(getApplicationContext(),MapminiActivity.class);
                        startActivity(mapmini);
                        break;
                    case 5:
                        Intent donhang = new Intent(getApplicationContext(),XemDonActivity.class);
                        startActivity(donhang);
                        break;
                }
            }
        });
    }

    private void GetDuLieuSPMoiNhat() {
       compositeDisposable.add(apiBanHang.getspMoi()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(
                       sanphamModel -> {
                            if(sanphamModel.isSuccess()){
                                mangspmoi = sanphamModel.getResult();
                                sanphamAdapter = new SanphamAdapter(getApplicationContext(),mangspmoi);
                                recyclerViewmanghinhchinh.setAdapter(sanphamAdapter);
                            }
                       },
                       Throwable ->{
                           Toast.makeText(getApplicationContext(), "Không kết nối được với server"+Throwable.getMessage(), Toast.LENGTH_SHORT).show();
                       }
               ));
    }

    private void GetDuLieuLoaisp() {
        compositeDisposable.add(apiBanHang.getloaiSP()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                loaispModel -> {
                    if(loaispModel.isSuccess()){
                        mangloaisp = loaispModel.getResult();
                        loaispAdapter = new LoaispAdapter((getApplicationContext()),mangloaisp);
                        listViewManHinhChinh.setAdapter(loaispAdapter);
                    }
                }
        ));
    }

    private void ActionViewFlipper() {
        ArrayList<String> mangquangcao = new ArrayList<>();
        mangquangcao.add("https://i.ytimg.com/vi/36HnmEcKDJk/maxresdefault.jpg");
        mangquangcao.add("https://thietkehaithanh.com/wp-content/uploads/2019/06/huong-dan-thiet-ke-banner-dien-thoai-bang-photoshop.jpg");
        mangquangcao.add("https://i.ytimg.com/vi/36HnmEcKDJk/maxresdefault.jpg");
        for (int i = 0;i<mangquangcao.size();i++)
        {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView((imageView));
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setInAnimation(animation_slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        imgsearch = findViewById(R.id.imgsearch);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewlipper);
        recyclerViewmanghinhchinh = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerViewmanghinhchinh.setLayoutManager(layoutManager);
        recyclerViewmanghinhchinh.setHasFixedSize(true);
        navigationView = (NavigationView) findViewById(R.id.navigationview);
        listViewManHinhChinh = (ListView) findViewById(R.id.listViewmanhinhchinh);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        badge = findViewById(R.id.menu_sl);
        frameLayout = findViewById(R.id.framegiohang);
        mangloaisp  = new ArrayList<>();

        mangspmoi = new ArrayList<>();

        if(Server.manggiohang == null){
            Server.manggiohang = new ArrayList<>();
        }
        else {
                int totalItem = 0;
                for(int i =0;i<Server.manggiohang.size();i++){
                    totalItem =totalItem+Server.manggiohang.get(i).getSoluong();
                }
                badge.setText(String.valueOf(totalItem));

        }
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent giohang = new Intent(getApplicationContext(),GioHangActivity.class);
                startActivity(giohang);
            }
        });
        imgsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalItem = 0;
        for(int i =0;i<Server.manggiohang.size();i++){
            totalItem =totalItem+Server.manggiohang.get(i).getSoluong();
        }
        badge.setText(String.valueOf(totalItem));
    }

    private boolean isConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected()) ){
            return true;

        }
        else {
            return false;
        }
    }
}