
package adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.AdapterCallbacks;
import com.echo_usa.echo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import data.card_content.CardContent;
import data.card_content.CardInfo;
import util.ImageLoaderTask;
import util.MetricCalc;
import widget.CardContentView;
import widget.CardCatalogView;
import widget.CardInfoView;
import widget.DividerView;

/**
 * Loads and handles list data that is displayed in a RecyclerView. Provides views that are all
 * wrapped in a CardView.
 */
public class CardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageLoaderTask.Callback {
    /**CLASS VARIABLES**/
    @Nullable private View mHeader; //Stores a header view for the list, passed through the constructor

    private List<CardContent> mCardList; //Stores list data passed through the constructor
    private static LruCache<String, Bitmap> sMemCache; //Stores a cache of bitmaps for quicker loading times
    private Resources mResources; //Stores a reference to a ParcelResource object used in the ImageLoaderTask object
    private AdapterCallbacks mCallback;
    private String mContentType; //Stores content type of list data, passed through the constructor
    private Picasso mPicasso;

    /**CONSTRUCTOR METHODS**/
    /**Creates instance of {@link CardAdapter} using the following parameters.
     *
     * @param header Nullable {@link #mHeader} to display in {@link RecyclerView}
     * @param contentType Type of list data: {@link CardContent#TYPE_CATALOG}
     *                    or {@link CardContent#TYPE_INFO}.
     * @param cardList List data used in {@link CardAdapter}, displayed in a {@link RecyclerView}.
     */
    public CardAdapter(@Nullable View header, String contentType, List<CardContent> cardList) {
        mHeader = header;
        mCardList = cardList;
        mContentType = contentType;
    }

