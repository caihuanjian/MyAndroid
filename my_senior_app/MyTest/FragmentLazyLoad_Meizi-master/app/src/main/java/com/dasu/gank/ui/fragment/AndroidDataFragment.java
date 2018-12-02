package com.dasu.gank.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dasu.gank.AppConstant;
import com.dasu.gank.GankApplication;
import com.dasu.gank.R;
import com.dasu.gank.mode.entity.Data;
import com.dasu.gank.ui.adapter.AndroidDataAdapter;
import com.dasu.gank.ui.base.OnItemClickListener;
import com.dasu.gank.utils.ListUtils;

/**
 * Created by dasu on 2016/9/26.
 * https://github.com/woshidasusu/Meizi
 */
public class AndroidDataFragment extends GankDataFragment {

    private static final String TAG = AndroidDataFragment.class.getSimpleName();

    private AndroidDataAdapter mDataAdapter;
    private Context mContext;

    RecyclerView mAndroidDataView;

    public AndroidDataFragment(String type) {
        mType = type;
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public String getTAG() {
        return TAG;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getLocalLatestIssue() {
        return GankApplication.getConfigSP().getString(AppConstant.GANK_ANDROID_LATEST_UPDATE_TIME);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_android, container, false);
            findView(rootView);
            bindWidgets();
        }
        return rootView;
    }

    @Override
    protected void onFragmentVisibleChange(boolean isVisible) {
        super.onFragmentVisibleChange(isVisible);
        if (isVisible) {
            if (ListUtils.isEmpty(mDataList) && !isLoadingData()) {
                setRefresh(true);
                if (isNeedLoadServerData()) {
                    loadServiceData(false);
                } else {
                    loadServiceData(false);
                }
            }
        }
    }

    private void findView(View rootView) {
        mAndroidDataView = (RecyclerView) rootView.findViewById(R.id.rv_android);
    }

    private void bindWidgets() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        mAndroidDataView.setLayoutManager(manager);
        mDataAdapter = new AndroidDataAdapter(getActivity(), mDataList);
        mAndroidDataView.setAdapter(mDataAdapter);

        mDataAdapter.setOnItemClickListener(getOnItemClick());
    }

    @Override
    protected void onLoadServiceDataSuccess() {
        super.onLoadServiceDataSuccess();
        mDataAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onLoadServiceDataFailure() {

    }

    public OnItemClickListener getOnItemClick() {
        return new OnItemClickListener() {
            @Override
            public void onItemClick(View view, View picture, View text, Data data) {
                Snackbar.make(view, data.getWho(), Snackbar.LENGTH_SHORT).show();
            }
        };
    }
}
