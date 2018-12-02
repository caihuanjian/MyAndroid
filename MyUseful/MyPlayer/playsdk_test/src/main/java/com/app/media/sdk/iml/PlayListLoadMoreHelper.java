package com.app.media.sdk.iml;


import com.app.media.bean.base.MediaList;

/**
 * 快要播放完毕该播放列表，主动加载更多分页数据，触发条件：播放到size-1项（倒数第1项）
 * 1、需要加载
 * 新闻
 * 有声
 * 2、不需要加载
 * 音乐：无分页
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/8
 */
// TODO: 2018/9/8
public class PlayListLoadMoreHelper {

    public static MediaList loadMoreNews(MediaList mediaList) {
        return mediaList;
    }

    public static MediaList loadMoreAudio(MediaList mediaList) {
        return mediaList;
    }


}
