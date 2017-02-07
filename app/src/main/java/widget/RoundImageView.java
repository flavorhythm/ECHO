package widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import util.ImageLoaderTask;

/**
 * Created by ZYuki on 7/20/2016.
 */
public class RoundImageView extends EchoSuperView implements ImageViewInterface, Target {
    private Bitmap mImage;
    private Path mPath;

    private WeakReference<ImageLoaderTask> mTaskRef;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setImage(bitmap);
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    @Override
    public int getRequiredImageWidth() {
        return ROUND_IMG_SIZE;
    }

    @Override
    public int getRequiredImageHeight() {
        return ROUND_IMG_SIZE;
    }

    @Override
    public ImageLoaderTask.ScalingLogic getScalingLogic() {
        return ImageLoaderTask.ScalingLogic.CROP;
    }

    @Override
    public int getImageSampleSize() {
        return 4;
    }

    @Override
    public void setImage(View v, ImageLoaderTask task, Bitmap bm) {
        if(task == mTaskRef.get() && v instanceof RoundImageView){
            setImage(bm);
        }
    }

    public void setLoaderTask(ImageLoaderTask task) {
        task.setImageViewInterface(this);
        mTaskRef = new WeakReference<>(task);
    }
    public ImageLoaderTask getLoaderTask() {return mTaskRef != null ? mTaskRef.get() : null;}

    public void setImage(Bitmap image) {
        this.mImage = image;
        invalidate();
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        if(attrs != null) {}

        mPath = new Path();
        mPath.addCircle(ROUND_IMG_SIZE / 2, ROUND_IMG_SIZE / 2, ROUND_IMG_SIZE / 2, Path.Direction.CW);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mImage != null) {
            if(mPath != null) canvas.clipPath(mPath, Region.Op.INTERSECT);
            canvas.drawBitmap(mImage, -1 * (mImage.getWidth() / 2), -1 * (mImage.getHeight() / 4), null);
        }
    }

    @Override
    protected void updateContentBounds() {}

    @Override
    protected int getDesiredWidth() {return ROUND_IMG_SIZE;}
    @Override
    protected int getDesiredHeight() {return ROUND_IMG_SIZE;}
}
