package adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.echo_usa.echo.R;

import java.util.List;

import adapter.viewholder.HeaderHolder;
import adapter.viewholder.CardHolder;
import data.Card;
import adapter.async.AsyncDrawable;
import adapter.async.ImageLoaderTask;
import widget.EchoCard;

import static adapter.viewholder.BaseHolder.*;

/**
 * Created by zyuki on 6/1/2016.
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageLoaderTask.Callback {
    private List<Card> cardList;
    private View header;
    private View divider;
    private View.OnClickListener listener;

    private static LruCache<String, Bitmap> memoryCache;
    private Resources resources;

    public HomeAdapter(List<Card> cardList, View header, View.OnClickListener listener) {
        this.cardList = cardList;
        this.header = header;
        this.listener = listener;
    }

    public HomeAdapter setActivity(Activity activity) {
        divider = new View(activity);
        this.resources = activity.getResources(); //Mecessary. Resources.getSystem() does not work in AsyncTask

        buildDivider();
        divider.setBackgroundResource(R.drawable.bg_divider_fade);

        return this;
    }

    private void buildDivider() {
        divider.setLayoutParams(
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, DIVIDER_HEIGHT)
        );
        divider.setBackgroundResource(android.R.color.transparent);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        Log.v("HomeAdapter", "Attached to RecyclerView: " + recyclerView.toString());

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
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        Log.v("HomeAdapter", "Detached from RecyclerView: " + recyclerView.getClass().toString());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case VIEW_TYPE_HEADER:
                return new HeaderHolder(header);
            case VIEW_TYPE_BLANK_DIVIDER:
                return new HeaderHolder(divider);
            case VIEW_TYPE_ITEM_SINGLE:
                return new CardHolder(new EchoCard(parent.getContext(), Card.CARD_SIZE_LARGE, true));
            default:
                return new CardHolder(new EchoCard(parent.getContext(), Card.CARD_SIZE_LARGE, false));
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int originalPos) {
        if(viewHolder instanceof CardHolder) {
            int adjustedPos = originalPos - 2;

            ((CardHolder)viewHolder).imageRes = cardList.get(adjustedPos).getDrawableRes();
            ((CardHolder)viewHolder).cardTitle.setText(cardList.get(adjustedPos).getCardTitle());
            ((CardHolder)viewHolder).cardSubtitle.setText(cardList.get(adjustedPos).getCardSubtitle());

            //Order may be important
            ((CardHolder)viewHolder).cardBtn.setTag(cardList.get(adjustedPos));
            ((CardHolder)viewHolder).cardBtn.setOnClickListener(listener);

            final int resId = ((CardHolder)viewHolder).imageRes;
            final ImageView image = ((CardHolder)viewHolder).cardImage;

            if(cancelPotentialWork(resId, image)) {
                final ImageLoaderTask task = new ImageLoaderTask(this, image);
                final AsyncDrawable asyncDrawable = new AsyncDrawable(resources, null, task);

                image.setImageDrawable(asyncDrawable);
                AsyncTaskCompat.executeParallel(task, resId);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch(position) {
            case 0:
                return VIEW_TYPE_HEADER;
            case 1:
                return VIEW_TYPE_BLANK_DIVIDER;
            default:
                if(position % 2 == 0) return VIEW_TYPE_ITEM_SINGLE;
                else return VIEW_TYPE_ITEM_DOUBLE;
        }
    }

    @Override
    public int getItemCount() {
        boolean emptyHeader = (header == null);
        boolean emptyDivider = (divider == null);

        if(emptyHeader && emptyDivider) return cardList.size();
        else if(emptyHeader || emptyDivider) return cardList.size() + 1;
        else return cardList.size() + 2;
    }

    @Override
    public Resources getResources() {
        return resources;
    }

    @Override
    public void addToCache(String key, Bitmap bitmap) {
        if(getFromCache(key) == null) {
            memoryCache.put(key, bitmap);
        }
    }

    @Override
    public Bitmap getFromCache(String key) {
        return memoryCache.get(key);
    }

    private boolean cancelPotentialWork(int data, ImageView imageView) {
        final ImageLoaderTask task = getTaskFromAsync(imageView);

        if (task != null) {
            final int bitmapData = task.getResId();
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

    @Override
    public ImageLoaderTask getTaskFromAsync(ImageView imageView) {
        if(imageView != null) {
            final Drawable drawable = imageView.getDrawable();
            if(drawable instanceof AsyncDrawable) {
                final AsyncDrawable asyncDrawable = (AsyncDrawable)drawable;
                return asyncDrawable.getTask();
            }
        }

        return null;
    }
}