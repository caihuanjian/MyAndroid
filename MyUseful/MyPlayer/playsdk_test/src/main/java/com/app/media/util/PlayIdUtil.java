package com.app.media.util;


import com.app.media.manager.PlayDataManager;
import com.app.media.player.IPlayHelper;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/19
 */
public class PlayIdUtil {

    /**
     * 获取正在播放的歌曲的id，唯一标识
     *
     * @param playHelper
     * @return
     */
    public static String getPlayingId(IPlayHelper playHelper) {
        String id = null;
        final String playUrl = playHelper.getPlayUrl();
        if (PlayDataManager.getInstance().getPlayMediaBean() != null
                && playUrl != null
                && playUrl.equals(PlayDataManager.getInstance().getPlayMediaBean().getPlayUrl())) {
            // 确保播放和记录的是同一首歌
            id = PlayDataManager.getInstance().getPlayMediaBean().getId();
        }

        return id;
    }
}
