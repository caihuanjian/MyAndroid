package com.yinxm.puzzle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by yinxuming on 2018/5/6.
 * 拼图游戏面板
 * 1、绘制图片，并将图片拆分
 * 2、随机摆放图片，确保能还原
 * 3、电击空白图片旁边的图片，交换图片位置
 * 4、交换后，检查是否拼图完成
 */
public class JigsawPuzzlePanel extends View {
    private static final int DEFAULT_ITEMS = 3;

    private boolean isInited = false;

    private Bitmap mBitmap;//原始图像
    private Bitmap[] mBitmapItems;//图像分割数组

    private PuzzleHelper puzzleHelper;
    private int[] mBmpIndex;//bmp的序号，打乱前为0,1,2...


    private int viewWidth, viewHeight;
    private int row, columns;
    private int itemWidth, itemHeight;
    private PointItem blankPoint;
    private boolean isSuccess = false;
    private int level = 0;//难度


    public JigsawPuzzlePanel(Context context) {
        this(context, null, 0);
    }

    public JigsawPuzzlePanel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JigsawPuzzlePanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LogUtil.d("JigsawPuzzlePanel construct");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        LogUtil.d("1, widthMeasureSpec="+widthMeasureSpec+", heightMeasureSpec="+heightMeasureSpec);
//        LogUtil.d("1, width="+MeasureSpec.getSize(widthMeasureSpec)+", mode="+MeasureSpec.getMode(widthMeasureSpec));

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LogUtil.d("2, widthMeasureSpec=" + widthMeasureSpec + ", heightMeasureSpec=" + heightMeasureSpec);

        //获取测量数据
        viewWidth = MeasureSpec.getSize(widthMeasureSpec);
        viewHeight = MeasureSpec.getSize(heightMeasureSpec);

        LogUtil.d("width=" + viewWidth + ", height=" + viewHeight + ", mode=" + MeasureSpec.getMode(widthMeasureSpec));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        LogUtil.d("onSizeChanged w=" + w + ", h=" + h);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        LogUtil.d("onDraw mBitmapItems=" + mBitmapItems);

        if (!isInited) {
            init();
        }

        if (mBitmapItems != null && mBmpIndex != null) {
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

            for (int i = 0; i < row; i++) {
                for (int j = 0; j < columns; j++) {

                    int index = mBmpIndex[i * columns + j];
                    if (puzzleHelper != null && puzzleHelper.getBlankIndex() == index && !isSuccess) {
                        //空白位置不用绘制
                        blankPoint = new PointItem(j, i);
                        continue;
                    }
                    Bitmap bmp = mBitmapItems[index];
                    if (bmp != null) {
                        canvas.drawBitmap(bmp, j * itemWidth, i * itemHeight, paint);
                    }
                    if (isSuccess) {
                        blankPoint = null;
                    }
                }

            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            LogUtil.d("x="+event.getX()+", y="+event.getY()+", rawX="+event.getRawX()+", rawY="+event.getRawY()
                    +"\n width=" + viewWidth + ", height=" + viewHeight+", \nitemWidth="+itemWidth+", itemHeight="+itemHeight);

            PointItem pointItem = xyToIndex((int)event.getX(), (int)event.getY());
            if (puzzleHelper.swapPoint(blankPoint, pointItem)) {
                invalidate();
                //检查是否拼图成功，数组顺序排列
                if (puzzleHelper.isSuccess()) {
                    isSuccess = true;
                    invalidate();
                    new AlertDialog.Builder(getContext())
                            .setTitle("拼图成功")
                            .setMessage("恭喜您拼图成功")
                            .setCancelable(true)
                            .setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startGame();
                                }
                            })
                            .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            })
                            .show();
                }
            }
        }

        return true;
    }

    public void setRowColums(int row, int columns) {
        this.row = row;
        this.columns = columns;
        if (isInited) {
            //动态修改行列，重新初始化
            init();
            invalidate();
        }
    }

    public int startGame() {
        return startGame(level);
    }

    public int getRandomLevel() {
        Random random = new Random();
        return random.nextInt(100)+20;//交换20-119次
    }

    /**
     * 开始游戏
     * 先随机打乱
     */
    public int startGame(int level) {
        puzzleHelper = new PuzzleHelper();
        if (level < 0) {
            level = getRandomLevel();
        }
        this.level = level;
        mBmpIndex = puzzleHelper.createRandomIndex(row, columns, level);//交换20-29次
        if (level == 0) {
            isSuccess = true;
        }else {
            isSuccess = false;
        }
        invalidate();
        return level;
    }


    public void init() {

        if (row <= 0) {
            row = DEFAULT_ITEMS;
        }
        if (columns <= 0) {
            columns = DEFAULT_ITEMS;
        }

        LogUtil.d("init width=" + viewWidth + ", height=" + viewHeight);
        if (viewWidth <= 0 || viewHeight <= 0) {
            return;
        }
        itemWidth = viewWidth / columns;
        itemHeight = viewHeight / row;

        Bitmap bmp = loadImg();

        if (bmp == null) return;


        mBitmap = Bitmap.createScaledBitmap(bmp, viewWidth, viewHeight, false);
        int total = row * columns;
        mBitmapItems = new Bitmap[total];

        int index = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < columns; j++) {
                //将1张图片拆分成多个图片，以一维数组存储，二维数组也可以额
                mBitmapItems[index++] = Bitmap.createBitmap(mBitmap, j * itemWidth, i * itemHeight, itemWidth, itemHeight);
            }
        }
        isInited = true;
        startGame(0);
    }


    /**
     * 加载要显示的图片
     *
     * @return
     */
    private Bitmap loadImg() {
        Bitmap bitmap = null;

        InputStream ins = null;
        try {
            AssetManager assetManager = getResources().getAssets();
            ins = assetManager.open("back.jpg");
            if (ins == null) return null;
            bitmap = BitmapFactory.decodeStream(ins);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        return bitmap;
    }

    /**
     * 将屏幕上的点转换成,对应拼图块的索引
     * @param xPix 相对容器x
     * @param yPix
     * @return
     */
    private PointItem xyToIndex(int xPix, int yPix){


        int colIndex = xPix/itemWidth;
        int rowIndex = yPix/itemHeight;
        LogUtil.d("xPix="+xPix+", colIndex="+colIndex+", rowIndex="+rowIndex);
        //越界处理
        if (colIndex >= columns) {
            colIndex = columns -1;
        }
        if (rowIndex >= row) {
            rowIndex = row -1;
        }
        // TODO: 2018/5/9
        Toast.makeText(getContext(),"("+colIndex+","+rowIndex+")",Toast.LENGTH_SHORT).show();
        return new PointItem(colIndex,rowIndex);

    }

}
