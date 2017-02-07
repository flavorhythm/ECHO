package util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.squareup.picasso.Cache;

import java.lang.ref.WeakReference;

import widget.ImageViewInterface;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class ImageLoaderTask extends AsyncTask<Integer, Void, Bitmap> {
    private Callback mCallback;

    private ImageViewInterface mImageViewInterface;

    private final WeakReference<View> mViewRef;
    private int mKey;

    public ImageLoaderTask(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, View view) {
        mViewRef = new WeakReference<>(view);
        mCallback = (Callback)adapter;
    }

    public ImageLoaderTask(Fragment fragment, View view) {
        mViewRef = new WeakReference<>(view);
        mCallback = (Callback)fragment;
    }

    public void setKey(int key) {mKey = key;}
    public int getKey() {return mKey;}

    public void setImageViewInterface(ImageViewInterface imageViewInterface) {
        mImageViewInterface = imageViewInterface;
    }

    @Override
    protected Bitmap doInBackground(Integer... params) {
        setKey(params[0]);

        Bitmap bitmap = mCallback.getFromCache(String.valueOf(getKey()));

        if(bitmap == null) {
            bitmap = decodeBitmapFromRes(getKey());
            mCallback.addToCache(String.valueOf(getKey()), bitmap);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()) bitmap = null;

        if(mViewRef.get() != null && bitmap != null) {
            mImageViewInterface.setImage(mViewRef.get(), this, bitmap);
        }
    }

    private Bitmap decodeBitmapFromRes(int resId) {
        Resources resources = mCallback.getResources();
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);

        options.inSampleSize = calculateInSampleSize(options);

        options.inJustDecodeBounds = false;
        Bitmap unscaled =  BitmapFactory.decodeResource(resources, resId, options);

        //TODO: force all bitmaps to go through scaleImageUp method?
        if(options.inSampleSize < 1) return scaleImageUp(unscaled, unscaled.getWidth(), unscaled.getHeight());
        else return unscaled;
    }

    private Bitmap scaleImageUp(Bitmap unscaled, final int imageWidth, final int imageHeight) {
        final int reqWidth = mImageViewInterface.getRequiredImageWidth();
        final int reqHeight = mImageViewInterface.getRequiredImageHeight();
        final ScalingLogic scalingLogic = mImageViewInterface.getScalingLogic();

        Rect src = calculateSrcRect(imageWidth, imageHeight, reqWidth, reqHeight, scalingLogic);
        Rect dst = calculateDstRect(imageWidth, imageHeight, reqWidth, reqHeight, scalingLogic);

        Bitmap scaled = Bitmap.createBitmap(reqWidth, reqHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(scaled);
        c.drawBitmap(unscaled, src, dst, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaled;
    }

    private int calculateInSampleSize(BitmapFactory.Options options) {
        //Required dimensions from interface
        final int reqWidth = mImageViewInterface.getRequiredImageWidth();
        final int reqHeight = mImageViewInterface.getRequiredImageHeight();

        // Raw height and width of cardImage
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = mImageViewInterface.getImageSampleSize();

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     *
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     *
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to a smaller size than
     * requested.
     *
     * CHANGES: replaced FIT with FIT_W (fit width) and FIT_H (fit height).
     *
     * @author Andreas Agvard (andreas.agvard@sonyericsson.com)
     * @link http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/
     */
    public static enum ScalingLogic {
        CROP, FIT
    }

    /**
     * Calculates source rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal source rectangle
     *
     * @author Andreas Agvard (andreas.agvard@sonyericsson.com)
     * @link http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/
     */
    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    /**
     * Calculates destination rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal destination rectangle
     *
     * @author Andreas Agvard (andreas.agvard@sonyericsson.com)
     * @link http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/
     */
    private static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;

            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public interface Callback {
        Bitmap getFromCache(String key);
        void addToCache(String key, Bitmap bitmap);
        Resources getResources();
    }
}
