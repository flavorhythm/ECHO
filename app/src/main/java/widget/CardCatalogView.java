package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;

import util.ImageLoaderTask;

/**
 * Created by zyuki on 8/4/2016.
 */
public class CardCatalogView extends CardContentView {
    private CharSequence mText;
    private StaticLayout mTextLayout;
    private Point mTextOrigin;
    private TextPaint mTextPaint;

    private Bitmap mBitmap;
    private Rect mImgMask;

    public CardCatalogView(Context context) {
        super(context);
    }

    public CardCatalogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardCatalogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CardCatalogView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        mImgMask = new Rect(0, 0, getDesiredWidth(), getDesiredHeight());
    }

    @Override
    public int getRequiredImageWidth() {
        return getDesiredWidth();
    }

    @Override
    public int getRequiredImageHeight() {
        return getDesiredHeight();
    }

    @Override
    public ImageLoaderTask.ScalingLogic getScalingLogic() {
        return ImageLoaderTask.ScalingLogic.CROP;
    }

    @Override
    public int getImageSampleSize() {
        return 0;
    }

    @Override
    public void setImage(View v, ImageLoaderTask task, Bitmap bm) {
        if (task == getLoaderTask() && v instanceof CardCatalogView) {
            setImage(bm);
        }
    }

    public void setImage(Bitmap cardImage) {
        mBitmap = cardImage;
        invalidate();
    }

    @Override
    protected void updateContentBounds() {}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mImgMask != null) {
            canvas.save();
            canvas.clipRect(mImgMask, Region.Op.INTERSECT);
            if (mBitmap != null) canvas.drawBitmap(mBitmap, 0, 0, null);
            canvas.restore();
        }
    }
}