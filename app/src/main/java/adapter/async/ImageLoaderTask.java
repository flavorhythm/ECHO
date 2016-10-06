package adapter.async;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class ImageLoaderTask extends AsyncTask<Integer, Void, Bitmap> {
    private Callback callback;

    private final WeakReference<ImageView> imageViewRef;
    private int resId = 0;

    public ImageLoaderTask(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, ImageView imageView) {
        imageViewRef = new WeakReference<>(imageView);

        callback = (Callback)adapter;
    }

    public void setResId(int resId) {this.resId = resId;}
    public int getResId() {return resId;}

    @Override
    protected Bitmap doInBackground(Integer... params) {
        setResId(params[0]);

        Bitmap bitmap = callback.getFromCache(String.valueOf(getResId()));

        if(bitmap == null) {
            bitmap = decodeBitmapFromRes(getResId());
            callback.addToCache(String.valueOf(getResId()), bitmap);
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(isCancelled()) bitmap = null;

        if(imageViewRef.get() != null && bitmap != null) {
            final ImageView imageView = imageViewRef.get();
            final ImageLoaderTask task = callback.getTaskFromAsync(imageView);

            if (this == task && imageView != null) {
                final TransitionDrawable transDrawable = getTransition(bitmap);

                imageView.setImageDrawable(transDrawable);
                transDrawable.startTransition(300);
            }
        }
    }

    private Bitmap decodeBitmapFromRes(int resId) {
        int cardWidth = callback.getResources().getDimensionPixelSize(R.dimen.card_home_imageWidth);
        int cardHeight = callback.getResources().getDimensionPixelSize(R.dimen.card_home_imageHeight);

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(callback.getResources(), resId, options);

        options.inSampleSize = calculateInSampleSize(options, cardWidth, cardHeight);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(callback.getResources(), resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of cardImage
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

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

    private TransitionDrawable getTransition(Bitmap bitmap) {
        return new TransitionDrawable(
                new Drawable[] {
                        new ColorDrawable(Color.TRANSPARENT),
                        new BitmapDrawable(callback.getResources(), bitmap)
                }
        );
    }

    public interface Callback {
        Bitmap getFromCache(String resId);
        void addToCache(String resId, Bitmap bitmap);
        ImageLoaderTask getTaskFromAsync(ImageView imageView);
        Resources getResources();
    }
}
