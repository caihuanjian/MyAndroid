package com.yinxuming.mywificonnect;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {


    private static final String WIFI_PROFILE = "/etc/wpa_supplicant/wpa_supplicant.conf";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}


//注意， raspbian默认是无法像ubuntu或windows一样通过选择wifi来连接公司Baidu_Wifi的， 因此需要特殊配置：
//        编辑相关的配置文件：
//        sudo vim /etc/wpa_supplicant/wpa_supplicant.conf
//        增加百度wifi配置如下：
//
//        # 配置百度的wifi
//        network={
//        ssid="Baidu_WiFi"
//        key_mgmt=WPA-EAP
//        eap=PEAP
//        identity="USERNAME"
//        password="PASSWORD"
//        }