package com.example.doanandroid.ultil;

import com.example.doanandroid.model.GioHang;
import com.example.doanandroid.model.User;

import java.util.List;

public class Server {
    public static final String BASE_URL = "http://192.168.1.8:8080/server/";
    public static List<GioHang> manggiohang;
    public  static User user_current = new User();
}
