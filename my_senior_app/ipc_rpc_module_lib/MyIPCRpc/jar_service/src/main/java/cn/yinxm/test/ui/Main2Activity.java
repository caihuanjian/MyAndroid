package cn.yinxm.test.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.yinxm.test.R;

public class Main2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Intent intent = getIntent();
        Log.d("yinxm", "Main2Activity="+Main2Activity.this+", intent="+intent);
//        startActivity(new Intent(this, FragmentTestActivity.class));
        intent.setClass(this, FragmentTestActivity.class);
        startActivity(intent);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(Main2Activity.this, FragmentTestActivity.class));
//
//            }
//        }, 3000);
    }
}
