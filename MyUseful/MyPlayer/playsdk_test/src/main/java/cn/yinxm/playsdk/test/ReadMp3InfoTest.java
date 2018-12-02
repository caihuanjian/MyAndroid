package cn.yinxm.playsdk.test;

import org.farng.mp3.MP3File;
import org.farng.mp3.TagException;
import org.farng.mp3.id3.AbstractID3v2;
import org.farng.mp3.id3.ID3v1;
import org.farng.mp3.lyrics3.AbstractLyrics3;

import java.io.IOException;

/**
 * 会有乱码问题，解决方法
 * https://www.jianshu.com/p/e368517ec7b9
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/22
 */
public class ReadMp3InfoTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        test("/Users/yinxuming/Downloads/简弘亦 - 问.MP3");

    }

    /**
     */
    public static void test(String filePath) {

        try {

            //MP3File file = new MP3File("c:\\TDDOWNLOAD\\shuangjiegun.mp3");//1,2
            MP3File file = new MP3File(filePath);
            // MP3File file = new MP3File("/home/zhubin/Music/1.mp3");//1,lyrics
            AbstractID3v2 id3v2 = file.getID3v2Tag();
            ID3v1 id3v1 = file.getID3v1Tag();
            System.out.println("id3v1="+id3v1);
            System.out.println("id3v2="+id3v2);

            if (id3v2 != null) {

                System.out.println("id3v2");

                System.out.println(id3v2.getAlbumTitle());//专辑名
                System.out.println("title="+new String(id3v2.getSongTitle().getBytes("GBK"), "UTF-16"));//歌曲名
                System.out.println("title2="+new String(id3v2.getSongTitle().getBytes("ISO-8859-1"), "GB2312"));//歌曲名
                System.out.println("title3="+new String(id3v2.getSongTitle().getBytes("ISO-8859-1"), "GBK"));//歌曲名
                System.out.println("title4="+new String(id3v2.getSongTitle().getBytes("ISO-8859-1"), "UTF-16"));//歌曲名
                System.out.println("title5="+new String(id3v2.getSongTitle().getBytes("ISO-8859-1"), "UTF-8"));//歌曲名
                System.out.println(id3v2.getLeadArtist());//歌手

            } else {
                System.out.println("id3v1");

                System.out.println(id3v1.getAlbumTitle());
                System.out.println(id3v1.getSongTitle());
                System.out.println(id3v1.getLeadArtist());

            }

            AbstractLyrics3 lrc3Tag = file.getLyrics3Tag();
            if (lrc3Tag != null) {
                String lyrics = lrc3Tag.getSongLyric();
                System.out.println(lyrics);
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("over");
    }
}
