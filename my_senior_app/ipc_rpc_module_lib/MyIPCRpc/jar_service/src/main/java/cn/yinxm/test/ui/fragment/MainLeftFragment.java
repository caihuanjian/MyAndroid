package cn.yinxm.test.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yinxm.test.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainLeftFragment extends Fragment implements View.OnClickListener{
    OnMainLeftFragmentClick mainLeftFragmentClick;

    public MainLeftFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_left, container, false);
//        view.setClickable(true);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public void onClick(View v) {
        Log.d("yinxm", "MainLeftFragment onClick "+v);
        if (mainLeftFragmentClick != null) {
            mainLeftFragmentClick.onMainLeftFragmentClick();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("yinxm", "MainLeftFragment onHiddenChanged="+hidden);
    }

    public interface OnMainLeftFragmentClick {
        void onMainLeftFragmentClick();
    }
}
