package com.app.media.player.constant;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 常用多媒体编码格式
 * <p>
 * Created by yinxuming on 2018/8/7.
 */
@Retention(RetentionPolicy.SOURCE)
@StringDef({MediaFormat.MP3, MediaFormat.MP4, MediaFormat.M4A, MediaFormat.WMA, MediaFormat.FLAC, MediaFormat.WAV, MediaFormat.PCM})
public @interface MediaFormat {
    String MP3 = "mp3";
    String MP4 = "mp4";

    String M4A = "m4a";
    String WMA = "wma";
    String FLAC = "flac";

    String WAV = "wav";
    String PCM = "pcm";
}
