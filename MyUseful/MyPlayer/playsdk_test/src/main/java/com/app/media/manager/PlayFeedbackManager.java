package com.app.media.manager;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaBeanUtil;
import com.app.media.bean.base.MediaFromType;
import com.app.media.bean.base.MediaPlayListType;

import cn.yinxm.playsdk.test.LogUtil;


/**
 * 播放统计
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/31
 */
// TODO: 2018/8/31  
public class PlayFeedbackManager {

    public static final int TYPE_START = 1;
    public static final int TYPE_FINISH = 2;
    public static final int TYPE_PREV = 3;
    public static final int TYPE_NEXT = 4;


    private PlayFeedbackManager() {
    }

    public static PlayFeedbackManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static PlayFeedbackManager INSTANCE = new PlayFeedbackManager();
    }

    /**
     * 统计反馈
     *
     * @param feedbackType
     */
    public void sendFeedback(int feedbackType, MediaBean mediaBean, long progress, long duration) {
        try {

            if (mediaBean == null) {
                return;
            }
            // mediaBean.getDuration()
            long currentDuration = progress;
            if (feedbackType == TYPE_FINISH) {
                currentDuration = duration;
            }

        } catch (Exception e) {
            LogUtil.e(e);
        }
    }

    /**
     * 统计有声听过的资源，下一次服务器下发LastEpisod
     **/
    public void sendAudioFinishFeedback(MediaPlayListType mediaPlayListType, MediaBean mediaBean) {
        try {
            if (mediaBean == null || MediaFromType.AUDIO != mediaBean.getFromType()) {
                return;
            }

            String messageId = null;
            Object obj = MediaBeanUtil.getExtJsonFromKey(mediaBean, MediaBeanUtil.KEY_MESSAGE_ID);
            if (obj != null && obj instanceof String) {
                messageId = (String) obj;
            }
            LogUtil.d("senAudioFinishFeedback messageId=" + messageId);
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
