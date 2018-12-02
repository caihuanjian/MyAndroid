package cn.yinxm.iflytek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import cn.yinxm.util.LogUtil;

/**
 * Created by yinxm on 2017/1/1.
 * 功能:
 */

public class VoiceLevelView extends View {
    private Drawable drawableEmpty;
    private Drawable drawableFull;
    private PaintFlagsDrawFilter paintFlagsDrawFilter = new PaintFlagsDrawFilter(1, 2);
    Path path;

    public VoiceLevelView(Context context) {
        super(context);

        try {
            this.drawableEmpty = ResourceUtil.getDrawble(this.getContext(), "voice_empty");
            this.drawableFull = ResourceUtil.getDrawble(this.getContext(), "voice_full");
            this.drawableEmpty.setBounds(new Rect(-this.drawableEmpty.getIntrinsicWidth() / 2, -this.drawableEmpty.getIntrinsicHeight() / 2, this.drawableEmpty.getIntrinsicWidth() / 2, this.drawableEmpty.getIntrinsicHeight() / 2));
            this.drawableFull.setBounds(new Rect(-this.drawableFull.getIntrinsicWidth() / 2, -this.drawableFull.getIntrinsicHeight() / 2, this.drawableFull.getIntrinsicWidth() / 2, this.drawableFull.getIntrinsicHeight() / 2));
            this.path = new Path();
            this.setVoiceLevel(0);
        } catch (Exception e) {
            LogUtil.e("异常", e);
        }

    }

    public void setVoiceLevel(int num) {
        this.path.reset();
        this.path.addCircle(0.0F, 0.0F, (float)(this.drawableEmpty.getIntrinsicWidth() * num / 12), Path.Direction.CCW);
    }

    public void onDraw(Canvas canvas) {
        canvas.save();
        canvas.setDrawFilter(this.paintFlagsDrawFilter);
        canvas.translate((float)(this.getWidth() / 2), (float)(this.getHeight() / 2));
        this.drawableEmpty.draw(canvas);
        canvas.clipPath(this.path);
        this.drawableFull.draw(canvas);
        canvas.restore();
    }

    public void finalize() throws Throwable {
        this.drawableEmpty = null;
        this.drawableFull = null;
        super.finalize();
    }

    protected void onMeasure(int var1, int var2) {
        super.onMeasure(var1, var2);
        int var3 = View.MeasureSpec.getSize(var1);
        int var4 = View.MeasureSpec.getSize(var2);
        Drawable var5 = this.getBackground();
        if(var5 != null) {
            var3 = var5.getMinimumWidth();
            var4 = var5.getMinimumHeight();
        }

        this.setMeasuredDimension(resolveSize(var3, var1), resolveSize(var4, var2));
    }
}
