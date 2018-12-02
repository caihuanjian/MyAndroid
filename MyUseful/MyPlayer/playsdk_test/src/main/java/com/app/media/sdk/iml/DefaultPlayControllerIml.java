package com.app.media.sdk.iml;


import com.app.media.sdk.IPlayController;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/2
 */
public class DefaultPlayControllerIml implements IPlayController {
    @Override
    public void startPlay() {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public int pauseByEvent(String eventName) {
        return 0;
    }

    @Override
    public boolean resumeByEvent(int pauseId) {
        return false;
    }

    @Override
    public void playNext() {

    }

    @Override
    public void playPrevious() {

    }

    @Override
    public void playPosition(int position) {

    }

    @Override
    public void seekTo(long positionMs) {

    }

    @Override
    public void stop() {

    }

    @Override
    public void exit() {

    }
}
