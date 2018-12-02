package cn.yinxm.img;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import cn.yinxm.img.util.ImageHelper;

/**
 * 像素点运算处理
 */
public class PixelProcessActivity extends AppCompatActivity {

    private ImageView iv1, iv2, iv3, iv4;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pixel_process);

        iv1 = (ImageView) findViewById(R.id.iv1);
        iv2 = (ImageView) findViewById(R.id.iv2);
        iv3 = (ImageView) findViewById(R.id.iv3);
        iv4 = (ImageView) findViewById(R.id.iv4);

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test2);
        iv1.setImageBitmap(mBitmap);

        iv2.setImageBitmap(ImageHelper.handleImgPixelNegative(mBitmap));
        iv3.setImageBitmap(ImageHelper.handleImgPixelOldPhoto(mBitmap));
        iv4.setImageBitmap(ImageHelper.handleImgPixelRelief(mBitmap));

    }

    public static void startActivity(Context fromContext) {
        fromContext.startActivity(new Intent(fromContext, PixelProcessActivity.class));
    }
}
