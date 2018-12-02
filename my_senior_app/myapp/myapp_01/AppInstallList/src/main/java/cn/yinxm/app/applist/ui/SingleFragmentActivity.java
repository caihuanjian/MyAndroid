package cn.yinxm.app.applist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ProgressBar;

import cn.yinxm.app.applist.R;
import cn.yinxm.lib.utils.ClickContinuousEvent;

public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();

    private View tv_title;
    private ProgressBar progressBar;
    /**
     * 连续点击判断工具
     */
    private ClickContinuousEvent mClickContinuousEvent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        tv_title =  findViewById(R.id.tv_title);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mClickContinuousEvent = new ClickContinuousEvent(3);

        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickContinuousEvent.click()) {
                    startActivity(new Intent(SingleFragmentActivity.this, AppInstallManagerActivity.class));
                }
            }
        });

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    protected void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
