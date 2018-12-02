package cn.yinxm.playsdk.test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMetadataRetriever;
import android.util.Log;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaFromType;

import java.io.IOException;

/**
 * https://www.jianshu.com/p/e38178f008ab
 * <p>
 *
 * @author yinxuming
 * @date 2018/10/2
 */
public class ReadMp3InfoByMmr {
    private static final String TAG = "ReadMp3InfoByMmr";

    public static MediaBean getFileInfo(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filePath);
        Log.d(TAG, "parseMp3File名称: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        Log.d(TAG, "parseMp3File专辑: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        Log.d(TAG, "parseMp3File歌手: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        Log.d(TAG, "parseMp3File码率: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        Log.d(TAG, "parseMp3File时长: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Log.d(TAG, "parseMp3File类型: " + mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE));

        // 获取专辑封面图
        byte[] data = mmr.getEmbeddedPicture();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Log.d(TAG, "bitmap="+bitmap);

//        获得mp3文件的频率，通过MediaFormat类，代码如下:

        MediaExtractor mex = new MediaExtractor();
        try {
            mex.setDataSource(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mf = mex.getTrackFormat(0);
        int sampleRate = mf.getInteger(MediaFormat.KEY_SAMPLE_RATE);
        mex.release();
        Log.d(TAG, "parseMp3File频率: " + sampleRate);


        MediaBean mediaBean = new MediaBean();
        mediaBean.setFromType(MediaFromType.LOCAL);

        mediaBean.setTitle(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        mediaBean.setArtist(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        mediaBean.setAlbumName(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        mediaBean.setDuration(Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)));

        mediaBean.setExtObject(bitmap);
        // MediaMetadataRetriever用完后及时释放
        mmr.release();
        return mediaBean;
    }

    public static void main(String[] args) {
        getFileInfo("/Users/yinxuming/Downloads/简弘亦 - 问.MP3");
    }
}
