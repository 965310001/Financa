package tech.com.commoncore.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

/**
 * Desc:
 */
public class RoundImageView extends androidx.appcompat.widget.AppCompatImageView {
    private int radius = 20;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //如果不设置背景圆角,就直接把裁剪代码放到这里.
        super.onDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        Path path = new Path();
        path.addRoundRect(new RectF(0, 0, getWidth(), getHeight()), radius, radius, Path.Direction.CCW);
        canvas.clipPath(path);//设置可显示的区域，canvas四个角会被剪裁掉
        super.draw(canvas);
    }
}
