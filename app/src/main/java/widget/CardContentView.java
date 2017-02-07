package widget;

import android.content.Context;
import android.util.AttributeSet;

import java.lang.ref.WeakReference;

import util.ImageLoaderTask;

/**
 * Created by flavorhythm on 1/10/17.
 */

public abstract class CardContentView extends EchoSuperView implements ImageViewInterface {
    private WeakReference<ImageLoaderTask> mTaskRef;

    public CardContentView(Context context) {
        super(context);
    }

    public CardContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CardContentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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

    public void setLoaderTask(ImageLoaderTask task) {
        task.setImageViewInterface(this);
        mTaskRef = new WeakReference<>(task);
    }

    public ImageLoaderTask getLoaderTask() {
        return mTaskRef != null ? mTaskRef.get() : null;
    }

    @Override
    protected void initialize(AttributeSet attrs) {}

    @Override
    protected void updateContentBounds() {}

    @Override
    protected int getDesiredWidth() {return CARD_WIDTH;}

    @Override
    protected int getDesiredHeight() {return CARD_HEIGHT;}
}
