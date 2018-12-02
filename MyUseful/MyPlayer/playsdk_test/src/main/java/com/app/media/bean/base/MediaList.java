package com.app.media.bean.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 播放列表公共数据
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/31
 */
@SuppressWarnings("unused")
public final class MediaList implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 播放列表
     */
    private List<MediaBean> mList = new ArrayList<>();

    /**
     * 分类id or 一级id
     * 服务器返回的一级分类id 或 本地自定义id {@link CategroyId}
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
     * 列表数据来源，用于区分当前列表类型，后续可能有混合类型数据加入
     */
    private MediaPlayListType playListType = MediaPlayListType.DEFAULT_UNKNOWN;

    // *************播放时需要分页加载的数据信息，例如：新闻**************/
    /**
     * 数据总量PageInfo
     */
    private int total;
    /**
     * 分页大小
     */
    private int perPageSize;
    /**
     * 当前页，建议都从1开始
     */
    private int thisPage;


    public List<MediaBean> getList() {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        return mList;
    }

    public void setList(List<MediaBean> list) {
        if (list == null) {
            mList.clear();
        } else {
            mList.addAll(list);
        }
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

    // TODO: 2018/9/26 id为空，确认播放项目
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


    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPerPageSize() {
        return perPageSize;
    }

    public void setPerPageSize(int perPageSize) {
        this.perPageSize = perPageSize;
    }

    public int getThisPage() {
        return thisPage;
    }

    public void setThisPage(int thisPage) {
        this.thisPage = thisPage;
    }

    public MediaPlayListType getPlayListType() {
        return playListType;
    }

    public void setPlayListType(MediaPlayListType playListType) {
        this.playListType = playListType;
    }
}
