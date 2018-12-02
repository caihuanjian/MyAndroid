package com.app.media.sdk;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/29
 */
public interface IPlayCallback {
    /**
     * 注册播放信息改变监听
     *
     * @param callback
     */
    void registerPlayInfoCallback(OnPlayInfoCallback callback);

    void unregisterPlayInfoCallback(OnPlayInfoCallback callback);

    /**
     * 注册播放进度改变监听
     *
     * @param callback
     */
    void registerProgressCallback(OnProgressCallback callback);

    void unregisterProgressCallback(OnProgressCallback callback);
}
