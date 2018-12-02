package com.app.media.sdk.iml;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaFromType;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.manager.UiCallBackManager;
import com.app.media.sdk.TaskExecutor;

import cn.yinxm.playsdk.test.LogUtil;


/**
 * 获取播放详情帮助类
 * <p>
 * 1、请求详情：新闻，音乐，有声（非手动搜索&语音）
 * <p>
 * 2、不请求详情：有声（手动搜索/语音，无albumId）
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/8
 */
@SuppressLint("unused")
public class PlayDetailHelper {
    /**
     * 重新请求详情时间间隔
     */
    private static final int RE_GET_DETAIL_TIME = 3600000;


    /**
     * 请求节目详情
     *
     * @param mediaBean
     * @return
     */
    public static boolean updateMediaDetail(MediaBean mediaBean, MediaPlayListType mediaPlayListType,
                                            boolean isShowLoading) {
        // 距离上一次请求详情事件太短，不请求
        if (!canReqDetail(mediaBean)) {
            LogUtil.d("reuse detail " + mediaBean);
            return false;
        }
        if (MediaFromType.MUSIC == mediaBean.getFromType()) {
            showLoading(isShowLoading);
            return updateMusicDetail(mediaBean);
        } else if (MediaFromType.AUDIO == mediaBean.getFromType()) {
            // 搜索的有声，不能请求详情
            if (MediaPlayListType.SEARCH_AUDIO == mediaPlayListType
                    || MediaPlayListType.VOICE == mediaPlayListType) {
                return false;
            } else {
                showLoading(isShowLoading);
                return updateAudioDetail(mediaBean);
            }
        } else if (MediaFromType.NEWS == mediaBean.getFromType()) {
            showLoading(isShowLoading);
            return updateNewsDetail(mediaBean);
        }
        return false;
    }

    public static void showLoading(boolean isShowLoading) {
        if (isShowLoading) {
            TaskExecutor.ui(new Runnable() {
                @Override
                public void run() {
                    UiCallBackManager.getInstance().getUiCallBackDialog().show();
                }
            });
        }
    }


    /**
     * 请求音乐详情
     *
     * @param mediaBean
     * @return
     */
    public static boolean updateMusicDetail(MediaBean mediaBean) {
//        String id = mediaBean.getId();
//        Result<MusicBean> result = new MusicRepository().loadMusicDetailData(id);
//        if (result.isSucceed()) {
//            MediaBean bean = MusicBeanMapper.transform(result.getData());
//            mediaBean.merge(bean);
//            return true;
//        }
        return false;
    }

    public static boolean updateAudioDetail(MediaBean mediaBean) {
//        String id = mediaBean.getId();
//        String albumId = mediaBean.getAlbumId();
//        String episode = String.valueOf(mediaBean.getEpisode());
//        Result<AudioProgramDetail> result = new AudioRepository().loadAudioProgramDetailData(id, albumId, episode);
//        if (result.isSucceed()) {
//            AudioProgramDetail programDetail = result.getData();
//            MediaBean bean = AudioBeanMapper.transform(programDetail);
//            mediaBean.merge(bean);
//            return true;
//        }
        return false;
    }

    public static boolean updateNewsDetail(MediaBean mediaBean) {
//        String id = mediaBean.getId();
//        Result<NewsDetailBean> result = new NewsRepository().loadNewsItemData(id);
//        if (result.isSucceed()) {
//            NewsDetailBean data = result.getData();
//            MediaBean bean = NewsBeanMapper.transform(data);
//            mediaBean.merge(bean);
//            return true;
//        }
        return false;
    }

    private static boolean canReqDetail(MediaBean mediaBean) {
        return TextUtils.isEmpty(mediaBean.getPlayUrl())
                || (System.currentTimeMillis() - mediaBean.getUpdateTime() > RE_GET_DETAIL_TIME);
    }
}
