package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import adapter.async.ImageLoaderTask;
import data.Card;

/**
 * Created by zyuki on 7/15/2016.
 */
public class BaseCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageLoaderTask.Callback {
    private List<Card> cardList;
    private View header;
    private View divider;
    private View.OnClickListener listener;

    private static LruCache<String, Bitmap> memoryCache;
    private static ImageLoaderTask asyncTask;
    private Resources resources;
    private int cardSize;

    public BaseCardAdapter(Context context, int cardSize, List<Card> cardList, View.OnClickListener listener) {
        this.resources = context.getResources();
        this.cardList = cardList;
        this.listener = listener;
        this.cardSize = cardSize;
    }
    @Override public int getItemCount() {return cardList.size();}
    @Override public int getItemViewType(int position) {
        return cardList.get(position).getCardType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case 0:
                break;
            case 1:
                break;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Card card = cardList.get(position);

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public Bitmap getFromCache(String resId) {
        return null;
    }

    @Override
    public void addToCache(String resId, Bitmap bitmap) {

    }

    @Override
    public ImageLoaderTask getTaskFromAsync(ImageView imageView) {
        return null;
    }

    @Override
    public Resources getResources() {
        return null;
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
}
