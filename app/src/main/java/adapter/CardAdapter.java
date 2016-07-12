package adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.echo_usa.echo.MainActivity;
import com.echo_usa.echo.R;

import java.lang.ref.WeakReference;
import java.util.List;

import data.Card;

/**
 * Created by zyuki on 6/1/2016.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_DIVIDER = 1;
    private static final int VIEW_TYPE_ITEM = 2;

    private static final int DIVIDER_HEIGHT = 150;

    private List<Card> cardsList;
    private View header;
    private View divider;
    private View.OnClickListener listener;

    private LruCache<String, Bitmap> memoryCache;
    private Resources resources;

    public CardAdapter(List<Card> cardsList, View header, View.OnClickListener listener) {
        this.cardsList = cardsList;
        this.header = header;
        this.listener = listener;
    }

    public CardAdapter setActivity(Activity activity) {
        divider = new View(activity);
        this.resources = activity.getResources();

        buildDivider();

        return this;
    }

    private void buildDivider() {
        divider.setLayoutParams(
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DIVIDER_HEIGHT)
        );
//        divider.setBackgroundResource(R.color.echo_orange);
        divider.setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(header);
            case VIEW_TYPE_DIVIDER:
                return new HeaderViewHolder(divider);
            default:
                int layoutRes = R.layout.card_home;
                return new ItemViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false)
                );
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int originalPos) {
        if(viewHolder instanceof ItemViewHolder) {
            int adjustedPos = originalPos - 2;

            ((ItemViewHolder)viewHolder).imageRes = cardsList.get(adjustedPos).getDrawableRes();
            ((ItemViewHolder)viewHolder).cardText.setText(cardsList.get(adjustedPos).getCardText());

            //Order may be important
            ((ItemViewHolder)viewHolder).cardView.setTag(adjustedPos);
            ((ItemViewHolder)viewHolder).cardView.setOnClickListener(listener);

            final int resId = ((ItemViewHolder)viewHolder).imageRes;
            final ImageView image = ((ItemViewHolder)viewHolder).image;
            if(cancelPotentialWork(resId, image)) {
                final ImageLoaderTask task = new ImageLoaderTask(image);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(resources, null, task);

                image.setImageDrawable(asyncDrawable);
                AsyncTaskCompat.executeParallel(task, resId);
            }
        }
    }

    private void addToCache(String key, Bitmap bitmap) {
        if(getFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    private Bitmap getFromCache(String key) {
        return memoryCache.get(key);
    }

    private static boolean cancelPotentialWork(int data, ImageView imageView) {
        final ImageLoaderTask task = getTaskFromAsync(imageView);

        if (task != null) {
            final int bitmapData = task.resId;
            // If bitmapData is not yet set or it differs from the new data
            if (bitmapData == 0 || bitmapData != data) {
                // Cancel previous task
                task.cancel(true);
            } else {
                // The same work is already in progress
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }

    private static ImageLoaderTask getTaskFromAsync(ImageView imageView) {
        if(imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if(drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                return asyncDrawable.getTask();
            }
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        switch(position) {
            case 0:
                return VIEW_TYPE_HEADER;
            case 1:
                return VIEW_TYPE_DIVIDER;
            default:
                return VIEW_TYPE_ITEM;
        }
    }

    @Override
    public int getItemCount() {
        boolean emptyHeader = (header == null);
        boolean emptyDivider = (divider == null);

        if(emptyHeader && emptyDivider) {
            return cardsList.size();
        } else if(emptyHeader || emptyDivider){
            return cardsList.size() + 1;
        } else {
            return cardsList.size() + 2;
        }
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View view) {super(view);}
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public int imageRes;

        public ImageView image;
        public TextView cardText;
        public CardView cardView;

        public ItemViewHolder(View card) {
            super(card);

            image = (ImageView) card.findViewById(R.id.card_home_image);
            cardText = (TextView) card.findViewById(R.id.card_home_text);
            cardView = (CardView) card.findViewById(R.id.card_home);
        }
    }

    class ImageLoaderTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewRef;
        private int resId;

        public ImageLoaderTask(ImageView imageView) {
            imageViewRef = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(Integer... params) {
            resId = params[0];

            Bitmap bitmap = getFromCache(String.valueOf(resId));
            if(bitmap != null) {
                return bitmap;
            } else {
                //bitmap = BitmapFactory.decodeResource(resources, resId);
                bitmap = decodeBitmapFromRes(resId);
                addToCache(String.valueOf(resId), bitmap);

                return bitmap;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewRef != null && bitmap != null) {
                final ImageView imageView = imageViewRef.get();
                final ImageLoaderTask task = getTaskFromAsync(imageView);

                if (this == task && imageView != null) {
                    final TransitionDrawable transDrawable = getTransition(bitmap);

                    imageView.setImageDrawable(transDrawable);
                    transDrawable.startTransition(300);
                }
            }
        }

        private Bitmap decodeBitmapFromRes(int resId) {
            int cardWidth = resources.getDimensionPixelSize(R.dimen.card_home_imageWidth);
            int cardHeight = resources.getDimensionPixelSize(R.dimen.card_home_imageHeight);

            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(resources, resId, options);

            options.inSampleSize = calculateInSampleSize(options, cardWidth, cardHeight);

            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeResource(resources, resId, options);
        }

        public int calculateInSampleSize(
                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;

                // Calculate the largest inSampleSize value that is a power of 2 and keeps both
                // height and width larger than the requested height and width.
                while ((halfHeight / inSampleSize) > reqHeight
                        && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }

            return inSampleSize;
        }

        private TransitionDrawable getTransition(Bitmap bitmap) {
            return new TransitionDrawable(
                    new Drawable[] {
                            new ColorDrawable(Color.TRANSPARENT),
                            new BitmapDrawable(resources, bitmap)
                    }
            );
        }
    }

    public static class AsyncDrawable extends BitmapDrawable {
        private final WeakReference<ImageLoaderTask> taskRef;

        public AsyncDrawable(Resources res, Bitmap bitmap, ImageLoaderTask task) {
            super(res, bitmap);

            taskRef = new WeakReference<>(task);
        }

        public ImageLoaderTask getTask() {
            return taskRef.get();
        }
    }
}