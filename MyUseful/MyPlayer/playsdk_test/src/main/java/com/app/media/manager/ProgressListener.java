package com.app.media.manager;

/**
 * Created by huangmei on 2018/8/7.
 */

public interface ProgressListener {

    void onProgressChange(long curProgress, long maxProgress);
}
