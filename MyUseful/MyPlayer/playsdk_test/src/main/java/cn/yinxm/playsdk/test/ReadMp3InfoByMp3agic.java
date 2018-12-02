package cn.yinxm.playsdk.test;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

/**
 * 没有问题
 * https://github.com/mpatric/mp3agic
 * <p>
 *
 * @author yinxuming
 * @date 2018/10/2
 */
public class ReadMp3InfoByMp3agic {

    public static void main(String[] args) throws InvalidDataException, IOException, UnsupportedTagException {
        test("/Users/yinxuming/Downloads/test.MP3");
    }

    public static void test(String filePath) throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mp3file = new Mp3File(filePath);
        System.out.println("Length of this mp3 is: " + mp3file.getLengthInSeconds() + " seconds");
        System.out.println("Bitrate: " + mp3file.getBitrate() + " kbps " + (mp3file.isVbr() ? "(VBR)" : "(CBR)"));
        System.out.println("Sample rate: " + mp3file.getSampleRate() + " Hz");
        System.out.println("Has ID3v1 tag?: " + (mp3file.hasId3v1Tag() ? "YES" : "NO"));
        System.out.println("Has ID3v2 tag?: " + (mp3file.hasId3v2Tag() ? "YES" : "NO"));
        System.out.println("Has custom tag?: " + (mp3file.hasCustomTag() ? "YES" : "NO"));
        if (mp3file.hasId3v1Tag()) {
            ID3v1 id3v1Tag = mp3file.getId3v1Tag();
            System.out.println("Track: " + id3v1Tag.getTrack());
            System.out.println("Artist: " + id3v1Tag.getArtist());
            System.out.println("Title: " + id3v1Tag.getTitle());
            System.out.println("Album: " + id3v1Tag.getAlbum());
            System.out.println("Year: " + id3v1Tag.getYear());
            System.out.println("Genre: " + id3v1Tag.getGenre() + " (" + id3v1Tag.getGenreDescription() + ")");
            System.out.println("Comment: " + id3v1Tag.getComment());
        }
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            System.out.println("Track: " + id3v2Tag.getTrack());
            System.out.println("Artist: " + id3v2Tag.getArtist());
            System.out.println("Title: " + id3v2Tag.getTitle());
            System.out.println("Album: " + id3v2Tag.getAlbum());
            System.out.println("Year: " + id3v2Tag.getYear());
            System.out.println("Genre: " + id3v2Tag.getGenre() + " (" + id3v2Tag.getGenreDescription() + ")");
            System.out.println("Comment: " + id3v2Tag.getComment());
            System.out.println("Lyrics: " + id3v2Tag.getLyrics());
            System.out.println("Composer: " + id3v2Tag.getComposer());
            System.out.println("Publisher: " + id3v2Tag.getPublisher());
            System.out.println("Original artist: " + id3v2Tag.getOriginalArtist());
            System.out.println("Album artist: " + id3v2Tag.getAlbumArtist());
            System.out.println("Copyright: " + id3v2Tag.getCopyright());
            System.out.println("URL: " + id3v2Tag.getUrl());
            System.out.println("Encoder: " + id3v2Tag.getEncoder());
            byte[] albumImageData = id3v2Tag.getAlbumImage();
            if (albumImageData != null) {
                System.out.println("Have album image data, length: " + albumImageData.length + " bytes");
                System.out.println("Album image mime type: " + id3v2Tag.getAlbumImageMimeType());
            }
        }

//        Getting ID3v2 album artwork
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            byte[] imageData = id3v2Tag.getAlbumImage();
//            if (imageData != null) {
//                String mimeType = id3v2Tag.getAlbumImageMimeType();
//                // Write image to file - can determine appropriate file extension from the mime type
//                RandomAccessFile file = new RandomAccessFile("album-artwork", "rw");
//                file.write(data);
//                file.close();
//            }
        }



//        Length of this mp3 is: 229 seconds
//        Bitrate: 128 kbps (CBR)
//        Sample rate: 44100 Hz
//        Has ID3v1 tag?: NO
//        Has ID3v2 tag?: YES
//        Has custom tag?: NO
//        Track: 1
//        Artist: 简弘亦
//        Title: 问
//        Album: 热门华语263
//        Year: null
//        Genre: -1 (null)
//        Comment: 163 key(Don't modify):L64FU3W4YxX3ZFTmbZ+8/aOPd3USSv3zmhj66L96MiwgOe7HX4y5MJ2xxc5GEHQYmMySgMtIuDtG0tfC3Hwcxl9WLF/RV5VY41VIRg3oeDNUDonqXztdJBx+kXE0PPv3ftIAVZTkSCDsZnBO2/56l5SuheRfV1wFjVODLYlxDwH+Ans2d3Cp4nZn/PaqplmHwMWh4JsIP49EZHAbclbSB2hqgjGZeTo/wwmr4S9xLuuwzIoYtMIHtkIkEtoI4dxBl1VzYUG+a+gxsTaWWcmiFh2/OYkHoJerHAi4PZhNf11IOHEEQgp7DrJHLBkJvaw03GJu4PUvg38L25qjLDS2HaS/EOfgyUKf2Yw7xsenJMJNJ6aHTN4OJqUuxCObjFF6XF9kZohmOcu5R01LyYUsS86bT2WK+I0FZTOTI5H1BxyrW0krItkzbsSKoVjZfbyQfjz0F3fD9O03Ta1bGb2qpw==
//        Lyrics: null
//        Composer: null
//        Publisher: null
//        Original artist: null
//        Album artist: null
//        Copyright: null
//        URL: null
//        Encoder: null
//        Have album image data, length: 152899 bytes
//        Album image mime type: image/jpeg

    }
}
