package cn.yinxm.test.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.yinxm.test.R;
import cn.yinxm.test.ui.fragment.MainFragment;
import cn.yinxm.test.ui.fragment.Test1Fragment;


public class FragmentTestActivity extends FragmentActivity implements Test1Fragment.OnFragmentInteractionListener{
    private  static final String  TAG = "yinxm";

    private String[] tabTitles = {"最近通话", "联系人", "拨号键盘", "消息"};
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private MainViewPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_test);

        tabLayout = (TabLayout) findViewById(R.id.tabId);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        fragmentList.add(Test1Fragment.newInstance(1));
        fragmentList.add(Test1Fragment.newInstance(2));
        fragmentList.add(Test1Fragment.newInstance(3));
        fragmentList.add(new MainFragment());

        adapter = new MainViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(3);
        processIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, ""+this+", onNewIntent="+intent);
        processIntent(intent);
    }

    private void initView() {
//        tabLayout.setpa
    }

    private class MainViewPagerAdapter extends FragmentPagerAdapter {
        MainFragment mainFragment;

        public MainViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }
    };

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("yinxm", "onFragmentInteraction uri="+uri);
    }

    private void processIntent(Intent intent) {
        Log.d(TAG, "FragmentTestActivity.processIntent intent="+intent);
        if (intent != null) {
            Bundle bundle1 =  intent.getExtras();
            final Bundle bundle =  intent.getBundleExtra("action_huiliao_vcr");
            Log.d(TAG, "bundle1="+bundle1+", bundle="+bundle);
            if (bundle != null) {//有需要的信息
                //选中消息页
                viewPager.setCurrentItem(3);
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                        MainFragment fragment = (MainFragment) fragmentList.get(3);
                //给Fragment传递数据1
                        fragment.setArguments(bundle);


                        //给Fragment传递数据2
//                        int opt = bundle.getInt("opt");
//                        Log.d(TAG, "opt="+opt);
//                        if (opt == 10) {
//                            fragment.gotoWorkEcMsg();//java.lang.IllegalStateException: Fragment has not been attached yet.
//                        } else if (opt == 11) {
//                            fragment.gotoMsgList();
//                        }
//                    }
//                }, 3000);
            }

        }
    }

//    @Override
//    public void onBackPressed() {
//        MainFragment fragment = (MainFragment) fragmentList.get(3);
//    }
}
