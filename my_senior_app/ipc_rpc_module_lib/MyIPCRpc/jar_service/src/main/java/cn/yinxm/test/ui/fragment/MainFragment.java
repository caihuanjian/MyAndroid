package cn.yinxm.test.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.com.work.ec.communicate.sdk.IWorkEcIMFace;
import cn.yinxm.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements IWorkEcIMFace, View.OnClickListener{
    //左边
    MainLeftFragment leftFragment = new MainLeftFragment();
    Test2Fragment test2Fragment = new Test2Fragment();
    //右边
    MainRightFragment mainRightFragment = new MainRightFragment();
    Test3Fragment test3Fragment = new Test3Fragment();

    private Button btn1, btn2;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle =  getArguments();
        Log.d("yinxm", "MainFragment.onAttach context="+context+", bundle="+bundle);
        //不能在此解析业务数据，必须的等Fragment初始化完毕
        if (bundle != null) {
//            bundle.putInt("opt", 10);
//            bundle.putString("ext", "小明");
            int opt = bundle.getInt("opt", 0);
            if (opt != 0) {
                Log.d("yinxm", "opt="+opt+", ext="+bundle.getString("ext"));
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("yinxm", "MainFragment.onCreateView ");

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        return view;
    }

    private  void initView(View view) {
        btn1 = (Button) view.findViewById(R.id.btn1);
        btn2 = (Button) view.findViewById(R.id.btn2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//        //左边
//        MainLeftFragment leftFragment = new MainLeftFragment();
//        Test2Fragment test2Fragment = new Test2Fragment();
//        //右边
//        MainRightFragment mainRightFragment = new MainRightFragment();
//        Test3Fragment test3Fragment = new Test3Fragment();

        fragmentTransaction.add(R.id.fm_main_left, leftFragment);
        fragmentTransaction.add(R.id.fm_main_left, test2Fragment);
        fragmentTransaction.add(R.id.fm_main_right, mainRightFragment);
        fragmentTransaction.add(R.id.fm_main_right, test3Fragment);
        fragmentTransaction.commit();

        fragmentTransaction.hide(test2Fragment);
        fragmentTransaction.hide(test3Fragment);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("yinxm", "MainFragment.onActivityCreated ");

      /*  Bundle bundle =  getArguments();
        Log.d("yinxm", "bundle="+bundle);
        //Fragment onCreateView初始化完毕 解析业务数据
        if (bundle != null) {
//            bundle.putInt("opt", 10);
//            bundle.putString("ext", "小明");
            int opt = bundle.getInt("opt", 0);
            if (opt != 0) {
                Log.d("yinxm", "opt="+opt+", ext="+bundle.getString("ext"));
                switch (opt) {
                    case 10:
                        gotoWorkEcMsg();
                        break;
                    case 11:
                        gotoMsgList();
                        break;
                }
            }
        }*/
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("yinxm", "MainFragment.onStart ");
        Bundle bundle =  getArguments();
        Log.d("yinxm", "bundle="+bundle);
        //Fragment onCreateView初始化完毕 解析业务数据
        if (bundle != null) {
//            bundle.putInt("opt", 10);
//            bundle.putString("ext", "小明");
            int opt = bundle.getInt("opt", 0);
            if (opt != 0) {
                Log.d("yinxm", "opt=" + opt + ", ext=" + bundle.getString("ext"));
                switch (opt) {
                    case 10:
                        gotoWorkEcMsg();
                        break;
                    case 11:
                        gotoMsgList();
                        break;
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("yinxm", "MainFragment.onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("yinxm", "MainFragment.onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("yinxm", "MainFragment.onStop ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("yinxm", "MainFragment.onDestroyView ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("yinxm", "MainFragment.onDestroy ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("yinxm", "MainFragment.onDetach ");
    }

    @Override
    public void onClick(View v) {
        Log.d("yinxm", "MainFragment.onClick "+v);
        switch (v.getId()) {
            case R.id.btn1:
                //先加入返回栈
                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.beginTransaction().addToBackStack(null).commit();
                // 再切换界面显示隐藏
                gotoWorkEcMsg();
                break;
            case R.id.btn2:
                gotoMsgList();
                break;
        }

    }

    @Override
    public void gotoWorkEcMsg() {
        Log.d("yinxm", "MainFragment.gotoWorkEcMsg");
        ////   java.lang.RuntimeException: Unable to start activity ComponentInfo{cn.yinxm.test/cn.yinxm.test.ui.FragmentTestActivity}: java.lang.IllegalStateException: Fragment has not been attached yet.
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.show(leftFragment);
        fragmentTransaction.hide(test2Fragment);

        fragmentTransaction.hide(mainRightFragment);
        fragmentTransaction.show(test3Fragment);
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void gotoMsgList() {
        Log.d("yinxm", "MainFragment.gotoMsgList");
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.hide(leftFragment);
        fragmentTransaction.show(test2Fragment);

        fragmentTransaction.show(mainRightFragment);
        fragmentTransaction.hide(test3Fragment);
        fragmentTransaction.commit();

    }

    @Override
    public void gotoFriendList() {
        Log.d("yinxm", "MainFragment.gotoFriendList");

    }

    @Override
    public void gotoWorkEcFriendList() {
        Log.d("yinxm", "MainFragment.gotoWorkEcFriendList");

    }

    @Override
    public void gotoChatGroup() {
        Log.d("yinxm", "MainFragment.gotoChatGroup");

    }

    @Override
    public void gotoNewFriend() {
        Log.d("yinxm", "MainFragment.gotoNewFriend");

    }

    @Override
    public void gotoNearbyFriend() {
        Log.d("yinxm", "MainFragment.gotoNearbyFriend");

    }

}