    /**OVERRIDE METHODS**/
    /**Returns item count dependent of the size of {@link #mCardList} and
     * {@link #mHeader} availability.
     *
     * @return Returns the size of of {@link #mCardList}.
     */
    @Override public int getItemCount() {
        return mHeader == null ? mCardList.size() : mCardList.size() + 1;
    }
    /**Returns item view type located at <code>position</code> located in {@link #mCardList}
     *
     * @param position Position of the current item in {@link #mCardList}
     * @return View type of item located at <code>position</code> in {@link #mCardList}
     * @see CardContent#HEADER
     * @see CardContent#DIVIDER
     * @see CardContent#CONTENT
     */
    @Override public int getItemViewType(int position) {
        boolean cond1 = mHeader != null;
        boolean cond2 = position == 0;
        //Returns header view type when header exists and position is 0.
        //Offset position by 1 if header exists.
        if(cond1) {
            if(cond2) return CardContent.HEADER;
            else position -= 1;
        }

        return mCardList.get(position).getCardType();
    }
    /**Initializes {@link #mResources}, {@link #mCallback} and {@link #sMemCache}
     * when adapter is attached to {@link RecyclerView}.
     *
     * @param recyclerView Provides {@link RecyclerView} that {@link CardAdapter} is attached to
     */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mResources = recyclerView.getContext().getResources();
        mCallback = (AdapterCallbacks)recyclerView.getContext();
        mPicasso = Picasso.with(recyclerView.getContext());

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        sMemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {return bitmap.getByteCount() / 1024;}
        };
    }
    /**Creates custom subclass of {@link RecyclerView.ViewHolder}.
     * Views are stored within viewholders and is recycled in the {@link RecyclerView}
     * Custom subclasses are declared within {@link CardAdapter}.
     *
     * @param parent ViewGroup that contains the {@link RecyclerView} attached to {@link CardAdapter}
     * @param viewType Integer value returned from method {@link #getItemViewType(int)}
     * @return Custom subclass of {@link RecyclerView.ViewHolder}
     * @see HeaderHolder
     * @see DividerHolder
     * @see ContentHolder
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch(viewType) {
            case CardContent.HEADER: return new HeaderHolder(mHeader);
            case CardContent.DIVIDER: return new DividerHolder(new DividerView(parent.getContext()));
            default:
                //Initializes CardView to wrap CardContentView
                CardView cardView = setupCardView(parent.getContext());
                CardContentView content = null;
                //Creates subclass of CardContentView depending on content type
                if(mContentType.equals(CardContent.TYPE_INFO)) {
                    content = new CardInfoView(parent.getContext());
                } else if(mContentType.equals(CardContent.TYPE_CATALOG)) {
                    content = new CardCatalogView(parent.getContext());
                }
                //Sets ID of CardContentView for easy retrieval within viewholder
                if(content != null) {
                    content.setId(R.id.card_content);
                    cardView.addView(content);
                }
                return new ContentHolder(cardView);
        }
    }
    /**Binds viewholder returned from {@link #onCreateViewHolder(ViewGroup, int)}
     * with data in {@link #mCardList} at <code>position</code>
     *
     * @param holder Superclass of custom holders declared in {@link CardAdapter}
     * @param position Position of respective data item in {@link #mCardList}
     * @see #bindDivider(DividerHolder, CardContent)
     * @see #bindCard(ContentHolder, CardContent)
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder == null) return;
        if(mHeader != null && position > 0) position -= 1;

        if(holder instanceof ContentHolder) bindCard((ContentHolder)holder, getItem(position));
        else if(holder instanceof DividerHolder) bindDivider((DividerHolder)holder, getItem(position));
    }

    /**IMPLEMENTED METHODS**/
    /**Retrieves cached bitmap from {@link #sMemCache}
     *
     * @param key String variable used to retrieve cached data
     * @return Cached bitmap from {@link #sMemCache}
     * @see ImageLoaderTask.Callback
     */
    @Override
    public Bitmap getFromCache(String key) {
        return sMemCache.get(key);
    }
    /**Adds bitmap to cache object {@link #sMemCache}
     *
     * @param key String variable used to store data in cache
     * @param bitmap Bitmap to cache
     * @see ImageLoaderTask.Callback
     */
    @Override
    public void addToCache(String key, Bitmap bitmap) {
        if(getFromCache(key) == null) sMemCache.put(key, bitmap);
    }
    /**Retrieves {@link Resources} object used in {@link ImageLoaderTask}
     *
     * @return {@link Resources} object
     * @see ImageLoaderTask.Callback
     */
    @Override
    public Resources getResources() {
        return mResources;
    }

    /**PRIVATE METHODS**/
    /**Shadow method to retrieve items from {@link #mCardList} at <code>position</code>
     *
     * @param position Position of item to retrieve from {@link #mCardList}
     * @return {@link CardContent} object
     */
    private CardContent getItem(int position) {
        return mCardList.get(position);
    }
    /**Sets up CardView used to wrap {@link CardContentView}
     *
     * @param c The current {@link Context}
     * @return CardView set up within this method
     * @see #onCreateViewHolder(ViewGroup, int)
     */
    private static CardView setupCardView(Context c) {
        CardView cv = new CardView(c);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        cv.setPreventCornerOverlap(false);
        cv.setUseCompatPadding(true);
        cv.setCardElevation(MetricCalc.dpToPxByVal(1));
        cv.setRadius(MetricCalc.dpToPxByVal(2));

        int padding = c.getResources().getDimensionPixelSize(R.dimen.standard_padding);
        lp.setMargins(padding, padding / 2, padding, padding / 2);

        cv.setLayoutParams(lp);
        return cv;
    }
    /**Binds view within {@param h} with data in {@param cc}
     *
     * @param h {@link ContentHolder} object,
     *                               subclass of {@link RecyclerView.ViewHolder}
     * @param cc {@link CardContent} object containing display data for the CardView within {@param h}
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    private void bindCard(ContentHolder h, CardContent cc) {
        final int resId = cc.getImgRes();

        h.setCardContent(cc);
        CardView cv = (CardView)h.getCardView();
        cv.setOnClickListener(mCallback.getCardListItemListener(cc));

//        mPicasso.load(resId).into((CardContentView)h.getCardContent());

        View viewForTask = h.getCardContent();
        if(cancelPotentialWork(resId, viewForTask) && viewForTask instanceof CardContentView) {
            CardContentView v = (CardContentView)viewForTask;
            v.setLoaderTask(new ImageLoaderTask(this, v));

            ImageLoaderTask task = v.getLoaderTask();
            AsyncTaskCompat.executeParallel(task, resId);
        }
    }
    /**Binds view within {@param h} with data in {@param cc}
     *
     * @param h {@link DividerHolder} object, subclass of {@link RecyclerView.ViewHolder}
     * @param cc {@link CardContent} object containing display data for {@link DividerView}
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
    private void bindDivider(DividerHolder h, CardContent cc) {
        h.setDividerText(cc.getText(), cc.getSubtext());
    }
    /**Cancels an ongoing task if the image is no longer needed. This may occur when user scrolls
     * {@link RecyclerView} containing this {@link CardAdapter}
     *
     * @param contentKey Integer value used as key, retrieved from {@link CardContent} to be displayed
     * @param content View containing ongoing task
     * @return <code>true</code> if view does not contain active task or task cancelled. Allows
     * execution of {@link AsyncTaskCompat#executeParallel(AsyncTask, Object[])}
     * <code>false</code> if same task is currently in progress. Prevents execution of AsyncTask
     * @see ImageLoaderTask
     * @see #bindCard(ContentHolder, CardContent)
     */
    private boolean cancelPotentialWork(int contentKey, View content) {
        final ImageLoaderTask task = getTaskFromView(content);

        if (task != null) {
            final int taskKey = task.getKey();
            // If bitmapData is not yet set or it differs from the new data
            // Cancel previous task
            if (taskKey == 0 || taskKey != contentKey) task.cancel(true);
            // The same work is already in progress
            else return false;
        }

        // No task associated with the ImageView, or an existing task was cancelled
        return true;
    }
    /**Retrieves task from {@param content}
     *
     * @param v View that potentially contains {@link ImageLoaderTask} object
     * @return {@link ImageLoaderTask} object
     */
    private ImageLoaderTask getTaskFromView(View v) {
        boolean cond1 = v != null;
        boolean cond2 = v instanceof CardInfoView;
        boolean cond3 = v instanceof CardCatalogView;

        if(cond1 && cond2) {return ((CardInfoView)v).getLoaderTask();}
        if(cond1 && cond3) {return ((CardCatalogView)v).getLoaderTask();}
        else return null;
    }

    /**INNER CLASSES**/
    /**{@link RecyclerView.ViewHolder} subclass that holds reference to CardView
     * and {@link CardContentView}
     */
    private class ContentHolder extends RecyclerView.ViewHolder {
        private View mCard;
        private View mContent;

        ContentHolder(View v) {
            super(v);

            if (v instanceof CardView) {
                mCard = v;
                CardView cv = (CardView)v;
                mContent = cv.findViewById(R.id.card_content);
            }
        }
        /**Sets card content with {@param cc} data.
         * This method shadows {@link CardInfoView#setContent(CardInfo)}
         *
         * @param cc {@link CardContent} object containing data to display in CardView
         */
        void setCardContent(CardContent cc) {
            if(mContent instanceof CardInfoView) {
                ((CardInfoView)mContent).setContent((CardInfo)cc);
            }
        }

        View getCardContent() {
            return mContent;
        }

        View getCardView() {
            return mCard;
        }
    }
    /**{@link RecyclerView.ViewHolder} subclass that holds reference to {@link DividerView}**/
    private class DividerHolder extends RecyclerView.ViewHolder {
        private DividerView mDivider;

        DividerHolder(View v) {
            super(v);
            if (v instanceof DividerView) {mDivider = (DividerView)v;}
        }

        /**Sets divider text and subtext
         *
         * @param title String variable containing title of divider
         * @param subtitle String variable containing subtitle of divider
         */
        void setDividerText(String title, String subtitle) {
            mDivider.setContent(title, subtitle);
        }
    }

    /**{@link RecyclerView.ViewHolder} subclass that holds reference to {@link #mHeader}**/
    private class HeaderHolder extends RecyclerView.ViewHolder {
        HeaderHolder(View v) {super(v);}
    }
}