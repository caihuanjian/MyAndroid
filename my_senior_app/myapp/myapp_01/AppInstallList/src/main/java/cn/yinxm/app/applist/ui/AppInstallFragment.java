package cn.yinxm.app.applist.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.yinxm.app.applist.R;
import cn.yinxm.app.applist.ui.adapter.AppAdapter;
import cn.yinxm.app.applist.ui.model.AppInfo;
import cn.yinxm.lib.utils.ApkHelper;


/**
 * 应用安装列表
 */
public class AppInstallFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private List<AppInfo> mAppInfoList;
    private ListView listView;
    private BaseAdapter mAdapter;


    public AppInstallFragment() {
    }

    public static AppInstallFragment newInstance() {
        AppInstallFragment fragment = new AppInstallFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_install, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        listView = (ListView) view.findViewById(R.id.listView);
    }

    private void initData() {
        mAppInfoList = new ArrayList<>();


        mAdapter = new AppAdapter (getActivity(), mAppInfoList);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        loadData();
    }

    protected void loadData() {

        Activity activity = getActivity();
        if (activity != null && activity instanceof SingleFragmentActivity) {
            ((SingleFragmentActivity) activity).showLoading();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<AppInfo> list = loadeInstallApps();

                final Activity activity = getActivity();
                if (activity != null && activity instanceof SingleFragmentActivity) {

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAppInfoList.clear();
                            mAppInfoList.addAll(list);
                            mAdapter.notifyDataSetChanged();
                            ((SingleFragmentActivity) activity).hideLoading();
                        }
                    });
                }

            }
        }).start();
    }

    private List<AppInfo> loadeInstallApps() {
        List<AppInfo> appInfoList = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);

        for (PackageInfo packageInfo : packageInfoList) {
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)    // 非系统应用
            {
                AppInfo info = new AppInfo();
                info.appName = packageInfo.applicationInfo.loadLabel(pm)
                        .toString();
                info.pkgName = packageInfo.packageName;
                info.appIcon = packageInfo.applicationInfo.loadIcon(pm);
                // 获取该应用安装包的Intent，用于启动该应用
                info.appIntent = pm.getLaunchIntentForPackage(packageInfo.packageName);

                info.versionName = packageInfo.versionName;
                info.versionCode = packageInfo.versionCode;

                appInfoList.add(info);
            } else {
                // 系统应用　　　　　　　　
            }
        }
        return appInfoList;

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        startActivity(mAppInfoList.get(position).appIntent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        showUninstallTip(mAppInfoList.get(position));
        return true;
    }

    private void showUninstallTip(final AppInfo appInfo) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(appInfo.appName);
        builder.setMessage("要卸载此应用吗？");
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ApkHelper.uninstallApk(getActivity(), appInfo.pkgName);
            }
        });
        builder.show();
    }

}
