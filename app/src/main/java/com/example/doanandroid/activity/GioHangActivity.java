package com.example.doanandroid.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.doanandroid.R;
import com.example.doanandroid.adapter.GioHangAdapter;
import com.example.doanandroid.model.EventBus.TinhTongEvent;
import com.example.doanandroid.model.GioHang;
import com.example.doanandroid.ultil.Server;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DecimalFormat;
import java.util.List;

public class GioHangActivity extends AppCompatActivity {
    TextView giohangtrong,tongtien;
    Toolbar toolbar;
    RecyclerView recyclerView;
    Button btnmuahang;
    GioHangAdapter adapter;
    long tongtiensp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        initView();
        initControl();
        tongtien();
    }

    private void tongtien() {
        tongtiensp = 0;
        for(int i=0;i<Server.manggiohang.size();i++){
            tongtiensp = tongtiensp + (Server.manggiohang.get(i).getGiasp()*Server.manggiohang.get(i).getSoluong());
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tongtien.setText(decimalFormat.format(tongtiensp));
    }

    private void initControl() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        if(Server.manggiohang.size() == 0){
            giohangtrong.setVisibility(View.VISIBLE);
        }
        else {
            adapter = new GioHangAdapter(getApplicationContext(),Server.manggiohang);
            recyclerView.setAdapter(adapter);
        }
        btnmuahang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ThanhToanActivity.class);
                intent.putExtra("tongtien",tongtiensp);
                startActivity(intent);
            }
        });

    }

    private void initView() {
        giohangtrong =  findViewById(R.id.txtgiohangtrong);
        toolbar =  findViewById(R.id.toobar);
        recyclerView =  findViewById(R.id.recyclerviewgiohang);
        tongtien =  findViewById(R.id.txttongtien);
        btnmuahang =  findViewById(R.id.btnmuahang);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(sticky = true,threadMode = ThreadMode.MAIN)
    public void eventTinhTienTong(TinhTongEvent event){
        if(event != null){
            tongtien();
        }
    }
}