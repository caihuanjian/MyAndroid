package com.app.media.sdk;

/**
 * 播放进度回调
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public interface OnProgressCallback {
    /**
     * 播放进度改变
     *
     * @param duration            总长度 ms
     * @param currentPlayPosition 当前播放长度 ms
     * @param bufferedPosition    当前缓冲长度 ms
     */
    void onProgressChanged(long duration, long currentPlayPosition, long bufferedPosition);
}
