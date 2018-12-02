// (c)2016 Flipboard Inc, All Rights Reserved.

package com.rengwuxian.rxjavasamples.module.elementary_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.rengwuxian.rxjavasamples.BaseFragment;
import com.rengwuxian.rxjavasamples.R;
import com.rengwuxian.rxjavasamples.adapter.ZhuangbiListAdapter;
import com.rengwuxian.rxjavasamples.model.ZhuangbiImage;
import com.rengwuxian.rxjavasamples.network.Network;
import com.rengwuxian.rxjavasamples.util.LogUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ElementaryFragment extends BaseFragment {
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.gridRv) RecyclerView gridRv;
    @BindView(R.id.btn_test)
    Button btnTest1;

    ZhuangbiListAdapter adapter = new ZhuangbiListAdapter();

    @OnCheckedChanged({R.id.searchRb1, R.id.searchRb2, R.id.searchRb3, R.id.searchRb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
            unsubscribe();
            adapter.setImages(null);
            swipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    private void search(String key) {
        disposable = Network.getZhuangbiApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<ZhuangbiImage>>() {
                    @Override
                    public void accept(@NonNull List<ZhuangbiImage> images) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        adapter.setImages(images);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        ButterKnife.bind(this, view);

        gridRv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        gridRv.setAdapter(adapter);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);

        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_elementary;
    }

    @OnClick(R.id.btn_test)
    public void test1() {
        LogUtil.d("test1 start");

        //1、创建被观察者Observable
        Observable observable = Observable.create(new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                LogUtil.d("subscribe \t"+e);
                e.onNext("hello");
                e.onNext(" this is");
                e.onNext(new Exception("asdf"));//走了Error就不会走onComplete
                e.onComplete();
            }
        });

        //2、创建观察者Observer
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.d("onSubscribe \t"+d);
            }

            @Override
            public void onNext(String s) {
                LogUtil.d("onNext \t"+s);

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("onError \t"+e);

            }

            @Override
            public void onComplete() {
                LogUtil.d("onComplete \t");

            }
        };

        //3、被观察者者 订阅 观察者
        observable.subscribe(observer);

        LogUtil.d("test1");

//        第二种写法
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                LogUtil.d("subscribe2 \t"+e);
                e.onNext("test2");

            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                LogUtil.d("onSubscribe2 \t"+d);

            }

            @Override
            public void onNext(String s) {
                LogUtil.d("onNext \t"+s);

            }

            @Override
            public void onError(Throwable e) {
                LogUtil.d("onError \t"+e);

            }

            @Override
            public void onComplete() {
                LogUtil.d("onComplete \t");

            }
        });
    }

    @OnClick(R.id.btn_test2)
    protected void threadTest() {

//        //1.0用法
//        Observable.just(1, 2, 3)
//                .subscribe(new Subscriber<Integer>() {
//                    @Override
//                    public void onNext(Integer item) {
//                        System.out.println("Next: " + item);
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//                        System.err.println("Error: " + error.getMessage());
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("Sequence complete.");
//                    }
//                });

        //2.0用法
        Observable.just(1, 2, 3, 4)
                .subscribeOn(Schedulers.io())   // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {//每隔500ms回调一次
                        LogUtil.d("accept: " + integer);
                        Thread.sleep(500);
                    }
                });

    }
}
