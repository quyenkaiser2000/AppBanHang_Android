package com.example.doanandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.doanandroid.R;
import com.example.doanandroid.model.GioHang;
import com.example.doanandroid.model.Sanpham;
import com.example.doanandroid.ultil.Server;
import com.nex3z.notificationbadge.NotificationBadge;

import java.text.DecimalFormat;

public class ChiTietActivity extends AppCompatActivity {
    TextView tensp,giasp,mota;
    Button btnthem;
    ImageView imgage;
    Spinner spinner;
    Toolbar toolbar;
    Sanpham sanpham;
    NotificationBadge badge;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);
        initView();
        ActionBar();
        initData();
        initControl();
    }

    private void initControl() {
        btnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                themGioHang();
            }
        });
    }

    private void themGioHang() {
        if(Server.manggiohang.size() > 0){
            boolean flag = false;
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString()) ;
            for (int i  = 0 ;i < Server.manggiohang.size();i++){
                if(Server.manggiohang.get(i).getIdsp()== sanpham.getId()){
                    Server.manggiohang.get(i).setSoluong(soluong + Server.manggiohang.get(i).getSoluong());
                    long gia = sanpham.getGiasanpham() * Server.manggiohang.get(i).getSoluong();
                    Server.manggiohang.get(i).setGiasp(gia);
                    flag = true;
                }
            }
            if(flag == false ){
                long gia = sanpham.getGiasanpham() * soluong;
                GioHang gioHang = new GioHang();
                gioHang.setGiasp(gia);
                gioHang.setSoluong(soluong);
                gioHang.setIdsp(sanpham.getId());
                gioHang.setTensp(sanpham.getTensanpham());
                gioHang.setHinhsp(sanpham.getHinhanhsanpham());
                Server.manggiohang.add(gioHang);
            }
        }else{
            int soluong = Integer.parseInt(spinner.getSelectedItem().toString()) ;
            long gia = sanpham.getGiasanpham() * soluong;
            GioHang gioHang = new GioHang();
            gioHang.setGiasp(gia);
            gioHang.setSoluong(soluong);
            gioHang.setIdsp(sanpham.getId());
            gioHang.setTensp(sanpham.getTensanpham());
            gioHang.setHinhsp(sanpham.getHinhanhsanpham());
            Server.manggiohang.add(gioHang);

        }
        badge.setText(String.valueOf(Server.manggiohang.size()));
    }

    private void initData() {
        sanpham = (Sanpham) getIntent().getSerializableExtra("chitiet");
        tensp.setText(sanpham.getTensanpham());
        mota.setText(sanpham.getMotasanpham());
        Glide.with(getApplicationContext()).load(sanpham.getHinhanhsanpham()).into(imgage);
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        giasp.setText("Giá: "+decimalFormat.format(sanpham.getGiasanpham())+ " Đ");
        Integer[] so = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapterspin = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item,so);
        spinner.setAdapter(adapterspin);
    }

    private void initView() {
        tensp = findViewById(R.id.txttensp);
        giasp = findViewById(R.id.txtgiasp);
        mota = findViewById(R.id.txtmotachitiet);
        btnthem = findViewById(R.id.btnthemvaogiohang);
        spinner = findViewById(R.id.spinner);
        imgage = findViewById(R.id.imgchitiet);
        toolbar = findViewById(R.id.toolbar);
        badge = findViewById(R.id.menu_sl);
        if(Server.manggiohang !=null){
            badge.setText(String.valueOf(Server.manggiohang.size()));
        }
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
}