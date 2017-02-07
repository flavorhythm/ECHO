package widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;

import util.ImageLoaderTask;

/**
 * Created by flavorhythm on 2/6/17.
 */

public class CardDealerView extends CardContentView implements ImageViewInterface {
    //To Display:
    //Dealer name
    //Address
    //Phone
    //Repair service Y/N
    //Signature Elite Y/N
    //Distance from current location
    //"Click card to get directions" footer

    //Get Directions intent

    public CardDealerView(Context context) {
        super(context);
    }

    public CardDealerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardDealerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CardDealerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
    public void setLoaderTask(ImageLoaderTask task) {
        super.setLoaderTask(task);
    }

    @Override
    public ImageLoaderTask getLoaderTask() {
        return super.getLoaderTask();
    }

    @Override
    protected void initialize(AttributeSet attrs) {
        super.initialize(attrs);
    }

    @Override
    protected void updateContentBounds() {
        super.updateContentBounds();
    }

    @Override
    public void setImage(View v, ImageLoaderTask task, Bitmap bm) {

    }

    @Override
    public int getRequiredImageWidth() {
        return 0;
    }

    @Override
    public int getRequiredImageHeight() {
        return 0;
    }

    @Override
    public ImageLoaderTask.ScalingLogic getScalingLogic() {
        return null;
    }

    @Override
    public int getImageSampleSize() {
        return 0;
    }
}
