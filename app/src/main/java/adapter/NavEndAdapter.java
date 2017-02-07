package adapter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.AdapterCallbacks;
import com.echo_usa.echo.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import data.navigation.Locator;
import data.navigation.Model;
import data.navigation.NavItem;
import util.ImageLoaderTask;
import widget.CardContentView;
import widget.RoundImageView;

/**
 * Created by ZYuki on 7/20/2016.
 */
public class NavEndAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageLoaderTask.Callback {
    private static final int HEADER = 0;

    private AdapterCallbacks mCallback;

    private static LruCache<String, Bitmap> sMemCache; //Stores a cache of bitmaps for quicker loading times
    private List<NavItem> mNavList;
    private View mHeader;
    private Resources mResources;
//    private Picasso mPicasso;

    public NavEndAdapter(View header, List<NavItem> navList) {
        mHeader = header;
        mNavList = navList;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        mResources = recyclerView.getResources();
        mCallback = (AdapterCallbacks)recyclerView.getContext();

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        sMemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {return bitmap.getByteCount() / 1024;}
        };
    }

    @Override
    public int getItemViewType(int position) {
        boolean cond1 = mHeader != null;
        boolean cond2 = position == 0;
        if(cond1 && cond2) return HEADER;

        position -= 1;
        switch(mNavList.get(position).getNavType()) {
            case LOCATOR: return R.layout.item_nav_end_locator;
            case MODEL_DOCS: return R.layout.item_nav_end_models;
        }

        return 0;
    }

    @Override public int getItemCount() {return mHeader == null ? mNavList.size() : mNavList.size() + 1;}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int resId) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(resId) {
            case HEADER: return new HeaderHolder(mHeader);
            default: return new NavItemHolder(inflater.inflate(resId, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h, int position) {
        if(h == null) return;
        if(mHeader != null && position > 0) position -= 1;

        if(mNavList.get(position) == null) return;
        switch(h.getItemViewType()) {
            case R.layout.item_nav_end_locator:
                bindHolder(h, (Locator)mNavList.get(position)); break;
            case R.layout.item_nav_end_models:
                bindHolder(h, (Model)mNavList.get(position));break;
        }
    }

    @Override
    public Bitmap getFromCache(String key) {
        return sMemCache.get(key);
    }

    @Override
    public void addToCache(String key, Bitmap bitmap) {
        if(getFromCache(key) == null) sMemCache.put(key, bitmap);
    }

    @Override
    public Resources getResources() {
        return mResources;
    }

    private void bindHolder(RecyclerView.ViewHolder h, @NonNull Locator l) {
        View v;

        if(h instanceof NavItemHolder) v = ((NavItemHolder)h).getView();
        else return;

        v.setOnClickListener(mCallback.getNavEndItemListener(l));
    }

    private void bindHolder(RecyclerView.ViewHolder h, @NonNull Model m) {
        View v;

        if(h instanceof NavItemHolder) v = ((NavItemHolder)h).getView();
        else return;

        ((TextView)v.findViewById(R.id.nav_models_name)).setText(m.getModelName());
        ((TextView)v.findViewById(R.id.nav_models_serial)).setText(m.getSerialNum());

        final int resId = m.getImgResource();
        final RoundImageView imageView = (RoundImageView)v.findViewById(R.id.nav_models_icon);

        v.setOnClickListener(mCallback.getNavEndItemListener(m));
        if(cancelPotentialWork(resId, imageView)) {
            imageView.setLoaderTask(new ImageLoaderTask(this, imageView));

            ImageLoaderTask task = imageView.getLoaderTask();
            AsyncTaskCompat.executeParallel(task, resId);
        }
    }

    private boolean cancelPotentialWork(int contentKey, RoundImageView imageView) {
        final ImageLoaderTask task = imageView.getLoaderTask();

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

    private static class NavItemHolder extends RecyclerView.ViewHolder {
        private View mNavItemView;

        NavItemHolder(View v) {
            super(v);
            mNavItemView = v;
        }

        View getView() {return mNavItemView;}
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        HeaderHolder(View view) {super(view);}
    }
}
