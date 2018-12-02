package cn.yinxm.test.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import cn.yinxm.test.R;

public class MainActivity extends AppCompatActivity {
    private  static final String  TAG = "yinxm";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FragmentTestActivity.class));
            }
        });

        findViewById(R.id.btnStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Main2Activity.class));
            }
        });

        Log.d(TAG, "MainActivity="+this);
        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, ""+this+", onNewIntent="+intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        Log.d(TAG, "processIntent intent="+intent);
        if (intent != null) {
            Bundle bundle1 =  intent.getExtras();
            Bundle bundle =  intent.getBundleExtra("action_huiliao_vcr");
            Log.d(TAG, "bundle1="+bundle1+", bundle="+bundle);
            if (bundle != null) {//有需要的信息
                int opt = bundle.getInt("opt");
                Log.d(TAG, "opt="+opt);
//               Intent intent1 =  new Intent(MainActivity.this, FragmentTestActivity.class);
//                intent1.putExtra("action_huiliao_vcr", bundle);
//                startActivity(intent1);
//                复用intent
                intent.setClass(MainActivity.this, FragmentTestActivity.class);
                startActivity(intent);
            }

        }
    }
}
