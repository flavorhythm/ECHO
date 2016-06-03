package fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import adapter.CardAdapter;
import data.DataAccessObject;

/**
 * Created by zyuki on 6/3/2016.
 */
public class FragmentHome extends BaseFragment implements ObservableScrollViewCallbacks {

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    //    private TextView mTitleView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_home;
        View fragView = inflater.inflate(layoutRes, container, false);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = getActionBarSize();

        DataAccessObject dataAccess = new DataAccessObject();

        ObservableRecyclerView recycler = (ObservableRecyclerView)fragView.findViewById(R.id.recycler);
        recycler.setScrollViewCallbacks(this);

        final View headerView = inflater.inflate(R.layout.header, null);
        headerView.post(new Runnable() {
            @Override
            public void run() {
                headerView.getLayoutParams().height = mFlexibleSpaceImageHeight;
            }
        });

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(new CardAdapter(dataAccess.getCards(), headerView));

        mImageView = fragView.findViewById(R.id.image);
        mOverlayView = fragView.findViewById(R.id.overlay);

        // mRecyclerViewBackground makes RecyclerView's background except header view.
        mRecyclerViewBackground = fragView.findViewById(R.id.list_background);

        //since you cannot programmatically add a header view to a RecyclerView we added an empty view as the header
        // in the adapter and then are shifting the views OnCreateView to compensate
        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
        mRecyclerViewBackground.post(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setTranslationY(mRecyclerViewBackground, mFlexibleSpaceImageHeight);
            }
        });
        ViewHelper.setTranslationY(mOverlayView, mFlexibleSpaceImageHeight);

        return fragView;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
    }

    private int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = getActivity().obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        return actionBarSize;
    }

    @Override
    public void onDownMotionEvent() {}

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {}
}
