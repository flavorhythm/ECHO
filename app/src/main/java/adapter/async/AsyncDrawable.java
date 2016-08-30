package adapter.async;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.lang.ref.WeakReference;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class AsyncDrawable extends BitmapDrawable {
    private final WeakReference<ImageLoaderTask> taskRef;

    public AsyncDrawable(Resources res, Bitmap bitmap, ImageLoaderTask task) {
        super(res, bitmap);

        taskRef = new WeakReference<>(task);
    }

    public ImageLoaderTask getTask() {
        return taskRef.get();
    }
}
