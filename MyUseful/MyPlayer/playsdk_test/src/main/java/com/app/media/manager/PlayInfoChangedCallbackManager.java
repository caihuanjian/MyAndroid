package com.app.media.manager;

import android.os.Handler;
import android.os.Looper;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.sdk.IPlayCallback;
import com.app.media.sdk.MediaPlayMode;
import com.app.media.sdk.MediaPlayState;
import com.app.media.sdk.OnPlayInfoCallback;
import com.app.media.sdk.OnProgressCallback;
import com.app.media.sdk.iml.ServiceOnPlayChangedCallback;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


/**
 * 播放状态改变回调
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public class PlayInfoChangedCallbackManager implements IPlayCallback {

    private Handler mHandler = new Handler(Looper.getMainLooper());

    // TODO: 2018/8/28 待优化
    private List<WeakReference<OnPlayInfoCallback>> playInfoCallbackList = new CopyOnWriteArrayList<>();

    private List<WeakReference<OnProgressCallback>> progressCallbackList = new CopyOnWriteArrayList<>();

    /**
     * 非UI层回调，不要使用弱引用
     */
    private ServiceOnPlayChangedCallback mServicePlayChangedCallback;


    private PlayInfoChangedCallbackManager() {
    }

    public static PlayInfoChangedCallbackManager getInstance() {
        return PlayInfoChangedCallbackManager.SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static PlayInfoChangedCallbackManager INSTANCE = new PlayInfoChangedCallbackManager();
    }

    @Override
    public void registerPlayInfoCallback(OnPlayInfoCallback playInfoCallback) {
        if (playInfoCallback != null) {
            playInfoCallbackList.add(new WeakReference<OnPlayInfoCallback>(playInfoCallback));
        }
    }

    @Override
    public void unregisterPlayInfoCallback(OnPlayInfoCallback playInfoCallback) {
        if (playInfoCallback != null) {
            Iterator<WeakReference<OnPlayInfoCallback>> iterator = playInfoCallbackList.iterator();
            while (iterator.hasNext()) {
                WeakReference<OnPlayInfoCallback> weakReference = iterator.next();
                if (weakReference != null && weakReference.get() != null) {
                    OnPlayInfoCallback callback = weakReference.get();
                    if (playInfoCallback == callback) {
                        playInfoCallbackList.remove(weakReference);
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public void registerProgressCallback(OnProgressCallback progressCallback) {
        if (progressCallback != null) {
            progressCallbackList.add(new WeakReference<OnProgressCallback>(progressCallback));
        }
    }

    @Override
    public void unregisterProgressCallback(OnProgressCallback progressCallback) {
        if (progressCallback != null) {
            Iterator<WeakReference<OnProgressCallback>> iterator = progressCallbackList.iterator();
            while (iterator.hasNext()) {
                WeakReference<OnProgressCallback> weakReference = iterator.next();
                if (weakReference != null && weakReference.get() != null) {
                    OnProgressCallback callback = weakReference.get();
                    if (progressCallback == callback) {
                        progressCallbackList.remove(weakReference);
                        break;
                    }
                } else {
                    iterator.remove();
                }
            }
        }
    }

    public void notifyPlayStateChanged(@MediaPlayState final int state, final boolean isPlaying) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mServicePlayChangedCallback != null) {
                    mServicePlayChangedCallback.onPlayStateChanged(state, isPlaying);
                }

                Iterator<WeakReference<OnPlayInfoCallback>> iterator = playInfoCallbackList.iterator();
                while (iterator != null && iterator.hasNext()) {
                    WeakReference<OnPlayInfoCallback> weakReference = iterator.next();
                    if (weakReference != null && weakReference.get() != null) {
                        OnPlayInfoCallback listener = weakReference.get();
                        listener.onPlayStateChanged(state, isPlaying);
                    } else {
                        iterator.remove();
                    }
                }
            }
        });

    }

    public void notifyPlayBeanChange(final int position, final MediaBean bean) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mServicePlayChangedCallback != null) {
                    mServicePlayChangedCallback.onPlayBeanChanged(position, bean);
                }

                Iterator<WeakReference<OnPlayInfoCallback>> iterator = playInfoCallbackList.iterator();
                while (iterator != null && iterator.hasNext()) {
                    WeakReference<OnPlayInfoCallback> weakReference = iterator.next();
                    if (weakReference != null && weakReference.get() != null) {
                        OnPlayInfoCallback listener = weakReference.get();
                        listener.onPlayBeanChanged(position, bean);
                    } else {
                        iterator.remove();
                    }
                }
            }
        });

    }

    public void notifyPlayListChanged(final MediaPlayListType playListType, final MediaList list) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mServicePlayChangedCallback != null) {
                    mServicePlayChangedCallback.onPlayListChanged(playListType, list);
                }

                Iterator<WeakReference<OnPlayInfoCallback>> iterator = playInfoCallbackList.iterator();
                while (iterator != null && iterator.hasNext()) {
                    WeakReference<OnPlayInfoCallback> weakReference = iterator.next();
                    if (weakReference != null && weakReference.get() != null) {
                        OnPlayInfoCallback listener = weakReference.get();
                        listener.onPlayListChanged(playListType, list);
                    } else {
                        iterator.remove();
                    }
                }
            }
        });
    }

    public void notifyPlayModeChanged(final MediaPlayMode mode) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mServicePlayChangedCallback != null) {
                    mServicePlayChangedCallback.onPlayModeChanged(mode);
                }

                Iterator<WeakReference<OnPlayInfoCallback>> iterator = playInfoCallbackList.iterator();
                while (iterator != null && iterator.hasNext()) {
                    WeakReference<OnPlayInfoCallback> weakReference = iterator.next();
                    if (weakReference != null && weakReference.get() != null) {
                        OnPlayInfoCallback listener = weakReference.get();
                        listener.onPlayModeChanged(mode);
                    } else {
                        iterator.remove();
                    }
                }
            }
        });
    }

    public void notifyProgressChanged(final long duration, final long currentPlayPosition, final long bufferedPosition) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mServicePlayChangedCallback != null) {
                    mServicePlayChangedCallback.onProgressChanged(duration, currentPlayPosition, bufferedPosition);
                }

                Iterator<WeakReference<OnProgressCallback>> iterator = progressCallbackList.iterator();
                while (iterator != null && iterator.hasNext()) {
                    WeakReference<OnProgressCallback> weakReference = iterator.next();
                    if (weakReference != null && weakReference.get() != null) {
                        OnProgressCallback listener = weakReference.get();
                        listener.onProgressChanged(duration, currentPlayPosition, bufferedPosition);
                    } else {
                        iterator.remove();
                    }
                }
            }
        });
    }

    public ServiceOnPlayChangedCallback getServicePlayChangedCallback() {
        return mServicePlayChangedCallback;
    }

    public void setServicePlayChangedCallback(ServiceOnPlayChangedCallback servicePlayChangedCallback) {
        mServicePlayChangedCallback = servicePlayChangedCallback;
    }
}
