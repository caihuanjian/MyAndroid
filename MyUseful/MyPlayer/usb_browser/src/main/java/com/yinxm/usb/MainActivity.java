package com.yinxm.usb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        info = findViewById(R.id.tv_info);
        AppUsbManager.getInstance().init(getApplicationContext());
    }

    public void updateInfo(View view) {
        info.setText("");
        List<StorageVolumeInfo> list = AppUsbManager.getInstance().getVolumeList();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("size=" + list.size()+"\n");

        for (StorageVolumeInfo storageVolume : list) {
            stringBuilder.append("" + storageVolume + "\n\n");
        }
        Log.e("yinxm", stringBuilder.toString());
        info.setText(stringBuilder.toString());
    }
}
