package com.app.media.sdk.iml;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaFromType;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.bean.bo.ProgressListCache;
import com.app.media.cache.AppCacheManager;
import com.app.media.manager.PlayDataManager;
import com.app.media.sdk.MediaPlayMode;
import com.app.media.util.DataValidCheck;
import com.app.media.util.SpUtil;

import cn.yinxm.playsdk.test.LogUtil;

import static com.app.media.cache.CacheKey.CACHE_KEY_PLAY_LIST;
import static com.app.media.cache.CacheKey.CACHE_KEY_PLAY_PROGRESS_LIST;


/**
 * 播放进度恢复
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/3
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
public class PlayListRecovery {

    /**
     * 有效进度>VALID_PROGRESS_DURATION（避免频繁切歌，写入文件）
     * 有效进度 < 总长-VALID_PROGRESS_DURATION （避免快结束时，手动切歌，下次再继续播结束段）
     */
    private static final int VALID_PROGRESS_DURATION = 5000;


    /**
     * 播放进度记录最大条数
     */
    private static final int MAX_PLAY_PROGRESS_SIZE = 500;

    /**
     * 记录播放位置
     */
    public static final String FILE_NAME_PLAY_LIST_CONFIG = "play_list_config";

    /**
     * 播放item 索引
     */
    public static final String KEY_PLAY_ITEM_POS = "play_item_pos";

    /**
     * 播放列表类型 {@link MediaPlayListType}
     */
    public static final String KEY_PLAY_LIST_TYPE = "play_list_type";

    /**
     * 播放模式
     */
    public static final String KEY_PLAY_MODE = "play_mode";

    /**
     * 当前音频播放进度位置
     */
    public static final String KEY_CURRENT_PLAY_TIME = "current_play_time";

    /**
     * 播放url和进度之间的连接符
     */
    public static final String KEY_PLAY_URL_PROGRESS = "#";
    /**
     * 播放进度和最大进度连接符
     */
    public static final String KEY_CURRENT_PROGRESS_2_MAX = "_";


    private ProgressListCache<String, String> mProgressCache;
    private String mCurrentPlayId;
    private String mCurrentProgress;
    private MediaBean mMediaBean;

    private PlayListRecovery() {
        try {
            Object object = AppCacheManager.getInstance().loadCache(CACHE_KEY_PLAY_PROGRESS_LIST);
            if (object != null && object instanceof ProgressListCache) {
                mProgressCache = (ProgressListCache) object;
                LogUtil.d("disk lru=" + mProgressCache.size());
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
        if (mProgressCache == null) {
            mProgressCache = new ProgressListCache(MAX_PLAY_PROGRESS_SIZE);
        }
        LogUtil.d("ProgressLruCache size=" + mProgressCache.size());
    }

    public static PlayListRecovery getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static PlayListRecovery INSTANCE = new PlayListRecovery();
    }

    // TODO: 2018/9/23 性能低效，换成mmkv
    public void saveProgress(SharedPreferences sp, String id, long duration, long progress) {
        if (TextUtils.isEmpty(id)) {
            LogUtil.e("saveProgress id is null");
            return;
        }

        if (progress < VALID_PROGRESS_DURATION) {
//            LogUtil.e("saveProgress progress is too short "+progress+", "+duration);
            return;
        }
        if (!id.equals(mCurrentPlayId)) {
            return;
        }
        String record = id + KEY_PLAY_URL_PROGRESS + progress + KEY_CURRENT_PROGRESS_2_MAX + duration;
        mCurrentProgress = record;
        // id#99806_187088
        SpUtil.putString(sp, KEY_CURRENT_PLAY_TIME, record);
//        LogUtil.d("progress=" + record);

    }

    public void savePlayList(Context context, MediaPlayListType playListType, final MediaList listBean) {
        if (listBean != null) {
            AppCacheManager.getInstance().saveCache(CACHE_KEY_PLAY_LIST, listBean);

            if (playListType != null) {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PLAY_LIST_CONFIG, Context.MODE_PRIVATE);
                sp.edit().putInt(KEY_PLAY_LIST_TYPE, playListType.getIndex()).commit();
            }
        }
    }

    public void savePlayItem(Context context, int position, MediaBean bean) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PLAY_LIST_CONFIG, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_PLAY_ITEM_POS, position).commit();
        LogUtil.d("newPlayItem bean=" + bean);

        // 记录上一次的最后断点位置： 有声的情况下，才记录播放断点
        if (mMediaBean != null && mCurrentPlayId != null
                && mCurrentPlayId.equals(mMediaBean.getId())
                && !TextUtils.isEmpty(mCurrentProgress)
                && isBeanCanSaveProgress(mMediaBean)) {
            LogUtil.d("old PlayItem bean progress save " + mCurrentPlayId + "， progress=" + mCurrentProgress);
            mProgressCache.put(mCurrentPlayId, mCurrentProgress);
            AppCacheManager.getInstance().saveCache(CACHE_KEY_PLAY_PROGRESS_LIST, mProgressCache);
//           Object obj = AppCacheManager.getInstance().loadCache(CACHE_KEY_PLAY_PROGRESS_LIST);
//           LogUtil.d("getCache="+obj);
        }

        mMediaBean = bean;
        if (mMediaBean != null) {
            mCurrentPlayId = mMediaBean.getId();
        } else {
            mCurrentPlayId = null;
        }
    }

    public void savePlayMode(Context context, MediaPlayMode playMode) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PLAY_LIST_CONFIG, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_PLAY_MODE, playMode.getCode()).commit();
    }

    /**
     * 恢复播放列表
     *
     * @param context
     */
    public void recoverPlayList(Context context) {
        LogUtil.e("recoverPlayList 播放数据恢复");

        // 恢复播放列表
        Object obj = AppCacheManager.getInstance().loadCache(CACHE_KEY_PLAY_LIST);
        MediaList mediaList = null;
        if (obj != null && obj instanceof MediaList) {
            mediaList = (MediaList) obj;
        }

        if (mediaList == null
                || mediaList.getList() == null
                || mediaList.getList().size() <= 0) {
            return;
        }

        LogUtil.d("recover playList=" + mediaList.getList().size());

        // 播放类型
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PLAY_LIST_CONFIG, Context.MODE_PRIVATE);
        int index = sp.getInt(KEY_PLAY_LIST_TYPE, 0);
        MediaPlayListType playListType = MediaPlayListType.fromIndex(index);

        int code = sp.getInt(KEY_PLAY_MODE, 0);
        MediaPlayMode mediaPlayMode = MediaPlayMode.fromCode(code);

        // 恢复播放item
        int position = sp.getInt(KEY_PLAY_ITEM_POS, -1);
        LogUtil.d("p=" + position);
        if (DataValidCheck.isInValidPosition(position, mediaList.getList().size())) {
            LogUtil.e("position " + position + " is out of range " + mediaList.getList().size());
            return;
        }

        LogUtil.d("recoverPlayList do mode=" + mediaPlayMode + ", type=" + playListType + ", pos=" + position + ", " + mediaList);
        // 启动恢复播放数据
        PlayDataManager.getInstance().setPlayList(mediaList);
        PlayDataManager.getInstance().setPlayListType(playListType);
        PlayDataManager.getInstance().setPlayMode(mediaPlayMode);
        PlayDataManager.getInstance().setPlayListPosition(position);
    }

    /**
     * 获取播放url的播放断点
     *
     * @param context
     * @param id      歌曲唯一标识
     * @return
     */
    public long getPlayHistoryProgress(Context context, String id) {
        long progress = 0;
        if (TextUtils.isEmpty(id)) {
            return progress;
        }
        try {
            String record = mProgressCache.get(id);
            LogUtil.d("ram record=" + record);
            if (TextUtils.isEmpty(record)) {
                SharedPreferences sp = context.getSharedPreferences(FILE_NAME_PLAY_LIST_CONFIG, Context.MODE_PRIVATE);
                record = sp.getString(KEY_CURRENT_PLAY_TIME, null);
                LogUtil.d("disk record=" + record);

            }
            if (TextUtils.isEmpty(record)) {
                LogUtil.e("progressStr=" + record);
                return progress;
            }

            int lastIndex = record.lastIndexOf(KEY_PLAY_URL_PROGRESS);
            if (lastIndex >= 0 && lastIndex < record.length() - 1) {
                String tempId = record.substring(0, lastIndex);
                if (id.equals(tempId)) {
                    // 同一个节目
                    String tempRecord = record.substring(lastIndex + 1, record.length());
                    String[] temp = tempRecord.split(KEY_CURRENT_PROGRESS_2_MAX);
                    if (temp != null && temp.length == 2) {
                        long tempPg = Long.parseLong(temp[0]);
                        long maxProgress = Long.parseLong(temp[1]);
                        LogUtil.d("tempId=" + tempId + ", tempProgress=" + tempPg + ", maxProgress=" + maxProgress);
                        if (tempPg > 0
                                && ((maxProgress - tempPg) > VALID_PROGRESS_DURATION)) {
                            // 只有还剩下大于5s的才能断点播放
                            progress = tempPg;
                            LogUtil.e("断点播放=" + progress);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }

        LogUtil.e("getPlayHistoryProgress progress=" + progress);
        return progress;
    }

    /**
     * 播放项是否能保存进度
     *
     * @param mediaBean
     * @return
     */
    public boolean isBeanCanSaveProgress(MediaBean mediaBean) {
        // 目前只记录有声
        return mediaBean != null
                && MediaFromType.AUDIO == mediaBean.getFromType();
    }
}