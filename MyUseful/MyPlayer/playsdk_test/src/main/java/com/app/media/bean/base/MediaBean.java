package com.app.media.bean.base;

import android.text.TextUtils;


import java.io.Serializable;


/**
 * 播放器使用的公共Bean，每个字段必须要加注释
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/22
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public final class MediaBean implements Serializable {
    private static final long serialVersionUID = 1L;

    // ***********必须字段***********

    /**
     * 音频id，唯一标识音频资源，三级id
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String artist;

    /**
     * 播放url
     */
    private String playUrl;

    /**
     * 歌曲封面
     */
    private String cover;


    // ***********服务端业务字段***********

    /**
     * 标识资源来源类型：音乐、新闻、听书等
     */
    private int fromType = MediaFromType.MUSIC;

    /**
     * 是否喜欢
     */
    private boolean isLike;

    /**
     * 是否订阅
     */
    private boolean isSubscribe;

    /**
     * 有声集数
     */
    private int episode;


    // ************扩展字段***********/

    /**
     * 分类id or 一级id
     */
    private String categoryId;

    /**
     * 分类名称 or 一级名称
     */
    private String categoryName;
    /**
     * 专辑id or 二级id
     */
    private String albumId;

    /**
     * 专辑名称 or 二级名称
     */
    private String albumName;

    /**
     * 总时长，（单位毫秒mS）
     */
    private int duration;

    /**
     * 本地音乐播放路径
     */
    private String localUrl;

    /**
     * lrc地址
     */
    private String lrcUrl;

    /**
     * 详情更新时间，避免每次去请求详情
     */
    private long updateTime;

    /**
     * 终极扩展字段，保存json.toString，用于字段无播放器无关，但是业务流程又需要的使用场景下
     */
    private String extJsonStr;

    /**
     * 终极扩展字段，保存Object，用于字段无播放器无关，但是业务流程又需要的使用场景下
     */
    private Object extObject;


    // ************非必须字段***********/

    /**
     * 描述，最长40个汉字
     */
    private String description;

    /**
     * 缩略图
     */
    private String thumb;

    public String getId() {
        return processGenId(true);
    }

    /**
     * 是否自动生成id，平时都使用getId()
     *
     * @param isAutoGenId 是否自动生成id
     * @return id
     */
    public String processGenId(boolean isAutoGenId) {
        if (isAutoGenId && TextUtils.isEmpty(id)) {
            // TODO: 2018/9/6 是否存在id为空的情况 ——》 "md5"+md5(url)
//            if (TextUtils.isEmpty(localUrl)) {
//                id = "md5" + MD5Util.getMD5String(localUrl);
//            } else if (TextUtils.isEmpty(playUrl)) {
//                id = "md5" + MD5Util.getMD5String(playUrl);
//            } else {
                id = "time" + System.currentTimeMillis();
//            }
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if (TextUtils.isEmpty(title)) {
            title = "未知";
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        if (TextUtils.isEmpty(artist)) {
            artist = "未知";
        }
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(@MediaFromType int fromType) {
        this.fromType = fromType;
    }

    public boolean isLike() {
        return isLike;
    }

    public void setLike(boolean like) {
        isLike = like;
    }

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public String getLrcUrl() {
        return lrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        this.lrcUrl = lrcUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public int getEpisode() {
        return episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getExtJsonStr() {
        return extJsonStr;
    }

    public void setExtJsonStr(String extJsonStr) {
        this.extJsonStr = extJsonStr;
    }

    public Object getExtObject() {
        return extObject;
    }

    public void setExtObject(Object extObject) {
        this.extObject = extObject;
    }

    public void merge(MediaBean mediaBean) {
        if (TextUtils.isEmpty(id)) {
            id = mediaBean.getId();
        }
        if (TextUtils.isEmpty(title)) {
            title = mediaBean.getTitle();
        }
        if (TextUtils.isEmpty(artist)) {
            artist = mediaBean.getArtist();
        }
        if (TextUtils.isEmpty(playUrl)) {
            playUrl = mediaBean.getPlayUrl();
        }
        if (TextUtils.isEmpty(cover)) {
            cover = mediaBean.getCover();
        }
        if (fromType == 0) {
            fromType = mediaBean.getFromType();
        }
        if (!isLike) {
            isLike = mediaBean.isLike();
        }
        if (!isSubscribe) {
            isSubscribe = mediaBean.isSubscribe();
        }
        if (episode == 0) {
            episode = mediaBean.getEpisode();
        }
        if (TextUtils.isEmpty(categoryId)) {
            categoryId = mediaBean.getCategoryId();
        }
        if (TextUtils.isEmpty(categoryName)) {
            categoryName = mediaBean.getCategoryName();
        }
        if (TextUtils.isEmpty(albumId)) {
            albumId = mediaBean.getAlbumId();
        }
        if (TextUtils.isEmpty(albumName)) {
            albumName = mediaBean.getAlbumName();
        }
        if (duration == 0) {
            duration = mediaBean.getDuration();
        }
        if (TextUtils.isEmpty(localUrl)) {
            localUrl = mediaBean.getLocalUrl();
        }
        if (TextUtils.isEmpty(lrcUrl)) {
            lrcUrl = mediaBean.getLrcUrl();
        }
        if (updateTime == 0) {
            updateTime = mediaBean.getUpdateTime();
        }
        if (TextUtils.isEmpty(description)) {
            description = mediaBean.getDescription();
        }
        if (TextUtils.isEmpty(thumb)) {
            thumb = mediaBean.getThumb();
        }
        if (TextUtils.isEmpty(extJsonStr)) {
            extJsonStr = mediaBean.getExtJsonStr();
        }

        if (extObject == null) {
            extObject = mediaBean.getExtObject();
        }
    }

    @Override
    public String toString() {
        return "\"MediaBean\": {"
                + "\"id\": \"" + id + '\"'
                + ", \"title\": \"" + title + '\"'
                + ", \"artist\": \"" + artist + '\"'
                + ", \"playUrl\": \"" + playUrl + '\"'
                + ", \"cover\": \"" + cover + '\"'
                + ", \"fromType\": \"" + fromType
                + ", \"isLike\": \"" + isLike
                + ", \"isSubscribe\": \"" + isSubscribe
                + ", \"episode\": \"" + episode
                + ", \"categoryId\": \"" + categoryId + '\"'
                + ", \"categoryName\": \"" + categoryName + '\"'
                + ", \"albumId\": \"" + albumId + '\"'
                + ", \"albumName\": \"" + albumName + '\"'
                + ", \"duration\": \"" + duration
                + ", \"localUrl\": \"" + localUrl + '\"'
                + ", \"lrcUrl\": \"" + lrcUrl + '\"'
                + ", \"updateTime\": \"" + updateTime
                + ", \"description\": \"" + description + '\"'
                + ", \"thumb\": \"" + thumb + '\"'
                + '}';
    }
}
