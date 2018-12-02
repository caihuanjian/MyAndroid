package com.app.media.sdk;

/**
 * 播放模式
 */
public enum MediaPlayMode {

    /**
     * 顺序播放
     */
    MODE_LIST_LOOP(0),

    /**
     * 随机播放
     */
    MODE_SINGLE(1),

    /**
     * 单曲循环
     */
    MODE_RANDOM(2);

    private int code;

    MediaPlayMode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static MediaPlayMode nextPlayMode(MediaPlayMode playMode) {
        MediaPlayMode[] playModes = MediaPlayMode.values();

        if (playMode == null) {
            return playModes[0];
        }
        return playModes[(playMode.ordinal() + 1) % playModes.length];
    }

    public static MediaPlayMode fromCode(int code) {
        MediaPlayMode[] playModes = MediaPlayMode.values();
        for (MediaPlayMode playMode : playModes) {
            if (playMode.getCode() == code) {
                return playMode;
            }
        }
        return MODE_LIST_LOOP;
    }
}
