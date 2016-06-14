package fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.echo_usa.echo.R;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import java.util.HashMap;
import java.util.Map;

import adapter.CardAdapter;
import data.DataAccessObject;

/**
 * Created by zyuki on 6/3/2016.
 */
public class FragmentHome extends BaseFragment implements ObservableScrollViewCallbacks {
//    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
//    private static final String ARG_SCROLL_Y = "ARG_SCROLL_Y";

    private View mImageView;
    private View mOverlayView;
    private View mRecyclerViewBackground;
    private int mFlexibleSpaceImageHeight;

    public static FragmentHome newInstance() {
        Bundle args = new Bundle();

        FragmentHome fragment = new FragmentHome();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int layoutRes = R.layout.frag_home;
        View fragView = inflater.inflate(layoutRes, container, false);

        ObservableRecyclerView recyclerView = (ObservableRecyclerView)fragView.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(false);

        final View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.header, container, false);
        final int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        headerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, flexibleSpaceImageHeight));

        recyclerView.setAdapter(new CardAdapter(callback.getCards(DataAccessObject.CARDS_FOR_HOME), headerView, callback.getCardListnener()).setActivity(getActivity()));

        // TouchInterceptionViewGroup should be a parent view other than ViewPager.
        // This is a workaround for the issue #117:
        // https://github.com/ksoichiro/Android-ObservableScrollView/issues/117
        recyclerView.setTouchInterceptionViewGroup((ViewGroup)fragView.findViewById(R.id.recycler_root));

        // Scroll to the specified offset after layout
//        Bundle args = getArguments();
//        if (args != null && args.containsKey(ARG_SCROLL_Y)) {
//            final int scrollY = args.getInt(ARG_SCROLL_Y, 0);
//            ScrollUtils.addOnGlobalLayoutListener(recyclerView, new Runnable() {
//                @Override
//                public void run() {
//                    int offset = scrollY % flexibleSpaceImageHeight;
//                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
//                    if (lm != null && lm instanceof LinearLayoutManager) {
//                        ((LinearLayoutManager) lm).scrollToPositionWithOffset(0, -offset);
//                    }
//                }
//            });
//            updateFlexibleSpace(scrollY, fragView);
//        } else {
//            updateFlexibleSpace(0, fragView);
//        }

        mRecyclerViewBackground = fragView.findViewById(R.id.list_background);
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mImageView = fragView.findViewById(R.id.image);
        mOverlayView = fragView.findViewById(R.id.overlay);
//
//        final float scale = 1 + MAX_TEXT_SCALE_DELTA;
//        mRecyclerViewBackground.post(new Runnable() {
//            @Override
//            public void run() {
//                ViewHelper.setTranslationY(mRecyclerViewBackground, mFlexibleSpaceImageHeight);
//            }
//        });
//        ViewHelper.setTranslationY(mOverlayView, mFlexibleSpaceImageHeight);

        recyclerView.setScrollViewCallbacks(this);

        return fragView;
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        int mActionBarSize = getActionBarSize();
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();

        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Translate list background
        ViewHelper.setTranslationY(mRecyclerViewBackground, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
    }

    @Override
    public final void onDownMotionEvent() {
        // We don't use this callback in this pattern.
    }

    @Override
    public final void onUpOrCancelMotionEvent(ScrollState scrollState) {
        // We don't use this callbac
    }

//    protected void updateFlexibleSpace(int scrollY, View view) {
//        int flexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
//
//        View recyclerViewBackground = view.findViewById(R.id.list_background);
//
//        // Translate list background
//        ViewHelper.setTranslationY(recyclerViewBackground, Math.max(0, -scrollY + flexibleSpaceImageHeight));
//
//        // Also pass this event to parent Activity
////        FlexibleActivity parentActivity =
////                (FlexibleActivity) getActivity();
////        if (parentActivity != null) {
////            parentActivity.onScrollChanged(scrollY, (ObservableRecyclerView) view.findViewById(R.id.scroll));
////        }
//    }

    protected int getActionBarSize() {
        Activity activity = getActivity();
        if (activity == null) {
            return 0;
        }
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, textSizeAttr);

        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();

        return actionBarSize;
    }
}