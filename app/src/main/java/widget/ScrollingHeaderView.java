package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;

import util.ImageLoaderTask;

/**
 * Created by flavorhythm on 1/12/17.
 */
public class ScrollingHeaderView extends EchoSuperView implements ImageViewInterface {
    private Bitmap mBitmap;
    private Rect mImageMask;
    private Rect mOverlayBounds;
    private Rect mRecyclerBgBounds;

    private Paint mOverlayPaint;
    private Paint mBgPaint;

    private WeakReference<ImageLoaderTask> mTaskRef;

    public ScrollingHeaderView(Context context) {
        super(context);
    }

    public ScrollingHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollingHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ScrollingHeaderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        mImageMask = new Rect(0, 0, SCREEN_WIDTH, HEADER_HEIGHT);
        mOverlayBounds = new Rect(0, 0, SCREEN_WIDTH, HEADER_HEIGHT);
        mRecyclerBgBounds = new Rect(0, HEADER_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);

        mOverlayPaint = new Paint();
        mOverlayPaint.setColor(ContextCompat.getColor(getContext(), R.color.echo_orange));
        mOverlayPaint.setAlpha(0);

        mBgPaint = new Paint();
        mBgPaint.setColor(ContextCompat.getColor(getContext(), R.color.bg_list));
    }

    @Override
    protected void updateContentBounds() {
        if(mImageMask != null) mImageMask.top = -getScrollY();
        if(mRecyclerBgBounds != null) mRecyclerBgBounds.bottom = SCREEN_HEIGHT + getScrollY();
    }

    @Override
    public void scrollTo(int x, int y) {
        y = clampScroll(y, 0, HEADER_HEIGHT);
        mOverlayPaint.setAlpha(clampAlpha(y, HEADER_HEIGHT));
        super.scrollTo(x, y);

        invalidate();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(l != oldl || t != oldt) {
            updateContentBounds();
        }
    }

    private int getBitmapOffset() {return (int)((getScrollY() * 0.5f) + 0.5f);}

    @Override
    protected void onDraw(Canvas canvas) {
        if(mImageMask != null) {
            canvas.save();
            canvas.clipRect(mImageMask, Region.Op.INTERSECT);

            if(mBitmap != null) canvas.drawBitmap(mBitmap, 0, getBitmapOffset(), null);
            if(mOverlayBounds != null) canvas.drawRect(mOverlayBounds, mOverlayPaint);

            canvas.restore();
        }

        if(mRecyclerBgBounds != null) {
            canvas.drawRect(mRecyclerBgBounds, mBgPaint);
        }
    }

    @Override
    protected int getDesiredWidth() {return SCREEN_WIDTH;}

    @Override
    protected int getDesiredHeight() {return SCREEN_HEIGHT;}

    @Override
    public void setImage(View v, ImageLoaderTask task, Bitmap bm) {
        if(task == mTaskRef.get() && v instanceof ScrollingHeaderView) setImage(bm);
    }

    public void setLoaderTask(ImageLoaderTask task) {
        task.setImageViewInterface(this);
        mTaskRef = new WeakReference<>(task);
    }

    public ImageLoaderTask getLoaderTask() {
        return mTaskRef != null ? mTaskRef.get() : null;
    }

    @Override
    public int getRequiredImageWidth() {
        return SCREEN_WIDTH;
    }

    @Override
    public int getRequiredImageHeight() {
        return HEADER_HEIGHT;
    }

    @Override
    public ImageLoaderTask.ScalingLogic getScalingLogic() {
        return ImageLoaderTask.ScalingLogic.CROP;
    }

    @Override
    public int getImageSampleSize() {
        return 0;
    }

//    private int clampScroll(int axis, final int min, final int max) {
//        axis = axis <= min ? min : axis;
//        axis = axis >= max ? max : axis;
//
//        return axis;
//    }

    private static int clampAlpha(int axis, int max) {
        final int maxAlpha = 255;
        return (int)((maxAlpha * axis / max) + 0.5f);
    }

    private void setImage(Bitmap bm) {
        mBitmap = bm;
        invalidate();
    }
}
