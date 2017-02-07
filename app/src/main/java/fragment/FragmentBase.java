package fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Visibility;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.echo_usa.echo.DataAccessApplication;
import com.echo_usa.echo.MainActivity;

import data.DataAccessObject;
import util.FragSpec;
import util.ImageLoaderTask;
import util.MetricCalc;
import widget.EchoSnackbar;
import widget.EchoToolbar;

/**
 * Created by zyuki on 6/2/2016.
 */

//TODO: make transitions smoother here
public class FragmentBase extends Fragment implements ImageLoaderTask.Callback {
    private static final int SCROLL_HEADER_THRESHOLD = MetricCalc.getDrawerHeaderHeight();
//    protected static final int NO_HEADER_THRESHOLD = 0;

    protected static DataAccessObject mDataAccess;

    protected static LruCache<String, Bitmap> sMemCache; //TODO: save?

    @Override
    public void onAttach(Context context) {
        Log.v("FragmentBase", "onAttach");
        super.onAttach(context);

        final int maxMemory = (int)(Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        sMemCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {return bitmap.getByteCount() / 1024;}
        };

//        if(getToolbar() != null) {
//            EchoToolbar.getShowHideAnim(getToolbar()).start();
//        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v("FragmentBase", "onCreate");
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        mDataAccess = ((DataAccessApplication)getActivity().getApplication()).getDataAccessObject();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        getToolbar().onPrepareFragmentMenu();
    }

    protected EchoToolbar getToolbar() {return ((MainActivity)getActivity()).getToolbar();}
    protected static DataAccessObject getDataAccess() {return mDataAccess;}

    protected enum Threshold {
        W_HEADER, NO_HEADER;
    }

    protected int getToolbarAlphaThreshold(Threshold threshold) {
        switch(threshold) {
            case W_HEADER: return SCROLL_HEADER_THRESHOLD - MetricCalc.getActionBarSize(getContext());
            default: return 0;
        }
//        switch(f) {
//            case HOME: case CATALOG: case SUBLIST:
//                return SCROLL_HEADER_THRESHOLD - MetricCalc.getActionBarSize(getContext());
//            case GUIDE: return NO_HEADER_THRESHOLD;
//            default: return NO_HEADER_THRESHOLD;
//        }
    }

    @Override
    public Bitmap getFromCache(String key) {return sMemCache.get(key);}

    @Override
    public void addToCache(String key, Bitmap bitmap) {
        if(getFromCache(key) == null) sMemCache.put(key, bitmap);
    }

    protected static View getPaddingView(Context context, int paddingY) {
        View v = new View(context);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                paddingY
        );

        v.setLayoutParams(lp);
        return v;
    }

    protected static boolean cancelPotentialWork(int contentKey, ImageLoaderTask task) {
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
}
