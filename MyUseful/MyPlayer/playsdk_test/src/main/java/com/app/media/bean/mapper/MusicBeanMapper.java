package com.app.media.bean.mapper;


import com.app.media.bean.base.MediaBean;
import com.app.media.bean.dto.MusicBean;

import cn.yinxm.playsdk.test.LogUtil;

/**
 * 音乐bean转换
 * 服务端数据变更后，只需要改变对应转换逻辑
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/2
 */
public class MusicBeanMapper {

    public static MediaBean transform(MusicBean bean) {
        if (bean == null) {
            return null;
        }
        MediaBean mediaBean = new MediaBean();

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

        // 必须数据
        mediaBean.setId(bean.getId());
        mediaBean.setTitle(bean.getTitle());
        mediaBean.setArtist(bean.getArtist());
        mediaBean.setPlayUrl(bean.getUrl());
        mediaBean.setCover(bean.getPic());

        // 服务端业务
        mediaBean.setFromType(bean.getType());
        mediaBean.setLike(bean.getFavorite() == 1);

        // 非必须
        mediaBean.setAlbumName(bean.getAlbum());
        mediaBean.setAlbumId(bean.getAlbumId());
        try {
            mediaBean.setDuration(Integer.valueOf(bean.getDuration()));
        } catch (Exception e) {
            LogUtil.e(e);
        }
        return mediaBean;
    }

    // TODO: 2018/9/2 需要用到才实现 
    public static MusicBean transform(MediaBean bean) {
        if (bean == null) {
            return null;
        }
        MusicBean musicBean = new MusicBean();

        return musicBean;
    }
}
