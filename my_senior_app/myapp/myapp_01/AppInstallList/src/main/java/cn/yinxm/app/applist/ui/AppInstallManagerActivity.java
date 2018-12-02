package cn.yinxm.app.applist.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import cn.yinxm.app.applist.R;
import cn.yinxm.lib.utils.ApkHelper;
import cn.yinxm.lib.utils.log.LogUtil;

/**
 * 应用管理相关测试
 */
public class AppInstallManagerActivity extends Activity implements View.OnClickListener {

    private EditText etInput;
    private Spinner spinner;

    private Button btnInstall, btnUninstall;

    private MySpinnerAdapter mAdapter;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_install_manager);

        etInput = (EditText) findViewById(R.id.etInput);
        spinner = (Spinner) findViewById(R.id.spinner);
        btnInstall = (Button) findViewById(R.id.btnInstall);
        btnUninstall = (Button) findViewById(R.id.btnUninstall);

        initData();

    }

    private void initData() {

        mList = new ArrayList<>();
        mList.add("普通模式");
        mList.add("静默模式");

        // android.R.layout.simple_spinner_item 、simple_spinner_dropdown_item
//        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,arrayList));
        mAdapter = new MySpinnerAdapter();
        spinner.setAdapter(mAdapter);
        spinner.setPrompt("模式选择");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnInstall.setOnClickListener(this);
        btnUninstall.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int selectedPos =  spinner.getSelectedItemPosition();
        String text = etInput.getText().toString();
        LogUtil.d("selectedPos="+selectedPos+", "+text);
        if (TextUtils.isEmpty(text)) {
            return;
        }
        switch (v.getId()) {
            case R.id.btnInstall:
                if (selectedPos == 1) {
                    try {
                        ApkHelper.installApkSilent(this, text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ApkHelper.installApk(this, text);
                }
                break;
            case R.id.btnUninstall:
                if (selectedPos == 1) {
                    try {
                        ApkHelper.uninstallApkSilent(this, text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ApkHelper.uninstallApk(this, text);
                }
                break;
        }
    }

    private class MySpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) { // 布局view为空，实例化，并添加View复用viewHolder
                convertView = AppInstallManagerActivity.this.getLayoutInflater().inflate(R.layout.spinner_item, null);
                viewHolder = new ViewHolder();
                viewHolder.tv = (TextView) convertView.findViewById(R.id.tv);
                convertView.setTag(viewHolder);
            } else { // 复用布局
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv.setText(mList.get(position));

            return convertView;
        }
        class ViewHolder {
            private TextView tv;
        }
    }
}
