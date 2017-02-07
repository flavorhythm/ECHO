package widget;

import android.graphics.Bitmap;
import android.view.View;

import util.ImageLoaderTask;

/**
 * Created by flavorhythm on 1/10/17.
 */

public interface ImageViewInterface {
    void setImage(View v, ImageLoaderTask task, Bitmap bm);
    int getRequiredImageWidth();
    int getRequiredImageHeight();
    ImageLoaderTask.ScalingLogic getScalingLogic();
    int getImageSampleSize();
}
