package cn.yinxm.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.yinxm.app.base.BaseActivity;
import cn.yinxm.lib.utils.DeviceUtil;
import cn.yinxm.lib.utils.log.LogUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView((R.id.tv_info))
    TextView tv_info;
    @BindView(R.id.btn_login) Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.setDebug(true);
        ButterKnife.bind(this);
        //        CrashReport.testJavaCrash();

        initView();
    }

    private void initView() {
        tv_info.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tv_info.setText("当前版本："+ DeviceUtil.getVersionName(getApplicationContext())+", versionCode="+DeviceUtil.getVersionCode(getApplicationContext()));
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.d("onResume btnLogin="+btnLogin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_info:
                break;
            case R.id.btn_login:
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                break;
            default:
                ;
        }
    }
}
