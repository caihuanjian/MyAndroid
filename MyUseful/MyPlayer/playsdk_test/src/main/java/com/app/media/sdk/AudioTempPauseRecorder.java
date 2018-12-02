package com.app.media.sdk;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.yinxm.playsdk.test.LogUtil;


/**
 * 由于外部原因，临时中断音频播放，管理中断事件，用于下次恢复
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/30
 */
public class AudioTempPauseRecorder {

    private int pauseId = 0;

    /**
     * 音乐临时暂停
     */
    private Map<Integer, String> musicTempPauseMap = new HashMap<>();


    /**
     * 临时中断播放
     *
     * @param pauseEventName 用于标识本次暂停事件场景唯一性，通常写为XXX.class.getCanonicalName()，
     *                       在一个场景中有多个不同事件，需要加额外的标识符例如：ChatActivity.class.getCanonicalName()+"_playIM"
     * @return
     */
    public int pauseMusic(String pauseEventName) {
        LogUtil.d("[AudioTempPauseRecorder.pauseMusic] pauseEventName="
                + pauseEventName + ", old-pauseId=" + pauseId + ", musicTempPauseMap=" + musicTempPauseMap);
        boolean isSamePauseEventBefore = clearThisEvent(pauseEventName);
        LogUtil.d("isSamePauseEventBefore=" + isSamePauseEventBefore);
        pauseId += 1;
        musicTempPauseMap.put(pauseId, pauseEventName);

        LogUtil.d("[AudioTempPauseRecorder.pauseMusic] new-pauseId="
                + pauseId + ", musicTempPauseMap=" + musicTempPauseMap);
        return pauseId;

    }

    /**
     * 恢复音乐播放
     *
     * @param pauseId 恢复id
     * @return 是否能恢复播放
     */
    public boolean resumeMusic(int pauseId) {
        LogUtil.d("[AudioTempPauseRecorder.resumeMusic] pauseId="
                + pauseId + ", musicTempPauseMap=" + musicTempPauseMap);
        boolean flag = false;
        if (musicTempPauseMap.containsKey(pauseId)) {
            LogUtil.d("找到pauseId=" + pauseId + "暂停事件");
            musicTempPauseMap.remove(pauseId);
            if (musicTempPauseMap.isEmpty()) {
                LogUtil.d("没有暂停音乐的事件了，恢复播放");
//                continueMusicLogic();
                flag = true;
            }
        } else {
            LogUtil.d("无pauseId=" + pauseId + "暂停事件");
        }
        LogUtil.d("[AudioTempPauseRecorder.resumeMusic] musicTempPauseMap=" + musicTempPauseMap + ", flag=" + flag);
        return flag;
    }

    public void clearTempPauseMusicEvent(int pauseId) {
        LogUtil.d("[AudioTempPauseRecorder.clearTempPauseMusicEvent] pauseId="
                + pauseId + ", musicTempPauseMap=" + musicTempPauseMap);
        musicTempPauseMap.remove(pauseId);
    }

    public void clearAllTempPauseMusicEvent() {
        LogUtil.d("[AudioTempPauseRecorder.clearAllTempPauseMusicEvent] pauseId="
                + pauseId + ", musicTempPauseMap=" + musicTempPauseMap);
        musicTempPauseMap.clear();
    }

    public boolean clearThisEvent(String pauseEventName) {
        LogUtil.d("[AudioTempPauseRecorder.clearThisEvent] pauseEventName=" + pauseEventName);
        boolean flag = false;
        Iterator<Map.Entry<Integer, String>> iterator = musicTempPauseMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            LogUtil.d(entry.getKey() + "=" + entry.getValue());
            if (!TextUtils.isEmpty(pauseEventName) && pauseEventName.equals(entry.getValue())) {
                LogUtil.e("[AudioTempPauseRecorder.clearThisEvent] same pauseEventName pauseId="
                        + entry.getKey());
                iterator.remove();
                flag = true;
            }
        }
        return flag;
    }

    public boolean isMusicPausedByOther() {
        LogUtil.d("[AudioTempPauseRecorder.isMusicPausedByOther] pauseId="
                + pauseId + ", musicTempPauseMap=" + musicTempPauseMap);
        boolean flag = false;
        if (!musicTempPauseMap.isEmpty()) {
            flag = true;
        }
        LogUtil.d("[AudioTempPauseRecorder.isMusicPausedByOther] flag=" + flag);
        return flag;
    }
}
