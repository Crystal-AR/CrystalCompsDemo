package com.crystal_ar.crystalcompsdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.crystal_ar.crystal_ar.IntPair;

/**
 * Created by Frederik on 2/25/17.
 */

public class TableView extends SurfaceView {
    private Paint paint;
    private SurfaceHolder mHolder;
    private Context context;

    public TableView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        mHolder = getHolder();
        this.setBackgroundColor(Color.TRANSPARENT);
        this.setZOrderOnTop(true); //necessary
        mHolder.setFormat(PixelFormat.TRANSPARENT);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void drawCorners(IntPair[] corners, int photoWidth, int photoHeight) {
        if (mHolder.getSurface().isValid()) {
            int viewWidth = this.getWidth();
            int viewHeight = this.getHeight();
            float transformedX;
            float transformedY;
            final Canvas canvas = mHolder.lockCanvas();
            if (canvas != null) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                for (IntPair corner : corners) {
                    transformedX = (float)corner.x / photoWidth;
                    transformedY = (float)corner.y / photoHeight;
                    canvas.drawPoint(Math.round(transformedX * viewWidth), Math.round(transformedY * viewHeight), paint);
                }
            }
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
}
