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
public class MainRightFragment extends Fragment  implements View.OnClickListener  {


    public MainRightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_right, container, false);
//        view.setClickable(true);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Log.d("yinxm", "MainRightFragment onClick "+v);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.d("yinxm", "MainRightFragment onHiddenChanged="+hidden);
    }
}
