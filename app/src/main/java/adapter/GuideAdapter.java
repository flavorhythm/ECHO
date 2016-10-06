package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import adapter.async.AsyncDrawable;
import adapter.async.ImageLoaderTask;
import adapter.viewholder.DividerHolder;
import adapter.viewholder.CardHolder;
import data.Card;
import widget.CardDivider;
import widget.EchoCard;

import static adapter.viewholder.BaseHolder.*;

/**
 * Created by ZYuki on 7/13/2016.
 */
public class GuideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageLoaderTask.Callback {
    private List<Card> cardList;
    private View.OnClickListener listener;

    private static LruCache<String, Bitmap> memoryCache;
    private Resources resources;

    private static final int CONTENT_DIVIDER_1_POS = 0;
    private static final int CONTENT_DIVIDER_2_POS = 4;

    public GuideAdapter(Context context, List<Card> cardList, View.OnClickListener listener) {
        this.resources = context.getResources();
        this.cardList = cardList;
        this.listener = listener;
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
            case VIEW_TYPE_CONTENT_DIVIDER:
                return new DividerHolder(new CardDivider(parent.getContext()));
            case VIEW_TYPE_ITEM_SINGLE:
                return new CardHolder(new EchoCard(parent.getContext(), Card.CARD_SIZE_LARGE, true));
            default:
                return new CardHolder(new EchoCard(parent.getContext(), Card.CARD_SIZE_LARGE, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int originalPos) {
        if(viewHolder instanceof CardHolder) {
            int adjustedPos;

            if(originalPos < CONTENT_DIVIDER_2_POS) adjustedPos = originalPos - 1;
            else adjustedPos = originalPos - 2;

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
        } else if(viewHolder instanceof DividerHolder) {
            switch(originalPos) {
                case CONTENT_DIVIDER_1_POS:
                    ((DividerHolder) viewHolder).setDividerText(
                            "RIGHT TOOL FOR THE JOB",
                            "Before you get started"
                    );
                    ((DividerHolder) viewHolder).setTopSpacer();
                    break;
                case CONTENT_DIVIDER_2_POS:
                    ((DividerHolder) viewHolder).setDividerText(
                            "GETTING STARTED SAFELY",
                            "Let's put that machine to work!"
                    );
                    break;
                default: break;
            }
        }
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

    @Override
    public int getItemViewType(int position) {
        switch(position) {
            case CONTENT_DIVIDER_1_POS: return VIEW_TYPE_CONTENT_DIVIDER;
            case CONTENT_DIVIDER_2_POS: return VIEW_TYPE_CONTENT_DIVIDER;
            default:
                if(position % 2 == 0) return VIEW_TYPE_ITEM_SINGLE;
                else return VIEW_TYPE_ITEM_DOUBLE;
        }
    }

    @Override
    public int getItemCount() {return cardList.size() != 0 ? cardList.size() + 2 : 0;}
}
