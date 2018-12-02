package cn.yinxm.app.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yinxm.lib.utils.log.LogUtil;


/**
 * 功能：Fragment重要生命周期监视
 * 查看时使用BaseFragmentUtil筛选log
 *
 */
public class BaseFragmentUtil extends Fragment {
    public static final String TAG = BaseFragmentUtil.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onAttach ");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onCreate  savedInstanceState="+savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onCreateView  container="+container+"， savedInstanceState="+savedInstanceState);
        return super.onCreateView(inflater,container,savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onActivityCreated  savedInstanceState="+savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onStart ");

    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onResume ");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onPause ");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onStop ");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onDestroyView ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onDestroy ");

    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onDetach ");
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onHiddenChanged  hidden="+hidden);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.d(TAG, getClass().getSimpleName() + "——》setUserVisibleHint  isVisibleToUser="+isVisibleToUser);
    }

}
