package com.app.media.bean.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

/**
 * 音乐-3、详情
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/2
 */
public class MusicBean implements Serializable {
    private static final long serialVersionUID = 7397681961724512514L;

    /**
     * id : 1ce09ac8b7e96b192d15fc6d735d8f5c
     * type : 3
     * format : 3
     * favorite : 1
     * title : 娃娃脸
     * artist : 后弦
     * url : http://zhangmenshiting.qianqian.com/data2/music/85625ca143f598654369953d3f354b32/594602929/594602929.mp3?xcode=8160653d7d027749e9c5304b21fec782
     * pic : http://qukufile2.qianqian.com/data2/pic/2c36bd7e96ab739dfeb44837a63a3205/537770125/537770125.jpg@s_1,w_150,h_150
     * genre :
     * tag :
     * language :
     * album : 来福
     * album_id : 1274237950
     * publish_time : 2017-03-06
     * publish_company : 代亚（上海）文化
     * from_site : baidu
     * duration : 192
     * score : 0
     * hot : 1840
     */

    @JSONField(name = "id")
    private String id;
    @JSONField(name = "type")
    private int type;
    @JSONField(name = "format")
    private int format;
    @JSONField(name = "favorite")
    private int favorite;
    @JSONField(name = "title")
    private String title;
    @JSONField(name = "artist")
    private String artist;
    @JSONField(name = "url")
    private String url;
    @JSONField(name = "pic")
    private String pic;
    @JSONField(name = "genre")
    private String genre;
    @JSONField(name = "tag")
    private String tag;
    @JSONField(name = "language")
    private String language;
    @JSONField(name = "album")
    private String album;
    @JSONField(name = "album_id")
    private String albumId;
    @JSONField(name = "publish_time")
    private String publishTime;
    @JSONField(name = "publish_company")
    private String publishCompany;
    @JSONField(name = "from_site")
    private String fromSite;
    @JSONField(name = "duration")
    private String duration;
    @JSONField(name = "score")
    private String score;
    @JSONField(name = "hot")
    private String hot;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFormat() {
        return format;
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishCompany() {
        return publishCompany;
    }

    public void setPublishCompany(String publishCompany) {
        this.publishCompany = publishCompany;
    }

    public String getFromSite() {
        return fromSite;
    }

    public void setFromSite(String fromSite) {
        this.fromSite = fromSite;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getHot() {
        return hot;
    }

    public void setHot(String hot) {
        this.hot = hot;
    }

    @Override
    public String toString() {
        return "\"MusicBean\": {"
                + "\"id\": \"" + id + '\"'
                + ", \"type\": \"" + type
                + ", \"format\": \"" + format
                + ", \"favorite\": \"" + favorite
                + ", \"title\": \"" + title + '\"'
                + ", \"artist\": \"" + artist + '\"'
                + ", \"url\": \"" + url + '\"'
                + ", \"pic\": \"" + pic + '\"'
                + ", \"genre\": \"" + genre + '\"'
                + ", \"tag\": \"" + tag + '\"'
                + ", \"language\": \"" + language + '\"'
                + ", \"album\": \"" + album + '\"'
                + ", \"albumId\": \"" + albumId + '\"'
                + ", \"publishTime\": \"" + publishTime + '\"'
                + ", \"publishCompany\": \"" + publishCompany + '\"'
                + ", \"fromSite\": \"" + fromSite + '\"'
                + ", \"duration\": \"" + duration + '\"'
                + ", \"score\": \"" + score + '\"'
                + ", \"hot\": \"" + hot + '\"'
                + '}';
    }


}
