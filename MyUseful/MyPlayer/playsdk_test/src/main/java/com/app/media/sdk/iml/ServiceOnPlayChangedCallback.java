package com.app.media.sdk.iml;

import android.content.Context;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.manager.PlayDataManager;
import com.app.media.sdk.IPlayService;
import com.app.media.sdk.MediaPlayMode;
import com.app.media.sdk.OnPlayInfoCallback;
import com.app.media.sdk.OnProgressCallback;
import com.app.media.sdk.TaskExecutor;


/**
 * 播放信息改变默认实现类
 * 用于：非UI层回调
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public class ServiceOnPlayChangedCallback implements OnProgressCallback, OnPlayInfoCallback {
    private Context mContext;
    private IPlayService mPlayService;
    private ProgressUpdateControl mProgressUpdateControl;


    public ServiceOnPlayChangedCallback(Context context, IPlayService playService) {
        mContext = context.getApplicationContext();
        mPlayService = playService;
        mProgressUpdateControl = new ProgressUpdateControl(mContext);
    }

    @Override
    public void onPlayStateChanged(int state, boolean isPlaying) {

        // 触发回调播放进度，并记忆播放进度
        if (mPlayService != null && mPlayService.getPlayer() != null
                && PlayDataManager.getInstance().isNormalMedia()) {
            mProgressUpdateControl.updateProgress(isPlaying, mPlayService.getPlayer());
        }

        BroadcastNotifyUtil.notifyPlayState(mContext, state, isPlaying);
    }

    @Override
    public void onPlayBeanChanged(final int position, final MediaBean bean) {

        // 触发记录当前播放项目
        TaskExecutor.io(new Runnable() {
            @Override
            public void run() {

                PlayListRecovery.getInstance().savePlayItem(mContext, position, bean);
            }
        });

        BroadcastNotifyUtil.notifyPlayInfo(mContext, bean,
                mPlayService.getPlayer().getDuration(),
                mPlayService.getPlayer().getCurrentPosition(),
                mPlayService.isPlaying());

    }

    @Override
    public void onPlayListChanged(final MediaPlayListType playListType, final MediaList listBean) {

        // 触发记录播放列表
        TaskExecutor.io(new Runnable() {
            @Override
            public void run() {
                PlayListRecovery.getInstance().savePlayList(mContext, playListType, listBean);
            }
        });
    }

    @Override
    public void onPlayModeChanged(final MediaPlayMode mode) {

        TaskExecutor.io(new Runnable() {
            @Override
            public void run() {
                PlayListRecovery.getInstance().savePlayMode(mContext, mode);
            }
        });

    }

    @Override
    public void onProgressChanged(long duration, long currentPlayPosition, long bufferedPosition) {

        // TODO: 2018/9/21 test 是否必要 
        BroadcastNotifyUtil.notifyPlayInfo(mContext,
                mPlayService.getCurrentPlayInfo().getPlayMediaBean(),
                duration, currentPlayPosition,
                mPlayService.isPlaying());
    }
}
