package cn.yinxm.language.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
    Button btn_land_mode;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("yinxm", "MainActivity onCreate "+this);

        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.tv);
        findViewById(R.id.btn_land_mode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LandModeActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("yinxm", "MainActivity onResume "+this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Log.e("yinxm", "MainActivity onDestroy "+this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        int height = tv.getHeight();
        tv.setText(""+height+", density="+getApplicationContext().getResources().getDisplayMetrics().density);
    }
}
