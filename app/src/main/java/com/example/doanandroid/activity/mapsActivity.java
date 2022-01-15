package com.example.doanandroid.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doanandroid.R;

public class mapsActivity extends AppCompatActivity {
    EditText noidi,noiden;
    Button btChiDuong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        noidi = findViewById(R.id.noidi);
        noiden = findViewById(R.id.noiden);
        btChiDuong = findViewById(R.id.btnchiduong);
        btChiDuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sNoidi = noidi.getText().toString().trim();
                String sNoiden = noiden.getText().toString().trim();

                if(sNoidi.equals("") && sNoiden.equals("")){
                    Toast.makeText(getApplicationContext(),"Nhập 2 địa chỉ",Toast.LENGTH_SHORT).show();
                }else {
                    DisplayTrack(sNoidi,sNoiden);
                }
            }
        });
    }

    private void DisplayTrack(String sNoidi, String sNoiden) {
        try {
            Uri uri = Uri.parse("https://www.google.co.in/maps/dir/" + sNoidi + "/" + sNoiden);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}