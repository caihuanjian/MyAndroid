package cn.yinxm.mylog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.yinxm.mylog.log4j.ConfigureLog4J;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLog4j();
    }

    private void initLog4j() {




        //加载配置
        ConfigureLog4J configureLog4J=new ConfigureLog4J(getApplicationContext());

        configureLog4J.init();


        for (int i=0; i<1000; i++) {
            LogFileUtil.d("不知道呀就是测试一下啊", "yika");
            LogFileUtil.i("不知道呀就是测试一下啊", "yika");
            LogFileUtil.e("不知道呀就是测试一下啊", "yika");
        }

    }

}
