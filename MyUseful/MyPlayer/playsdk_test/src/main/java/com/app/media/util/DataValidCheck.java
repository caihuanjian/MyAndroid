package com.app.media.util;

/**
 * Created by yinxm on 2018/9/5.
 */
public class DataValidCheck {
    public static boolean isValidPosition(int pos, int playListSize) {
        return pos >= 0 && pos < playListSize;
    }

    public static boolean isInValidPosition(int pos, int playListSize) {
        return !isValidPosition(pos, playListSize);
    }
}
