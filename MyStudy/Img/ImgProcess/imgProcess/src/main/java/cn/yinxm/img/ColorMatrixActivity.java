package cn.yinxm.img;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;

/**
 * 矩阵变换运算
 */
public class ColorMatrixActivity extends AppCompatActivity {
    private ImageView iv;
    private GridLayout gridLayout;

    private Bitmap mBitmap;
    private int mEtWidth, mEtHeight;
    private EditText[] mEts = new EditText[20];
    private float[] mColorMatrix = new float[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_matrix);

        iv = (ImageView) findViewById(R.id.iv);
        gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        gridLayout.post(new Runnable() {//无法直接获取控件宽高，可以通过view.post在控件绘制完毕后获取得到
            @Override
            public void run() {//计算每个ET的宽高
                mEtWidth = gridLayout.getWidth() / 5;
                mEtHeight = gridLayout.getHeight() / 4;
                addEditex();
                initMatrix();
            }
        });

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.test1);
        iv.setImageBitmap(mBitmap);
    }

    /**
     * 初始化矩阵数值,对角线为1
     */
    private void initMatrix() {
        for (int i = 0; i < 20; i++) {
            if (i % 6 == 0) {
                mEts[i].setText("1");
            } else {
                mEts[i].setText("0");
            }
        }
    }

    private void addEditex() {
        for (int i = 0; i < 20; i++) {
            EditText et = new EditText(this);
            mEts[i] = et;
            gridLayout.addView(et, mEtWidth, mEtHeight);
        }

    }


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ColorMatrixActivity.class));
    }

    public void clickChange(View view) {
        getInputMatrix();
        setImgMatrix();
    }

    public void clickReset(View view) {
        initMatrix();
        getInputMatrix();
        setImgMatrix();
    }

    //获取输入矩阵值
    private void getInputMatrix() {
        for (int i = 0; i < 20; i++) {
            mColorMatrix[i] = Float.valueOf(mEts[i].getText().toString());
        }
    }

    //设置图像的矩阵值
    private void setImgMatrix() {
        Bitmap bmp = Bitmap.createBitmap(mBitmap.getWidth(), mBitmap.getHeight(), Bitmap.Config.ARGB_8888);

        ColorMatrix colorMatrix = new ColorMatrix();//组装矩阵参数
        colorMatrix.set(mColorMatrix);

        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));//设置画笔的矩阵参数

        canvas.drawBitmap(mBitmap, 0, 0, paint);
        iv.setImageBitmap(bmp);
    }
}
