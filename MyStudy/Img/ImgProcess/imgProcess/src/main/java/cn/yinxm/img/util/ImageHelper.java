package cn.yinxm.img.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import cn.yinxm.util.LogUtil;

/**
 * Created by yinxm on 2018/4/14.
 * 图像处理工具类
 * 1、ColorMatrix 调整色光三原色
 */
public class ImageHelper {

    /**
     * 处理图片色光原色
     *
     * @param bm         原图
     * @param hue        色调
     * @param saturation 饱和度
     * @param lum        亮度
     * @return
     */
    public static Bitmap handleImgPrimaryColor(Bitmap bm, float hue, float saturation, float lum) {
        Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), Bitmap.Config.ARGB_8888);//创建一个与原图同样大小的bmp，传进来的bm不能直接修改，否则会报错

        Canvas canvas = new Canvas(bmp);//创建一个绘制bitmap的画布
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        ColorMatrix hueMatrix = new ColorMatrix();//设置色调,setRotate
        hueMatrix.setRotate(0, hue);
        hueMatrix.setRotate(1, hue);
        hueMatrix.setRotate(2, hue);
        ColorMatrix saturationMatrix = new ColorMatrix();//饱和度,setSaturation
        saturationMatrix.setSaturation(saturation);
        ColorMatrix lumMatrix = new ColorMatrix();//亮度,setScale
        lumMatrix.setScale(lum, lum, lum, 1);
        ColorMatrix colorMatrix = new ColorMatrix();//整合3个参数配置
        colorMatrix.postConcat(hueMatrix);
        colorMatrix.postConcat(saturationMatrix);
        colorMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));//将色光参数设置到画笔里面
        canvas.drawBitmap(bm, 0, 0, paint);//画布上已色光参数绘制原
        LogUtil.d("oldBitmap=" + bm + ", isMutable=" + bm.isMutable() + ", newBitmap=" + bmp + ", isMutable=" + bmp.isMutable());
        return bmp;
    }


    /**
     * 底片效果，像素点处理
     *
     * @param bm
     * @return
     */
    public static Bitmap handleImgPixelNegative(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int totalPixel = width * height;
        int[] oldPixel = new int[totalPixel];
        int[] newPixel = new int[totalPixel];
        bm.getPixels(oldPixel, 0, width, 0, 0, width, height);//读取pix到一维素组，第三个参数为每行有多少个（行距）

        int color;
        int r, g, b, a;

        for (int i = 0; i < totalPixel; i++) {
            color = oldPixel[i];//argb,每个8位
            a = Color.alpha(color);//color >>> 24
            r = Color.red(color);//color>>16 && 0xFF
            g = Color.green(color);//color>>8 && 0xFF
            b = Color.blue(color);//color && 0xFF

            //底片效果
            r = 255 - r;
            g = 255 - g;
            b = 255 - b;

            //数据合法性
            if (r < 0) {
                r = 0;
            }/* else if (r > 255) {
                r = 255;
            }*/
            if (g < 0) {
                g = 0;
            }
            if (b < 0) {
                b = 0;
            }

            newPixel[i] = Color.argb(a, r, g, b);// 合成新像素
        }
        //设置像素为新图像
        bmp.setPixels(newPixel, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     * 老照片效果，像素点处理
     *
     * @param bm
     * @return
     */
    public static Bitmap handleImgPixelOldPhoto(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int totalPixel = width * height;
        int[] oldPixel = new int[totalPixel];
        int[] newPixel = new int[totalPixel];
        bm.getPixels(oldPixel, 0, width, 0, 0, width, height);//读取pix到一维素组，第三个参数为每行有多少个（行距）

        int color;
        int r, g, b, a;

        for (int i = 0; i < totalPixel; i++) {
            color = oldPixel[i];//argb,每个8位
            a = Color.alpha(color);//color >>> 24
            r = Color.red(color);//color>>16 && 0xFF
            g = Color.green(color);//color>>8 && 0xFF
            b = Color.blue(color);//color && 0xFF

            //老照片效果
            r = (int)(0.393*r+0.769*g+0.189*b);
            g = (int)(0.349*r+0.686*g+0.168*b);
            b = (int)(0.272*r+0.534*g+0.131*b);

            //数据合法性
            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }

            newPixel[i] = Color.argb(a, r, g, b);// 合成新像素
        }
        //设置像素为新图像
        bmp.setPixels(newPixel, 0, width, 0, 0, width, height);
        return bmp;
    }

    /**
     * 浮雕效果，像素点处理
     *
     * @param bm
     * @return
     */
    public static Bitmap handleImgPixelRelief(Bitmap bm) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        int totalPixel = width * height;
        int[] oldPixel = new int[totalPixel];
        int[] newPixel = new int[totalPixel];
        bm.getPixels(oldPixel, 0, width, 0, 0, width, height);//读取pix到一维素组，第三个参数为每行有多少个（行距）

        int colorBefore, color;//需要前一个的像素值
        int r, g, b, a;
        int r1, g1, b1, a1;

        for (int i = 1; i < totalPixel; i++) {//从1开始
            //前一个像素值
            colorBefore = oldPixel[i-1];//argb,每个8位
            r = Color.red(colorBefore);//color>>16 && 0xFF
            g = Color.green(colorBefore);//color>>8 && 0xFF
            b = Color.blue(colorBefore);//color && 0xFF
            a = Color.alpha(colorBefore);//color >>> 24

            color = oldPixel[i];//argb,每个8位
            r1 = Color.red(color);//color>>16 && 0xFF
            g1 = Color.green(color);//color>>8 && 0xFF
            b1 = Color.blue(color);//color && 0xFF
            a1 = Color.alpha(color);//color >>> 24

            //浮雕效果
            r = r - r1 + 127;//当前值-前一个值，+127
            g = g - g1 + 127;
            b = b - b1 + 127;


            //数据合法性
            if (r > 255) {
                r = 255;
            }else if (r < 0) {
                r = 0;
            }
            if (g > 255) {
                g = 255;
            }else if (g < 0) {
                g = 0;
            }
            if (b > 255) {
                b = 255;
            }else if (b < 0) {
                b = 0;
            }

            newPixel[i] = Color.argb(a, r, g, b);// 合成新像素
        }
        //设置像素为新图像
        bmp.setPixels(newPixel, 0, width, 0, 0, width, height);
        return bmp;
    }
}
