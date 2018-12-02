package cn.yinxm.language.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class HomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("yinxm", "HomeActivity onCreate "+this);
        setContentView(R.layout.activity_home);
    }

    public void click2(View view) {
        startActivity(new Intent(HomeActivity.this, MainActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("yinxm", "HomeActivity onResume "+this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("yinxm", "HomeActivity onDestroy "+this);
    }
}
