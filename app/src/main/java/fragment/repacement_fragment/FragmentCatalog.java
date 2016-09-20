package fragment.repacement_fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.AsyncTaskCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;

import fragment.FragmentBase;
import util.MetricCalcs;

/**
 * Created by zyuki on 9/16/2016.
 */
public class FragmentCatalog extends FragmentBase {
    public static FragmentCatalog thisFragment;

    public static FragmentCatalog newInstance() {
        if(thisFragment == null) thisFragment = new FragmentCatalog();
        return thisFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_catalog;
        View fragView = inflater.inflate(layoutRes, container, false);
        //((TextView)fragView.findViewById(R.id.test)).setText(callback.getFragName());

        ImageView image = (ImageView)fragView.findViewById(R.id.test_catalog);
        ImageAsyncTask task = new ImageAsyncTask(image);
        AsyncTaskCompat.executeParallel(task, R.drawable.catalog);

        return fragView;
    }

    @Override
    public void onViewCreated(View fragmentView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragmentView, savedInstanceState);
    }

    class ImageAsyncTask extends AsyncTask<Integer, Void, Bitmap> {
        final WeakReference<ImageView> imageViewReference;
        int resource;

        ImageAsyncTask(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            resource = params[0];

            return decodeSampledBitmapFromResource(
                    getContext().getResources(),
                    resource,
                    MetricCalcs.getScreenWidth(),
                    MetricCalcs.getScreenHeight() - MetricCalcs.getStatusBarHeight()
            );
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if(imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if(imageView != null) imageView.setImageBitmap(bitmap);
            }

            super.onPostExecute(bitmap);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 2;

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

        public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                      int reqWidth, int reqHeight) {

            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(res, resId, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(res, resId, options);
        }
    }
}
