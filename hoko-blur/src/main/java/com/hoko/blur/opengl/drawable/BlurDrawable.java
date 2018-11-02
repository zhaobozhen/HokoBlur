package com.hoko.blur.opengl.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.hoko.blur.HokoBlur;
import com.hoko.blur.anno.Mode;
import com.hoko.blur.api.IParams;
import com.hoko.blur.api.IScreenRenderer;
import com.hoko.blur.opengl.functor.DrawFunctor;
import com.hoko.blur.opengl.functor.ScreenBlurRenderer;
import com.hoko.blur.util.BlurUtil;
import com.hoko.blur.util.Preconditions;

/**
 * Created by yuxfzju on 16/11/23.
 */
public class BlurDrawable extends Drawable implements IParams {

    private DrawFunctor mDrawFunctor;

    private IScreenRenderer mBlurRenderer;

    private int alpha;

    private Paint mPaint;
    private volatile boolean mBlurEnabled = true;

    public BlurDrawable() {
        mBlurRenderer = new ScreenBlurRenderer.Builder().build();
        mDrawFunctor = new DrawFunctor(mBlurRenderer);
        mPaint = new Paint();
        mPaint.setColor(Color.TRANSPARENT);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        if (canvas.isHardwareAccelerated() && mBlurEnabled) {
            mDrawFunctor.doDraw(canvas);
        } else {
            canvas.drawRect(getBounds(), mPaint);
        }
    }

    /**
     * BlurDrawable设置Alpha值暂时无效，之后将做进一步改进实现
     */
    @Override
    @Deprecated
    public void setAlpha(int alpha) {
        // TODO: 2017/2/3
        this.alpha = alpha;
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        // TODO: 2017/1/25  
    }

    @Override
    public int getOpacity() {
        return alpha == 255 ? PixelFormat.OPAQUE : PixelFormat.TRANSLUCENT;
    }


    public void disableBlur() {
        mBlurEnabled = false;
    }

    public void enableBlur() {
        mBlurEnabled = true;
    }

    @Override
    public void mode(@Mode int mode) {
        mBlurRenderer.mode(mode);
        invalidateSelf();
    }

    @Override
    public void radius(int radius) {
        mBlurRenderer.radius(BlurUtil.checkRadius(radius));
        invalidateSelf();
    }

    @Override
    public void sampleFactor(float factor) {
        mBlurRenderer.sampleFactor(factor);
        invalidateSelf();
    }

    @Override
    public int mode() {
        return mBlurRenderer.mode();
    }

    @Override
    public int radius() {
        return mBlurRenderer.radius();
    }

    @Override
    public float sampleFactor() {
        return mBlurRenderer.sampleFactor();
    }

    public void freeGLResource() {
        mBlurRenderer.free();
    }

}