package cn.yinxm.img;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.SeekBar;

import cn.yinxm.img.util.ImageHelper;

/**
 * 图片的色光三原色处理
 * https://www.imooc.com/video/6468/0
 */
public class PrimaryColorActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    private static final int MAX_VALUE = 255;
    private static final int MID_VALUE = 127;

    private ImageView iv;
    private SeekBar sbHue, sbSaturation, sbLum;
    private float mHue, mSaturation, mLum;
    private Bitmap mBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary_color);

        iv = (ImageView) findViewById(R.id.iv);
        sbHue = (SeekBar) findViewById(R.id.sbHue);
        sbSaturation = (SeekBar) findViewById(R.id.sbSaturation);
        sbLum = (SeekBar) findViewById(R.id.sbLum);

        sbHue.setOnSeekBarChangeListener(this);
        sbSaturation.setOnSeekBarChangeListener(this);
        sbLum.setOnSeekBarChangeListener(this);


        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test3);
        iv.setImageBitmap(mBitmap);
        sbHue.setMax(MAX_VALUE);
        sbSaturation.setMax(MAX_VALUE);
        sbLum.setMax(MAX_VALUE);
        sbHue.setProgress(MID_VALUE);
        sbSaturation.setProgress(MID_VALUE);
        sbLum.setProgress(MID_VALUE);
    }

    public static void startActivity(Context fromContext) {
        fromContext.startActivity(new Intent(fromContext, PrimaryColorActivity.class));
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch (seekBar.getId()) {
            case R.id.sbHue:
                mHue = (progress-MID_VALUE)*1.0f /MID_VALUE * 180;//[-180, 180]
                break;
            case R.id.sbSaturation:
                mSaturation = progress * 1.0f / MID_VALUE;
                break;
            case R.id.sbLum:
                mLum = progress * 1.0f / MID_VALUE;
                break;
        }
        Bitmap bitmap = ImageHelper.handleImgPrimaryColor(mBitmap, mHue, mSaturation, mLum);
        iv.setImageBitmap(bitmap);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
